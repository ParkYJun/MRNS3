package com.mrns.invest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.main.R;

import java.util.ArrayList;

public class PenAdapter extends BaseAdapter {
    private final int INDEX_PEN_TYPE = 0;
    private final int INDEX_DASH_TYPE = 1;
    private final int INDEX_PEN_COLOR = 2;
    private final int INDEX_PEN_WIDTH = 3;

    private Context mContext;
    private int mOptionIndex;
    private ArrayList<PenOptions> penOptionsList = new ArrayList<>();

    public PenAdapter(Context mContext, int mOptionIndex) {
        super();
        this.mContext = mContext;
        this.mOptionIndex = mOptionIndex;

        int nameArrayResourceId;

        ArrayList<Integer> resourceIdArray = new ArrayList<>();

        switch (mOptionIndex) {
            case INDEX_PEN_TYPE:
                nameArrayResourceId = R.array.insert_pen_type_name;
                resourceIdArray.add(R.drawable.image_pen_type_1);
                resourceIdArray.add(R.drawable.image_pen_type_2);
                resourceIdArray.add(R.drawable.image_pen_type_3);
                break;
            case INDEX_DASH_TYPE:
                nameArrayResourceId = R.array.insert_dash_type_name;
                resourceIdArray.add(R.drawable.image_dash_type_1);
                resourceIdArray.add(R.drawable.image_dash_type_2);
                resourceIdArray.add(R.drawable.image_dash_type_3);
                break;
            case INDEX_PEN_COLOR:
                nameArrayResourceId = R.array.insert_pen_color_name;
                resourceIdArray.add(R.drawable.image_pen_color_1);
                resourceIdArray.add(R.drawable.image_pen_color_2);
                resourceIdArray.add(R.drawable.image_pen_color_3);
                resourceIdArray.add(R.drawable.image_pen_color_4);
                resourceIdArray.add(R.drawable.image_pen_color_5);
                break;
            default:
                nameArrayResourceId = R.array.insert_pen_width_name;
                resourceIdArray.add(R.drawable.image_pen_width_1);
                resourceIdArray.add(R.drawable.image_pen_width_2);
                resourceIdArray.add(R.drawable.image_pen_width_3);
                resourceIdArray.add(R.drawable.image_pen_width_4);
                resourceIdArray.add(R.drawable.image_pen_width_5);
                break;
        }

        String nameArray[] = mContext.getResources().getStringArray(nameArrayResourceId);

        for (int i = 0; i< nameArray.length; i++) {
            penOptionsList.add(new PenOptions(nameArray[i], resourceIdArray.get(i)));
        }
    }

    @Override
    public int getCount() {
        return penOptionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return penOptionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String penTypeName, int penTypeResourceId) {
        penOptionsList.add(new PenOptions(penTypeName, penTypeResourceId));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_pen_option, null);

            holder.textView = (TextView)convertView.findViewById(R.id.textView);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PenOptions penOptions = penOptionsList.get(position);

        holder.textView.setText(penOptions.getPenTypeName());
        holder.imageView.setImageResource(penOptions.getPentTypeResourceId());

        return convertView;
    }
}

class ViewHolder {
    public TextView textView;
    public ImageView imageView;
}

class PenOptions {
    private String penTypeName;
    private int pentTypeResourceId;

    public PenOptions(String penTypeName, int pentTypeResourceId) {
        this.penTypeName = penTypeName;
        this.pentTypeResourceId = pentTypeResourceId;
    }

    public String getPenTypeName() {
        return penTypeName;
    }
    public int getPentTypeResourceId() {
        return pentTypeResourceId;
    }

}