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

public class ResetPasswordFragment extends Fragment {
    private AuthViewModel authViewModel;
    private NavController navController;

    private Button btnResetPassword;
    private Button btnReturnToSignIn;

    private EditText etResetPasswordEmail;
    private ProgressBar pbLoading;

    public ResetPasswordFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        this.btnResetPassword = view.findViewById(R.id.btn_reset_password);
        this.btnReturnToSignIn = view.findViewById(R.id.btn_return_to_sign_in);

        this.etResetPasswordEmail = view.findViewById(R.id.et_forgot_password_email);
        this.pbLoading = view.findViewById(R.id.pb_reset_loading);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        this.navController = Navigation.findNavController(view);

        this.authViewModel.getResetPasswordStatus().observe(getViewLifecycleOwner(), new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if(status == Status.ERROR){
                    pbLoading.setVisibility(View.INVISIBLE);
                    etResetPasswordEmail.setError(getString(R.string.error_reset));
                    etResetPasswordEmail.requestFocus();
                    return;
                }

                if(status == Status.SUCCESS){
                    navController.navigateUp();
                    return;
                }

                if(status == Status.LOADING){
                    pbLoading.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });

        this.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etResetPasswordEmail.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    etResetPasswordEmail.setError(getString(R.string.error_empty_email));
                    etResetPasswordEmail.requestFocus();
                    return;
                }

                authViewModel.resetPassword(email);
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