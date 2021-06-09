package me.alexiriving992.offtransfer.main;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Project created by ExpDev
 */

public class MojangAuth {

    /**
     * Runs when the program starts
     */

    public static String main(String player) {
        // The endpoint which we will send out payload to
        String requestUrl = "https://api.mojang.com/profiles/minecraft";
        // The player name which we are testing for
        String playerName = player;

        // Our payload array/object which we will send to our endpoint
        JSONArray payloadObject = new JSONArray();
        // Putting the player name in our array
        payloadObject.put(playerName);

        // Our string builder which we will append our results to later
        StringBuilder sb = new StringBuilder();

        try {
            // Using the String and turning into an URL object
            URL url = new URL(requestUrl);
            // Opening a connection with our URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // The connection should be able to handle inputs and outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            // According to mojang, this should be a POST request with a payload
            connection.setRequestMethod("POST");
            // We want JSON back
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // So we can write the payload
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payloadObject.toString()); // = [%playerName%]
            // Closing the writer is important
            writer.close();

            // So we can read our response
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            // Closing the reader
            br.close();
            // Disconnecting the connection
            connection.disconnect();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


        String response = sb.toString();
        Gson gson = new Gson();
        JsonArray json = gson.fromJson(response, JsonArray.class);
        return (json.get(0).getAsJsonObject().get("id").getAsString());


    }

}