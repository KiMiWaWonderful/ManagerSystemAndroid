package com.example.managersystem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.animation.AlphaAnimation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private MapView mMapView = null;
//    private BaiduMap mBaiduMap = null;
//
//    private LocationClient locationClient = null;
//    private boolean firstLocation;
//    private BitmapDescriptor bitmapDescriptor;
//    private MyLocationConfiguration myLocationConfiguration;

    //private HomePageFragment homePageFragment;
    //private TraceFragment traceFragment;
    //private TraceFragmentTwo traceFragmentTwo;
    private TraceFragmentThere traceFragmentThere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        homePageFragment = new HomePageFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.home_container,homePageFragment);
//        fragmentTransaction.show(homePageFragment);
//        fragmentTransaction.commit();

//        traceFragment = new TraceFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.home_container,traceFragment);
//        fragmentTransaction.show(traceFragment);
//        fragmentTransaction.commit();

//        traceFragmentTwo = new TraceFragmentTwo();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.home_container,traceFragmentTwo);
//        fragmentTransaction.show(traceFragmentTwo);
//        fragmentTransaction.commit();

        traceFragmentThere = new TraceFragmentThere();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_container,traceFragmentThere);
        fragmentTransaction.show(traceFragmentThere);
        fragmentTransaction.commit();

        //此方法要再setContentView方法之前实现
//        SDKInitializer.initialize(getApplicationContext());
//        setContentView(R.layout.activity_main);
//
//        //获取地图控件引用
//        mMapView =  findViewById(R.id.bmapView);
//        mBaiduMap = mMapView.getMap();

//        //定义Maker坐标点
//        LatLng point = new LatLng(21.160716, 110.305047);
//////构建Marker图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_marka);
//////构建MarkerOption，用于在地图上添加Marker
//        OverlayOptions option = new MarkerOptions()
//                .position(point)
//                .icon(bitmap);
////       //在地图上添加Marker，并显示
//        mBaiduMap.addOverlay(option);

        //设置地图状态
//        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15f);
//        mBaiduMap.setMapStatus(mapStatusUpdate);
//
//        //普通地图 ,mBaiduMap是地图控制器对象
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//
//        //定位初始化
//        locationClient = new LocationClient(this);
//        firstLocation = true;
//
//        //设置定位相关配置
//        LocationClientOption locationClientOption = new LocationClientOption();
//        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        locationClientOption.setOpenGps(true);
//        locationClientOption.setCoorType("bd09ll");
//        locationClientOption.setScanSpan(1000);
//        locationClientOption.setIsNeedAddress(true);
//        locationClientOption.setLocationNotify(true);
//        locationClientOption.setIsNeedLocationDescribe(true);
//        locationClientOption.setIsNeedLocationPoiList(true);
//        locationClient.setLocOption(locationClientOption);
//
//        // 设置自定义图标
//        BitmapDescriptor myMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_foreground);
//        myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,true,myMarker);
//        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
//
//        locationClient.registerLocationListener(new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//
//                // map view 销毁后不在处理新接收的位置
//                if (bdLocation == null || mMapView == null)
//                    return;
//
//                // 构造定位数据
//                //不要精度圈
//                bdLocation.setRadius(0);
//                MyLocationData myLocationData = new MyLocationData.Builder()
//                        .accuracy(bdLocation.getRadius())
//                        .direction(100).latitude(bdLocation.getLatitude())
//                        .longitude(bdLocation.getLongitude()).build();
//
//                //设置定位数据
//                mBaiduMap.setMyLocationData(myLocationData);
//
//                // 第一次定位时，将地图位置移动到当前位置
//                if(firstLocation){
//                    firstLocation = false;
//                    LatLng xy = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
//                    MapStatus.Builder builder = new MapStatus.Builder();
//                    //设置缩放中心点；缩放比例；
//                    builder.target(xy).zoom(18.0f);
////                    MapStatusUpdate mapStatusUpdate1 = MapStatusUpdateFactory.newLatLng(xy);
////                    mBaiduMap.animateMapStatus(mapStatusUpdate1);
//                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build())  );
//                }
//            }
//        });
        //mBaiduMap.setMyLocationEnabled(true);
    }

