package de.prog3.ackerschlagkartei.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.data.repositories.AuthRepository;
import de.prog3.ackerschlagkartei.data.repositories.FirestoreRepository;

public class FieldsListViewModel extends AndroidViewModel {
    private final Application application;

    private final FirestoreRepository firestoreRepository;

    private FieldModel selectedFieldModel;

    public FieldsListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        this.firestoreRepository = new FirestoreRepository(application);
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
}
