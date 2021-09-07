package com.mrns.invest.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.invest.adapter.listener.ClickListener;
import com.mrns.invest.record.InvestResultRecord;
import com.mrns.main.R;
import com.mrns.utils.CommonUtils;

import java.util.ArrayList;


public class InvestResultListAdapter extends RecyclerView.Adapter<InvestResultListAdapter.InvestResultViewHolder> {
    public ArrayList<InvestResultRecord> items;
    private static ClickListener clickListener;
    private Context context;
    private CommonUtils mCommonUtils;

    public InvestResultListAdapter(Context context) {
        this.context = context;
        mCommonUtils = new CommonUtils(context);
    }

    public void setItems(ArrayList<InvestResultRecord> mItems) {
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

//    private int getPointKndRes(String points_knd){
//        int pKid = 0;
//        if(points_knd.equals("201"))
//            pKid = R.drawable.ic_list_type_04;
//        else if(points_knd.equals("301"))
//            pKid = R.drawable.ic_list_type_05;
//        else if(points_knd.equals("401"))
//            pKid = R.drawable.ic_list_type_red;
//        return pKid;
//    }

    private int getPointKndRes(String sttus){
        int pKid = 0;

        switch (sttus) {
            case "0":
                pKid = R.drawable.ic_list_type_gray;
                break;
            case "1":
                pKid = R.drawable.ic_list_type_red;
                break;
            case "2":
                pKid = R.drawable.ic_list_type_blue;
                break;
            case "3":
                pKid = R.drawable.ic_list_type_green;
                break;
        }

        return pKid;
    }


    @Override
    public InvestResultListAdapter.InvestResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_invest_result, parent, false);
        return new InvestResultViewHolder(itemLayoutView);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(InvestResultViewHolder holder, int position) {
        holder.iv_invest_result_pointKnd.setImageResource(getPointKndRes(items.get(position).getInq_sttus()));
        holder.tv_invest_result_pointnm.setText(items.get(position).getPointsNm());
        String sttus = items.get(position).getInq_sttus();
        if(sttus.equals("2")){//완료면 현황조사한 결과인 년월일, 조사 상태를 표출
            holder.tv_invest_result_mtrsttus.setText(getMtrSttus(items.get(position).getBeMtrSttusCd()));
            holder.tv_invest_result_instlde.setText(items.get(position).getBeInqDe());
        } else {
            holder.tv_invest_result_mtrsttus.setText(getMtrSttus(items.get(position).getMtrSttusCd()));
            holder.tv_invest_result_instlde.setText(items.get(position).getInqDe());
        }
        holder.tv_invest_result_inqsttus.setText(getInqSttus(items.get(position).getInq_sttus()));
        holder.tv_invest_result_inqsttus.setTextColor(getColorInqSttus(items.get(position).getInq_sttus()));

    }

    private int getColorInqSttus(String sttus){
        return mCommonUtils.getColorInqSttus(sttus);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class InvestResultViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_invest_result_pointKnd;
        private TextView tv_invest_result_pointnm;
        private TextView tv_invest_result_mtrsttus;
        private TextView tv_invest_result_instlde;
        private TextView tv_invest_result_inqsttus;
        private ImageView tv_invest_result_location;


        public InvestResultViewHolder(View itemView) {
            super(itemView);
            iv_invest_result_pointKnd = (ImageView) itemView.findViewById(R.id.iv_invest_result_pointKnd);
            tv_invest_result_pointnm = (TextView) itemView.findViewById(R.id.tv_invest_result_pointnm);
            tv_invest_result_mtrsttus = (TextView) itemView.findViewById(R.id.tv_invest_result_mtrsttus);
            tv_invest_result_instlde = (TextView) itemView.findViewById(R.id.tv_invest_result_instlde);
            tv_invest_result_inqsttus = (TextView) itemView.findViewById(R.id.tv_invest_result_inqsttus);
            tv_invest_result_location = (ImageView) itemView.findViewById(R.id.tv_invest_result_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition(), v);
                }
            });
            tv_invest_result_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onLocationClick(getAdapterPosition(), v);
                }
            });
        }
    }

}
