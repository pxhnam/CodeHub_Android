package com.example.codehub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codehub.R;
import com.example.codehub.models.Method;

import java.util.List;

public class MethodAdapter extends RecyclerView.Adapter<MethodAdapter.Viewholder> {
    private List<Method> methods;
    private OnItemClickListener itemClickListener;

    public MethodAdapter(List<Method> methods) {
        this.methods = methods;
    }

    public interface OnItemClickListener {
        void onItemClicked(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_method, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Method method = methods.get(position);
        holder.name.setText(method.getName());
        Context context = holder.pic.getContext();
        int imageID = context.getResources().getIdentifier(method.getImage(), "mipmap", context.getPackageName());
        if (imageID != 0) {
            holder.pic.setImageResource(imageID);
        }


        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClicked(method.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return methods.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
        }
    }
}
