package com.example.codehub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.codehub.R;
import com.example.codehub.models.Source;

import java.util.List;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.viewholder> {
    private final List<Source> sources;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public SourcesAdapter(List<Source> sources) {
        this.sources = sources;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int itemId);
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_source, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.name.setText(sources.get(position).getName());
        holder.coin.setText(String.valueOf(sources.get(position).getFee()));
        Glide.with(context)
                .load(sources.get(position).getImage())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(sources.get(position).getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return sources.size();
    }


    public static class viewholder extends RecyclerView.ViewHolder {
        TextView name, coin;
        ImageView pic;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            coin = itemView.findViewById(R.id.tv_coin);
            pic = itemView.findViewById(R.id.iv_pic);
        }
    }
}
