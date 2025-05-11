import java.io.*;
import java.util.*;

// Student class for managing student data
class Student implements Serializable {
    String name;
    List<Integer> grades;

    Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    void addGrade(int grade) {
        grades.add(grade);
    }

    double getAverage() {
        if (grades.isEmpty())
            return 0.0;
        int sum = 0;
        for (int g : grades) {
            sum += g;
        }
        return sum / (double) grades.size();
    }

    boolean isUnderperforming() {
        return getAverage() < 60.0;
    }

    @Override
    public String toString() {
        return name + " | Grades: " + grades + " | Avg: " + String.format("%.2f", getAverage());
    }
}

// Main Gradebook class with menu and persistence
public class Gradebook {
    static Scanner scanner = new Scanner(System.in);
    static List<Student> students = new ArrayList<>();
    static final String FILE_NAME = "students.dat";

    public static void main(String[] args) {
        loadStudents();
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getChoice();
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGrade();
                case 3 -> showAllStudents();
                case 4 -> showUnderperformers();
                case 5 -> searchStudent();
                case 6 -> {
                    saveStudents();
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void displayMenu() {
        System.out.println("\n==== Gradebook Menu ====");
        System.out.println("1. Add Student");
        System.out.println("2. Add Grade to Student");
        System.out.println("3. Show All Students & Averages");
        System.out.println("4. Show Underperforming Students");
        System.out.println("5. Search for Student");
        System.out.println("6. Save & Exit");
        System.out.print("Enter your choice: ");
    }

    static int getChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return choice;
    }

    static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        students.add(new Student(name));
        System.out.println("Added student: " + name);
    }

    static void addGrade() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        Student student = findStudent(name);
        if (student != null) {
            System.out.print("Enter grade (0-100): ");
            int grade = scanner.nextInt();
            scanner.nextLine();
            student.addGrade(grade);
            System.out.println("Grade added.");
        } else {
            System.out.println("Student not found.");
        }
    }

    static void showAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
        }
    }

    static void showUnderperformers() {
        boolean found = false;
        for (Student s : students) {
            if (s.isUnderperforming()) {
                System.out.println("⚠ " + s.name + " | Avg: " + String.format("%.2f", s.getAverage()));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No underperforming students.");
        }
    }

    static void searchStudent() {
        System.out.print("Enter student name to search: ");
        String name = scanner.nextLine();
        Student s = findStudent(name);
        if (s != null) {
            System.out.println("Found: " + s);
        } else {
            System.out.println("Student not found.");
        }
    }

    static Student findStudent(String name) {
        for (Student s : students) {
            if (s.name.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static void loadStudents() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (List<Student>) in.readObject();
            System.out.println("Loaded saved student data.");
        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }

    static void saveStudents() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(students);
            System.out.println("Data saved to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
