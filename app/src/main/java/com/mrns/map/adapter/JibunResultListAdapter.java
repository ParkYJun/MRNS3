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

public class JibunResultListAdapter extends RecyclerView.Adapter<JibunResultListAdapter.JibunResultViewHolder>
{

    public ArrayList<AddrSearchResultRecord> items;
    private static ClickListener clickListener;
    //private Context context;

    public JibunResultListAdapter(Context context) {
        //this.context = context;
    }

    public void setItems(ArrayList<AddrSearchResultRecord> mItems) {
        this.items = mItems;
    }

    public void setOnItemClickListener(ClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public JibunResultListAdapter.JibunResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_jibun_result, parent, false);
        return new JibunResultViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(JibunResultViewHolder holder, int position) {
        holder.textviewParcel.setText(items.get(position).getAddress());
    }

    @Override
    public int getItemCount() { return items.size(); }


    public static class JibunResultViewHolder extends RecyclerView.ViewHolder {

        private TextView textviewParcel;
        private ImageView imageviewMoveMap;

        private JibunResultViewHolder(View itemView) {
            super(itemView);
            textviewParcel = (TextView) itemView.findViewById(R.id.textview_parcel);
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
