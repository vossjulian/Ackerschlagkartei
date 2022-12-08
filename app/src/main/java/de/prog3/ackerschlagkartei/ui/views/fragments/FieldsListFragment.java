package de.prog3.ackerschlagkartei.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.interfaces.ItemClickListener;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.ui.adapters.FieldsListAdapter;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldsListViewModel;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldsMapViewModel;

public class FieldsListFragment extends Fragment implements ItemClickListener {
    private FieldsListViewModel fieldsListViewModel;
    private FieldsMapViewModel fieldsMapViewModel;

    private NavController navController;

    private RecyclerView rvFieldModels;
    private FieldsListAdapter fieldsListAdapter;

    public FieldsListFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fields_list, container, false);
        this.rvFieldModels = view.findViewById(R.id.rv_field_list);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fieldsListViewModel = new ViewModelProvider(requireActivity()).get(FieldsListViewModel.class);
        this.fieldsMapViewModel = new ViewModelProvider(requireActivity()).get(FieldsMapViewModel.class);

        this.navController = Navigation.findNavController(view);
        this.rvFieldModels.setLayoutManager(new LinearLayoutManager(requireContext()));

        this.fieldsListViewModel.getFieldListData().observe(getViewLifecycleOwner(), new Observer<List<FieldModel>>() {
            @Override
            public void onChanged(List<FieldModel> fieldModels) {
                fieldsListAdapter = new FieldsListAdapter(requireActivity(), fieldModels);
                fieldsListAdapter.setClickListener(FieldsListFragment.this);
                rvFieldModels.setAdapter(fieldsListAdapter);
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
        inflater.inflate(R.menu.fields_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_add_field){
            this.navController.navigate(R.id.action_fieldListFragment_to_fieldAddFragment);
            return true;
        }

        if(id == R.id.menu_change_view){
            this.navController.navigateUp();
            return true;
        }

        if(id == R.id.menu_open_profile){
            this.navController.navigate(R.id.action_fieldsListFragment_to_profileFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        FieldModel selectedField = this.fieldsListAdapter.getItem(position);

        this.fieldsListViewModel.setSelectedFieldModel(selectedField);
        this.fieldsMapViewModel.setSelectedFieldModel(selectedField);

        this.navController.navigate(R.id.action_fieldsListFragment_to_fieldDetailsFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}