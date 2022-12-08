package de.prog3.ackerschlagkartei.data.repositories;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.prog3.ackerschlagkartei.data.models.ActionModel;
import de.prog3.ackerschlagkartei.data.models.DocumentModel;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.utils.Status;

public class FirestoreRepository {
    private final Application application;
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;

    private final MutableLiveData<List<FieldModel>> fieldListGetData;
    private final MutableLiveData<FieldModel> fieldGetData;
    private final MutableLiveData<Status> fieldListGetStatus;
    private final MutableLiveData<Status> fieldGetStatus;
    private final MutableLiveData<Status> fieldCreateStatus;
    private final MutableLiveData<Status> fieldUpdateStatus;
    private final MutableLiveData<Status> fieldDeleteStatus;

    private final MutableLiveData<List<ActionModel>> actionListGetData;
    private final MutableLiveData<Status> actionListGetStatus;
    private final MutableLiveData<Status> actionCreateStatus;

    private final MutableLiveData<List<DocumentModel>> documentListGetData;
    private final MutableLiveData<Status> documentListGetStatus;
    private final MutableLiveData<Status> documentCreateStatus;

    public FirestoreRepository(@NonNull Application application) {
        this.application = application;

        // Enable Offline Persistence
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();


        this.fieldListGetData = new MutableLiveData<>();
        this.fieldGetData = new MutableLiveData<>();
        this.fieldListGetStatus = new MutableLiveData<>(Status.INITIAL);
        this.fieldGetStatus = new MutableLiveData<>(Status.INITIAL);
        this.fieldCreateStatus = new MutableLiveData<>(Status.INITIAL);
        this.fieldUpdateStatus = new MutableLiveData<>(Status.INITIAL);
        this.fieldDeleteStatus = new MutableLiveData<>(Status.INITIAL);

        this.actionListGetData = new MutableLiveData<>();
        this.actionListGetStatus = new MutableLiveData<>(Status.INITIAL);
        this.actionCreateStatus = new MutableLiveData<>(Status.INITIAL);

        this.documentListGetData = new MutableLiveData<>();
        this.documentListGetStatus = new MutableLiveData<>(Status.INITIAL);
        this.documentCreateStatus = new MutableLiveData<>(Status.INITIAL);
    }

    private CollectionReference getFieldCollection() {

        String uid = this.firebaseAuth.getUid();

        return this.firebaseFirestore
                .collection("Users")
                .document(uid)
                .collection("Fields");
    }

    // FIELDMODEL

    public MutableLiveData<List<FieldModel>> getFieldListGetData() {
        if(firebaseAuth.getCurrentUser() == null) return fieldListGetData;

        this.fieldListGetStatus.postValue(Status.LOADING);
        this.getFieldCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    fieldListGetStatus.postValue(Status.ERROR);
                    return;
                }

                if (value != null) {
                    List<FieldModel> fieldList = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : value) {
                        if (doc != null) {
                            fieldList.add(doc.toObject(FieldModel.class));
                        }
                    }

