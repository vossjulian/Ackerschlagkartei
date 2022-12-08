package de.prog3.ackerschlagkartei.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import de.prog3.ackerschlagkartei.R;

public class FieldActionsCategoryAdapter extends ArrayAdapter {
    private final List objects;
    private final Integer[] images;
    private final Activity context;

    public FieldActionsCategoryAdapter(@NonNull Activity context, List objects, Integer[] images) {
        super(context, R.layout.item_action, objects);
        this.context = context;
        this.objects = objects;
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView == null) {
            row = inflater.inflate(R.layout.item_action, null, true);
        }

        TextView labelListItem = row.findViewById(R.id.tv_item_action_name);
        TextView dateListItem = row.findViewById(R.id.tv_item_action_date);
        ImageView iconListItem = row.findViewById(R.id.iv_item_action);

        labelListItem.setText(objects.get(position).toString());
        dateListItem.setText("");
        if(images != null) {
            iconListItem.setImageResource(images[position]);
        }

        return row;
    }
}
