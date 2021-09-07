package com.mrns.invest.innerDetail.install;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrns.invest.adapter.listener.ClickListener;
import com.mrns.invest.record.RnsInstlHist;
import com.mrns.main.R;

import java.util.ArrayList;


public class InvestInstallListAdapter extends RecyclerView.Adapter<InvestInstallListAdapter.InvestInstallViewHolder> {
    public ArrayList<RnsInstlHist> items;
    private static ClickListener clickListener;
    private Context context;

    public InvestInstallListAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<RnsInstlHist> mItems) {
        this.items = mItems;
    }


    public void setOnItemClickListener(ClickListener listener) {
        this.clickListener = listener;
    }


    @Override
    public InvestInstallListAdapter.InvestInstallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_invest_install, parent, false);
        return new InvestInstallViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(InvestInstallViewHolder holder, int position) {
        holder.tv_invest_install_instlsn.setText(String.valueOf(items.get(position).getInstlIhSn()));
        holder.tv_invest_install_instlse.setText(items.get(position).getInstlSeCd());
        holder.tv_invest_install_instlde.setText(items.get(position).getInstlDe());
        holder.tv_invest_install_instlnm.setText(items.get(position).getInstlNm());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class InvestInstallViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_invest_install_instlsn;
        private TextView tv_invest_install_instlse;
        private TextView tv_invest_install_instlde;
        private TextView tv_invest_install_instlnm;


        private InvestInstallViewHolder(View itemView) {
            super(itemView);
            tv_invest_install_instlsn = (TextView) itemView.findViewById(R.id.tv_invest_install_instlsn);
            tv_invest_install_instlse = (TextView) itemView.findViewById(R.id.tv_invest_install_instlse);
            tv_invest_install_instlde = (TextView) itemView.findViewById(R.id.tv_invest_install_instlde);
            tv_invest_install_instlnm = (TextView) itemView.findViewById(R.id.tv_invest_install_instlnm);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }
}