                    fieldListGetData.postValue(fieldList);
                    fieldListGetStatus.postValue(Status.SUCCESS);

                }
            }
        });

        return fieldListGetData;
    }

    public MutableLiveData<FieldModel> getFieldMutableLiveData(@NonNull String uid) {
        if(firebaseAuth.getCurrentUser() == null) return fieldGetData;

        this.fieldGetStatus.postValue(Status.LOADING);
        this.getFieldCollection().document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    fieldGetStatus.postValue(Status.ERROR);
                    return;
                }

                if (value != null && value.exists()) {
                    FieldModel fieldModel = value.toObject(FieldModel.class);
                    fieldGetData.postValue(fieldModel);
                    fieldGetStatus.postValue(Status.SUCCESS);
                }

            }
        });

        return fieldGetData;
    }

    public void createFieldModel(@NonNull FieldModel fieldModel) {
        if(firebaseAuth.getCurrentUser() == null) return;

        this.fieldCreateStatus.postValue(Status.LOADING);
        this.getFieldCollection().add(fieldModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                fieldCreateStatus.postValue(Status.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fieldCreateStatus.postValue(Status.ERROR);
                //Toast.makeText(application, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateFieldModel(@NonNull FieldModel fieldModel, String field, Object changes) {
        if(firebaseAuth.getCurrentUser() == null) return;

        this.fieldUpdateStatus.postValue(Status.LOADING);
        this.getFieldCollection()
                .document(fieldModel.getUid())
                .update(field, changes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        fieldUpdateStatus.postValue(Status.SUCCESS);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fieldUpdateStatus.postValue(Status.ERROR);
                //Toast.makeText(application, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteFieldModel(@NonNull FieldModel fieldModel) {
        if(firebaseAuth.getCurrentUser() == null) return;

        this.fieldDeleteStatus.postValue(Status.LOADING);
        this.getFieldCollection().document(fieldModel.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                fieldDeleteStatus.postValue(Status.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fieldDeleteStatus.postValue(Status.ERROR);
                //Toast.makeText(application, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ACTIONMODEL

    public MutableLiveData<List<ActionModel>> getActionListGetData(@NonNull String fieldUid, String category) {
        if(firebaseAuth.getCurrentUser() == null) return actionListGetData;

        this.actionListGetStatus.postValue(Status.LOADING);
        this.getFieldCollection()
                .document(fieldUid)
                .collection("Actions")
                .whereEqualTo("type", category)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            actionListGetStatus.postValue(Status.ERROR);
                            return;
                        }

                        if (value != null) {
                            List<ActionModel> actionList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : value) {
                                if (doc != null) {
                                    actionList.add(doc.toObject(ActionModel.class));
                                }
                            }

                            actionListGetData.postValue(actionList);
                            actionListGetStatus.postValue(Status.SUCCESS);

                        }
                    }
                });

        return actionListGetData;
    }

    public void createActionModel(@NonNull String fieldUid, ActionModel actionModel) {
        if(firebaseAuth.getCurrentUser() == null) return;

        this.actionCreateStatus.postValue(Status.LOADING);
        this.getFieldCollection()
                .document(fieldUid)
                .collection("Actions")
                .add(actionModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        actionCreateStatus.postValue(Status.SUCCESS);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionCreateStatus.postValue(Status.ERROR);
            }
        });
    }

    // DOCUMENTMODEL

    public void createDocumentModel(@NonNull String fieldUid, DocumentModel documentModel) {
        if(firebaseAuth.getCurrentUser() == null) return;

        this.documentCreateStatus.postValue(Status.LOADING);
        this.getFieldCollection()
                .document(fieldUid)
                .collection("Documents")
                .add(documentModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentCreateStatus.postValue(Status.SUCCESS);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                documentCreateStatus.postValue(Status.ERROR);
            }
        });
    }

    public MutableLiveData<List<DocumentModel>> getDocumentListGetData(@NonNull String fieldUid) {
        if(firebaseAuth.getCurrentUser() == null) return documentListGetData;

        this.documentListGetStatus.postValue(Status.LOADING);
        this.getFieldCollection()
                .document(fieldUid)
                .collection("Documents")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            documentListGetStatus.postValue(Status.ERROR);
                            return;
                        }

                        if (value != null) {
                            List<DocumentModel> documentModelList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : value) {
                                if (doc != null) {
                                    documentModelList.add(doc.toObject(DocumentModel.class));
                                }
                            }

                            documentListGetData.postValue(documentModelList);
                            documentListGetStatus.postValue(Status.SUCCESS);
                        }
                    }
                });

        return documentListGetData;
    }

    // STATUS GETTER FOR OPTIONAL UI UPDATES

    public MutableLiveData<Status> getFieldListGetStatus() {
        return fieldListGetStatus;
    }

    public MutableLiveData<Status> getFieldGetStatus() {
        return fieldGetStatus;
    }

    public MutableLiveData<Status> getFieldCreateStatus() {
        return fieldCreateStatus;
    }

    public MutableLiveData<Status> getFieldUpdateStatus() {
        return fieldUpdateStatus;
    }

    public MutableLiveData<Status> getFieldDeleteStatus() {
        return fieldDeleteStatus;
    }

    public MutableLiveData<Status> getActionListGetStatus() {
        return actionListGetStatus;
    }

    public MutableLiveData<Status> getActionCreateStatus() {
        return actionCreateStatus;
    }

    public MutableLiveData<Status> getDocumentListGetStatus() {
        return documentListGetStatus;
    }

    public MutableLiveData<Status> getDocumentCreateStatus() {
        return documentCreateStatus;
    }
}
