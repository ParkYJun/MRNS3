package com.mrns.invest.innerDetail.history;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrns.invest.adapter.listener.ClickListener;
import com.mrns.invest.record.RnsNpSttusInq;
import com.mrns.main.R;
import com.mrns.utils.CommonUtils;

import java.util.ArrayList;


public class InvestHisoryListNcpAdapter extends RecyclerView.Adapter<InvestHisoryListNcpAdapter.InvestHisoryViewHolder> {
    public ArrayList<RnsNpSttusInq> items;
    private static ClickListener clickListener;
    private Context context;
    CommonUtils mCommonUtil;

    public InvestHisoryListNcpAdapter(Context context) {
        this.context = context;
        mCommonUtil = new CommonUtils(context);
    }

    public void setItems(ArrayList<RnsNpSttusInq> mItems) {
        this.items = mItems;
    }

    public void setOnItemClickListener(ClickListener listener) {
        this.clickListener = listener;
    }


    @Override
    public InvestHisoryListNcpAdapter.InvestHisoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_invest_history, parent, false);
        return new InvestHisoryViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(InvestHisoryViewHolder holder, int position) {
        holder.tv_invest_history_sttussn.setText(String.valueOf(items.get(position).getNpsiSn()));
        holder.tv_invest_result_inqde.setText(mCommonUtil.changeNullValue(items.get(position).getInqDe(), "-"));
        holder.tv_invest_result_inqnm.setText(mCommonUtil.changeNullValue(items.get(position).getInqNm(), "-"));
        //holder.tv_invest_result_inqinstt.setText(items.get(position).getInqInsttCd() != null ? DBUtil.getCmmnCode("116", items.get(position).getInqInsttCd()) : "");
        holder.tv_invest_result_inqinstt.setText(mCommonUtil.changeNullValue(mCommonUtil.changeNullValue(items.get(position).getInqInsttCd(), "-")));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class InvestHisoryViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_invest_history_sttussn;
        private TextView tv_invest_result_inqde;
        private TextView tv_invest_result_inqnm;
        private TextView tv_invest_result_inqinstt;


        private InvestHisoryViewHolder(View itemView) {
            super(itemView);
            tv_invest_history_sttussn = (TextView) itemView.findViewById(R.id.tv_invest_history_sttussn);
            tv_invest_result_inqde = (TextView) itemView.findViewById(R.id.tv_invest_result_inqde);
            tv_invest_result_inqnm = (TextView) itemView.findViewById(R.id.tv_invest_result_inqnm);
            tv_invest_result_inqinstt = (TextView) itemView.findViewById(R.id.tv_invest_result_inqinstt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }
}

