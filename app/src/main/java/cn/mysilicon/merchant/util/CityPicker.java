package cn.mysilicon.merchant.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.merchant.R;
import cn.mysilicon.merchant.adapter.CityAdapter;
import cn.mysilicon.merchant.adapter.ProvinceAdapter;
import cn.mysilicon.merchant.entity.City;


public class CityPicker extends PopupWindow {
    View view;
    Context mContext;
    RecyclerView mRvProvince;
    RecyclerView mRvCity;
    ArrayList<City> beans;
    List<String> provinceList = new ArrayList<>();
    List<String> cityList = new ArrayList<>();
    ProvinceAdapter mProvinceAdapter;
    CityAdapter mCityAdapter;
    String province, city;
    City mCity;
    TextView mTvProvince;
    TextView mTvCity;
    ImageView mImgClose;
    LinearLayout mLlSelect;
    onCitySelect citySelect;

    public CityPicker(Context context, onCitySelect citySelect) {
        super(context);
        this.mContext = context;
        this.citySelect = citySelect;
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_city_picker, null);
        mRvProvince = view.findViewById(R.id.rv_province);
        mRvCity = view.findViewById(R.id.rv_city);
        mTvProvince = view.findViewById(R.id.tv_province);
        mTvCity = view.findViewById(R.id.tv_city);
        mLlSelect = view.findViewById(R.id.ll_select);
        mImgClose = view.findViewById(R.id.img_close);
        this.setContentView(view);
        this.setBackgroundDrawable(new BitmapDrawable());

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = (int) (wm.getDefaultDisplay().getHeight() * 0.8);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(height);
        this.setFocusable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            this.setClippingEnabled(false);
            this.setOutsideTouchable(false);
        }
        this.setAnimationStyle(R.style.PopupWindow);

        String data = cn.mysilicon.merchant.util.JsonParser.getJson(context, "city.json");
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(data).getAsJsonArray();//获取JsonArray对象
        beans = new ArrayList<>();
        Gson gson = new Gson();
        for (JsonElement bean : jsonElements) {
            City bean1 = gson.fromJson(bean, City.class);//解析
            beans.add(bean1);
        }
        initData(beans);

        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTvProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                province = "";
                city = "";
                mTvCity.setVisibility(View.GONE);
                mTvProvince.setVisibility(View.GONE);
                mLlSelect.setVisibility(View.VISIBLE);
                mRvProvince.setVisibility(View.VISIBLE);
                mRvCity.setVisibility(View.GONE);
                cityList.clear();
            }
        });

        mTvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = "";
                mTvCity.setVisibility(View.GONE);
//                initCity();
                mRvProvince.setVisibility(View.GONE);
                mRvCity.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initData(ArrayList<City> beans) {
        for (int i = 0; i < beans.size(); i++) {
            provinceList.add(beans.get(i).getName());
        }
        initProvince();
    }

    private void initProvince() {
        mRvProvince.setVisibility(View.VISIBLE);
        mRvCity.setVisibility(View.GONE);
        mProvinceAdapter = new ProvinceAdapter(provinceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvProvince.setLayoutManager(linearLayoutManager);
        mRvProvince.setAdapter(mProvinceAdapter);
        mProvinceAdapter.setOnItemListener(new ProvinceAdapter.onItemClick() {
            @Override
            public void onClick(int position, String name) {
                mProvinceAdapter.setSelection(position);
                mProvinceAdapter.notifyDataSetChanged();
                if (TextUtils.isEmpty(province)) {
                    province = name;
                    mTvProvince.setVisibility(View.VISIBLE);
                    mTvProvince.setText(name);
                    initCity();
                }
                mCity = beans.get(position);
                for (int i = 0; i < mCity.getCity().size(); i++) {
                    cityList.add(mCity.getCity().get(i).getName());
                }
            }
        });
        mProvinceAdapter.notifyDataSetChanged();
    }

    private void initCity() {
        mRvProvince.setVisibility(View.GONE);
        mRvCity.setVisibility(View.VISIBLE);
        mCityAdapter = new CityAdapter(cityList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRvCity.setLayoutManager(linearLayoutManager);
        mRvCity.setAdapter(mCityAdapter);
        mCityAdapter.setOnItemListener(new CityAdapter.onItemClick() {
            @Override
            public void onClick(int position, String name) {
                if (TextUtils.isEmpty(city)) {
                    city = name;
                    mTvCity.setVisibility(View.VISIBLE);
                    mTvCity.setText(city);
                    citySelect.onSelect(province, city);
                    dismiss();
                }
            }
        });
    }

    public interface onCitySelect {
        void onSelect(String province, String city);
    }
}
