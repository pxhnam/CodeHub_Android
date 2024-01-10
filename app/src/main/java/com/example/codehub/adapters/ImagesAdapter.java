package com.example.codehub.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.codehub.R;
import com.example.codehub.models.ImageUrl;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.viewholder> {
    private final List<ImageUrl> urls;
    private Context context;

    public ImagesAdapter(List<ImageUrl> urls) {
        this.urls = urls;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_image, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Glide.with(context)
                .load(urls.get(position).getUrl())
                .transform(new CenterCrop())
                .into(holder.pic);
        holder.pic.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                showImageDialog(urls.get(position).getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    private void showImageDialog(String imageUrl) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_full_screen_image);
        ImageView fullScreenImageView = dialog.findViewById(R.id.full_screen_image_view);
        ImageView btnClose = dialog.findViewById(R.id.close_image);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        Glide.with(context)
                .load(imageUrl)
                .into(fullScreenImageView);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.iv_pic);
        }
    }
}
