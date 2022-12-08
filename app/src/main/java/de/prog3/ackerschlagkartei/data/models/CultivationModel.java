package de.prog3.ackerschlagkartei.data.models;

public class CultivationModel {
    private String previousCrop;
    private String primaryCrop;
    private String secondaryCrop;
    private String zwfGroup;
    private String nextCrop;

    public CultivationModel() { }

    public CultivationModel(String previousCrop, String primaryCrop, String secondaryCrop, String zwfGroup, String zwfCulture) {
        this.previousCrop = previousCrop;
        this.primaryCrop = primaryCrop;
        this.secondaryCrop = secondaryCrop;
        this.zwfGroup = zwfGroup;
        this.nextCrop = zwfCulture;
    }

    public String getPreviousCrop() {
        return previousCrop;
    }

    public String getPrimaryCrop() {
        return primaryCrop;
    }

    public String getSecondaryCrop() {
        return secondaryCrop;
    }

    public String getZwfGroup() {
        return zwfGroup;
    }

    public String getNextCrop() {
        return nextCrop;
    }

}
