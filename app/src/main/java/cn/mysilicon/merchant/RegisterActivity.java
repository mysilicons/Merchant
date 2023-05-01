package cn.mysilicon.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText edt_username;
    private EditText edt_password;
    private Button login;
    private Button register;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initLinstener();
    }

    private void initLinstener() {
        edt_username = findViewById(R.id.edt_register_username);
        edt_password = findViewById(R.id.edt_register_password);
        login = findViewById(R.id.btn_goLogin);
        register = findViewById(R.id.btn_submit);

        login.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        register.setOnClickListener(view -> {
            String username = edt_username.getText().toString();
            String password = edt_password.getText().toString();

            Boolean matchResult = isPhone(username);
            if (username.equals("") || password.equals("")) {
                Toast.makeText(RegisterActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            } else if (username.length() != 11) {
                Toast.makeText(RegisterActivity.this, "手机号应为11位数", Toast.LENGTH_SHORT).show();
            } else if (!matchResult) {
                Toast.makeText(RegisterActivity.this, "手机号格式有误", Toast.LENGTH_SHORT).show();
            } else {
                register(username, password);
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull android.os.Message msg) {
            if (msg.what == 0) {
                switch (result) {
                    case "注册成功":
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "用户已存在":
                        Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(RegisterActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    private void register(String username, String password) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "http://mysilicon.cn/merchant/register?username=" + username + "&password=" + password;
            Log.d(TAG, "register: " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create("", null))
                    .build();
            Call call = client.newCall(request);
            Response response;
            try {
                response = call.execute();
                result = response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (response.code()!=200){
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    public static boolean isPhone(String phone) {
        String regex = "^(13[0-9]|14[579]|15[0-35-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        if (isMatch) {
            return true;
        } else {
            return false;
        }
    }
}