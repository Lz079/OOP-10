import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleNoteApp extends Application {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/notes_db";
    private static final String USER = "root"; // Default username for XAMPP
    private static final String PASSWORD = ""; // Default password (leave empty if not set)
    private List<String> notes = new ArrayList<>();
    private TextArea textArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        loadNotes();

        primaryStage.setTitle("Simple Note App");

        // Create UI components
        textArea = new TextArea();
        Button saveButton = new Button("Save Note");
        saveButton.setOnAction(e -> addNote());

        Button viewButton = new Button("View Notes");
        viewButton.setOnAction(e -> viewNotes());

        VBox vbox = new VBox(textArea, saveButton, viewButton);
        Scene scene = new Scene(vbox, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Add note to the list and save it to the database
    private void addNote() {
        String note = textArea.getText();
        if (!note.trim().isEmpty()) {
            notes.add(note);
            textArea.clear();
            saveNoteToDatabase(note);
        }
    }

    // Save the note to the database
    private void saveNoteToDatabase(String note) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO notes (content) VALUES (?)")) {
            pstmt.setString(1, note);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving note to database: " + e.getMessage());
        }
    }

    // View all notes
    private void viewNotes() {
        StringBuilder allNotes = new StringBuilder();
        for (String note : notes) {
            allNotes.append(note).append("\n");
        }
        textArea.setText(allNotes.toString());
    }

    // Load notes from the database
    private void loadNotes() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT content FROM notes")) {
            while (rs.next()) {
                notes.add(rs.getString("content"));
            }
        } catch (SQLException e) {
            System.out.println("No previous notes found: " + e.getMessage());
        }
    }
}