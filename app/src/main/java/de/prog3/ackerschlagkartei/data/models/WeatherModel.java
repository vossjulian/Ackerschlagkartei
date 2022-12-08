package de.prog3.ackerschlagkartei.data.models;

public class WeatherModel {
    private final String locationName;
    private final double temp;
    private final String description;

    public WeatherModel(String locationName, double temp, String description) {
        this.locationName = locationName;
        this.temp = temp;
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getTemp() {
        return temp;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return locationName + " - " + temp + "°C - " + description;
    }
}
