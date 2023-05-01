package cn.mysilicon.merchant;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;

import cn.mysilicon.merchant.entity.Order;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private Integer id;
    private Order order;
    private ImageView orderImage;
    private TextView orderNumber;
    private TextView orderTime;
    private TextView orderName;
    private TextView userName;
    private TextView userPhone;
    private TextView userAddress;
    private TextView serverTime;
    private TextView orderStatus;
    private Button btnOrderDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        getData(id);
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

        orderImage = findViewById(R.id.iv_order_image);
        orderNumber = findViewById(R.id.tv_order_number);
        orderTime = findViewById(R.id.tv_order_create_time);
        orderName = findViewById(R.id.tv_order_server);
        userName = findViewById(R.id.tv_order_user);
        userPhone = findViewById(R.id.tv_order_phone);
        userAddress = findViewById(R.id.tv_order_address);
        serverTime = findViewById(R.id.tv_order_server_time);
        orderStatus = findViewById(R.id.tv_order_status);
        btnOrderDelete = findViewById(R.id.btn_order_delete);
        if (order.getCur_status().equals("已完成")) {
            btnOrderDelete.setVisibility(Button.VISIBLE);
            btnOrderDelete.setOnClickListener(v -> {
                deleteOrder(order.getId());
            });
        } else {
            btnOrderDelete.setVisibility(Button.GONE);
        }

        Glide.with(this).load(order.getImage()).into(orderImage);
        orderNumber.setText(String.valueOf(order.getOrder_number()));
        orderTime.setText(order.getOrder_time());
        orderName.setText(order.getName());
        userName.setText(order.getUsername());
        userPhone.setText(order.getPhone());
        userAddress.setText(order.getAddress());
        serverTime.setText(order.getServer_time());
        orderStatus.setText(order.getCur_status());
    }

    private void deleteOrder(int id) {
        //删除订单
        new Thread(() -> {
            //获取订单数据
            OkHttpClient client = new OkHttpClient();
            String url = "http://mysilicon.cn/merchant/order/delete?id=" + id;
            Log.d("url", url);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create("", null))
                    .build();
            Call call = client.newCall(request);
            Response response = null;
            String result = null;
            try {
                response = call.execute();
                result = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response.code() != 200) {
                Looper.prepare();
                Toast.makeText(OrderDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    final Handler handler = new Handler(Looper.getMainLooper(), msg -> {
        switch (msg.what) {
            case 0:
                initView();
                break;
            case 1:
                Toast.makeText(OrderDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
        return false;
    });


    private void getData(Integer id) {
        new Thread(() -> {
            //获取订单数据
            OkHttpClient client = new OkHttpClient();
            String url = "http://mysilicon.cn/merchant/order/get?id=" + id;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Call call = client.newCall(request);
            Response response = null;
            String result = null;
            try {
                response = call.execute();
                result = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response.code() != 200) {
                Looper.prepare();
                Toast.makeText(OrderDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                order = JSONArray.parseObject(result, Order.class);
                handler.sendEmptyMessage(0);
            }
        }).start();
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