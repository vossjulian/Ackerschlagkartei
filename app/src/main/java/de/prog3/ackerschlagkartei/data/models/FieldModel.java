package de.prog3.ackerschlagkartei.data.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class FieldModel {
    @DocumentId
    private String uid;
    private CultivationModel cultivation;
    private GroundModel ground;
    private InfoModel info;

    public FieldModel() { }

    public FieldModel(String description, List<GeoPoint> positions, double area) {
        this.cultivation = new CultivationModel("", "", "", "", "");
        this.ground = new GroundModel("", "", "", 0, 0, 0, 0, 0);
        this.info = new InfoModel(area, description, false, false, false, true, positions);
    }

    public String getUid() {
        return uid;
    }

    public CultivationModel getCultivation() {
        return cultivation;
    }

    public GroundModel getGround() {
        return ground;
    }

    public InfoModel getInfo() {
        return info;
    }

}
