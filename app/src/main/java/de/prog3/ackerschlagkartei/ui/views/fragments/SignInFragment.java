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

public class SignInFragment extends Fragment {
    private AuthViewModel authViewModel;
    private NavController navController;

    private Button btnOpenSignUp;
    private Button btnOpenResetPassword;
    private Button btnSignIn;

    private EditText etSignInEmail;
    private EditText etSignInPassword;

    private ProgressBar pbLoading;

    public SignInFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        this.btnOpenSignUp = view.findViewById(R.id.btn_open_sign_up);
        this.btnOpenResetPassword = view.findViewById(R.id.btn_open_password_reset);
        this.btnSignIn = view.findViewById(R.id.btn_sign_out);

        this.etSignInEmail = view.findViewById(R.id.et_sign_in_email);
        this.etSignInPassword = view.findViewById(R.id.et_sign_in_password);

        this.pbLoading = view.findViewById(R.id.pb_sign_in_loading);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        this.navController = Navigation.findNavController(view);

        this.authViewModel.getLoginStatus().observe(getViewLifecycleOwner(), new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if(status == Status.ERROR){
                    pbLoading.setVisibility(View.INVISIBLE);
                    etSignInPassword.setError(getString(R.string.error_sign_in));
                    etSignInPassword.requestFocus();
                    return;
                }

                if(status == Status.SUCCESS){
                    navController.navigate(R.id.action_signInFragment_to_splashFragment);
                    return;
                }

                if(status == Status.LOADING){
                    pbLoading.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });

        this.btnOpenSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signInFragment_to_signUpFragment);
            }
        });

        this.btnOpenResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signInFragment_to_resetPasswordFragment);
            }
        });

        this.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = etSignInEmail.getText().toString();
                String password = etSignInPassword.getText().toString();

                if(TextUtils.isEmpty(emailAddress)){
                    etSignInEmail.setError(getString(R.string.error_empty_email));
                    etSignInEmail.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etSignInPassword.setError(getString(R.string.error_empty_password));
                    etSignInPassword.requestFocus();
                    return;
                }

                if(TextUtils.getTrimmedLength(password) < 8){
                    etSignInPassword.setError(getString(R.string.error_short_password));
                    etSignInPassword.requestFocus();
                    return;
                }

                authViewModel.login(emailAddress.trim(), password.trim());
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