package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parses an expression for the massrename shell command and produces an
 * appropriate NameBuilder.
 * 
 * @author Disho
 *
 */
public class NameBuilderParser {
	/**
	 * Text that will be parsed.
	 */
	private char[] text;
	/**
	 * Current index of the character being parsed.
	 */
	private int currentIndex;
	/**
	 * NameBuilder that will be provided to the user for filename building
	 */
	NameBuilder finalNameBuilder;

	/**
	 * Aggregates multiple name builders into one.
	 * 
	 * @author Disho
	 *
	 */
	private static class AggregateNameBuilder implements NameBuilder {
		private List<NameBuilder> builders;

		/**
		 * Initializes the objects. With the given NameBuilders.
		 * 
		 * @param builders
		 *            NameBuilders, should be sorted in order in which they should be
		 *            executed.
		 */
		public AggregateNameBuilder(List<NameBuilder> builders) {
			Objects.requireNonNull(builders, "Bulders can't be null.");
			if (builders.size() == 0)
				throw new IllegalArgumentException("builders array can't be empty.");

			this.builders = builders;
		}

		@Override
		public void execute(NameBuilderInfo info) {
			for (NameBuilder builder : builders) {
				builder.execute(info);
			}
		}

	}

	/**
	 * NameBulder that appends some given string to the name.
	 * 
	 * @author Disho
	 *
	 */
	private static class StringNameBuilder implements NameBuilder {
		/**
		 * string that will be appended to the name
		 */
		private final String string;

		/**
		 * Initializes the object with the given string.
		 * 
		 * @param string
		 *            string that will be appended to the name
		 */
		public StringNameBuilder(String string) {
			this.string = string;
		}

		@Override
		public void execute(NameBuilderInfo info) {
			info.getStringBuilder().append(string);

		}

	}

	/**
	 * NameBulder that appends group string, according to the massrename shell
	 * command, to the name.
	 * 
	 * @author Disho
	 *
	 */
	private static class GroupNameBulder implements NameBuilder {
		private final int groupIndex;
		private final int additional;
		private final boolean zeroFill;

		/**
		 * Initializes the object with the given parameters.
		 * 
		 * @param groupIndex
		 *            group to append
		 * @param additional
		 *            minimal space that the string occupies, rest is filled with zeros
		 *            or spaces. If you don't want to use this feature provide some
		 *            number <= 0.
		 * @param zeroFill
		 *            if true, spaces will be filed with zeros instead of spaces
		 */
		public GroupNameBulder(int groupIndex, int additional, boolean zeroFill) {
			if (groupIndex < 0)
				throw new IllegalArgumentException("groupIndex cant be negative. It was: " + groupIndex);

			this.groupIndex = groupIndex;
			this.additional = additional;
			this.zeroFill = zeroFill;
		}

		@Override
		public void execute(NameBuilderInfo info) {
			String s = info.getGroup(groupIndex);
			String prefix = "";
			if (additional > 0 && additional > s.length()) {
				if (zeroFill) {
					prefix = String.format("%0" + (additional - s.length()) + "d", 0);
				} else {
					prefix = String.format("%" + (additional - s.length()) + "s", "");
				}
			}

			String group = prefix+s;
			info.getStringBuilder().append(group);
		}

	}

	/**
	 * Initializes the parser with the given text and parses it.
	 * 
	 * @param text
	 */
	public NameBuilderParser(String text) {
		this.text = text.toCharArray();
		finalNameBuilder = parse();
	}

	/**
	 * Parses the text and constructs a final AggregateNameBuilder.
	 * 
	 * @return
	 */
	private NameBuilder parse() {
		List<NameBuilder> builders = new ArrayList<>();

		StringBuilder sb = new StringBuilder();

		while (currentIndex < text.length) {
			char currentChar = text[currentIndex];

			if (currentChar == '$') {
				if (currentIndex + 1 < text.length) {
					if (text[currentIndex + 1] == '{') {
						currentIndex += 2;
						builders.add(new StringNameBuilder(sb.toString()));
						sb = new StringBuilder();

						while (true) {
							if (currentIndex >= text.length)
								throw new IllegalArgumentException("Group tag opened but not closed.");
							if (text[currentIndex] == '}') {
								builders.add(parseGroup(sb.toString()));
								sb = new StringBuilder();
								currentIndex++;
								break;
							}
							sb.append(text[currentIndex]);
							currentIndex++;
						}
						continue;
					}
				}
			}

			sb.append(currentChar);
			currentIndex++;
		}
		
		if(sb.length() > 0) {
			builders.add(new StringNameBuilder(sb.toString()));
		}

		return new AggregateNameBuilder(builders);
	}

	/**
	 * Parses a string representing a content of a group into a GroupNameBuilder.
	 * 
	 * @param string
	 *            content of a group
	 * @return constructed GroupNameBuilder
	 */
	private NameBuilder parseGroup(String string) {
		string = string.replace(" ", "");
		String[] info = string.split(",");

		if (info.length > 2)
			throw new IllegalArgumentException("Ilegal token in group: " + string);

		int groupIndex;
		int additional = 0;
		boolean zeroFill = false;

		try {
			groupIndex = Integer.parseInt(info[0]);
			if (groupIndex < 0)
				throw new IllegalArgumentException("Illegal group index: " + groupIndex + ". Should be >= 0");

			if (info.length == 2) {
				if (info[1].startsWith("0")) {
					zeroFill = true;
					info[1] = info[1].substring(1);
				}
				additional = Integer.parseInt(info[1]);
				if (additional < 0)
					throw new IllegalArgumentException("Illegal minimal space: " + additional + ". Should be >= 0");
			}

		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Ilegal token in group: " + string);
		}

		return new GroupNameBulder(groupIndex, additional, zeroFill);
	}
	
	/**
	 * Name builder for the parsed text.
	 * 
	 * @return name builder
	 */
	public NameBuilder getNameBuilder() {
		return finalNameBuilder;
	}
}
