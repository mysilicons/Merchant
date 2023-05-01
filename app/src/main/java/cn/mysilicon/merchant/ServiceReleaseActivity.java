package cn.mysilicon.merchant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.HashMap;

import cn.mysilicon.merchant.entity.Service;
import cn.mysilicon.merchant.util.CityPicker;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServiceReleaseActivity extends AppCompatActivity {
    private static final String TAG = "ServiceReleaseActivity";
    private int merchant_id;
    private int classification1_id;
    private int classification2_id;
    private String City;
    private Button btn_service_release;
    private Spinner spinner1;
    private Spinner spinner2;
    private EditText et_service_name;
    private EditText et_service_content;
    private EditText et_service_price;
    private EditText et_service_image;
    private CityPicker mCityPicker;
    private TextView tv_city_pick;
    private TextView tv_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_release);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        merchant_id = sharedPreferences.getInt("id", 0);

        initView();
        initListener();
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

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        String[] classification1 = new String[]{"家庭清洁", "保姆月嫂", "放心搬家", "家庭应急", "二手回收", "房屋维修", "水电五金", "衣物洗护", "餐饮美食", "数码维修"};
        HashMap<String, String[]> classification2 = new HashMap<>();
        classification2.put("家庭清洁", new String[]{"居家卫生", "电器清洗", "专项清洁", "布艺除螨", "消毒杀菌"});
        classification2.put("保姆月嫂", new String[]{"照顾老幼", "科学月子", "育婴启蒙"});
        classification2.put("放心搬家", new String[]{"货车搬家", "托管式搬家", "办公室搬家"});
        classification2.put("家庭应急", new String[]{"厨卫疏通", "开锁", "换锁装锁"});
        classification2.put("二手回收", new String[]{"家电", "家具", "金属", "厨房设备", "电脑", "文体乐器", "办公用品"});
        classification2.put("房屋维修", new String[]{"墙体墙面", "地面地暖"});
        classification2.put("水电五金", new String[]{"水电维修", "五金安装"});
        classification2.put("衣物洗护", new String[]{"衣物清洗", "鞋子清洗", "高端洗护"});
        classification2.put("餐饮美食", new String[]{"上门做菜"});
        classification2.put("数码维修", new String[]{"电脑", "网络", "手机", "办公设备"});
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classification1); // 实例化ArrayAdapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
        spinner1.setAdapter(adapter); // 将适配器与选择列表框关联

        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String classification_1 = spinner1.getSelectedItem().toString();
                String[] classification_2;
                ArrayAdapter<String> adapter;
                switch (classification_1) {
                    case "家庭清洁":
                        classification_2 = classification2.get("家庭清洁");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter); // 将适配器与选择列表框关联
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "居家卫生":
                                        classification1_id = 1;
                                        classification2_id = 1;
                                        break;
                                    case "电器清洗":
                                        classification1_id = 1;
                                        classification2_id = 2;
                                        break;
                                    case "专项清洁":
                                        classification1_id = 1;
                                        classification2_id = 3;
                                        break;
                                    case "布艺除螨":
                                        classification1_id = 1;
                                        classification2_id = 4;
                                        break;
                                    case "消毒杀菌":
                                        classification1_id = 1;
                                        classification2_id = 5;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "保姆月嫂":
                        classification_2 = classification2.get("保姆月嫂");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter); // 将适配器与选择列表框关联
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "照顾老幼":
                                        classification1_id = 2;
                                        classification2_id = 6;
                                        break;
                                    case "科学月子":
                                        classification1_id = 2;
                                        classification2_id = 7;
                                        break;
                                    case "育婴启蒙":
                                        classification1_id = 2;
                                        classification2_id = 8;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "放心搬家":
                        classification_2 = classification2.get("放心搬家");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter); // 将适配器与选择列表框关联
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "货车搬家":
                                        classification1_id = 3;
                                        classification2_id = 9;
                                        break;
                                    case "托管式搬家":
                                        classification1_id = 3;
                                        classification2_id = 10;
                                        break;
                                    case "办公室搬家":
                                        classification1_id = 3;
                                        classification2_id = 11;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "家庭应急":
                        classification_2 = classification2.get("家庭应急");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter); // 将适配器与选择列表框关联
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "厨卫疏通":
                                        classification1_id = 4;
                                        classification2_id = 12;
                                        break;
                                    case "开锁":
                                        classification1_id = 4;
                                        classification2_id = 13;
                                        break;
                                    case "换锁装锁":
                                        classification1_id = 4;
                                        classification2_id = 14;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "二手回收":
                        classification_2 = classification2.get("二手回收");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter); // 将适配器与选择列表框关联
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "家电":
                                        classification1_id = 5;
                                        classification2_id = 15;
                                        break;
                                    case "家具":
                                        classification1_id = 5;
                                        classification2_id = 16;
                                        break;
                                    case "金属":
                                        classification1_id = 5;
                                        classification2_id = 17;
                                        break;
                                    case "厨房设备":
                                        classification1_id = 5;
                                        classification2_id = 18;
                                        break;
                                    case "电脑":
                                        classification1_id = 5;
                                        classification2_id = 19;
                                        break;
                                    case "文体乐器":
                                        classification1_id = 5;
                                        classification2_id = 20;
                                        break;
                                    case "办公用品":
                                        classification1_id = 5;
                                        classification2_id = 21;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "房屋维修":
                        classification_2 = classification2.get("房屋维修");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter); // 将适配器与选择列表框关联
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "墙体墙面":
                                        classification1_id = 6;
                                        classification2_id = 22;
                                        break;
                                    case "地面地暖":
                                        classification1_id = 6;
                                        classification2_id = 23;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "水电五金":
                        classification_2 = classification2.get("水电五金");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter); // 将适配器与选择列表框关联
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "水电维修":
                                        classification1_id = 7;
                                        classification2_id = 24;
                                        break;
                                    case "五金安装":
                                        classification1_id = 7;
                                        classification2_id = 25;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "衣物洗护":
                        classification_2 = classification2.get("衣物洗护");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter); // 将适配器与选择列表框关联
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "衣物清洗":
                                        classification1_id = 8;
                                        classification2_id = 26;
                                        break;
                                    case "鞋子清洗":
                                        classification1_id = 8;
                                        classification2_id = 27;
                                        break;
                                    case "高端洗护":
                                        classification1_id = 8;
                                        classification2_id = 28;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "餐饮美食":
                        classification_2 = classification2.get("餐饮美食");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter); // 将适配器与选择列表框关联
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "上门做菜":
                                        classification1_id = 9;
                                        classification2_id = 29;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    case "数码维修":
                        classification_2 = classification2.get("数码维修");
                        adapter = new ArrayAdapter<>(ServiceReleaseActivity.this, android.R.layout.simple_spinner_item, classification_2); // 实例化ArrayAdapter
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 为适配器设置列表框下拉时的选项样式
                        spinner2.setAdapter(adapter);
                        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                //获取选择的二级分类对应ID
                                String classification_2 = spinner2.getSelectedItem().toString();
                                switch (classification_2) {
                                    case "电脑":
                                        classification1_id = 10;
                                        classification2_id = 30;
                                        break;
                                    case "网络":
                                        classification1_id = 10;
                                        classification2_id = 31;
                                        break;
                                    case "手机":
                                        classification1_id = 10;
                                        classification2_id = 32;
                                        break;
                                    case "办公设备":
                                        classification1_id = 10;
                                        classification2_id = 33;
                                        break;
                                    default:
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {

                            }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void initListener() {
        et_service_name = findViewById(R.id.et_service_name);
        et_service_price = findViewById(R.id.et_service_price);
        et_service_content = findViewById(R.id.et_service_content);
        et_service_image = findViewById(R.id.et_service_image);
        tv_city_pick = findViewById(R.id.tv_city_pick);
        tv_city = findViewById(R.id.tv_city_select);
        btn_service_release = findViewById(R.id.btn_service_release);

        tv_city_pick.setOnClickListener(v -> {
            bgAlpha(0.7f);
            hideKeyboard(ServiceReleaseActivity.this);
            mCityPicker = new CityPicker(ServiceReleaseActivity.this, new CityPicker.onCitySelect() {
                @Override
                public void onSelect(String province, String city) {
                    tv_city.setText(province + city);
                    City = city;
                }
            });
            mCityPicker.showAtLocation(tv_city_pick, Gravity.BOTTOM, 0, 0);

            mCityPicker.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    bgAlpha(1.0f);
                }
            });
        });

        btn_service_release.setOnClickListener(v -> {
            String name = et_service_name.getText().toString();
            String content = et_service_content.getText().toString();
            String price = et_service_price.getText().toString();
            String image = et_service_image.getText().toString();
            Service service = new Service(classification1_id, classification2_id, image, name, content, price, City, merchant_id);
            releaseService(service);
        });

    }

    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                Toast.makeText(ServiceReleaseActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    private void releaseService(Service service) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = "http://mysilicon.cn/merchant/service/release";
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(service)))
                    .build();
            Call call = client.newCall(request);
            Log.d(TAG,"JSON：" + JSON.toJSONString(service));
            Response response;
            String result;
            try {
                response = call.execute();
                result = response.body().string();
            } catch (IOException e) {
                Looper.prepare();
                Toast.makeText(ServiceReleaseActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                Looper.loop();
                throw new RuntimeException(e);
            }
            if (response.code() != 200) {
                Looper.prepare();
                Log.d(TAG,"发布服务返回结果：" + result);
                Toast.makeText(ServiceReleaseActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Log.d(TAG,"发布服务返回结果：" + result);
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

    //背景变暗
    private void bgAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = f;
        getWindow().setAttributes(layoutParams);
    }

    // 隐藏软键盘
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }
}