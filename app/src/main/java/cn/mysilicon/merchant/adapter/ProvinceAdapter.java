package cn.mysilicon.merchant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.mysilicon.merchant.R;


public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {
    List<String> dataList;
    int selection = -1;

    onItemClick itemClick;

    public void setOnItemListener(onItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public ProvinceAdapter(List<String> list) {
        this.dataList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_picker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mTvName.setText(dataList.get(position));

        if (selection == position) {
            holder.mImgCheck.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClick(position, dataList.get(position));
            }
        });

    }

    public void setSelection(int position) {
        this.selection = position;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        ImageView mImgCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.name);
            mImgCheck = itemView.findViewById(R.id.img_check);
        }
    }

    public interface onItemClick {
        void onClick(int position, String name);
    }

}
