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

import java.text.SimpleDateFormat;
import java.util.List;

import de.prog3.ackerschlagkartei.R;
import de.prog3.ackerschlagkartei.data.models.ActionModel;

public class FieldActionsAdapter extends ArrayAdapter {
    private final List<ActionModel> objects;
    private Integer icon;
    private final Activity context;

    public FieldActionsAdapter(@NonNull Activity context, List<ActionModel> objects) {
        super(context, R.layout.item_action, objects);
        this.context = context;
        this.objects = objects;
        this.icon = 0;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
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

        labelListItem.setText(objects.get(position).getDescription());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        dateListItem.setText(simpleDateFormat.format(objects.get(position).getDate()));
        iconListItem.setImageResource(icon);

        return row;
    }
}
