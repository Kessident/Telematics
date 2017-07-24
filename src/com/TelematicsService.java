package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.File;
import java.io.FileNotFoundException;
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

            Element addOnToMe = dashboardHTML.getElementById("addontome");
            addOnToMe = resetTable(addOnToMe);

            for (VehicleInfo vi : allVehicles){
                Element newRow = newTableRow(vi, df);
                addOnToMe.appendChild(newRow);
            }

            FileUtils.writeStringToFile(dashboardFile, dashboardHTML.outerHtml(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Element resetTable (Element addontome){
        addontome.html("");

        Element newTR = new Element(Tag.valueOf("tr"), "");
        Element vinTH = new Element(Tag.valueOf("th"), "");
        vinTH.attr("align", "center");
        vinTH.html("VIN");
        newTR.appendChild(vinTH);

        Element odometerTH = new Element(Tag.valueOf("th"), "");
        odometerTH.attr("align", "center");
        odometerTH.html("Odometer (miles)");
        newTR.appendChild(odometerTH);

        Element consumptionTH = new Element(Tag.valueOf("th"), "");
        consumptionTH.attr("align", "center");
        consumptionTH.html("Consumption (gallons)");
        newTR.appendChild(consumptionTH);

        Element lastOilChangeTH = new Element(Tag.valueOf("th"), "");
        lastOilChangeTH.attr("align", "center");
        lastOilChangeTH.html("Last Oil Change");
        newTR.appendChild(lastOilChangeTH);

        Element engineSizeTH = new Element(Tag.valueOf("th"), "");
        engineSizeTH.attr("align", "center");
        engineSizeTH.html("Engine Size (liters)");
        newTR.appendChild(engineSizeTH);
        addontome.appendChild(newTR);

        return addontome;
    }

    private static Element newTableRow(VehicleInfo vi, DecimalFormat df) {
        Element newTR = new Element(Tag.valueOf("tr"), "");

        Element vinTD = new Element(Tag.valueOf("td"), "");
        vinTD.attr("align", "center");
        String vehicleVIN = df.format(vi.getVehicleIdentificationNumber());
        vinTD.html(vehicleVIN);
        newTR.appendChild(vinTD);

        Element odometerTD = new Element(Tag.valueOf("td"), "");
        odometerTD.attr("align", "center");
        String vehicleOdometer = df.format(vi.getOdometer());
        odometerTD.html(vehicleOdometer);
        newTR.appendChild(odometerTD);

        Element consumptionTD = new Element(Tag.valueOf("td"), "");
        consumptionTD.attr("align", "center");
        String vehicleGasConsumption = df.format(vi.getGasConsumption());
        consumptionTD.html(vehicleGasConsumption);
        newTR.appendChild(consumptionTD);

        Element lastOilChangeTD = new Element(Tag.valueOf("td"), "");
        lastOilChangeTD.attr("align", "center");
        String vehicleLastOilChange = df.format(vi.getMilesSinceLastOilChange());
        lastOilChangeTD.html(vehicleLastOilChange);
        newTR.appendChild(lastOilChangeTD);

        Element engineSizeTD = new Element(Tag.valueOf("td"), "");
        engineSizeTD.attr("align", "center");
        String vehicleEngineSize = df.format(vi.getEngineSize());
        engineSizeTD.html(vehicleEngineSize);
        newTR.appendChild(engineSizeTD);

        return newTR;
    }
}
