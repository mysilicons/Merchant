package cn.mysilicon.merchant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.merchant.adapter.OrderAdapter;
import cn.mysilicon.merchant.entity.Address;
import cn.mysilicon.merchant.entity.Order;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderManagementActivity extends AppCompatActivity {
    private static final String TAG = "OrderManagementActivity";
    private int merchantId;
    private Address address;
    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        merchantId = sharedPreferences.getInt("id", 0);
        getOrderData(merchantId);
    }

    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    initView();
                    break;
                default:
                    break;
            }
        }
    };

    private void getOrderData(Integer merchant_id) {
        new Thread(() -> {
            //获取订单数据
            OkHttpClient client = new OkHttpClient();
            String url = "http://mysilicon.cn/merchant/order/list?merchant_id=" + merchant_id;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Call call = client.newCall(request);
            Response response;
            String result = null;
            try {
                response = call.execute();
                result = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderList = JSONArray.parseArray(result, Order.class);
            handler.sendEmptyMessage(0);
        }).start();
    }



    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("订单管理");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //显示订单列表
        RecyclerView recyclerView = findViewById(R.id.rv_order);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        OrderAdapter orderAdapter = new OrderAdapter(orderList, this);
        recyclerView.setAdapter(orderAdapter);
        Log.d(TAG, "initView: " + orderList.size());
        Log.d(TAG, "initView: " + merchantId);
    }

    /**
     * 监听标题栏按钮点击事件.
     *
     * @param item 按钮
     * @return 结果
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}