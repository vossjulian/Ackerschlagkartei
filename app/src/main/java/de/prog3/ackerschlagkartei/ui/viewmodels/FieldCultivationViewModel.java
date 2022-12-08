package de.prog3.ackerschlagkartei.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.data.models.WeatherModel;
import de.prog3.ackerschlagkartei.data.repositories.FirestoreRepository;
import de.prog3.ackerschlagkartei.data.repositories.WeatherRepository;

public class FieldCultivationViewModel extends AndroidViewModel {
    private final Application application;
    private FirestoreRepository firestoreRepository;
    private WeatherRepository weatherRepository;

    public FieldCultivationViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        this.firestoreRepository = new FirestoreRepository(application);
        this.weatherRepository = new WeatherRepository(application);
    }

    public MutableLiveData<FieldModel> getFieldModelMutableLiveData(@NonNull FieldModel selectedField) {
        return this.firestoreRepository.getFieldMutableLiveData(selectedField.getUid());
    }

    public void loadWeather(@NonNull FieldModel selectedField) {
        this.weatherRepository.loadWeather(selectedField.getInfo().getPositions().get(0));
    }

    public MutableLiveData<WeatherModel> getWeatherMutableLiveData() {
        return this.weatherRepository.getWeatherModelGetData();
    }

    public void updateField(@NonNull FieldModel selectedField, @NonNull String field, @NonNull Object changes) {

        this.firestoreRepository.updateFieldModel(selectedField, field, changes);
    }
}
