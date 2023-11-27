package com.example.laundryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundryapp.R;
import com.example.laundryapp.model.ModelMenu;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<ModelMenu> modelMenuList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(ModelMenu modelMenu);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MenuAdapter(Context context, List<ModelMenu> modelMenuList) {
        this.context = context;
        this.modelMenuList = modelMenuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        ModelMenu currentItem = modelMenuList.get(position);
        holder.tvTitle.setText(currentItem.getTvTitle());
        holder.imageMenu.setImageResource(currentItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return modelMenuList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView imageMenu;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imageMenu = itemView.findViewById(R.id.imageMenu);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(modelMenuList.get(position));
                    }
                }
            });
        }
    }
}
