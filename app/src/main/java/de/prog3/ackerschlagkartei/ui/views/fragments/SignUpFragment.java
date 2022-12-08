package de.prog3.ackerschlagkartei.ui.views.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.ui.viewmodels.AuthViewModel;
import de.prog3.ackerschlagkartei.utils.Status;

public class SignUpFragment extends Fragment {
    private AuthViewModel authViewModel;
    private NavController navController;

    private Button btnSignUp;
    private Button btnReturnToSignIn;

    private EditText etEmailAddress;
    private EditText etPassword;
    private EditText etPasswordConfirm;

    private ProgressBar pbLoading;

    public SignUpFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        this.btnSignUp = view.findViewById(R.id.btn_sign_up);
        this.btnReturnToSignIn = view.findViewById(R.id.btn_return_to_sign_in);

        this.etEmailAddress = view.findViewById(R.id.et_sign_up_email);
        this.etPassword = view.findViewById(R.id.et_sign_up_password);
        this.etPasswordConfirm = view.findViewById(R.id.et_sign_up_password_confirm);

        this.pbLoading = view.findViewById(R.id.pb_sign_up_loading);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        this.navController = Navigation.findNavController(view);

        this.authViewModel.getRegisterStatus().observe(getViewLifecycleOwner(), new Observer<Status>() {
            @Override
            public void onChanged(Status status) {

                if(status == Status.ERROR){
                    pbLoading.setVisibility(View.INVISIBLE);
                    etEmailAddress.setError(getString(R.string.error_sign_up));
                    etEmailAddress.requestFocus();
                    return;
                }

                if(status == Status.SUCCESS){
                    navController.navigate(R.id.action_signUpFragment_to_splashFragment);
                    return;
                }

                if(status == Status.LOADING){
                    pbLoading.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });

        this.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = etEmailAddress.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                if(TextUtils.isEmpty(emailAddress)){
                    etEmailAddress.setError(getString(R.string.error_empty_email));
                    etEmailAddress.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etPassword.setError(getString(R.string.error_empty_password));
                    etPassword.requestFocus();
                    return;
                }

                if(TextUtils.getTrimmedLength(password) < 8){
                    etPassword.setError(getString(R.string.error_short_password));
                    etPassword.requestFocus();
                    return;
                }

                if(!TextUtils.equals(password, passwordConfirm)){
                    etPasswordConfirm.setError(getString(R.string.error_match_password));
                    etPasswordConfirm.requestFocus();
                    return;
                }

                authViewModel.register(emailAddress.trim(), password.trim());
            }
        });

        this.btnReturnToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigateUp();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}