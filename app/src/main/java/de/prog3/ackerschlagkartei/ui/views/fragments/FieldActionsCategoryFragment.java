package de.prog3.ackerschlagkartei.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.ui.adapters.FieldActionsCategoryAdapter;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldActionsViewModel;

public class FieldActionsCategoryFragment extends Fragment {
    private FieldActionsViewModel fieldActionsViewModel;
    private NavController navController;
    private ListView fieldActionsListView;
    private List<String> actionList;
    private FieldActionsCategoryAdapter adapter;
    private Integer[] listIcons;

    public FieldActionsCategoryFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_actions_category, container, false);

        this.fieldActionsListView = view.findViewById(R.id.field_actions_list_view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fieldActionsViewModel = new ViewModelProvider(requireActivity()).get(FieldActionsViewModel.class);
        this.navController = Navigation.findNavController(view);

        this.actionList = new ArrayList<>();
        this.listIcons = new Integer[] {
                R.drawable.ic_baseline_fence_42,
                R.drawable.ic_baseline_local_florist_42,
                R.drawable.ic_baseline_scatter_plot_42,
                R.drawable.ic_baseline_bug_report_42,
                R.drawable.ic_baseline_filter_vintage_42
        };

        adapter = new FieldActionsCategoryAdapter(requireActivity(), this.actionList, this.listIcons);
        this.fieldActionsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        this.fieldActionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fieldActionsViewModel.setActionCategory(fieldActionsListView.getItemAtPosition(position).toString());
                navController.navigate(R.id.action_fieldActionsCategoryFragment_to_fieldActionsFragment);
            }
        });

        initListData();
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

    private void initListData() {
        this.actionList.add(getString(R.string.soil_cultivation));
        this.actionList.add(getString(R.string.sowing));
        this.actionList.add(getString(R.string.fertilization));
        this.actionList.add(getString(R.string.plant_protection));
        this.actionList.add(getString(R.string.harvest));
    }
}