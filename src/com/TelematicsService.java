package com;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TelematicsService {

    static void report(VehicleInfo vehicleInfo) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String pathname = vehicleInfo.getVehicleIdentificationNumber() + ".json";
            mapper.writeValue(new File(pathname), vehicleInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] getFileContents (String fileName) {
        File file = new File (fileName);
        try {
            Scanner fileScanner = new Scanner(file);
            List<String> fileContents = new ArrayList<>();
            while (fileScanner.hasNext()) {
                fileContents.add(fileScanner.nextLine());
            }
            return fileContents.toArray(new String[0]); //Converts the list to an array
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find file *" + fileName + "*");
            ex.printStackTrace();
            return null;
        }
    }

}