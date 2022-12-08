package de.prog3.ackerschlagkartei.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.models.ActionModel;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.ui.adapters.FieldActionsAdapter;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldActionsViewModel;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldsMapViewModel;

public class FieldActionsFragment extends Fragment {
    private FieldsMapViewModel fieldsMapViewModel;
    private FieldActionsViewModel fieldActionsViewModel;
    private NavController navController;
    private ListView fieldActionList;
    private List<ActionModel> fieldActions;
    private FieldActionsAdapter arrayAdapter;
    private String selectedCategory;
    private FieldModel selectedFieldModel;

    public FieldActionsFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_actions, container, false);

        this.fieldActionList = view.findViewById(R.id.actions_list);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fieldsMapViewModel = new ViewModelProvider(requireActivity()).get(FieldsMapViewModel.class);
        this.fieldActionsViewModel = new ViewModelProvider(requireActivity()).get(FieldActionsViewModel.class);
        this.navController = Navigation.findNavController(view);

        this.fieldActions = new ArrayList<>();
        this.arrayAdapter = new FieldActionsAdapter(requireActivity(), fieldActions);
        this.selectedCategory = fieldActionsViewModel.getActionCategory();
        this.selectedFieldModel = fieldsMapViewModel.getSelectedFieldModel();

        this.fieldActionsViewModel.getActions(selectedFieldModel).observe(requireActivity(), new Observer<List<ActionModel>>() {
            @Override
            public void onChanged(List<ActionModel> actionModels) {
                fieldActions.clear();
                if(actionModels != null) {
                    fieldActions.addAll(actionModels);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        if(selectedCategory.equals(getString(R.string.soil_cultivation))) {
            arrayAdapter.setIcon(R.drawable.ic_baseline_fence_42);
        }else if(selectedCategory.equals(getString(R.string.sowing))) {
            arrayAdapter.setIcon(R.drawable.ic_baseline_local_florist_42);
        }else if(selectedCategory.equals(getString(R.string.fertilization))) {
            arrayAdapter.setIcon(R.drawable.ic_baseline_scatter_plot_42);
        }else if(selectedCategory.equals(getString(R.string.plant_protection))) {
            arrayAdapter.setIcon(R.drawable.ic_baseline_bug_report_42);
        }else if(selectedCategory.equals(getString(R.string.harvest))) {
            arrayAdapter.setIcon(R.drawable.ic_baseline_filter_vintage_42);
        }

        fieldActionList.setAdapter(arrayAdapter);

        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.field_action_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.field_action_menu_add) {
            AddActionDialog popup = new AddActionDialog();
            popup.show(requireActivity().getSupportFragmentManager(), "AddAction");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}