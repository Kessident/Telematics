package com;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("vehicleIdentificationNumber (Integer): ");
        int vehicleIdentificationNumber = Integer.parseInt(scan.nextLine());
        System.out.print("odometer (double): ");
        double odometer = Double.parseDouble(scan.nextLine());
        System.out.print("gasConsumption (double): ");
        double gasConsumption = Double.parseDouble(scan.nextLine());
        System.out.print("milesSinceLastOilChange (double): ");
        double milesSinceLastOilChange = Double.parseDouble(scan.nextLine());
        System.out.print("engineSize (double): ");
        double engineSize = Double.parseDouble(scan.nextLine());

        VehicleInfo newVehicle = new VehicleInfo();
        newVehicle.setVehicleIdentificationNumber(vehicleIdentificationNumber);
        newVehicle.setOdometer(odometer);
        newVehicle.setGasConsumption(gasConsumption);
        newVehicle.setMilesSinceLastOilChange(milesSinceLastOilChange);
        newVehicle.setEngineSize(engineSize);

        TelematicsService.report(newVehicle);
    }
}
