package de.prog3.ackerschlagkartei.ui.views.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.prog3.ackerschlagkartei.BuildConfig;
import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.interfaces.ItemClickListener;
import de.prog3.ackerschlagkartei.data.interfaces.ItemLongClickListener;
import de.prog3.ackerschlagkartei.data.models.DocumentModel;
import de.prog3.ackerschlagkartei.data.models.FieldModel;
import de.prog3.ackerschlagkartei.ui.adapters.FieldDocumentsAdapter;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldDocumentsViewModel;
import de.prog3.ackerschlagkartei.ui.viewmodels.FieldsMapViewModel;

public class FieldDocumentsFragment extends Fragment implements ItemClickListener, ItemLongClickListener {
    private FieldsMapViewModel fieldsMapViewModel;
    private FieldDocumentsViewModel fieldDocumentsViewModel;

    private NavController navController;

    private RecyclerView rvFieldDocuments;
    private FieldDocumentsAdapter fieldDocumentsAdapter;

    private FieldModel selectedFieldModel;
    private DocumentModel selectedDocumentModel;

    private Uri tmpImgUri;

    public FieldDocumentsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_documents, container, false);
        this.rvFieldDocuments = view.findViewById(R.id.rv_field_documents);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.fieldsMapViewModel = new ViewModelProvider(requireActivity()).get(FieldsMapViewModel.class);
        this.fieldDocumentsViewModel = new ViewModelProvider(requireActivity()).get(FieldDocumentsViewModel.class);

        this.selectedFieldModel = this.fieldsMapViewModel.getSelectedFieldModel();
        this.navController = Navigation.findNavController(view);

        this.rvFieldDocuments.setLayoutManager(new GridLayoutManager(requireContext(), 4));

        this.fieldDocumentsViewModel.getDocumentsMutableLiveData(this.selectedFieldModel).observe(getViewLifecycleOwner(), new Observer<List<DocumentModel>>() {
            @Override
            public void onChanged(List<DocumentModel> documentModels) {
                fieldDocumentsAdapter = new FieldDocumentsAdapter(requireContext(), documentModels);
                fieldDocumentsAdapter.setClickListener(FieldDocumentsFragment.this);
                fieldDocumentsAdapter.setItemLongClickListener(FieldDocumentsFragment.this);
                rvFieldDocuments.setAdapter(fieldDocumentsAdapter);
            }
        });

        this.fieldDocumentsViewModel.getFileMutableLiveData().observe(getViewLifecycleOwner(), new Observer<File>() {
            @Override
            public void onChanged(File file) {
                if (file == null) return;
                if (selectedDocumentModel == null) return;

                Uri uri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", file);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, selectedDocumentModel.getContentType());
                startActivity(intent);
            }
        });

        this.createTempPictureFile();

    }

    private void createTempPictureFile() {
        try {
            File tmpFile = File.createTempFile("img_", null);
            this.tmpImgUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        DocumentModel doc = fieldDocumentsAdapter.getItem(position);
        this.selectedDocumentModel = doc;
        this.fieldDocumentsViewModel.downloadDocument(doc);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(getString(R.string.confirm_delete));
        builder.setPositiveButton(getString(R.string.confirm_delete_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DocumentModel doc = fieldDocumentsAdapter.getItem(position);
                fieldDocumentsViewModel.deleteDocument(doc);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.confirm_delete_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.documents_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btn_add_doc) {
            getContentActivity.launch("application/pdf");
            return true;
        }

        if (id == R.id.btn_add_image) {
            requestPermissionAndTakePicture.launch(Manifest.permission.CAMERA);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final ActivityResultLauncher<String> requestPermissionAndTakePicture = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (!result) return;

            takePictureActivity.launch(tmpImgUri);
        }
    });

    private final ActivityResultLauncher<Uri> takePictureActivity = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (!result) return;

            fieldDocumentsViewModel.updateDocument(selectedFieldModel, tmpImgUri);
        }
    });

    private final ActivityResultLauncher<String> getContentActivity = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            if (uri == null) return;

            fieldDocumentsViewModel.updateDocument(selectedFieldModel, uri);
        }
    });

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}