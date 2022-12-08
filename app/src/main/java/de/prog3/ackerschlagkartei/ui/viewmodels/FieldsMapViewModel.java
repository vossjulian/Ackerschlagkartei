package de.prog3.ackerschlagkartei.ui.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.data.repositories.AuthRepository;
import de.prog3.ackerschlagkartei.data.repositories.FirestoreRepository;

public class FieldsMapViewModel extends AndroidViewModel {
    private final Application application;

    private final FirestoreRepository firestoreRepository;

    private List<FieldModel> fieldModels;
    private FieldModel selectedFieldModel;

    public FieldsMapViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        this.firestoreRepository = new FirestoreRepository(application);
    }

    public void setFieldModels(List<FieldModel> fieldModels) {
        this.fieldModels = fieldModels;
    }

    public List<FieldModel> getFieldModels() {
        return this.fieldModels;
    }

    public MutableLiveData<List<FieldModel>> getFieldListData() {
        return this.firestoreRepository.getFieldListGetData();
    }

    public FieldModel getSelectedFieldModel() {
        return selectedFieldModel;
    }

    public void setSelectedFieldModel(FieldModel selectedFieldModel) {
        this.selectedFieldModel = selectedFieldModel;
    }

    public void createFieldPolygons(GoogleMap googleMap, List<FieldModel> fieldModels) {
        if (googleMap == null || fieldModels == null || fieldModels.isEmpty()) {
            return;
        }

        googleMap.clear();
        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();

        for (FieldModel field : fieldModels) {

            List<LatLng> latLngs = new ArrayList<>();

            for (GeoPoint point : field.getInfo().getPositions()) {
                if (!field.getInfo().getPositions().isEmpty()) {
                    LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                    latLngs.add(latLng);
                    latLngBounds.include(latLng);
                }
            }

            Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                    .addAll(latLngs)
                    .fillColor(ContextCompat.getColor(application, R.color.field_polygon))
                    .clickable(true)
                    .strokeWidth(2));

            polygon.setTag(field);
            polygon.setVisible(field.getInfo().isVisible());
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 50));
    }
}
