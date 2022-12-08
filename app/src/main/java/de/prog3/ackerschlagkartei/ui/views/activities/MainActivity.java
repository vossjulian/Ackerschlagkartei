package de.prog3.ackerschlagkartei.ui.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.Openable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.prog3.ackerschlagkartei.R;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.navController = Navigation.findNavController(this, R.id.main_nav_host_fragment);
        this.appBarConfiguration = new AppBarConfiguration.Builder(R.id.signInFragment, R.id.fieldsMapFragment).build();
        NavigationUI.setupActionBarWithNavController(this, this.navController, appBarConfiguration);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(this.navController, this.appBarConfiguration) || super.onSupportNavigateUp();
    }
}