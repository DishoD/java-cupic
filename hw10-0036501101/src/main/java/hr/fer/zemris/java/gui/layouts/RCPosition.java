package hr.fer.zemris.java.gui.layouts;

/**
 * Represents a (row, column) pair. A coordinate position in a integer grid.
 * 
 * @author Disho
 *
 */
public class RCPosition {
	/**
	 * row
	 */
	public final int row;
	/**
	 * column
	 */
	public final int column;
	
	/**
	 * Initializes the pair with the given parameters.
	 * 
	 * @param row row
	 * @param column column
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RCPosition other = (RCPosition) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("(%d,%d)", row, column);
	}
}
