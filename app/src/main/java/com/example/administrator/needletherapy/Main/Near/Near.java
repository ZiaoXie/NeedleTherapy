package com.example.administrator.needletherapy.Main.Near;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.administrator.needletherapy.Main.GuaHao.GuaHao;
import com.example.administrator.needletherapy.MainActivity;
import com.example.administrator.needletherapy.R;
import com.example.administrator.needletherapy.toolClass.PermissionUtils;
import com.example.administrator.needletherapy.toolClass.SearchView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Near extends AppCompatActivity  implements INaviInfoCallback,PoiSearch.OnPoiSearchListener,AMapLocationListener {

    RelativeLayout relativeLayout;
    EditText editText;
    ImageView delete,back;
    RecyclerView recyclerView;
    TextView sort;
    ImageView start_search;

    String near[]={"朝阳模拟医院", "南京鼓楼医院" , "南大和园针灸诊所" , "南京三秋诊所", "南邮针疗馆","玄武大道医院","南医大二附院"};

    //声明mlocationClient对象
    AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);

        sort=(TextView)findViewById(R.id.sort_select);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Near.this);
                builder.setTitle("请选择排序规则");
                final String[] sex = {"推荐", "距离优先","收藏","诊所","医院"};
                builder.setItems(sex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sort.setText(sex[i]);
                    }
                });
                builder.show();
            }
        });


        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(Near.this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(Near.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(Near.this,LinearLayoutManager.VERTICAL));

        relativeLayout=(RelativeLayout) findViewById(R.id.activity_near);
        delete=(ImageView)findViewById(R.id.search_iv_delete);
        editText=(EditText)findViewById(R.id.search_et_input);

        new SearchView(editText,delete,new View[]{relativeLayout,recyclerView}).init(Near.this);

        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        start_search=(ImageView)findViewById(R.id.start_search);
        start_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWord = "诊所";
                int currentPage = 0;
                PoiSearch.Query query = new PoiSearch.Query(keyWord, "", "南京市");

                query.setPageSize(20);// 设置每页最多返回多少条poiitem
                query.setPageNum(currentPage);// 设置查第一页

                LatLonPoint lp = new LatLonPoint(mlocationClient.getLastKnownLocation().getLatitude() ,mlocationClient.getLastKnownLocation().getLongitude());
                if (lp != null) {
                    PoiSearch poiSearch = new PoiSearch(Near.this, query);
                    poiSearch.setOnPoiSearchListener(Near.this);
                    poiSearch.setBound(new PoiSearch.SearchBound(lp,5000,true));//
                    poiSearch.searchPOIAsyn();// 异步搜索
                }
            }
        });

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Holder holder=new Holder(LayoutInflater.from(Near.this).inflate(R.layout.item_hospital,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((Holder)holder).textView.setText(near[position]);
        }

        @Override
        public int getItemCount() {
            return near.length;
        }

        class Holder extends RecyclerView.ViewHolder{
            TextView textView;

            public Holder(View itemView) {
                super(itemView);
                textView=(TextView)itemView.findViewById(R.id.hospital_name);
            }
        }
    }

}
