package de.prog3.ackerschlagkartei.ui.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.ui.viewmodels.AuthViewModel;

public class ProfileFragment extends Fragment {

    private AuthViewModel authViewModel;
    private NavController navController;

    private Button btnSignOut;
    private TextView tvEmailAddress;


    public ProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        this.btnSignOut = view.findViewById(R.id.btn_sign_out);
        this.tvEmailAddress = view.findViewById(R.id.tv_email_address);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        this.navController = Navigation.findNavController(view);

        this.authViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    tvEmailAddress.setText(firebaseUser.getEmail());
                }
            }
        });

        this.btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authViewModel.logout();
                navController.navigate(R.id.action_profileFragment_to_splashFragment2);
            }
        });

    }
}