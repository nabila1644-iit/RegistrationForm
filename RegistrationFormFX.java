package com.example.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class RegistrationFormFX extends Application {

    private String selectedFilePath = "";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Registration Form with File Save");


        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);


        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);


        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        grid.add(emailLabel, 0, 1);
        grid.add(emailField, 1, 1);


        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();
        grid.add(phoneLabel, 0, 2);
        grid.add(phoneField, 1, 2);


        Label genderLabel = new Label("Experienced:");
        RadioButton male = new RadioButton("Yes");
        RadioButton female = new RadioButton("No");
        ToggleGroup genderGroup = new ToggleGroup();
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        HBox genderBox = new HBox(10, male, female);
        grid.add(genderLabel, 0, 3);
        grid.add(genderBox, 1, 3);


        Label countryLabel = new Label("Position:");
        ComboBox<String> countryBox = new ComboBox<>();
        countryBox.getItems().addAll("Select", "Goalkeeper", "Defender", "Mid-fielder", "Forwards","Striker");
        countryBox.getSelectionModel().selectFirst();
        grid.add(countryLabel, 0, 4);
        grid.add(countryBox, 1, 4);


        Label dobLabel = new Label("Date of Birth:");
        ComboBox<String> dayBox = new ComboBox<>();
        for (int i = 1; i <= 31; i++) dayBox.getItems().add(String.valueOf(i));
        dayBox.getSelectionModel().selectFirst();

        ComboBox<String> monthBox = new ComboBox<>();
        monthBox.getItems().addAll("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        monthBox.getSelectionModel().selectFirst();

        ComboBox<String> yearBox = new ComboBox<>();
        for (int i = 0; i < 100; i++) yearBox.getItems().add(String.valueOf(2025 - i));
        yearBox.getSelectionModel().selectFirst();

        HBox dobBox = new HBox(10, dayBox, monthBox, yearBox);
        grid.add(dobLabel, 0, 5);
        grid.add(dobBox, 1, 5);


        Label fileLabel = new Label("Profile Photo:");
        Button fileButton = new Button("Choose File");
        Label filePathLabel = new Label();
        filePathLabel.setMaxWidth(200);
        filePathLabel.setWrapText(true);

        fileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                selectedFilePath = file.getAbsolutePath();
                filePathLabel.setText(file.getName());
            }
        });

        HBox fileBox = new HBox(10, fileButton, filePathLabel);
        fileBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(fileLabel, 0, 6);
        grid.add(fileBox, 1, 6);


        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String gender = genderGroup.getSelectedToggle() == null ? "Not selected" :
                    ((RadioButton) genderGroup.getSelectedToggle()).getText();
            String country = countryBox.getValue();
            String dob = dayBox.getValue() + "-" + monthBox.getValue() + "-" + yearBox.getValue();
            String photo = selectedFilePath.isEmpty() ? "Not uploaded" : selectedFilePath;

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                    || gender.equals("Not selected") || country.equals("Select")) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields!");
            } else if (selectedFilePath.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Field", "Please upload a profile photo.");
                return;

            } else {
                try (FileWriter writer = new FileWriter("registration_data.txt", true)) {
                    writer.write("Name: " + name + "\n");
                    writer.write("Email: " + email + "\n");
                    writer.write("Phone: " + phone + "\n");
                    writer.write("Gender: " + gender + "\n");
                    writer.write("DOB: " + dob + "\n");
                    writer.write("Country: " + country + "\n");
                    writer.write("Profile Photo: " + photo + "\n");
                    writer.write("----------------------------------\n");

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Saved Successfully!");

                    // Clear fields
                    nameField.clear();
                    emailField.clear();
                    phoneField.clear();
                    genderGroup.selectToggle(null);
                    countryBox.getSelectionModel().selectFirst();
                    dayBox.getSelectionModel().selectFirst();
                    monthBox.getSelectionModel().selectFirst();
                    yearBox.getSelectionModel().selectFirst();
                    filePathLabel.setText("");
                    selectedFilePath = "";
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "File Error", "Error writing to file!");
                    ex.printStackTrace();
                }
            }
        });

        VBox root = new VBox(20, grid, registerBtn);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 550, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
