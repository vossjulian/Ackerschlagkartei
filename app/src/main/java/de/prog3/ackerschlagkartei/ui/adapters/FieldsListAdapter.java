package de.prog3.ackerschlagkartei.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.interfaces.ItemClickListener;
import de.prog3.ackerschlagkartei.data.models.FieldModel;

public class FieldsListAdapter extends RecyclerView.Adapter<FieldsListAdapter.ViewHolder> {

    private List<FieldModel> fieldModelList;
    private final LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView fieldDescription;
        private TextView fieldArea;
        private ImageView ivVisible;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            fieldDescription = itemView.findViewById(R.id.tv_item_field_description);
            fieldArea = itemView.findViewById(R.id.tv_item_field_area);
            ivVisible = itemView.findViewById(R.id.iv_item_field_visible);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public FieldsListAdapter(Context context, List<FieldModel> fieldModelList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.fieldModelList = fieldModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_field, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FieldModel fieldModel = fieldModelList.get(position);
        holder.fieldDescription.setText(fieldModel.getInfo().getDescription());
        holder.fieldArea.setText(String.format("%.2f ha", fieldModel.getInfo().getArea()));
        if(fieldModel.getInfo().isVisible()){
            holder.ivVisible.setVisibility(View.INVISIBLE);
        }else{
            holder.ivVisible.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return fieldModelList.size();
    }

    public FieldModel getItem(int id) {
        return fieldModelList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}