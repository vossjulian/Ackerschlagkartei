package de.prog3.ackerschlagkartei.ui.views.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldAddViewModel;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldsMapViewModel;

public class FieldAddFragment extends Fragment implements OnMapReadyCallback {
    private FieldAddViewModel fieldAddViewModel;
    private FieldsMapViewModel fieldsMapViewModel;
    private NavController navController;

    private MapView mapView;
    private GoogleMap googleMap;

    private EditText etDescription;

    private final List<GeoPoint> fieldPositions = new ArrayList<>();
    private final List<LatLng> fieldLatLngs = new ArrayList<>();
    private List<FieldModel> fieldModels;

    private Polygon polygon;


    public FieldAddFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_add, container, false);
        this.etDescription = view.findViewById(R.id.field_add_description);
        this.mapView = view.findViewById(R.id.mv_field_add);

        this.mapView.onCreate(savedInstanceState);
        this.mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fieldAddViewModel = new ViewModelProvider(requireActivity()).get(FieldAddViewModel.class);
        this.fieldsMapViewModel = new ViewModelProvider(requireActivity()).get(FieldsMapViewModel.class);
        this.navController = Navigation.findNavController(view);

        this.fieldModels = fieldsMapViewModel.getFieldModels();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mapView.onStart();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        this.googleMap.setOnMapClickListener(onMapClick);
        this.googleMap.setOnMapLongClickListener(onMapLongClick);

        this.fieldsMapViewModel.createFieldPolygons(this.googleMap, this.fieldModels);

        this.checkLocationPermission();
    }

    private final GoogleMap.OnMapClickListener onMapClick = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(@NonNull LatLng latLng) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Position");
            googleMap.addMarker(markerOptions);
            fieldLatLngs.add(latLng);
            fieldPositions.add(new GeoPoint(latLng.latitude, latLng.longitude));
        }
    };

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

    private final GoogleMap.OnMapLongClickListener onMapLongClick = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(@NonNull LatLng latLng) {
            if (!fieldLatLngs.isEmpty()) {

                polygon = googleMap.addPolygon(new PolygonOptions()
                        .addAll(fieldLatLngs)
                        .fillColor(ContextCompat.getColor(requireActivity(), R.color.field_polygon))
                        .strokeWidth(2));
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_fiel_menu_confirm) {
            addFieldData();
        }

        if (id == R.id.add_fiel_menu_reset) {
            resetAll();
            this.fieldsMapViewModel.createFieldPolygons(this.googleMap, this.fieldModels);
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetAll() {
        etDescription.getText().clear();
        googleMap.clear();
        if (polygon != null) {
            polygon.remove();
        }
        fieldPositions.clear();
        fieldLatLngs.clear();
    }

    private void addFieldData() {
        String fieldDescription = etDescription.getText().toString();

        if (TextUtils.isEmpty(fieldDescription)) {
            etDescription.setError(getString(R.string.error_description_empty));
            etDescription.requestFocus();
            return;
        }

        if (fieldPositions.isEmpty()) {
            etDescription.setError(getString(R.string.error_positions_empty));
            etDescription.requestFocus();
            return;
        }

        double area = SphericalUtil.computeArea(fieldLatLngs) / 10000;

        FieldModel newField = new FieldModel(fieldDescription, this.fieldPositions, area);

        this.fieldAddViewModel.createFieldModel(newField);
        this.navController.navigateUp();
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