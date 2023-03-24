package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

public class StarWars {
    public static void main(String[] args) {
        String link = "https://swapi.dev/api/films/1/";
        //System.out.println(grabTitle(link));
        //System.out.println(Arrays.toString(getAllMovieTitle(1)));

        String[] titles = getAllMovieTitle(10);
        for (String t : titles) {
            System.out.println(t);
        }

    }

    //Goal: Given a personID, return the list of movie titles
    public static String[] getAllMovieTitle(int personID) {
        String URL = "https://swapi.dev/api/people/" + personID + "/";
        String jsonRepsonse = APIConnector.makeGETRequest(URL);
        ObjectMapper mapper = new ObjectMapper();
        PeopleDTO dto = null;
        try {
            dto = mapper.readValue(jsonRepsonse, PeopleDTO.class);
        } catch (Exception e) {
            return null;
        }
        List<String> filmURLs = dto.getFilms();
        String[] basket = new String[filmURLs.size()];
        for (int i = 0; i < filmURLs.size(); i++) {
            basket[i] = grabTitle(filmURLs.get(i));
        }

        return basket;
    }

    //Goal: Given a film URL, return the title
    public static String grabTitle(String filmURL) {
        String response = APIConnector.makeGETRequest(filmURL);
        ObjectMapper mapper = new ObjectMapper();
        try {
            FilmDTO filmDTO = mapper.readValue(response, FilmDTO.class);
            return filmDTO.getTitle();
        } catch (Exception e) {
            return "Title Not Found";
        }
    }
}
