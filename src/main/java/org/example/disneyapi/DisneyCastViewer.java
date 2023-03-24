package org.example.disneyapi;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisneyCastViewer {
    final static String ROOT_URL = "https://api.disneyapi.dev/character";

    public static void main(String[] args) {

        String movie = getFilm();
        System.out.println(movie);

        String movieURL = formatURL(ROOT_URL, movie);
        System.out.println(movieURL);

        String json = letsGetIt(movieURL);
        //System.out.println(json);

        CastDTO dto = convert(json);
        List<Data> characters = dto.getData();
        writeToFile(grabURLS(characters,movie));
        //System.out.println(characters.get(0).getName());
        /*
        for (Data character : characters) {
            if (character.getFilms().contains(movie)) {
                System.out.println(character.getName());
                //System.out.println("Films: " + character.getFilms().contains(movie));
            }
        }
        */
    }

    public static String getFilm() {
        Scanner scan = new Scanner(System.in);
        System.out.println("What disney movie title?");
        String movie = scan.nextLine();

        return movie;
    }

    //goal: take in a movie name and return the formatted URL
    public static String formatURL(String URL, String movie) {
        movie = movie.replaceAll(" ", "%20");
        movie = movie.replaceAll("'", "%27");

        String bigBoyURL = URL + "?films=" + movie;

        return bigBoyURL;
    }

    public static String letsGetIt(String url) {
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

    public static CastDTO convert(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            CastDTO dto = mapper.readValue(json, CastDTO.class);
            return dto;
        } catch (Exception e) {
            System.out.println("Something went wrong");
            return null;
        }
    }

    //goal: take a list of characters return a list of img urls
    //data type: String URl
    //if statement on the movie name

    public static List<String> grabURLS(List<Data> characters, String movie){
        ArrayList<String> castURLS = new ArrayList<>();
        for (Data c : characters) {
            if (c.getFilms().contains(movie)) {
                String image = c.getImageUrl();
                castURLS.add(image);
            }
        }
        return castURLS;
    }

    //goal: write an html file that hass all the image urls

    public static void writeToFile(List<String> imgURLS) {

        try {
            FileWriter fw = new FileWriter("index.html");
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("<html>\n");
            writer.write("<head>\n<title>Images</title>\n</head>\n");
            writer.write("<body>\n");
            for (int i = 0; i < imgURLS.size(); i++) {
                writer.write("<img src=\"" + imgURLS.get(i) + "\">\n");
            }
            writer.write("</body>\n");
            writer.write("</html>");

            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
