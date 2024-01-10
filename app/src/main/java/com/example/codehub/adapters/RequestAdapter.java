package com.example.codehub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codehub.R;
import com.example.codehub.models.Request;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.Viewholder> {
    private List<Request> requests;
    private OnItemClickListener itemClickListener;

    public RequestAdapter(List<Request> requests) {
        this.requests = requests;
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
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_request, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Request request = requests.get(position);
        holder.name.setText(request.getName());
        holder.desc.setText(request.getDesc());
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClicked(request.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView name, desc;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            desc = itemView.findViewById(R.id.tv_desc);
        }
    }
}
