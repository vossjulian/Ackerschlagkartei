package de.prog3.ackerschlagkartei.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import de.prog3.ackerschlagkartei.data.repositories.AuthRepository;
import de.prog3.ackerschlagkartei.utils.Status;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        authRepository = new AuthRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
    }

    public void register(String email, String password) {
        authRepository.register(email, password);
    }

    public void login(String email, String password) {
        authRepository.login(email, password);
    }

    public void resetPassword(String email) {
        authRepository.resetPassword(email);
    }

    public void logout(){
        authRepository.logout();
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Status> getRegisterStatus() {
        return authRepository.getRegisterStatus();
    }

    public MutableLiveData<Status> getLoginStatus() {
        return authRepository.getLoginStatus();
    }

    public MutableLiveData<Status> getLogoutStatus() {
        return authRepository.getLogoutStatus();
    }

    public MutableLiveData<Status> getResetPasswordStatus() {
        return authRepository.getResetPasswordStatus();
    }
}
