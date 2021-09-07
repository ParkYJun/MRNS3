package com.mrns.updata.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.main.R;
import com.mrns.updata.adapter.listener.ClickListener;
import com.mrns.updata.record.UploadWorkNcpRecord;
import com.mrns.utils.CommonUtils;

import java.util.ArrayList;

public class UploadWorkListNcpAdapter extends RecyclerView.Adapter<UploadWorkListNcpAdapter.UploadWorkViewHolder> {

    public ArrayList<UploadWorkNcpRecord> items;
    private static ClickListener clickListener;
    private Context context;
    private CommonUtils mCommonUtils;

    public UploadWorkListNcpAdapter(Context context) {
        this.context = context;
        mCommonUtils = new CommonUtils(context);
    }

    public void setItems(ArrayList<UploadWorkNcpRecord> mItems) {
        this.items = mItems;
    }

    public void setOnItemClickListener(ClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public UploadWorkListNcpAdapter.UploadWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_upload_work_list, parent, false);
        return new UploadWorkViewHolder(itemLayoutView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(UploadWorkViewHolder holder, final int position) {
        holder.cb_upload_list.setTag(items.get(position));
        holder.cb_upload_list.setChecked(items.get(position).isSelectecd());
        holder.iv_upload_work_pointKnd.setImageResource(getPointKndRes(items.get(position).getPointsKnd()));
        holder.tv_upload_work_pointnm.setText(items.get(position).getPointsNm());
        holder.tv_upload_work_mtrsttus.setText(getMtrSttus(items.get(position).getMtrSttusCd()));
        holder.tv_upload_work_inqde.setText(items.get(position).getInqDe());
        holder.tv_upload_work_inqsttus.setText(getInqSttus(items.get(position).getInq_sttus()));
        holder.tv_upload_work_inqsttus.setTextColor(getColorInqSttus(items.get(position).getInq_sttus()));
    }


    public static class UploadWorkViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cb_upload_list;
        private ImageView iv_upload_work_pointKnd;
        private TextView tv_upload_work_pointnm;
        private TextView tv_upload_work_mtrsttus;
        private TextView tv_upload_work_inqde;
        private TextView tv_upload_work_inqsttus;

        private UploadWorkViewHolder(View itemView) {

            super(itemView);
            cb_upload_list = (CheckBox) itemView.findViewById(R.id.upload_ck);
            iv_upload_work_pointKnd = (ImageView) itemView.findViewById(R.id.point_kind);
            tv_upload_work_pointnm = (TextView) itemView.findViewById(R.id.point_title);
            tv_upload_work_mtrsttus = (TextView) itemView.findViewById(R.id.point_status);
            tv_upload_work_inqde = (TextView) itemView.findViewById(R.id.invest_date);
            tv_upload_work_inqsttus = (TextView) itemView.findViewById(R.id.invest_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition(), v);
                }
            });

            cb_upload_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onCheckBoxClick(getAdapterPosition(), v);
                }
            });

        }

    }

    private int getColorInqSttus(String sttus){
        return mCommonUtils.getColorInqSttus(sttus);
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

}
