package com.mrns.map.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.invest.adapter.listener.ClickListener;
import com.mrns.main.R;
import com.mrns.map.record.AddrSearchResultRecord;

import java.util.ArrayList;

public class BuildResultListAdapter extends RecyclerView.Adapter<BuildResultListAdapter.BuildResultViewHolder>  {

    public ArrayList<AddrSearchResultRecord> items;
    public static ClickListener clickListener;
    Context context;

    public BuildResultListAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<AddrSearchResultRecord> mItems) {
        this.items = mItems;
    }

    public void setOnItemClickListener(ClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public BuildResultListAdapter.BuildResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_build_result, parent, false);
        BuildResultViewHolder viewHolder = new BuildResultViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BuildResultViewHolder holder, int position) {
        holder.textviewAddress.setText(items.get(position).getAddress());
    }

    @Override
    public int getItemCount() { return items.size(); }

    public static class BuildResultViewHolder extends RecyclerView.ViewHolder {

        public TextView textviewAddress;
        public ImageView imageviewMoveMap;


        public BuildResultViewHolder(View itemView) {
            super(itemView);
            textviewAddress = (TextView) itemView.findViewById(R.id.textview_address);
            imageviewMoveMap = (ImageView) itemView.findViewById(R.id.imageview_move_map);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition(), v);
                }
            });
            imageviewMoveMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onLocationClick(getAdapterPosition(), v);
                }
            });
        }
    }
}
