package com.mrns.map.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.invest.util.DBUtil;
import com.mrns.main.R;
import com.mrns.map.adapter.listener.ClickListener;
import com.mrns.map.record.PointResultRecord;
import com.mrns.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class PointResultListAdapter extends RecyclerView.Adapter<PointResultListAdapter.PointResultViewHolder> {
    public ArrayList<PointResultRecord> items;
    private static ClickListener clickListener;
    private Context context;
    private CommonUtils mCommonUtils;

    public PointResultListAdapter(Context context) {
        this.context = context;
        mCommonUtils = new CommonUtils(context);
    }

    public void setItems(ArrayList<PointResultRecord> mItems) {
        this.items = mItems;
    }


    public void setOnItemClickListener(ClickListener listener) {
        this.clickListener = listener;
    }

    private String getInqSttus(String sttus){
        return mCommonUtils.getInqSttus(sttus);
    }

    private String getMtrSttus(String sttus) {
        return mCommonUtils.getMtrSttus(sttus, false);
    }

    private int getPointKndRes(String points_knd){
        return mCommonUtils.getPointKndRes(points_knd);
    }

    @Override
    public PointResultListAdapter.PointResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_point_result, parent, false);
        return new PointResultViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(PointResultViewHolder holder, int position) {
        PointResultRecord record = items.get(position);
        int pointKindImageResourceId = getPointKndRes(record.getPointKindCode());
        String pointName = record.getPointName();
        String mtrStatus = DBUtil.getCmmnCode("110", record.getMtrStatusCode());
        String installDate = record.getInstallDate();
        String inqStatusCode = checkInqStatus(record.getInqDate());
        String inqStatusText = mCommonUtils.getInqSttus(inqStatusCode);
        int inqStatusColor = mCommonUtils.getColorInqSttus(inqStatusCode);

        holder.imageviewPointKind.setImageResource(pointKindImageResourceId);
        holder.textviewPointName.setText(checkNull(pointName));
        holder.textviewMtrStatus.setText(checkNull(mtrStatus));
        holder.textviewInstallDate.setText(checkNull(installDate));
        holder.textviewInqStatus.setText(checkNull(inqStatusText));
        holder.textviewInqStatus.setTextColor(inqStatusColor);
    }

    private String checkNull(String checkString) {
        if (checkString == null || checkString.equals("null") || checkString.equals("")) {
            checkString = context.getString(R.string.hyphen);
        }

        return checkString;
    }

    private String checkInqStatus(String inqDate) {
        String result = null;

        if (inqDate != null && !inqDate.equals("")) {
            Calendar calendar = java.util.Calendar.getInstance();
            int year = calendar.get(calendar.YEAR);

            if (inqDate.substring(0, 4).equals(String.valueOf(year))) {
                result = "2";
            } else {
                result = "0";
            }
        }

        return result;
    }

    private int getColorInqSttus(String sttus){
        return mCommonUtils.getColorInqSttus(sttus);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PointResultViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageviewPointKind;
        private TextView textviewPointName;
        private TextView textviewMtrStatus;
        private TextView textviewInstallDate;
        private TextView textviewInqStatus;

        private PointResultViewHolder(View itemView) {
            super(itemView);
            imageviewPointKind = (ImageView) itemView.findViewById(R.id.imageview_point_kind);
            textviewPointName = (TextView) itemView.findViewById(R.id.textview_point_name);
            textviewMtrStatus = (TextView) itemView.findViewById(R.id.textview_mtr_status);
            textviewInstallDate = (TextView) itemView.findViewById(R.id.textview_install_date);
            textviewInqStatus = (TextView) itemView.findViewById(R.id.textview_inq_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }


}