package com.boomingbones.ncov_mvvm.ui.rumors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.boomingbones.ncov_mvvm.R;
import com.boomingbones.ncov_mvvm.bean.Rumor;
import com.boomingbones.ncov_mvvm.utils.UnitUtil;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;

    private final List<Rumor> rumorList;
    private final int length;

    public RecyclerViewAdapter(List<Rumor> rumorList) {
        this.rumorList = rumorList;
        this.length = rumorList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_rumors, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rumor rumor = rumorList.get(position);
        holder.number_textView.setText(rumor.number);
        holder.title_textView.setText(rumor.title);
        holder.summary_textView.setText(rumor.summary);
        holder.content_textView.setText(rumor.content);
        switch (rumor.type) {
            case -1:
                holder.type_imageView.setImageResource(R.drawable.rumor_false);
                break;
            case 0:
                holder.type_imageView.setImageResource(R.drawable.rumor_unknown);
                break;
            case 1:
                holder.type_imageView.setImageResource(R.drawable.rumor_true);
                break;
        }

        if (position == length - 1) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            int px = UnitUtil.dp2px(context, 10);
            params.setMargins(px, px, px, px);
            holder.rumor_cardView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView rumor_cardView;
        private final TextView number_textView;
        private final TextView title_textView;
        private final TextView summary_textView;
        private final TextView content_textView;
        private final ImageView type_imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rumor_cardView = itemView.findViewById(R.id.cardView_rumor);
            number_textView = itemView.findViewById(R.id.rumor_textView_number);
            title_textView = itemView.findViewById(R.id.rumor_textView_title);
            summary_textView = itemView.findViewById(R.id.rumor_textView_summary);
            content_textView = itemView.findViewById(R.id.rumor_textView_content);
            type_imageView = itemView.findViewById(R.id.rumor_imageView_type);
        }
    }
}
