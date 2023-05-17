package cn.mysilicon.merchant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

public class MainActivity extends AppCompatActivity {
    private Button btnServiceRelease;
    private Button btnServiceManagement;
    private Button btnOrderManagement;
    private Button btnCustomerMessage;
    private Button btnCustomerComment;
    private Button btnCustomerService;
    private Button btnLogout;
    private TextView tvUsername;
    private String username;
    private Integer userId;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //环信
        Context context = getApplicationContext();
        EMOptions options = new EMOptions();
        options.setAppKey("填入API key");
        // 其他 EMOptions 配置。
        EMClient.getInstance().init(context, options);

        //判断用户是否登录
        //如果没有登录，跳转到登录页面
        //如果已经登录，跳转到主页面
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        userId = sharedPreferences.getInt("id", 0);
        token = sharedPreferences.getString("token", "");
        if (token.equals("")) {
            //没有登录
            //跳转到登录页面
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            //已经登录
            //跳转到主页面
            setContentView(R.layout.activity_main);
            initView();
            initListener();
        }
    }

    private void initView() {
        tvUsername = findViewById(R.id.tv_username);
        tvUsername.setText(username);
    }

    private void initListener() {
        btnServiceRelease = findViewById(R.id.btn_service_release);
        btnServiceManagement = findViewById(R.id.btn_service_management);
        btnOrderManagement = findViewById(R.id.btn_order_management);
        btnCustomerComment = findViewById(R.id.btn_customer_comment);
        btnCustomerMessage = findViewById(R.id.btn_customer_message);
        btnCustomerService = findViewById(R.id.btn_customer_service);

        btnLogout = findViewById(R.id.btn_logout);

        btnServiceRelease.setOnClickListener(v -> {
            Intent intent = new Intent(this, ServiceReleaseActivity.class);
            startActivity(intent);
        });

        btnServiceManagement.setOnClickListener(v -> {
            Intent intent = new Intent(this, ServiceManagementActivity.class);
            startActivity(intent);
        });

        btnOrderManagement.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderManagementActivity.class);
            startActivity(intent);
        });

        btnCustomerComment.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerCommentActivity.class);
            startActivity(intent);
        });

        btnCustomerMessage.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerMessageActivity.class);
            startActivity(intent);
        });

        btnCustomerService.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("客服电话");
            builder.setMessage("将跳转到拨号界面");
            builder.setPositiveButton("拨号", (dialog, which) -> {
                //拨号028-88888888
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(android.net.Uri.parse("tel:" + "028-88888888"));
                startActivity(intent);
            });
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            builder.show();
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
}