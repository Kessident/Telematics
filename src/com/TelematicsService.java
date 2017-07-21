package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.jsoup.parser.Tag;


public class TelematicsService {

    static void report(VehicleInfo vehicleInfo) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            String pathname = vehicleInfo.getVehicleIdentificationNumber() + ".json";
            mapper.writeValue(new File(pathname), vehicleInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateDashboard();
    }

    private static List<String> getFileContents (String fileName) {
        File file = new File (fileName);
        try {
            Scanner fileScanner = new Scanner(file);
            List<String> fileContents = new ArrayList<>();
            while (fileScanner.hasNext()) {
                fileContents.add(fileScanner.nextLine());
            }
            return fileContents;
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find file *" + fileName + "*");
            ex.printStackTrace();
            return null;
        }
    }

//    private static void editDashboard(){
//        String dashboard = "dashboard.html";
//        List<String> fileContents = getFileContents(dashboard);
//
//        DecimalFormat df = new DecimalFormat("#.#");
//        df.setRoundingMode(RoundingMode.DOWN);
//        //FOR LOOP FOR ALL JSON
//        String vehicleVIN = df.format(vehicleInfo.getVehicleIdentificationNumber());
//        String vehicleOdometer = df.format(vehicleInfo.getOdometer());
//        String vehicleGasConsumption = df.format(vehicleInfo.getGasConsumption());
//        String vehicleLastOilChange = df.format(vehicleInfo.getMilesSinceLastOilChange());
//        String vehicleEngineSize = df.format(vehicleInfo.getEngineSize());
//
//        String newTableRow = "<tr>\n<td align=\"center\">"+vehicleVIN+"</td><td align=\"center\">"+vehicleOdometer+"</td><td align=\"center\">"+vehicleGasConsumption+"</td><td align=\"center\">"+vehicleLastOilChange+"</td><td align=\"center\">"+vehicleEngineSize+"</td>\n</tr>";
//
//        fileContents.add(fileContents.lastIndexOf("</tr>") + 1, newTableRow);
//
//        try {
//            File file = new File(dashboard);
//            FileWriter fileWriter = new FileWriter(file);
//            for (String line : fileContents){
//                fileWriter.write(line);
//                fileWriter.write("\n");
//            }
//            fileWriter.close();
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        updateDashboard();
//    }

    private static void updateDashboard(){
        File file = new File(".");
        ArrayList<VehicleInfo> allVehicles = new ArrayList<>();

        for (File f : file.listFiles()) {
            if (f.getName().endsWith(".json")) {
                String vehicleJSON = getFileContents(f.getName()).get(0);
                ObjectMapper mapper = new ObjectMapper();
                try {
                    VehicleInfo vi = mapper.readValue(vehicleJSON, VehicleInfo.class);
                    allVehicles.add(vi);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        //AVERAGES CALCULATIONS
        int numOfVehicles = allVehicles.size();
        double averageOdometer = 0;
        double averageConsumption = 0;
        double averageLastOilChange = 0;
        double averageEngineSize = 0;
        for (VehicleInfo vi : allVehicles){
            averageOdometer += vi.getOdometer();
            averageConsumption += vi.getGasConsumption();
            averageLastOilChange += vi.getMilesSinceLastOilChange();
            averageEngineSize += vi.getEngineSize();
        }
        averageOdometer /= numOfVehicles;
        averageConsumption /= numOfVehicles;
        averageLastOilChange /= numOfVehicles;
        averageEngineSize /= numOfVehicles;



        File dashboardFile = new File("dashboard.html");
        Document dashboardHTML;
        try {
            dashboardHTML = Jsoup.parse(dashboardFile, "UTF-8");

            Element numOfVehiclesTag = dashboardHTML.getElementById("numVehicles");
            Element averageOdometerTag = dashboardHTML.getElementById("odometerAverage");
            Element averageConsumptionTag = dashboardHTML.getElementById("consumptionAverage");
            Element averageLastOilChangeTag = dashboardHTML.getElementById("oilChangeAverage");
            Element averageEngineSizeTag = dashboardHTML.getElementById("engineSizeAverage");

            //FORMAT AVERAGES
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.DOWN);
            numOfVehiclesTag.html(Integer.toString(numOfVehicles));
            averageOdometerTag.html(df.format(averageOdometer));
            averageConsumptionTag.html(df.format(averageConsumption));
            averageLastOilChangeTag.html(df.format(averageLastOilChange));
            averageEngineSizeTag.html(df.format(averageEngineSize));

            for (VehicleInfo vi : allVehicles){
                Element addOnToMe = dashboardHTML.getElementById("addontome");

                Element newTR = new Element(Tag.valueOf("tr"), dashboardFile.getName());
                Element vinTD = new Element(Tag.valueOf("td"), dashboardFile.getName());
                String vehicleVIN = df.format(vi.getVehicleIdentificationNumber());
                vinTD.html(vehicleVIN);
                newTR.appendChild(vinTD);

                Element odometerTD = new Element(Tag.valueOf("td"), dashboardFile.getName());
                String vehicleOdometer = df.format(vi.getOdometer());
                odometerTD.html(vehicleOdometer);
                newTR.appendChild(odometerTD);

                Element consumptionTD = new Element(Tag.valueOf("td"), dashboardFile.getName());
                String vehicleGasConsumption = df.format(vi.getGasConsumption());
                consumptionTD.html(vehicleGasConsumption);
                newTR.appendChild(consumptionTD);

                Element lastOilChangeTD = new Element(Tag.valueOf("td"), dashboardFile.getName());
                String vehicleLastOilChange = df.format(vi.getMilesSinceLastOilChange());
                lastOilChangeTD.html(vehicleLastOilChange);
                newTR.appendChild(lastOilChangeTD);

                Element engineSizeTD = new Element(Tag.valueOf("td"), dashboardFile.getName());
                String vehicleEngineSize = df.format(vi.getEngineSize());
                engineSizeTD.html(vehicleEngineSize);
                newTR.appendChild(engineSizeTD);

                addOnToMe.appendChild(newTR);
            }

            FileUtils.writeStringToFile(dashboardFile, dashboardHTML.outerHtml(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}