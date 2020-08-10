package hr.fer.zemris.java.hw06.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Demonstration of stream API on the list of student records.
 * 
 * @author Disho
 *
 */
public class StudentDemo {

	/**
	 * Runs the demonstration
	 * 
	 * @param args
	 *            ignorable
	 * @throws IOException
	 *             if can't load the file
	 */
	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/main/resources/studenti.txt"));
		List<StudentRecord> records = convert(lines);
		
		long broj = vratiBodovaViseOd25(records);
		long broj5 = vratiBrojOdlikasa(records);
		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);
		List<StudentRecord> odlikasiSortirano = vratiSortiranuListuOdlikasa(records);
		List<String> nepolozeniJMBAGovi = vratiPopisNepolozenih(records);
		Map<Integer, List<StudentRecord>> mapaPoOcjenama = razvrstajStudentePoOcjenama(records);
		Map<Integer, Integer> mapaPoOcjenama2 = vratiBrojStudenataPoOcjenama(records);
		Map<Boolean, List<StudentRecord>> prolazNEprolaz = razvrstajProlazPad(records);
		
		System.out.println("Broj studenata s brojem bodova većim od 25: " + broj);
		System.out.println();
		System.out.println("Broj odlikaša: " + broj5);
		System.out.println();
		
		System.out.println("Odlikaši: ");
		odlikasi.forEach(System.out::println);
		System.out.println();
		
		System.out.println("Odlikaši sortirani po bodovima: ");
		odlikasiSortirano.forEach(System.out::println);
		System.out.println();
		
		System.out.println("Jmbagovi studenata koji nisu položili kolegij: ");
		nepolozeniJMBAGovi.forEach(System.out::println);
		System.out.println();
		
		mapaPoOcjenama.entrySet().forEach(e -> {
			System.out.println("Studenti s ocjenom " + e.getKey());
			e.getValue().forEach(System.out::println);
			System.out.println();
		});
		System.out.println();
		
		mapaPoOcjenama2.entrySet().forEach(e -> System.out.println("Studenata s ocjenom " + e.getKey() + " ima " + e.getValue()));
		System.out.println();
		
		System.out.println("Studenti koji su položili kolegij: ");
		prolazNEprolaz.get(true).forEach(System.out::println);
		System.out.println();
		System.out.println("Studenti koji su nisu položili kolegij: ");
		prolazNEprolaz.get(false).forEach(System.out::println);
		
		
	}

	/**
	 * Converts a list of stings to a list of student records.
	 * 
	 * @param lines
	 *            list of strings
	 * @return list of student records
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		List<StudentRecord> records = new ArrayList<>();

		for (String line : lines) {
			if(line.isEmpty()) continue;
			
			records.add(StudentRecord.fromLine(line));
		}

		return records;
	}

	/**
	 * Counts the number of student records that have a sum of all student points
	 * greater than 25.
	 * 
	 * @param records
	 *            student records
	 * @return number of records
	 */
	private static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream()
						.filter(s -> s.getPoints() > 25)
						.count();
	}

	/**
	 * Counts the number of student records that have a grade equal to 5.
	 * 
	 * @param records
	 *            student records
	 * @return number of records
	 */
	private static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream()
						.filter(s -> s.getGrade() == 5)
						.count();
	}

	/**
	 * Returns a list of student records that have a grade equal to 5.
	 * 
	 * @param records
	 *            student records
	 * @return list of records
	 */
	private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
						.filter(s -> s.getGrade() == 5)
						.collect(Collectors.toList());
	}

	/**
	 * Returns a list of student records that have a grade equal to 5 sorted by sum
	 * of ther points.
	 * 
	 * @param records
	 *            student records
	 * @return list of records
	 */
	private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
						.filter(s -> s.getGrade() == 5)
						.sorted(StudentRecord.BY_POINTS.reversed())
						.collect(Collectors.toList());
	}

	/**
	 * Returns a list of sorted students jmbags that have failed a class.
	 * 
	 * @param records
	 *            student records
	 * @return list of jmbags
	 */
	private static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream()
						.filter(s -> s.getGrade() == 1)
						.sorted()
						.map(StudentRecord::getJmbag)
						.collect(Collectors.toList());
	}

	/**
	 * Returns a map of pairs (grade, list of student records). Students are
	 * separated by their grades.
	 * 
	 * @param records
	 *            student records
	 * @return map of pairs (grade, list of student records)
	 */
	private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		return records.stream()
						.collect(Collectors.groupingBy(StudentRecord::getGrade));
	}

	/**
	 * Returns a map of pairs (grade, number of students with that grade). Students
	 * are separated by their grades.
	 * 
	 * @param records
	 *            student records
	 * @return map of pairs (grade, number of students with that grade)
	 */
	private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		return records.stream()
						.collect(Collectors.toMap(StudentRecord::getGrade, s -> 1, (o, n) -> o + 1));
	}

	/**
	 * Returns a map of pairs (true of false, list of students that passed a class
	 * or not). Students are separated by condition if they passed the class.
	 * 
	 * @param records
	 *            student records
	 * @return map of pairs (true of false, list of students that passed a class or
	 *         not)
	 */
	private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		return records.stream()
						.collect(Collectors.partitioningBy(s -> s.getGrade() > 1));
	}

}
