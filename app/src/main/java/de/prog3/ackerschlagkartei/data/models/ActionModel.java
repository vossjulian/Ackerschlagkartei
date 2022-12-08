package de.prog3.ackerschlagkartei.data.models;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;


public class ActionModel {
    @DocumentId
    private String uid;
    private String description;
    private Date date;
    private String type;

    public ActionModel() { }

    public ActionModel(String uid, String description, Date date, String type) {
        this.uid = uid;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
