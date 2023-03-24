package org.example;

import java.io.IOException;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APIConnector {

    //goal: write a method, that takes in a json string, and returns a BabyTVDTO
    public static BabyTVDTO convertBaby(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            BabyTVDTO dto = mapper.readValue(jsonString, BabyTVDTO.class);
            return dto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String makeGETRequest(String url){
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
            if (statusCode == 200) {
                return httpResponse.body();
            } else {
                // String.format is fun! Worth a Google if you're interested
                return String.format("GET request failed: %d status code received", statusCode);
            }
        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }
    }

    public static void main(String[] args) {
        //GOAL #1: Make a dynamic URL
        //I want to scanner to read in a tv show
        //and then I want to build the URl for that show

        final String ROOT_URL = "api.tvmaze.com";
        final String PROTOCOL = "https://";
        String path = "/singlesearch/shows";

        //0. Import Scanner
        //1. Init the scanner
        Scanner scan = new Scanner(System.in);
        //2. prompt the user (tell them what we need)
        System.out.println("Enter the name of a tv show");
        //3. receive the input
        String tvShowInput = scan.nextLine();

        String queryValue = tvShowInput.replaceAll(" ", "%20").replaceAll("'", "%27");
        queryValue = queryValue.toLowerCase();

        String URL = PROTOCOL + ROOT_URL + path + "?q=" + queryValue;
        System.out.println(URL);

        try {
            String jsonResponse = makeGETRequest(URL);
            System.out.println(jsonResponse);

            BabyTVDTO DTO = convertBaby(jsonResponse);
            System.out.println("Show Name: " + DTO.getName());
            System.out.println("Show Language: " + DTO.getLanguage());

            //what time does this show air?
                //Gonna need to:
                    //make the TVDTO with the data
                    //get the schedule
                    //then, get the time
            ObjectMapper mapper = new ObjectMapper();
            TVDTO tvdto = mapper.readValue(jsonResponse, TVDTO.class);
            System.out.println("Time: " + tvdto.getSchedule().getTime());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}
