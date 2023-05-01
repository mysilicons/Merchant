package cn.mysilicon.merchant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.merchant.adapter.ServiceAdapter;
import cn.mysilicon.merchant.entity.Service;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServiceManagementActivity extends AppCompatActivity {
    private static final String TAG = "ServiceManagementActivity";
    private int merchant_id;
    private List<Service> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        merchant_id = sharedPreferences.getInt("id", 0);
        getServiceData(merchant_id);
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

    private void getServiceData(Integer merchant_id) {
        new Thread(() -> {
            //获取服务数据
            OkHttpClient client = new OkHttpClient();
            String url = "http://mysilicon.cn/merchant/service/list?merchant_id=" + merchant_id;
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
            if (response.code()!=200){
                Looper.prepare();
                Toast.makeText(ServiceManagementActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                serviceList = JSONArray.parseArray(result, Service.class);
                Log.d(TAG, "getServiceData: " + serviceList.size());
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("服务管理");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.rv_service);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        ServiceAdapter serviceAdapter = new ServiceAdapter(serviceList, this);
        recyclerView.setAdapter(serviceAdapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        getServiceData(merchant_id);
    }
}