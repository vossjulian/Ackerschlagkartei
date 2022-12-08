package de.prog3.ackerschlagkartei.data.models;

public class GroundModel {
    private String bkr;
    private String date;
    private String groundType;
    private double humus;
    private double magnesium;
    private double phValue;
    private double phosphorus;
    private double potassium;

    public GroundModel() { }

    public GroundModel(String bkr, String date, String groundType, double humus, double magnesium, double phValue, double phosphorus, double potassium) {
        this.bkr = bkr;
        this.groundType = groundType;
        this.humus = humus;
        this.magnesium = magnesium;
        this.phValue = phValue;
        this.phosphorus = phosphorus;
        this.potassium = potassium;
        this.date = date;
    }

    public String getBkr() {
        return bkr;
    }

    public String getGroundType() {
        return groundType;
    }

    public double getHumus() {
        return humus;
    }

    public double getMagnesium() {
        return magnesium;
    }

    public double getPhValue() {
        return phValue;
    }

    public double getPhosphorus() {
        return phosphorus;
    }

    public double getPotassium() {
        return potassium;
    }

    public String getDate() { return date; }

}
