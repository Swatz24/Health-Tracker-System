package userManagementSystem;

import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserManagementSystem {
    private List<User> users;
    public User currentUser;
    private static final String FILE_PATH = "C:\\CTAC\\Health-Tracker-System\\HealthTrackerSystem\\src\\userManagementSystem\\users.txt";

    public UserManagementSystem() {
        users = new ArrayList<>();
        currentUser = null;
        loadDataFromFile();
    }

    public boolean createUser(String username, String password) {
        if (findUser(username) == null) {
            User user = new User(username, password);
            users.add(user);
            System.out.println("User created successfully.");
            saveDataToFile();
            return true;
        } else {
            System.out.println("Username already exists. Please choose a different username.");
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        User user = findUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("User not found. Please check your username.");
            return false;
        }
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    private User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private void loadDataFromFile() {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2) { // Check if line has at least 2 elements
                    String username = parts[0];
                    String password = parts[1];
                    User user = new User(username, password);
                    users.add(user);
                } else {
                    System.out.println("Invalid data format in file: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing user data found. Starting with an empty user list.");
        }
    }


    private void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                writer.println(user.getUsername() + "," + user.getPassword());
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to save user data to file.");
        }
    }
}
