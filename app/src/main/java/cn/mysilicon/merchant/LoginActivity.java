package cn.mysilicon.merchant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.io.IOException;

import cn.mysilicon.merchant.entity.Merchant;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText login_username;
    private EditText login_password;
    private Button btn_login;
    private Button btnRegister;
    private String username;
    private String password;
    Merchant merchant = new Merchant();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initListener();
    }

    private void initListener() {
        login_username = findViewById(R.id.edt_login_username);
        login_password = findViewById(R.id.edt_login_password);

        btn_login = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_registerActivity);

        btn_login.setOnClickListener(v -> {
            username = login_username.getText().toString();
            password = login_password.getText().toString();
            login(username, password);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Intent intent = new Intent();
                    if (merchant.getToken().equals("")) {
                        //登录失败
                        //提示用户登录失败
                        //跳转到登录页面
                        Toast.makeText(LoginActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                    } else {
                        EMClient.getInstance().login(username, password, new EMCallBack() {
                            // 登录成功回调
                            @Override
                            public void onSuccess() {
                            }
                            // 登录失败回调，包含错误信息
                            @Override
                            public void onError(final int code, final String error) {
                            }
                            @Override
                            public void onProgress(int i, String s) {
                            }
                        });

                        //登录成功
                        //保存token
                        //跳转到主页面
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", merchant.getToken());
                        editor.putString("username", merchant.getUsername());
                        editor.putInt("id", merchant.getId());
                        editor.apply();
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        finish();
                    }
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        };
    };

    private void login(String username, String password) {
        new Thread(() -> {
            //1.创建OkHttpClient对象
            OkHttpClient client = new OkHttpClient();
            //2.创建Request对象
            String url = "http://mysilicon.cn/merchant/login?username=" + username + "&password=" + password;
            Log.d(TAG,"url: " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create("", null))
                    .build();
            //3.创建一个call对象,参数就是Request请求对象
            Call call = client.newCall(request);
            //4.执行请求
            Response response;
            String result;
            try {
                response = call.execute();
                result = response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (response.code() != 200) {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                merchant = JSONArray.parseObject(result, Merchant.class);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
}