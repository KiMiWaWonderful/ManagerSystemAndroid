package com.example.managersystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment {

    private View view;
    private MapView mapView;
    private BaiduMap baiduMap;
    //标记的图标
    private BitmapDescriptor bitmapDescriptor;
    //通知窗口
    private InfoWindow infoWindow;
    //标记点击事件
    private MarkerOnInfoWindowClickListener markerListener;
    //通知窗口的布局文件
    private LinearLayout baidumap_infowindow;

    private LocationClient locationClient = null;
    private boolean firstLocation;
    private MyLocationConfiguration myLocationConfiguration;

    private final class  MarkerOnInfoWindowClickListener implements InfoWindow.OnInfoWindowClickListener {
        @Override
        public void onInfoWindowClick(){
            //隐藏InfoWindow
            baiduMap.hideInfoWindow();
        }
    }

    private void initView(View rootView){
        mapView =  rootView.findViewById (R.id.bmapView);
        baidumap_infowindow = (LinearLayout) LayoutInflater.from (getActivity ()).inflate (R.layout.layout_map_info, null);
        // 构建Marker图标
        bitmapDescriptor = BitmapDescriptorFactory.fromResource (R.drawable.icon_marka);
    }

    private void initMap(){
        // 获取BaiduMap类 BaiduMap类才可以添加自定义的图层
        baiduMap = mapView.getMap ();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo (15.0f);
        baiduMap.setMapStatus (msu);
        markerListener = new MarkerOnInfoWindowClickListener();

        //普通地图 ,mBaiduMap是地图控制器对象
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        locationClient = new LocationClient(getContext());
        firstLocation = true;

        //设置定位相关配置
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClientOption.setOpenGps(true);
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setScanSpan(1000);
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setLocationNotify(true);
        locationClientOption.setIsNeedLocationDescribe(true);
        locationClientOption.setIsNeedLocationPoiList(true);
        locationClient.setLocOption(locationClientOption);
//
//        // 设置自定义图标
        BitmapDescriptor myMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_foreground);

        myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,true,myMarker);
        baiduMap.setMyLocationConfiguration(myLocationConfiguration);
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                 // map view 销毁后不在处理新接收的位置
                if (bdLocation == null || mapView == null)
                    return;

              //构造定位数据,不要精度圈
                bdLocation.setRadius(0);
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();

                //设置定位数据
                baiduMap.setMyLocationData(myLocationData);

                // 第一次定位时，将地图位置移动到当前位置
                if(firstLocation){
                    firstLocation = false;
                    LatLng xy = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    //设置缩放中心点；缩放比例；
                    builder.target(xy).zoom(18.0f);
                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build())  );
                }
            }
        });
        //对Marker的点击事件
        baiduMap.setOnMarkerClickListener (new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker){
                //获得marker中的数据
                CarInfo carInfo = (CarInfo) marker.getExtraInfo ().get ("marker");
                createInfoWindow(baidumap_infowindow, carInfo);
                //将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                infoWindow = new InfoWindow (BitmapDescriptorFactory.fromView (baidumap_infowindow), ll, -47, markerListener);
                //显示InfoWindow
                baiduMap.showInfoWindow(infoWindow);
                return true;
            }
        });
    }

    @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
    private void createInfoWindow(LinearLayout baidumap_infowindow, CarInfo bean){
        InfoWindowHolder holder = null;
        if(baidumap_infowindow.getTag () == null){
            holder = new InfoWindowHolder ();
            holder.textViewId = baidumap_infowindow.findViewById(R.id.textview_id);
            holder.textViewName =  baidumap_infowindow.findViewById (R.id.textview_name);
            holder.textViewNumber =  baidumap_infowindow.findViewById (R.id.textview_number);
            baidumap_infowindow.setTag (holder);
        }

        holder = (InfoWindowHolder) baidumap_infowindow.getTag ();

        holder.textViewId.setText (String.format (getString (R.string.home_page_id), bean.getId()));
        holder.textViewName.setText (String.format (getString (R.string.home_page_name), bean.getName()));
        holder.textViewNumber.setText (String.format (getString (R.string.home_page_number), bean.getNumber()));
    }


    private void test(){

        List<CarInfo> carInfos = new ArrayList<>();
        carInfos.add(new CarInfo(1,"Acid","xxx",110.305047,21.160716));
        carInfos.add(new CarInfo(2,"Black","xxx",110.306574,21.161306));
        carInfos.add(new CarInfo(3,"Cherry","xxx",110.307365,21.16134));
        carInfos.add(new CarInfo(4,"Larc","xxx",110.309521,21.160632));
        carInfos.add(new CarInfo(5,"Jean","xxx",110.310563,21.160261));
        carInfos.add(new CarInfo(6,"Ayu","xxx",110.311695,21.159149));
        carInfos.add(new CarInfo(7,"xxx","xxx",110.31227,21.159149));
        carInfos.add(new CarInfo(8,"xxx","xxx",110.310455,21.158272));
        carInfos.add(new CarInfo(9,"xxx","xxx",110.310967,21.158112));
        carInfos.add(new CarInfo(10,"xxx","xxx",110.308128,21.158693));

        showData (carInfos);
    }

    private void showData(List<CarInfo> carInfos){
        baiduMap.clear ();
        addMarker (carInfos);
    }

    private void addMarker(List<CarInfo> carInfos){
        for ( int i = 0 ; i < carInfos.size () ; i++ ) {
            CarInfo carInfo = carInfos.get (i);
            // 经度
            double longitude = carInfo.getLng();
            // 纬度
            double latitude = carInfo.getLat();

            if (longitude > 0 && latitude > 0) {
                // 定义Maker坐标点
                LatLng ll = new LatLng (latitude,longitude);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions ().position (ll).icon (bitmapDescriptor);
                // 在地图上添加Marker，并显示
                Marker marker = (Marker) baiduMap.addOverlay (options);
                // 将信息保存
                Bundle bundle = new Bundle ();
                bundle.putSerializable ("marker", carInfo);
                marker.setExtraInfo (bundle);

                if (i == 0) {
                    // 把第一个默认为当前的位置图层
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng (ll);
                    baiduMap.setMapStatus (u);
                }
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        view = LayoutInflater.from (getActivity ()).inflate (R.layout.fragment_home_page, null);
        initView (view);
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initMap ();
        baiduMap.setMyLocationEnabled(true);
        if (!locationClient.isStarted())
        {
            locationClient.start();
        }
        test ();
    }

    @Override
    public void onStop() {
        // 关闭图层定位
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        super.onStop();
    }

    @Override
    public void onResume() {
        mapView.onResume ();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy ();
        mapView = null;
        super.onDestroy();
    }
}
