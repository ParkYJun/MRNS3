package com.mrns.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mrns.main.R;
import java.util.ArrayList;

public class PointVisiblityAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PointVisiblity> pointVisiblityList = new ArrayList<>();
    private ArrayList<Boolean> mVisibleList = new ArrayList<>();
    private String mTitle;

    public PointVisiblityAdapter(Context mContext, boolean isNational, String[] layerIds) {
        super();
        this.mContext = mContext;
        String[] pointKindNames;
        ArrayList<Integer> resourceIdArray = new ArrayList<>();
        String[] titles = mContext.getResources().getStringArray(R.array.point_class_array);

        if (isNational) {
            mTitle = titles[0];
            pointKindNames = mContext.getResources().getStringArray(R.array.national_point_kind_array);
            resourceIdArray.add(R.drawable.icon_point_kind_101);
            resourceIdArray.add(R.drawable.icon_point_kind_102);
            resourceIdArray.add(R.drawable.icon_point_kind_103);
            resourceIdArray.add(R.drawable.icon_point_kind_104);
            resourceIdArray.add(R.drawable.icon_point_kind_105);
            resourceIdArray.add(R.drawable.icon_point_kind_106);
            resourceIdArray.add(R.drawable.icon_point_kind_107);
            resourceIdArray.add(R.drawable.icon_point_kind_108);
            resourceIdArray.add(R.drawable.icon_point_kind_109);
        } else {
            mTitle = titles[1];
            pointKindNames = mContext.getResources().getStringArray(R.array.common_point_kind_array);
            resourceIdArray.add(R.drawable.icon_point_kind_502);
            resourceIdArray.add(R.drawable.icon_point_kind_503);
            resourceIdArray.add(R.drawable.icon_point_kind_501);
            resourceIdArray.add(R.drawable.icon_point_kind_504);
        }

        int loopCount = resourceIdArray.size();

        for (int i = 0; i < loopCount; i++) {
            boolean isVisible = false;

            for (String layerId : layerIds) {
                if (layerId.equals(String.valueOf(i))) {
                    isVisible = true;
                    break;
                }
            }

            mVisibleList.add(isVisible);
            pointVisiblityList.add(new PointVisiblity(resourceIdArray.get(i), pointKindNames[i + 1], isVisible));
        }
    }

    @Override
    public int getCount() {
        int count = pointVisiblityList.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return pointVisiblityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layer_map_nc_points_visible, null);

            holder.imageViewPointKind = (ImageView)convertView.findViewById(R.id.imageview_point_kind);
            holder.textviewPointKindName = (TextView)convertView.findViewById(R.id.textview_point_kind_name);
            holder.switchVisibility = (Switch)convertView.findViewById(R.id.switch_visiblity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        PointVisiblity pointVisiblity = pointVisiblityList.get(position);

        holder.imageViewPointKind.setImageResource(pointVisiblity.getPointKindResourceId());
        holder.textviewPointKindName.setText(pointVisiblity.getPointKindName());
        holder.switchVisibility.setChecked(pointVisiblity.isVisible());
        holder.switchVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVisibleList.set(position, ((Switch)v).isChecked());
            }
        });

        return convertView;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getNewParam() {
        String result = "";
        int count = 0;

        for (int i = 0; i < mVisibleList.size(); i++) {
            if (mVisibleList.get(i)) {
                if (0 == count) {
                    result = "show:" + i;
                } else {
                    result += ", " + i;
                }

                count++;
            }
        }

        return result;
    }
}

class ViewHolder {
    public ImageView imageViewPointKind;
    public TextView textviewPointKindName;
    public Switch switchVisibility;
}

class PointVisiblity {
    private int pointKindResourceId;
    private String pointKindName;
    private boolean isVisible;

    public PointVisiblity(int pointKindResourceId, String pointKindName, boolean isVisible) {
        this.pointKindResourceId = pointKindResourceId;
        this.pointKindName = pointKindName;
        this.isVisible = isVisible;
    }

    public int getPointKindResourceId() {
        return pointKindResourceId;
    }

    public String getPointKindName() {
        return pointKindName;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
