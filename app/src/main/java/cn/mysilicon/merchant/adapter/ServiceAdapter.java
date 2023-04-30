package cn.mysilicon.merchant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.mysilicon.merchant.R;
import cn.mysilicon.merchant.entity.Service;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private static final String TAG = "ServiceAdapter";
    private List<Service> serviceList;
    private Context mContext;

    public ServiceAdapter(List<Service> serviceList, Context mContext) {
        this.serviceList = serviceList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.tvServiceName.setText(service.getTitle());
        holder.tvServiceContent.setText(service.getContent());
        holder.tvServicePrice.setText(String.valueOf(service.getPrice()));
        Glide.with(mContext).load(service.getImage_url()).into(holder.ivServiceImage);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName;
        TextView tvServiceContent;
        TextView tvServicePrice;
        ImageView ivServiceImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);
            tvServiceContent = itemView.findViewById(R.id.tv_service_content);
            tvServicePrice = itemView.findViewById(R.id.tv_service_price);
            ivServiceImage = itemView.findViewById(R.id.iv_service_image);
        }
    }
}
