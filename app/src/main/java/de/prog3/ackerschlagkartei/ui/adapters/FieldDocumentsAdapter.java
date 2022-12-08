package de.prog3.ackerschlagkartei.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.interfaces.ItemClickListener;
import de.prog3.ackerschlagkartei.data.interfaces.ItemLongClickListener;
import de.prog3.ackerschlagkartei.data.models.DocumentModel;

public class FieldDocumentsAdapter extends RecyclerView.Adapter<FieldDocumentsAdapter.ViewHolder> {

    private final List<DocumentModel> documentModelList;
    private final LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    private ItemLongClickListener itemLongClickListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView myTextView;
        private ImageView ivThumbnail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.info_text);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
            itemClickListener.onItemClick(view, getAdapterPosition());

            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(view, getAdapterPosition());
            }

            return true;
        }
    }

    public FieldDocumentsAdapter(Context context, List<DocumentModel> documentModelList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.documentModelList = documentModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_document, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocumentModel documentModel = documentModelList.get(position);

        holder.myTextView.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(documentModel.getUploadDate()));

        if (documentModel.getContentType().equals("image/jpeg")) {
            StorageReference sr = FirebaseStorage.getInstance().getReference().child(documentModel.getUriThumbnail());

            Glide.with(this.context)
                    .load(sr)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_photo_camera_24)
                    .into(holder.ivThumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return documentModelList.size();
    }

    public DocumentModel getItem(int id) {
        return documentModelList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }
}
