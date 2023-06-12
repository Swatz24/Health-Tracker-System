package healthDataInput;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class ExerciseActivity {
    private Map<String, Integer> exerciseActivityMap;
        private static final String FILE_PATH = "C:\\CTAC\\Health-Tracker-System\\HealthTrackerSystem\\src\\healthDataInput\\exerciseActivityData.txt";
        private String username;

    private CalorieIntake calorieIntake;
    private CalorieIntake calorieIntake;
    private SleepRecord sleepRecord;
    public ExerciseActivity(String username) {
        this.username = username;
        exerciseActivityMap = new HashMap<>();

        calorieIntake = new CalorieIntake(username);
        sleepRecord = new SleepRecord(username);
        loadDataFromFile();

    }

    public void logExerciseActivity(String exerciseType, int duration, int caloriesBurned, String date) {
        String key = username + "_" + date;
        String data = username + "," + exerciseType + "," + duration + "," + caloriesBurned + "," + date;
        exerciseActivityMap.put(key, caloriesBurned);
        saveDataToFile(data);
        System.out.println("Exercise activity logged successfully.");
    }

    public int getCaloriesBurnedByDate(String username, String date) {
        int totalCaloriesBurned = 0;
        for (Map.Entry<String, Integer> entry : exerciseActivityMap.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split("_");
            String entryUsername = parts[0];
            String entryDate = parts[1];
            if (entryUsername.equals(username) && entryDate.equals(date)) {
                int caloriesBurned = entry.getValue();
                totalCaloriesBurned += caloriesBurned;
            }
        }
        return totalCaloriesBurned;
    }

    public void displayExerciseLog(String username) {
        List<String> userEntries = new ArrayList<>();
        loadDataFromFile();
        for (Map.Entry<String, Integer> entry : exerciseActivityMap.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split("_");
            String entryUsername = parts[0];
            if (entryUsername.equals(username)) {
                userEntries.add(key);
            }
        }

        if (userEntries.isEmpty()) {
            System.out.println("No exercise activity found for user: " + username);
            return;
        }

        System.out.println("Exercise Log for user: " + username);
        for (String key : userEntries) {
            String[] parts = key.split("_");
            String date = parts[1];
            int caloriesBurned = exerciseActivityMap.get(key);
            System.out.println("Username: " + username);
            System.out.println("Date: " + date);
            System.out.println("Exercise Type: " + getExerciseTypeByKey(key));
            System.out.println("Duration: " + getDurationByKey(key) + " minutes");
            System.out.println("Calories Burned: " + caloriesBurned);
            System.out.println();
        }
    }

    public void displayCategorySummaries(String username) {
        Map<String, Integer> categorySummaries = new HashMap<>();
        loadDataFromFile();

        for (Map.Entry<String, Integer> entry : exerciseActivityMap.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split("_");
            String entryUsername = parts[0];
            if (entryUsername.equals(username)) {
                String exerciseType = getExerciseTypeByKey(key);
                int caloriesBurned = entry.getValue();
                categorySummaries.put(exerciseType, categorySummaries.getOrDefault(exerciseType, 0) + caloriesBurned);
            }
        }

        System.out.println("Category Summaries for user: " + username);
        for (Map.Entry<String, Integer> entry : categorySummaries.entrySet()) {
            String exerciseType = entry.getKey();
            int caloriesBurned = entry.getValue();
            System.out.println("Exercise Type: " + exerciseType);
            System.out.println("Total Calories Burned: " + caloriesBurned);
            System.out.println();
        }
    }

    private String getExerciseTypeByKey(String key) {
        String[] parts = key.split("_");
        String username = parts[0];
        String date = parts[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineParts = line.split(",");
                if (lineParts.length >= 5 && lineParts[0].equals(username) && lineParts[4].equals(date)) {
                    return lineParts[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private int getDurationByKey(String key) {
        String[] parts = key.split("_");
        String username = parts[0];
        String date = parts[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineParts = line.split(",");
                if (lineParts.length >= 5 && lineParts[0].equals(username) && lineParts[4].equals(date)) {
                    return Integer.parseInt(lineParts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String key = parts[0] + "_" + parts[4];
                    int caloriesBurned = Integer.parseInt(parts[3]);

                    exerciseActivityMap.put(key, caloriesBurned);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing exercise activity data found. Starting with an empty map.");
        }
    }


    private String findMostCommonExerciseType(Map<String, Integer> exerciseTypeCounts) {
        String mostCommonExerciseType = "";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : exerciseTypeCounts.entrySet()) {
            String exerciseType = entry.getKey();
            int count = entry.getValue();
            if (count > maxCount) {
                mostCommonExerciseType = exerciseType;
                maxCount = count;
            }
        }
        return mostCommonExerciseType;
    }

    private boolean isDateInRange(Date date, String startDate, String endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            // Set the time component of the start and end dates to midnight
            start = removeTimeComponent(start);
            end = removeTimeComponent(end);
            date = removeTimeComponent(date);

            return date.compareTo(start) >= 0 && date.compareTo(end) <= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Date removeTimeComponent(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public void displayHealthSummary(String startDate, String endDate) {
        int totalCaloriesBurned = 0;
        int totalCaloriesConsumed = 0;
        int totalHoursOfSleep = 0;
        int entryCount = 0;
        Map<String, Integer> exerciseTypeCounts = new HashMap<>();

        for (Map.Entry<String, Integer> entry : exerciseActivityMap.entrySet()) {
            String key = entry.getKey();
            Date entryDate = getDateByKey(key);
            if (isDateInRange(entryDate, startDate, endDate)) {
             //   totalCaloriesBurned += entry.getValue();
                String exerciseType = getExerciseTypeByKey(key);
                exerciseTypeCounts.put(exerciseType, exerciseTypeCounts.getOrDefault(exerciseType, 0) + 1);
                entryCount++;
            }
        }
        totalCaloriesBurned = getTotalCaloriesBurned(startDate, endDate); // Calculate total calories burned
        totalCaloriesConsumed = calorieIntake.getTotalCaloriesConsumed(startDate,endDate);
        totalHoursOfSleep = sleepRecord.getTotalSleepHoursInRange(startDate,endDate);

        System.out.println("Health Summary");
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
        System.out.println("Total Calories Consumed: " + totalCaloriesConsumed);
        System.out.println("Total Hours of Sleep: " + totalHoursOfSleep);

        if (!exerciseTypeCounts.isEmpty()) {
            String mostCommonExerciseType = findMostCommonExerciseType(exerciseTypeCounts);
            System.out.println("Most Common Exercise Type: " + mostCommonExerciseType);
        }

        System.out.println();
    }




    public int getTotalCaloriesBurned(String startDate, String endDate) {
        int totalCaloriesBurned = 0;

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        for (LocalDate date = start; date.isBefore(end.plusDays(1)); date = date.plusDays(1)) {
            String formattedDate = date.toString();
            totalCaloriesBurned += getCaloriesBurnedByDate(username, formattedDate);
        }

        return totalCaloriesBurned;
    }


    private Date getDateByKey(String key) {
        String[] parts = key.split("_");
        String username = parts[0];
        String date = parts[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineParts = line.split(",");
                if (lineParts.length >= 5 && lineParts[0].equals(username) && lineParts[4].equals(date)) {
                    return parseDate(lineParts[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void saveDataToFile(String data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.print(data + "\n");
        } catch (IOException e) {
            System.out.println("Failed to save exercise activity data to file.");
        }
    }

    private String getFormattedDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}

