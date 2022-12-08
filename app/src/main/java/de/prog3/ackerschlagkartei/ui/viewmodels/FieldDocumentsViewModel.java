package de.prog3.ackerschlagkartei.ui.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.prog3.ackerschlagkartei.data.models.DocumentModel;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.data.repositories.FirestoreRepository;
import de.prog3.ackerschlagkartei.data.repositories.StorageRepository;

public class FieldDocumentsViewModel extends AndroidViewModel {
    private final Application application;
    private FirestoreRepository firestoreRepository;
    private StorageRepository storageRepository;

    private MutableLiveData<File> fileMutableLiveData;

    public FieldDocumentsViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        this.firestoreRepository = new FirestoreRepository(application);
        this.storageRepository = new StorageRepository(application);

        fileMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<FieldModel> getFieldModelMutableLiveData(@NonNull FieldModel selectedField) {
        return this.firestoreRepository.getFieldMutableLiveData(selectedField.getUid());
    }

    public MutableLiveData<List<DocumentModel>> getDocumentsMutableLiveData(@NonNull FieldModel selectedField){
        return this.firestoreRepository.getDocumentListGetData(selectedField.getUid());
    }

    public void updateDocument(@NonNull FieldModel selectedField, Uri documentUri){
        this.storageRepository.uploadFieldDocument(selectedField.getUid(), documentUri);
    }

    public void updateBytes(@NonNull FieldModel selectedField, byte[] data){
        this.storageRepository.uploadBytes(selectedField.getUid(), data);
    }

    public void downloadDocument(DocumentModel documentModel) {
        this.storageRepository.downloadFieldDocument(documentModel);
    }

    public void deleteDocument(DocumentModel documentModel) {
        this.storageRepository.deleteDocument(documentModel);
    }

    public MutableLiveData<File> getFileMutableLiveData(){
        return storageRepository.getDownloadDocumentFile();
    }

}
