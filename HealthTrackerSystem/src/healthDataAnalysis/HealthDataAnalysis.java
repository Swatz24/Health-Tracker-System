package healthDataAnalysis;
import healthDataInput.*;
import userManagementSystem.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HealthDataAnalysis {
    private CalorieIntake calorieIntake;
    private ExerciseActivity exerciseActivity;
    private Scanner scanner;
    private String username;

    public HealthDataAnalysis(String username) {
        this.username = username;
        calorieIntake = new CalorieIntake(username);
        exerciseActivity = new ExerciseActivity(username);
        scanner = new Scanner(System.in);
    }

    public void calculateDailyCaloricBalance() {
        System.out.print("Enter the date (yyyy-MM-dd): ");
        String date = scanner.next();

        int consumedCalories = calorieIntake.getCalorieIntakeByDate(date);
        int burnedCalories = exerciseActivity.getCaloriesBurnedByDate(username,date);
        int caloricBalance = consumedCalories - burnedCalories;

        System.out.println("Daily Caloric Balance for " + date);
        System.out.println("Consumed Calories: " + consumedCalories);
        System.out.println("Burned Calories: " + burnedCalories);
        System.out.println("Caloric Balance: " + caloricBalance);
    }


}

