package de.prog3.ackerschlagkartei.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.data.models.WeatherModel;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldCultivationViewModel;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldsMapViewModel;

public class FieldCultivationFragment extends Fragment {
    private FieldsMapViewModel fieldsMapViewModel;
    private FieldCultivationViewModel fieldCultivationViewModel;
    private NavController navController;

    private AutoCompleteTextView ddPreviousCrop;
    private AutoCompleteTextView ddPrimaryCrop;
    private AutoCompleteTextView ddSecondaryCrop;
    private AutoCompleteTextView ddZwfGroup;
    private AutoCompleteTextView ddNextCrop;

    private TextView tvTemp;

    private ArrayAdapter<CharSequence> cropAdapter;
    private ArrayAdapter<CharSequence> secondaryCropAdapter;
    private ArrayAdapter<CharSequence> zwfGroupAdapter;

    private FieldModel selectedField;

    public FieldCultivationFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_cultivation, container, false);

        this.ddPreviousCrop = view.findViewById(R.id.dd_previous_crop);
        this.ddPrimaryCrop = view.findViewById(R.id.dd_primary_crop);
        this.ddSecondaryCrop = view.findViewById(R.id.dd_secondary_crop);
        this.ddNextCrop = view.findViewById(R.id.dd_next_crop);
        this.ddZwfGroup = view.findViewById(R.id.dd_zwf_group);
        this.tvTemp = view.findViewById(R.id.tv_temp);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.navController = Navigation.findNavController(view);

        this.cropAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.previous_primary_next_crop_array,
                R.layout.support_simple_spinner_dropdown_item);

        this.secondaryCropAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.secondary_crop_array,
                R.layout.support_simple_spinner_dropdown_item);

        this.zwfGroupAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.zwf_group_array,
                R.layout.support_simple_spinner_dropdown_item);

        this.ddPreviousCrop.setAdapter(this.cropAdapter);
        this.ddPrimaryCrop.setAdapter(this.cropAdapter);
        this.ddSecondaryCrop.setAdapter(this.secondaryCropAdapter);
        this.ddNextCrop.setAdapter(this.cropAdapter);
        this.ddZwfGroup.setAdapter(this.zwfGroupAdapter);

        this.fieldsMapViewModel = new ViewModelProvider(requireActivity()).get(FieldsMapViewModel.class);
        this.fieldCultivationViewModel = new ViewModelProvider(requireActivity()).get(FieldCultivationViewModel.class);

        this.selectedField = this.fieldsMapViewModel.getSelectedFieldModel();

        this.fieldCultivationViewModel.getFieldModelMutableLiveData(this.selectedField).observe(getViewLifecycleOwner(), new Observer<FieldModel>() {
            @Override
            public void onChanged(FieldModel fieldModel) {
                ddPreviousCrop.setText(fieldModel.getCultivation().getPreviousCrop(), false);
                ddPrimaryCrop.setText(fieldModel.getCultivation().getPrimaryCrop(), false);
                ddSecondaryCrop.setText(fieldModel.getCultivation().getSecondaryCrop(), false);
                ddZwfGroup.setText(fieldModel.getCultivation().getZwfGroup(), false);
                ddNextCrop.setText(fieldModel.getCultivation().getNextCrop(), false);

                fieldCultivationViewModel.loadWeather(fieldModel);
            }
        });


        this.fieldCultivationViewModel.getWeatherMutableLiveData().observe(getViewLifecycleOwner(), new Observer<WeatherModel>() {
            @Override
            public void onChanged(WeatherModel weatherModel) {
                tvTemp.setText(weatherModel.toString());
            }
        });


        this.ddPreviousCrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String changes = cropAdapter.getItem(position).toString();

                fieldCultivationViewModel.updateField(selectedField,"cultivation.previousCrop",changes);

                view.clearFocus();
                ddPreviousCrop.clearFocus();
            }
        });

        this.ddPreviousCrop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String changes = ddPreviousCrop.getText().toString();
                    fieldCultivationViewModel.updateField(selectedField, "cultivation.previousCrop", changes);
                    v.clearFocus();
                    ddPreviousCrop.clearFocus();
                }
            }
        });

        this.ddPrimaryCrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String changes = cropAdapter.getItem(position).toString();

                fieldCultivationViewModel.updateField(selectedField,"cultivation.primaryCrop",changes);
                view.clearFocus();
                ddPrimaryCrop.clearFocus();
            }
        });

        this.ddPrimaryCrop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String changes = ddPrimaryCrop.getText().toString();
                    fieldCultivationViewModel.updateField(selectedField, "cultivation.primaryCrop", changes);
                    v.clearFocus();
                    ddPrimaryCrop.clearFocus();
                }
            }
        });

        this.ddSecondaryCrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String changes = secondaryCropAdapter.getItem(position).toString();

                fieldCultivationViewModel.updateField(selectedField,"cultivation.secondaryCrop", changes);
                view.clearFocus();
                ddSecondaryCrop.clearFocus();
            }
        });

        this.ddSecondaryCrop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String changes = ddSecondaryCrop.getText().toString();
                    fieldCultivationViewModel.updateField(selectedField, "cultivation.secondaryCrop", changes);
                    v.clearFocus();
                    ddSecondaryCrop.clearFocus();
                }
            }
        });

        this.ddZwfGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String changes = zwfGroupAdapter.getItem(position).toString();

                fieldCultivationViewModel.updateField(selectedField,"cultivation.zwfGroup", changes);
                view.clearFocus();
                ddZwfGroup.clearFocus();
            }
        });

        this.ddZwfGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String changes = ddZwfGroup.getText().toString();
                    fieldCultivationViewModel.updateField(selectedField, "cultivation.zwfGroup", changes);
                    v.clearFocus();
                    ddZwfGroup.clearFocus();
                }
            }
        });

        this.ddNextCrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String changes = cropAdapter.getItem(position).toString();

                fieldCultivationViewModel.updateField(selectedField,"cultivation.nextCrop", changes);
                view.clearFocus();
                ddNextCrop.clearFocus();
            }
        });

        this.ddNextCrop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String changes = ddNextCrop.getText().toString();
                    fieldCultivationViewModel.updateField(selectedField, "cultivation.nextCrop", changes);
                    v.clearFocus();
                    ddNextCrop.clearFocus();
                }
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