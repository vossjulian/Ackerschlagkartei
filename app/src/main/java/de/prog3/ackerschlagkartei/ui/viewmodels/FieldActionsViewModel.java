package de.prog3.ackerschlagkartei.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.List;

import de.prog3.ackerschlagkartei.data.models.ActionModel;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.data.repositories.FirestoreRepository;

public class FieldActionsViewModel extends AndroidViewModel {
    private final Application application;
    private FirestoreRepository firestoreRepository;
    private String actionCategory;

    public FieldActionsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        this.firestoreRepository = new FirestoreRepository(application);

        this.actionCategory = "";
    }

    public void setActionCategory(String actionCategory) {
        this.actionCategory = actionCategory;
    }

    public String getActionCategory() {
        return this.actionCategory;
    }

    public MutableLiveData<List<ActionModel>> getActions(FieldModel fieldModel) {
        return this.firestoreRepository.getActionListGetData(fieldModel.getUid(), actionCategory);
    }

    public MutableLiveData<FieldModel> getFieldModelMutableLiveData(@NonNull FieldModel selectedField) {
        return this.firestoreRepository.getFieldMutableLiveData(selectedField.getUid());
    }

    public void setAction(FieldModel fieldModel, String action, Date date, String category) {
        this.firestoreRepository.createActionModel(fieldModel.getUid(), new ActionModel(fieldModel.getUid(), action, date, category));
    }
}
