package team.uptech.motionviews.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mrns.main.R;

import java.util.ArrayList;
import java.util.List;

public class StickerSelectActivity extends AppCompatActivity {

    public static final String EXTRA_STICKER_ID = "extra_sticker_id";

    private final int[] stickerIds = {
            R.drawable.icon_object_01,
            R.drawable.icon_object_02,
            R.drawable.icon_object_03,
            R.drawable.icon_object_04,
            R.drawable.icon_object_05,
            R.drawable.icon_object_06,
            R.drawable.icon_object_07,
            R.drawable.icon_object_08,
            R.drawable.icon_object_09,
            R.drawable.icon_object_10,
            R.drawable.icon_object_11,
            R.drawable.icon_object_12,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_sticker_activity);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stickers_recycler_view);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(glm);

        List<Integer> stickers = new ArrayList<>(stickerIds.length);
        for (Integer id : stickerIds) {
            stickers.add(id);
        }

        recyclerView.setAdapter(new StickersAdapter(stickers, this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onStickerSelected(int stickerId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_STICKER_ID, stickerId);
        setResult(RESULT_OK, intent);
        finish();
    }

    class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.StickerViewHolder> {

        private final List<Integer> stickerIds;
        private final Context context;
        private final LayoutInflater layoutInflater;

        StickersAdapter(@NonNull List<Integer> stickerIds, @NonNull Context context) {
            this.stickerIds = stickerIds;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public StickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StickerViewHolder(layoutInflater.inflate(R.layout.sticker_item, parent, false));
        }

        @Override
        public void onBindViewHolder(StickerViewHolder holder, int position) {
            holder.image.setImageDrawable(ContextCompat.getDrawable(context, getItem(position)));
        }

        @Override
        public int getItemCount() {
            return stickerIds.size();
        }

        private int getItem(int position) {
            return stickerIds.get(position);
        }

        class StickerViewHolder extends RecyclerView.ViewHolder {

            ImageView image;

            StickerViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.sticker_image);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        if (pos >= 0) { // might be NO_POSITION
                            onStickerSelected(getItem(pos));
                        }
                    }
                });
            }
        }
    }
}