//    @Override
//    protected void onStart() {
//        // 如果要显示位置图标,必须先开启图层定位
////        mBaiduMap.setMyLocationEnabled(true);
////        if (!locationClient.isStarted())
////        {
////            locationClient.start();
////        }
//
//        //定义Maker坐标点
////        LatLng point = new LatLng(21.160716, 110.305047);
//////构建Marker图标
////        BitmapDescriptor bitmap = BitmapDescriptorFactory
////                .fromResource(R.drawable.map_maker_green);
//////构建MarkerOption，用于在地图上添加Marker
////        OverlayOptions option = new MarkerOptions()
////                .position(point)
////                .icon(bitmap);
////       //在地图上添加Marker，并显示
////        mBaiduMap.addOverlay(option);
////        Marker marker = (Marker) mBaiduMap.addOverlay(option);
////        AlphaAnimation animation = new AlphaAnimation(1.0f,0.0f);//marker点动画（需导入百度地图的Animation包）
////        animation.setDuration(3000);
////        marker.setAnimation(animation);
////        marker.startAnimation();
//
//
////        BitmapDescriptor bdA = BitmapDescriptorFactory
////                .fromResource(R.drawable.holydizhi);
////        //创建OverlayOptions的集合
////        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
//////        //构造大量坐标数据
////        LatLng point1 = new LatLng(21.160716, 110.305047);
////        LatLng point2 = new LatLng(21.161306, 110.306574);
////        LatLng point3 = new LatLng(21.16134,110.307365 );
////        LatLng point4 = new LatLng(21.160632, 110.309521);
////        LatLng point5 = new LatLng(21.160261, 110.310563);
////        LatLng point6 = new LatLng(21.159149, 110.311695);
////        LatLng point7 = new LatLng(21.159149, 110.31227);
////        LatLng point8 = new LatLng(21.158272, 110.310455);
////        LatLng point9 = new LatLng(21.15598, 110.307401);
////        LatLng point10 = new LatLng(21.158693, 110.308128);
//////        //创建OverlayOptions属性
////        OverlayOptions option1 =  new MarkerOptions()
////                .position(point1)
////                .icon(bdA);
////        OverlayOptions option2 =  new MarkerOptions()
////                .position(point2)
////                .icon(bdA);
////        OverlayOptions option3 =  new MarkerOptions()
////                .position(point3)
////                .icon(bdA);
////        OverlayOptions option4 =  new MarkerOptions()
////                .position(point4)
////                .icon(bdA);
////        OverlayOptions option5 =  new MarkerOptions()
////                .position(point5)
////                .icon(bdA);
////        OverlayOptions option6 =  new MarkerOptions()
////                .position(point6)
////                .icon(bdA);
////        OverlayOptions option7 =  new MarkerOptions()
////                .position(point7)
////                .icon(bdA);
////        OverlayOptions option8 =  new MarkerOptions()
////                .position(point8)
////                .icon(bdA);
////        OverlayOptions option9 =  new MarkerOptions()
////                .position(point9)
////                .icon(bdA);
////        OverlayOptions option10 =  new MarkerOptions()
////                .position(point10)
////                .icon(bdA);
//////        //将OverlayOptions添加到list
////        options.add(option1);
////        options.add(option2);
////        options.add(option3);
////        options.add(option4);
////        options.add(option5);
////        options.add(option6);
////        options.add(option7);
////        options.add(option8);
////        options.add(option9);
////        options.add(option10);
//
////        Marker marker1 = (Marker) mBaiduMap.addOverlay(option1);
////        Marker marker2 = (Marker) mBaiduMap.addOverlay(option2);
////        Marker marker3 = (Marker) mBaiduMap.addOverlay(option3);
////        Marker marker4 = (Marker) mBaiduMap.addOverlay(option4);
////        Marker marker5 = (Marker) mBaiduMap.addOverlay(option5);
////        Marker marker6 = (Marker) mBaiduMap.addOverlay(option6);
////        Marker marker7 = (Marker) mBaiduMap.addOverlay(option7);
////        Marker marker8 = (Marker) mBaiduMap.addOverlay(option8);
////        Marker marker9 = (Marker) mBaiduMap.addOverlay(option9);
////        Marker marker10 = (Marker) mBaiduMap.addOverlay(option10);
////
////        AlphaAnimation animation = new AlphaAnimation(1.0f,0.0f);//marker点动画（需导入百度地图的Animation包）
////        animation.setDuration(3000);
////        marker1.setAnimation(animation);
////        marker1.startAnimation();
////        marker2.setAnimation(animation);
////        marker2.startAnimation();
////        marker3.setAnimation(animation);
////        marker3.startAnimation();
////        marker4.setAnimation(animation);
////        marker4.startAnimation();
////        marker5.setAnimation(animation);
////        marker5.startAnimation();
////        marker6.setAnimation(animation);
////        marker6.startAnimation();
////        marker7.setAnimation(animation);
////        marker7.startAnimation();
////        marker8.setAnimation(animation);
////        marker8.startAnimation();
////        marker9.setAnimation(animation);
////        marker9.startAnimation();
////        marker10.setAnimation(animation);
////        marker10.startAnimation();
//
////        //在地图上批量添加
////        mBaiduMap.addOverlays(options);
//        super.onStart();
//    }

//    @Override
//    protected void onStop() {
//        // 关闭图层定位
////        mBaiduMap.setMyLocationEnabled(false);
////        locationClient.stop();
//        super.onStop();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
////        mMapView.onResume();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
////        mMapView.onPause();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
////        mMapView.onDestroy();
//    }


//6.0之后要动态获取权限，重要！！！

    protected void judgePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            /*
            检查该权限是否已经获取
            权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            sd卡权限
            */
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, SdCardPermission, 100);
            }

            //手机状态权限
            String[] readPhoneStatePermission = {Manifest.permission.READ_PHONE_STATE};
            if (ContextCompat.checkSelfPermission(this, readPhoneStatePermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, readPhoneStatePermission, 200);
            }

            //定位权限
            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, locationPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, locationPermission, 300);
            }

            String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, ACCESS_COARSE_LOCATION, 400);
            }

            String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, READ_EXTERNAL_STORAGE, 500);
            }

            String[] WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, WRITE_EXTERNAL_STORAGE, 600);
            }
        }else{
            //doSdCardResult();
        }
        //LocationClient.reStart();
    }

}

