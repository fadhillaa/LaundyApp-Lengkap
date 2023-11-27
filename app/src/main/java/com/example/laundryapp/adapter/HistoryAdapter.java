package com.example.laundryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laundryapp.R;
import com.example.laundryapp.db.DatabaseHelper;
import com.example.laundryapp.model.ModelHistory;
import com.example.laundryapp.util.FunctionHelper;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<ModelHistory> historyList;
    private DatabaseHelper databaseHelper;

    public HistoryAdapter(List<ModelHistory> historyList, DatabaseHelper dbHelper) {
        this.historyList = historyList;
        this.databaseHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelHistory history = historyList.get(position);
        holder.bind(history);
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDate, tvItems, tvPrice;
        private ImageView imageDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvItems = itemView.findViewById(R.id.tvItems);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imageDelete = itemView.findViewById(R.id.imageDelete);

            imageDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ModelHistory history = historyList.get(position);
                    deleteItem(history.getId());
                    historyList.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }

        public void bind(ModelHistory history) {
            tvTitle.setText(history.getTitle());
            tvDate.setText(FunctionHelper.getCurrentDate());
            tvItems.setText(String.valueOf(history.getItems()));
            tvPrice.setText(FunctionHelper.rupiahFormat(history.getPrice()));
        }

        private void deleteItem(int id) {
            databaseHelper.deleteItemData(id);
        }
    }
}
