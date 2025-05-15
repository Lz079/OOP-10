import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimpleNoteApp {
    private static final String FILE_NAME = "notes.txt";
    private List<String> notes = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SimpleNoteApp app = new SimpleNoteApp();
        app.run();
    }

    private void run() {
        loadNotes();
        String command;
        do {
            System.out.println("\n1. View Notes\n2. Add Note\n3. Exit");
            System.out.print("Choose an option: ");
            command = scanner.nextLine();

            switch (command) {
                case "1":
                    viewNotes();
                    break;
                case "2":
                    addNote();
                    break;
            }
        } while (!command.equals("3"));
        saveNotes();
    }

    private void viewNotes() {
        System.out.println("Your Notes:");
        for (String note : notes) {
            System.out.println("- " + note);
        }
    }

    private void addNote() {
        System.out.print("Enter a note: ");
        String note = scanner.nextLine();
        notes.add(note);
        System.out.println("Note added!");
    }

    private void loadNotes() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                notes.add(line);
            }
        } catch (IOException e) {
            System.out.println("No previous notes found.");
        }
    }

    private void saveNotes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String note : notes) {
                bw.write(note);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving notes.");
        }
    }
}