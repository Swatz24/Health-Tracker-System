package healthDataInput;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CalorieIntake {

    private String username;
    private Map<String, Integer> calorieIntakeMap;
    private static final String FILE_PATH = "C:\\CTAC\\Health-Tracker-System\\HealthTrackerSystem\\src\\healthDataInput\\calorieIntakeData.txt";

    public CalorieIntake(String username) {
        this.username = username;
        calorieIntakeMap = new HashMap<>();
        loadDataFromFile();
    }

    public void addCalorieIntake(String foodItem, int caloricValue, String date) {
        if (username != null) {
            String data = username + "," + foodItem + "," + caloricValue + "," + date;
            String key = username + "_" + date;
            calorieIntakeMap.put(key, caloricValue);
            saveDataToFile(data);
            System.out.println("Calorie intake added successfully.");
        } else {
            System.out.println("No user currently logged in.");
        }
    }


    public int getCalorieIntakeByDate(String date) {
        String key = this.username + "_" + date;
        return calorieIntakeMap.getOrDefault(key, 0);
    }




    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String key = parts[0] + "_" + parts[3];
                    int calories = Integer.parseInt(parts[2]);
                    calorieIntakeMap.put(key, calories);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing calorie intake data found. Starting with an empty map.");
        }
    }

    private void saveDataToFile(String data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.print(data);
        } catch (IOException e) {
            System.out.println("Failed to save calorie intake data to file.");
        }
    }

    public int getTotalCaloriesConsumed(String startDate, String endDate) {
        int totalCaloriesConsumed = 0;

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            String formattedDate = date.toString();
            totalCaloriesConsumed += getCalorieIntakeByDate(formattedDate);
        }

        return totalCaloriesConsumed;
    }




}
