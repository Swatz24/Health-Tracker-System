import healthDataAnalysis.HealthDataAnalysis;
import userManagementSystem.UserManagementSystem;
import healthDataInput.CalorieIntake;
import healthDataInput.*;

import java.util.Scanner;

public class HealthTrackerSystem {
    public static void main(String[] args) {
        UserManagementSystem system = new UserManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (system.currentUser == null) {
                System.out.println("1. Create a new user");
                System.out.println("2. Log in");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter username: ");
                        String newUsername = scanner.next();
                        System.out.print("Enter password: ");
                        String newPassword = scanner.next();
                        system.createUser(newUsername, newPassword);
                        break;
                    case 2:
                        System.out.print("Enter username: ");
                        String username = scanner.next();
                        System.out.print("Enter password: ");
                        String password = scanner.next();
                        system.loginUser(username, password);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Logged in as " + system.currentUser.getUsername());
                CalorieIntake dailyCalorieIntake = new CalorieIntake(system.currentUser.getUsername());
                ExerciseActivity exerciseActivity = new ExerciseActivity(system.currentUser.getUsername());
                SleepRecord sleepRecords = new SleepRecord(system.currentUser.getUsername());
                System.out.println("1. Add Daily Calorie Intake");
                System.out.println("2. Log Exercise Activity");
                System.out.println("3. Log Sleep Record");
                System.out.println("4. Health Data Analysis");
                System.out.println("5. Logout");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter food item: ");
                        String foodItem = scanner.next();
                        System.out.print("Enter caloric value: ");
                        int caloricValue = scanner.nextInt();
                        System.out.print("Enter the date it was consumed (yyyy-MM-dd): ");
                        String date = scanner.next();


                        dailyCalorieIntake.addCalorieIntake(foodItem, caloricValue, date);
                        break;

                    case 2:
                        System.out.print("Enter exercise type: ");
                        String exerciseType = scanner.next();
                        System.out.print("Enter duration (in minutes): ");
                        int duration = scanner.nextInt();
                        System.out.print("Enter estimated calories burned: ");
                        int caloriesBurned = scanner.nextInt();
                        System.out.print("Enter the date it was done (yyyy-MM-dd): ");
                        String exerciseDate = scanner.next();


                        exerciseActivity.logExerciseActivity(exerciseType, duration, caloriesBurned, exerciseDate);
                        break;

                    case 3:
                        System.out.print("Enter sleep time (HH:mm): ");
                        String sleepTime = scanner.next();
                        System.out.print("Enter wake time (HH:mm): ");
                        String wakeTime = scanner.next();
                        System.out.print("Enter the date (yyyy-MM-dd): ");
                        String sleepDate = scanner.next();


                        sleepRecords.logSleepRecord(sleepTime, wakeTime, sleepDate);
                        break;
                    case 4:
                        // Health Data Analysis

                        System.out.println("1. Daily Caloric Balance");
                        System.out.println("2. Sleep Analysis");
                        System.out.println("3. Exercise Log");
                        System.out.println("4. Health Summary");
                        System.out.println("0. Exit");
                        System.out.print("Enter your choice: ");
                        int analysisChoice = scanner.nextInt();

                        switch (analysisChoice) {
                            case 1:
                                // Perform daily caloric balance analysis
                                // Retrieve and display the user's daily caloric balance
                                HealthDataAnalysis healthDataAnalysis = new HealthDataAnalysis(system.currentUser.getUsername());

                                // Calculate daily caloric balance
                                healthDataAnalysis.calculateDailyCaloricBalance();
                                break;

                            case 2:
                                // Perform sleep analysis
                                // Retrieve and display the user's sleep records
                                double averageSleepHours = sleepRecords.getAverageSleepHoursPerDay("2023-06-08", "2023-06-12");
                                System.out.println("Average sleep hours per day: " + averageSleepHours);

                                sleepRecords.identifyDaysWithLessSleepThanAverage("2023-06-08", "2023-06-12");

                                break;

                            case 3:

                                exerciseActivity.displayExerciseLog(system.currentUser.getUsername());

                                exerciseActivity.displayCategorySummaries(system.currentUser.getUsername());
                                break;

                            case 4:
                                // Perform health summary analysis
                                // Retrieve and display the user's health summary
                                System.out.print("Enter start date (yyyy-MM-dd): ");
                                String startDate = scanner.next();

                                System.out.print("Enter end date (yyyy-MM-dd): ");
                                String endDate = scanner.next();

                                exerciseActivity.displayHealthSummary(startDate, endDate);
                                break;

                            case 0: break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                        break;
                    case 5:
                        system.logout();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }
}
