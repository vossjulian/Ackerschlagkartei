package de.prog3.ackerschlagkartei.ui.views.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Polygon;

import java.util.List;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldsListViewModel;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldsMapViewModel;

public class FieldsMapFragment extends Fragment implements OnMapReadyCallback {
    private FieldsMapViewModel fieldsMapViewModel;
    private FieldsListViewModel fieldsListViewModel;
    private NavController navController;

    private MapView mapView;
    private GoogleMap googleMap;

    private List<FieldModel> currentFieldModels;

    public FieldsMapFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fields_map, container, false);
        this.mapView = view.findViewById(R.id.mv_fields_overview);

        this.mapView.onCreate(savedInstanceState);
        this.mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fieldsMapViewModel = new ViewModelProvider(requireActivity()).get(FieldsMapViewModel.class);
        this.fieldsListViewModel = new ViewModelProvider(requireActivity()).get(FieldsListViewModel.class);

        this.navController = Navigation.findNavController(view);

        this.fieldsMapViewModel.getFieldListData().observe(getViewLifecycleOwner(), new Observer<List<FieldModel>>() {
            @Override
            public void onChanged(List<FieldModel> fieldModels) {
                currentFieldModels = fieldModels;
                fieldsMapViewModel.setFieldModels(currentFieldModels);
                fieldsMapViewModel.createFieldPolygons(googleMap, fieldModels);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fields_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add_field) {
            navController.navigate(R.id.action_fieldMapFragment_to_fieldAddFragment);
        }

        if (id == R.id.menu_change_view) {
            navController.navigate(R.id.action_fieldMapFragment_to_fieldListFragment);
        }

        if (id == R.id.menu_open_profile) {
            navController.navigate(R.id.action_fieldsMapFragment_to_profileFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);

        this.googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(@NonNull Polygon polygon) {
                FieldModel selectedField = (FieldModel) polygon.getTag();

                fieldsListViewModel.setSelectedFieldModel(selectedField);
                fieldsMapViewModel.setSelectedFieldModel(selectedField);

                navController.navigate(R.id.action_fieldsMapFragment_to_fieldDetailsFragment);
            }
        });

        fieldsMapViewModel.createFieldPolygons(this.googleMap, this.currentFieldModels);

        checkLocationPermission();
    }

    private final ActivityResultLauncher<String> requestLocationPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) { }
    });

    private void checkLocationPermission() {
        this.requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        this.requestLocationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }
}