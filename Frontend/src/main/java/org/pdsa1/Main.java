package org.pdsa1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private static final String BASE_URL = "http://localhost:8094/journey-genie-backend-api/locations/search?name=";
    private static final String POST_URL = "http://localhost:8094/journey-genie-backend-api/locations/";
    private static final String TASK_POST_URL = "http://localhost:8095/journey-genie-backend-api/tasks/";

    public static void main(String[] args) {
        displayWelcomeMessage();
        String locationName = getUserLocationWithConfirmation();

        try {
            String locationInfo = fetchLocationInfo(locationName);
            System.out.println("Location Information: " + locationInfo);
            double[] coordinates = extractCoordinatesFromJson(locationInfo);
            addLocationToDatabase(locationName, coordinates[0], coordinates[1]);

            do {
                int locationId = addTaskWithLocation();
                addTaskToDatabase(locationId);
            } while (askForMoreTasks());

            askForReorderingOptions();
        } catch (IOException e) {
            System.out.println("Error fetching location information: " + e.getMessage());
        }
    }

    private static void displayWelcomeMessage() {
        System.out.println("=====================================");
        System.out.println(" Welcome to Smart-To-Do-List ");
        System.out.println("=====================================");
        System.out.println("Manage your tasks efficiently and effectively.");
        System.out.println("Let's get started!");
    }

    private static String getUserLocationWithConfirmation() {
        Scanner scanner = new Scanner(System.in);
        String locationName;
        boolean confirmed = false;

        do {
            System.out.print("Please enter your current location name: ");
            locationName = scanner.nextLine();

            String locationInfo;
            try {
                locationInfo = fetchLocationInfo(locationName);
                System.out.println("Location Information: " + locationInfo);
            } catch (IOException e) {
                System.out.println("Error fetching location information: " + e.getMessage());
                continue;
            }

            System.out.print("Is this the correct location? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            confirmed = confirmation.equals("yes");
            if (!confirmed) {
                System.out.println("Let's try again.");
            }
        } while (!confirmed);

        return locationName;
    }

    private static String fetchLocationInfo(String locationName) throws IOException {
        URL url = new URL(BASE_URL + locationName);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new IOException("GET request failed: " + conn.getResponseCode());
        }
    }

    private static int addLocationToDatabase(String name, double latitude, double longitude) throws IOException {
        URL url = new URL(POST_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = String.format("{\"name\": \"%s\", \"latitude\": %f, \"longitude\": %f}", name, latitude, longitude);

        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes(jsonInputString);
            wr.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            System.out.println("Location added successfully.");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return extractIdFromJson(response.toString());
        } else {
            System.out.println("Failed to add location. Response Code: " + responseCode);
            return -1;
        }
    }

    private static int addTaskWithLocation() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the initial task name: ");
        String taskName = scanner.nextLine();

        String taskLocationName;
        boolean confirmedLocation = false;
        int locationId = -1;

        do {
            System.out.print("Please enter the associated location name for this task: ");
            taskLocationName = scanner.nextLine();

            String taskLocationInfo;
            try {
                taskLocationInfo = fetchLocationInfo(taskLocationName);
                System.out.println("Task Location Information: " + taskLocationInfo);

                System.out.print("Is this the correct associated location? (yes/no): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();
                confirmedLocation = confirmation.equals("yes");

                if (confirmedLocation) {
                    double[] coordinates = extractCoordinatesFromJson(taskLocationInfo);
                    locationId = addLocationToDatabase(taskLocationName, coordinates[0], coordinates[1]);
                    if (locationId == -1) {
                        System.out.println("Failed to add location, please try again.");
                        confirmedLocation = false;
                    }
                } else {
                    System.out.println("Let's try again.");
                }
            } catch (IOException e) {
                System.out.println("Error fetching task location information: " + e.getMessage());
            }
        } while (!confirmedLocation);

        return locationId;
    }

    private static void addTaskToDatabase(int locationId) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int priorityLevel;

        do {
            System.out.print("Enter the priority of the task (1 for high,... 2 for medium, 3 for low): ");

            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number (1, 2, or 3): ");
                scanner.next();
            }

            priorityLevel = scanner.nextInt();

            if (priorityLevel < 1 || priorityLevel > 3) {
                System.out.println("Invalid priority level. Please enter a number between 1 and 3.");
            }

        } while (priorityLevel < 1 || priorityLevel > 3);

        scanner.nextLine();
        System.out.print("Enter the task name again for saving: ");
        String taskName = scanner.nextLine();

        URL url = new URL(TASK_POST_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = String.format("{\"name\": \"%s\", \"priority\": %d, \"location_id\": %d}", taskName, priorityLevel, locationId);

        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes(jsonInputString);
            wr.flush();
        }

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            System.out.println("Task added successfully.");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("Response from server: " + response.toString());

        } else {
            System.out.println("Failed to add task. Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuilder errorResponse = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                errorResponse.append(inputLine);
            }

            System.out.println("Error Response: " + errorResponse.toString());
        }
    }

    private static boolean askForMoreTasks() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you have any more tasks to add? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes");
    }

    private static void askForReorderingOptions() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nWhat reordering is required?");
            System.out.println("1. Default");
            System.out.println("2. Priority");
            System.out.println("3. Distance");
            System.out.print("Please select an option (1-3): ");

            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number between 1 and 3: ");
                scanner.next();
            }
            choice = scanner.nextInt();

            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (choice < 1 || choice > 3);

        switch(choice) {
            case 1:
                handleDefaultOrdering();
                break;
            case 2:
                handlePriorityOrdering();
                break;
            case 3:
                handleDistanceOrdering();
                break;
        }
    }

    private static void handleDefaultOrdering() throws IOException {
        URL url = new URL("http://localhost:8095/journey-genie-backend-api/tasks/show?criteria=default");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // Show JSON objects
            displayTasks(response.toString());
        } else {
            throw new IOException("GET request failed: " + conn.getResponseCode());
        }
    }

    private static void handlePriorityOrdering() throws IOException {
        // First reorder by priority
        URL reorderUrl = new URL("http://localhost:8095/journey-genie-backend-api/tasks/reorder/priority");
        HttpURLConnection reorderConn = (HttpURLConnection) reorderUrl.openConnection();
        reorderConn.setRequestMethod("GET");

        if (reorderConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Reorder request failed: " + reorderConn.getResponseCode());
        }

        // Then show tasks by priority
        URL showUrl = new URL("http://localhost:8095/journey-genie-backend-api/tasks/show?criteria=priority");
        HttpURLConnection showConn = (HttpURLConnection) showUrl.openConnection();
        showConn.setRequestMethod("GET");

        if (showConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(showConn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // Show JSON objects
            displayTasks(response.toString());
        } else {
            throw new IOException("GET request failed: " + showConn.getResponseCode());
        }
    }

    private static void handleDistanceOrdering() throws IOException {
        // Step 1: Add locations to graph
        URL addToGraphUrl = new URL("http://localhost:8094/journey-genie-backend-api/locations/add-to-graph");
        HttpURLConnection addToGraphConn= (HttpURLConnection) addToGraphUrl.openConnection();
        addToGraphConn.setRequestMethod("POST");

        if (addToGraphConn.getResponseCode() != HttpURLConnection.HTTP_OK && addToGraphConn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new IOException("Add to graph request failed: " + addToGraphConn.getResponseCode());
        }

        // Step 2: Create edges
        URL createEdgesUrl= new URL ("http://localhost:8094/journey-genie-backend-api/locations/create-edges");
        HttpURLConnection createEdgesConn= (HttpURLConnection) createEdgesUrl.openConnection();
        createEdgesConn.setRequestMethod ("POST");

        if(createEdgesConn.getResponseCode() != HttpURLConnection.HTTP_OK && createEdgesConn.getResponseCode() != HttpURLConnection.HTTP_CREATED){
            throw new IOException ("Create edges request failed:" + createEdgesConn.getResponseCode ());
        }

        // Step 3: Calculate distances
        URL calculateDistancesUrl= new URL ("http://localhost:8094/journey-genie-backend-api/locations/calculate-distances");
        HttpURLConnection calculateDistancesConn= (HttpURLConnection) calculateDistancesUrl.openConnection ();
        calculateDistancesConn.setRequestMethod ("GET");

        if(calculateDistancesConn.getResponseCode () == HttpURLConnection.HTTP_OK){
            BufferedReader in= new BufferedReader(new InputStreamReader(calculateDistancesConn.getInputStream()));
            while(in.readLine () != null); // Consume the response without storing it
        } else {
            throw new IOException ("Calculate distances request failed:" + calculateDistancesConn.getResponseCode ());
        }

        // Step 4: Get optimal path
        URL optimalPathUrl= new URL ("http://localhost:8094/journey-genie-backend-api/locations/optimal-path?sourceId=1");
        HttpURLConnection optimalPathConn= (HttpURLConnection) optimalPathUrl.openConnection ();
        optimalPathConn.setRequestMethod ("GET");

        if(optimalPathConn.getResponseCode () == HttpURLConnection.HTTP_OK){
            BufferedReader in= new BufferedReader(new InputStreamReader(optimalPathConn.getInputStream()));
            while(in.readLine () != null); // Consume the response without storing it
        } else {
            throw new IOException ("Optimal path request failed:" + optimalPathConn.getResponseCode ());
        }

        // Step 5: Show tasks by distance
        URL showDistanceUrl= new URL ("http://localhost:8095/journey-genie-backend-api/tasks/show?criteria=distance");
        HttpURLConnection showDistanceConn= (HttpURLConnection) showDistanceUrl.openConnection ();
        showDistanceConn.setRequestMethod ("GET");

        if(showDistanceConn.getResponseCode () == HttpURLConnection.HTTP_OK){
            BufferedReader in= new BufferedReader(new InputStreamReader(showDistanceConn.getInputStream()));
            String inputLine;
            StringBuilder response= new StringBuilder ();
            while((inputLine= in.readLine ()) != null){
                response.append(inputLine);
            }
            in.close ();
            // Show JSON objects
            displayTasks(response.toString());
        } else {
            throw new IOException ("Show distance tasks request failed:" + showDistanceConn.getResponseCode ());
        }
    }

    private static void displayTasks(String jsonResponse){
        JSONArray tasksArray= new JSONArray(jsonResponse); // Parse JSON array of tasks
        for(int i=0; i<tasksArray.length(); i++){
            JSONObject taskObject= tasksArray.getJSONObject(i); // Get each task object
            // Display relevant details of each task
            System.out.printf(
                    "Task ID: %d\nTask Name: %s\nPriority Level: %d\n\n",
                    taskObject.optInt ("id"),
                    taskObject.optString ("name"),
                    taskObject.optInt ("priority")
            );
        }
    }

    private static int extractIdFromJson(String jsonResponse) {
        jsonResponse.trim();

        if (jsonResponse.startsWith("{")) { // Single object
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                return jsonObject.getInt("id");
            }
        } else if (jsonResponse.startsWith("[") && !jsonResponse.equals("[{}]")) { // Array with at least one object
            JSONArray jsonArray= new JSONArray(jsonResponse);

            if(jsonArray.length()>0){
                JSONObject jsonObject=jsonArray.getJSONObject(0);

                if(jsonObject.has ("id") && !jsonObject.isNull ("id")){
                    return jsonObject.getInt ("id");
                }
            }
        }

        return -1;
    }

    private static double[] extractCoordinatesFromJson(String jsonResponse) {
        JSONObject jsonObject=new JSONObject(jsonResponse);
        double latitude=jsonObject.optDouble ("latitude");
        double longitude=jsonObject.optDouble ("longitude");
        return new double[]{latitude, longitude};
    }
}
