package cn.mysilicon.merchant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.mysilicon.merchant.OrderDetailActivity;
import cn.mysilicon.merchant.R;
import cn.mysilicon.merchant.entity.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private static final String TAG = "OrderAdapter";
    private Context mContext;
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderNumber.setText(String.valueOf(order.getOrder_number()));
        holder.tvOrderStatus.setText(order.getCur_status());
        holder.tvOrderDate.setText(order.getOrder_time());
        Glide.with(mContext).load(order.getImage()).into(holder.ivOrderImage);
        holder.tvOrderName.setText(order.getUsername());
        holder.tvOrderPhone.setText(order.getPhone());
        holder.tvOrderAddress.setText(order.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", order.getId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber;
        TextView tvOrderStatus;
        TextView tvOrderDate;
        ImageView ivOrderImage;
        TextView tvOrderName;
        TextView tvOrderPhone;
        TextView tvOrderAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tv_order_number);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            ivOrderImage = itemView.findViewById(R.id.iv_order_image);
            tvOrderName = itemView.findViewById(R.id.tv_order_name);
            tvOrderPhone = itemView.findViewById(R.id.tv_order_phone);
            tvOrderAddress = itemView.findViewById(R.id.tv_order_address);
        }
    }
}
