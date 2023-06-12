package healthDataInput;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class SleepRecord {
    private Map<String, Integer> sleepRecordsMap;
    private static final String FILE_PATH = "C:\\CTAC\\Health-Tracker-System\\HealthTrackerSystem\\src\\healthDataInput\\sleepRecords.txt";
    private String username;

    public SleepRecord(String username) {
        this.username = username;
        sleepRecordsMap = new HashMap<>();
        loadDataFromFile();
    }

    public void logSleepRecord(String sleepTime, String wakeTime, String date) {
        int totalSleepHours = calculateTotalSleepHours(sleepTime, wakeTime);

        String key = username + "_" + date;
        String data = username + "," + sleepTime + "," + wakeTime + "," + date + "," + totalSleepHours;
        sleepRecordsMap.put(key, totalSleepHours);
        saveDataToFile(data);
        System.out.println("Sleep record logged successfully.");
    }

    public int getTotalSleepHours() {
        return sleepRecordsMap.getOrDefault(username, 0);
    }

    private int calculateTotalSleepHours(String sleepTime, String wakeTime) {
        int sleepHour = Integer.parseInt(sleepTime.split(":")[0]);
        int sleepMinute = Integer.parseInt(sleepTime.split(":")[1]);
        int wakeHour = Integer.parseInt(wakeTime.split(":")[0]);
        int wakeMinute = Integer.parseInt(wakeTime.split(":")[1]);

        // If wake time is earlier than sleep time, it means the sleep duration crosses midnight
        if (wakeHour < sleepHour || (wakeHour == sleepHour && wakeMinute < sleepMinute)) {
            // Add 24 hours to the wake time to account for the crossing of midnight
            wakeHour += 24;
        }

        int totalSleepMinutes = (wakeHour * 60 + wakeMinute) - (sleepHour * 60 + sleepMinute);
        return totalSleepMinutes / 60; // Convert to hours
    }

    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String key = parts[0] + "_" + parts[3];
                    int totalSleepHours = Integer.parseInt(parts[4]);

                    sleepRecordsMap.put(key, totalSleepHours);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing sleep records found. Starting with an empty map.");
        }
    }

    private void saveDataToFile(String data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(data);
        } catch (IOException e) {
            System.out.println("Failed to save sleep record to file.");
        }
    }

    public double getAverageSleepHoursPerDay(String startDate, String endDate) {
        int totalSleepHours = 0;
        int totalDays = 0;

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            String key = username + "_" + date.toString();
            if (sleepRecordsMap.containsKey(key)) {
                totalSleepHours += sleepRecordsMap.get(key);
                totalDays++;
            }
        }

        return (double) totalSleepHours / totalDays;
    }

    public void identifyDaysWithLessSleepThanAverage(String startDate, String endDate) {
        double averageSleepHours = getAverageSleepHoursPerDay(startDate, endDate);

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            String key = username + "_" + date.toString();
            if (sleepRecordsMap.containsKey(key)) {
                int sleepHours = sleepRecordsMap.get(key);
                if (sleepHours < averageSleepHours) {
                    System.out.println("On " + date.toString() + ", you slept " + sleepHours + " hours, which is less than the average of " + averageSleepHours + " hours.");
                }
            }
        }
    }

    public int getTotalSleepHoursInRange(String startDate, String endDate) {
        int totalSleepHours = 0;

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            String key = username + "_" + date.toString();
            if (sleepRecordsMap.containsKey(key)) {
                totalSleepHours += sleepRecordsMap.get(key);
            }
        }

        return totalSleepHours;
    }

}
