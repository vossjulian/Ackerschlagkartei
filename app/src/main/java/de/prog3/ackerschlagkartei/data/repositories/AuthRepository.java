package de.prog3.ackerschlagkartei.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.prog3.ackerschlagkartei.utils.Status;

public class AuthRepository {
    private final Application application;
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    private final MutableLiveData<Status> registerStatus;
    private final MutableLiveData<Status> loginStatus;
    private final MutableLiveData<Status> logoutStatus;
    private final MutableLiveData<Status> resetPasswordStatus;

    public AuthRepository(Application application) {
        this.application = application;

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userMutableLiveData = new MutableLiveData<>();

        this.registerStatus = new MutableLiveData<>(Status.INITIAL);
        this.loginStatus = new MutableLiveData<>(Status.INITIAL);
        this.logoutStatus = new MutableLiveData<>(Status.INITIAL);
        this.resetPasswordStatus = new MutableLiveData<>(Status.INITIAL);

        this.refreshCurrentUser();
    }

    public void refreshCurrentUser(){
        this.userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
    }

    public void register(String email, String password) {
        this.logoutStatus.postValue(Status.INITIAL);
        this.registerStatus.postValue(Status.LOADING);
        this.firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if (authResult.getUser() != null) {
                    authResult.getUser().sendEmailVerification();

                    registerStatus.postValue(Status.SUCCESS);
                    refreshCurrentUser();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                registerStatus.postValue(Status.ERROR);
                refreshCurrentUser();
            }
        });
    }

    public void login(String email, String password) {
        this.logoutStatus.postValue(Status.INITIAL);
        this.loginStatus.postValue(Status.LOADING);
        this.firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                refreshCurrentUser();
                loginStatus.postValue(Status.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loginStatus.postValue(Status.ERROR);
                refreshCurrentUser();
            }
        });
    }

    public void logout() {
        this.logoutStatus.postValue(Status.LOADING);
        this.firebaseAuth.signOut();
        this.refreshCurrentUser();

        this.registerStatus.postValue(Status.INITIAL);
        this.loginStatus.postValue(Status.INITIAL);
        this.resetPasswordStatus.postValue(Status.INITIAL);

        this.logoutStatus.postValue(Status.SUCCESS);
    }

    public void resetPassword(String email) {
        this.resetPasswordStatus.postValue(Status.LOADING);
        this.firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                resetPasswordStatus.postValue(Status.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resetPasswordStatus.postValue(Status.ERROR);
            }
        });
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Status> getRegisterStatus() {
        return registerStatus;
    }

    public MutableLiveData<Status> getLoginStatus() {
        return loginStatus;
    }

    public MutableLiveData<Status> getLogoutStatus() {
        return logoutStatus;
    }

    public MutableLiveData<Status> getResetPasswordStatus() {
        return resetPasswordStatus;
    }
}
