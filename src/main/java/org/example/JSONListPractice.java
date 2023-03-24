package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONListPractice {
    public static void main(String[] args) {
        //goal: turn the lis of cereals into a list of CerealDTOs

        //setup
        //1. get the json
        //2. make the object mapper
        //new step: make the TypeReference (object mapper needs help with lists)
        //process:
        //3. read the json + map to DTO
        //afterwards:
        //4. you play with the object

        File jsonFile = new File("ListOfCereals.json");
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<List<CerealDTO_Generated>> dataType = new TypeReference<>(){};

        try {
            List<CerealDTO_Generated> cereals = mapper.readValue(jsonFile, dataType);
            for (CerealDTO_Generated cereal : cereals) {
                System.out.println(cereal.getName());
            }
            CerealDTO_Generated grapeNuts = cereals.get(2);
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            String grapeJson = mapper.writeValueAsString(grapeNuts);
            System.out.println(grapeJson);

        } catch (IOException e) {
            System.out.println("something went wrong");
        }
    }
}
