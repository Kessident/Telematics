package com;

public class VehicleInfo {
    private int vehicleIdentificationNumber;
    private double odometer;
    private double gasConsumption;
    private double milesSinceLastOilChange;
    private double engineSize;

    public VehicleInfo() {}

    public int getVehicleIdentificationNumber() {
        return vehicleIdentificationNumber;
    }

    public void setVehicleIdentificationNumber(int vehicleIdentificationNumber) {
        this.vehicleIdentificationNumber = vehicleIdentificationNumber;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public double getGasConsumption() {
        return gasConsumption;
    }

    public void setGasConsumption(double gasConsumption) {
        this.gasConsumption = gasConsumption;
    }

    public double getMilesSinceLastOilChange() {
        return milesSinceLastOilChange;
    }

    public void setMilesSinceLastOilChange(double milesSinceLastOilChange) {
        this.milesSinceLastOilChange = milesSinceLastOilChange;
    }

    public double getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(double engineSize) {
        this.engineSize = engineSize;
    }
}
