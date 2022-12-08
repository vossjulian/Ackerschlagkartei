package de.prog3.ackerschlagkartei.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.data.repositories.FirestoreRepository;

public class FieldGroundViewModel extends AndroidViewModel {
    private final Application application;
    private FirestoreRepository firestoreRepository;

    public FieldGroundViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.firestoreRepository = new FirestoreRepository(application);
    }

    public MutableLiveData<FieldModel> getFieldModelMutableLiveData(@NonNull FieldModel selectedField) {
        return this.firestoreRepository.getFieldMutableLiveData(selectedField.getUid());
    }

    public void updateField(@NonNull FieldModel selectedField, @NonNull String field, @NonNull Object changes){
        this.firestoreRepository.updateFieldModel(selectedField, field, changes);
    }

}
