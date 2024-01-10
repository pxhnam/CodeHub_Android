package com.example.codehub.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.codehub.R;
import com.example.codehub.models.Cart;

import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    private List<Cart> carts;
    private Context context;
    private OnRemoveItemClickListener removeItemClickListener;
    private OnSeeDetailClickListener seeDetailClickListener;

    public CartAdapter(List<Cart> carts) {
        this.carts = carts;
    }


    public interface OnSeeDetailClickListener {
        void onseeDetailClicked(int id);
    }

    public interface OnRemoveItemClickListener {
        void onRemoveClicked(int pos, int id);
    }


    public void setOnSeeDetailClickListener(OnSeeDetailClickListener listener) {
        this.seeDetailClickListener = listener;
    }

    public void setOnRemoveItemClickListener(OnRemoveItemClickListener listener) {
        this.removeItemClickListener = listener;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Cart cart = carts.get(position);

        holder.name.setText(cart.getName());
        holder.language.setText(cart.getLanguage());
        holder.fee.setText(String.valueOf(cart.getFee()));
        Glide.with(context)
                .load(cart.getUrl())
                .transform(new CenterCrop(), new RoundedCorners(15))
                .into(holder.pic);


        holder.seeDetail.setOnClickListener(v -> {
            if (seeDetailClickListener != null) {
                seeDetailClickListener.onseeDetailClicked(cart.getIdSource());
            }
        });


        holder.btnRemove.setOnClickListener(v -> {
            if (removeItemClickListener != null) {
                removeItemClickListener.onRemoveClicked(position, cart.getId());
            }
        });
    }

    public void removeCartAtPosition(int position) {
        carts.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }


    public static class viewholder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name, language, fee, seeDetail;
        ImageButton btnRemove;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.iv_pic);
            name = itemView.findViewById(R.id.tv_Name);
            fee = itemView.findViewById(R.id.tv_Fee);
            seeDetail = itemView.findViewById(R.id.btn_SeeDetail);
            language = itemView.findViewById(R.id.tv_Language);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
