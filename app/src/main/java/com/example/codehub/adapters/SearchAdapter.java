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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewholder> {
    List<Source> sources;
    private Context context;

    public SearchAdapter(List<Source> sources) {
        this.sources = sources;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_search, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Source source = sources.get(position);

        holder.name.setText(source.getName());
        holder.language.setText(source.getLanguage());
        holder.fee.setText(String.valueOf(source.getFee()));
        Glide.with(context)
                .load(source.getImage())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name, language, fee, seeDetail;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.iv_pic);
            name = itemView.findViewById(R.id.tv_Name);
            fee = itemView.findViewById(R.id.tv_Fee);
            seeDetail = itemView.findViewById(R.id.btn_SeeDetail);
            language = itemView.findViewById(R.id.tv_Language);
        }
    }
}
