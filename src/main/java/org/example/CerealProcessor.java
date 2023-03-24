package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.plaf.synth.SynthUI;
import java.io.File;

public class CerealProcessor {
    public static void main(String[] args) {
        File jsonFile = new File("cereal.json");
        ObjectMapper mapper = new ObjectMapper();
        try {
            CerealDTO dto = mapper.readValue(jsonFile, CerealDTO.class);
            System.out.println("Name: " + dto.getName());

            CerealDTO_Generated dtoGen = mapper.readValue(jsonFile, CerealDTO_Generated.class);
            System.out.println(dtoGen.getMarshmallows());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
