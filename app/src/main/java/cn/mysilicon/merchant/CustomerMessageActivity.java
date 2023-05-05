package cn.mysilicon.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMessageActivity extends AppCompatActivity {

    private static final String TAG = "CustomerMessageActivity";
    private ListView listView;
    private SimpleAdapter simp_adapter;
    private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    private Map<String, EMConversation> localvalue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_message);

        initView();
    }

    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("消息列表");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.message_list);


        // 异步方法。同步方法为 fetchConversationsFromServer()。
        // pageNum：当前页面，从 1 开始。
        // pageSize：每页获取的会话数量。取值范围为 [1,20]。
        if (EMClient.getInstance().chatManager().getAllConversations().isEmpty()) {
            EMClient.getInstance().chatManager().asyncFetchConversationsFromServer(new EMValueCallBack<Map<String, EMConversation>>() {
                @Override
                public void onSuccess(Map<String, EMConversation> value) {
                    localvalue = value;
                    dataList = getData(localvalue);
                }

                @Override
                public void onError(int error, String errorMsg) {

                }
            });
        }
        localvalue = EMClient.getInstance().chatManager().getAllConversations();
        dataList = getData(localvalue);


        //1.新建一个数据适配器
        //ArrayAdapter（上下文，当前listView加载的每一个列表项所对应的布局文件，数据源）
        //2.适配器加载数据源
        /*SimpleAdapter——
         * 1.context：上下文
         * 5.data：数据源List<? extends Map<String,?>> data 一个Map所组成的List集合，
         * 每一个Map都会去对应ListView列表中的一行，
         * 每一个Map（键-值对）中的键必须包含所在from中所指定的键
         * 2.resource：列表项的布局文件ID
         * 4.from：Map中的键名
         * 3.to：绑定数据视图中的ID，与from对应关系
         * */
        simp_adapter = new SimpleAdapter(this, dataList, R.layout.message_item, new String[]{"pic", "name", "text"}, new int[]{R.id.pic, R.id.name, R.id.text});
        //3.视图加载适配器
        listView.setAdapter(simp_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = dataList.get(position).get("name").toString();
                Log.d(TAG, name);
                //传递数据
                Intent intent = new Intent(CustomerMessageActivity.this, MessageActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        simp_adapter.notifyDataSetChanged();

    }

    private List<Map<String, Object>> getData(Map<String, EMConversation> value) {
        for (EMConversation conversation : value.values()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", R.drawable.icon1);
            map.put("name", conversation.conversationId());
            map.put("text", conversation.getLastMessage().getBody().toString());
            dataList.add(map);
        }
        return dataList;
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