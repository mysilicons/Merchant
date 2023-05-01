package cn.mysilicon.merchant;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;

import cn.mysilicon.merchant.entity.Service;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServiceDetailActivity extends AppCompatActivity {

    private static final String TAG = "ServiceDetailActivity";
    private Integer id;
    private ImageView iv_image;
    private Service service;
    private EditText et_iamge;
    private EditText et_name;
    private EditText et_price;
    private EditText et_description;
    private Button btn_submit;
    private Button btn_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        getData(id);
    }

    final Handler handler = new Handler(Looper.getMainLooper(), msg -> {
        switch (msg.what) {
            case 0:
                initView();
                initListener();
                break;
            case 1:
                Toast.makeText(ServiceDetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 2:
                Toast.makeText(ServiceDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
        return false;
    });

    private void initListener() {
        btn_submit = findViewById(R.id.service_submit);
        btn_submit.setOnClickListener(v -> {
            String image_url = et_iamge.getText().toString();
            String title = et_name.getText().toString();
            String price = et_price.getText().toString();
            String content = et_description.getText().toString();
            Service submitService = new Service(service.getId(), service.getClassification1(),service.getClassification2(), image_url, title, content, price, service.getCity(), service.getMerchant_id());
            serviceSubmit(submitService);
        });

        btn_delete = findViewById(R.id.service_delete);
        btn_delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ServiceDetailActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确认删除该服务？");
            builder.setPositiveButton("确认", (dialog, which) -> {
                serviceDelete(service.getId());
            });
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            builder.show();
        });
    }

    private void serviceDelete(Integer id) {
        new Thread(() -> {
            //获取订单数据
            OkHttpClient client = new OkHttpClient();
            String url = "http://mysilicon.cn/service/delete?id=" + id;
            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();
            Call call = client.newCall(request);
            Response response = null;
            try {
                response = call.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response.code()!=200){
                Looper.prepare();
                Toast.makeText(ServiceDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                handler.sendEmptyMessage(2);
            }
        }).start();
    }

    private void serviceSubmit(Service submitService) {
        new Thread(() -> {
            //获取订单数据
            OkHttpClient client = new OkHttpClient();
            String url = "http://mysilicon.cn/service/update";
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(submitService)))
                    .build();
            Call call = client.newCall(request);
            Response response = null;
            try {
                response = call.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response.code()!=200){
                Looper.prepare();
                Toast.makeText(ServiceDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void getData(Integer id) {
        new Thread(() -> {
            //获取订单数据
            OkHttpClient client = new OkHttpClient();
            String url = "http://mysilicon.cn/service/get?id=" + id;
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
                Toast.makeText(ServiceDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                service = JSONArray.parseObject(result, Service.class);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("服务详情");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        iv_image = findViewById(R.id.service_image);
        et_iamge = findViewById(R.id.service_imageurl);
        et_name = findViewById(R.id.service_name);
        et_price = findViewById(R.id.service_price);
        et_description = findViewById(R.id.service_detail);

        Glide.with(this).load(service.getImage_url()).into(iv_image);
        et_iamge.setText(service.getImage_url());
        et_name.setText(service.getTitle());
        et_price.setText(service.getPrice());
        et_description.setText(service.getContent());
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