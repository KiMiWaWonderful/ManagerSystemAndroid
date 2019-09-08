package com.example.managersystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
//import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.TraceLocation;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

public class TraceFragment extends Fragment {

    private View view;

    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    Button staButton;   //开始记录轨迹按钮
    boolean flag = true;
    boolean isFirstTrace = true;

    public LocationClient mLocClient = null;

    private static OnTraceListener startTraceListener = null;
    private static OnEntityListener entityListener = null;
    private RefreshThread refreshThread = null;
    private static BitmapDescriptor realtimeBitmap = null;
    private static OverlayOptions overlay; //起始点图标overlay
    private static PolylineOptions polyline;
    private List<LatLng> pointList = new ArrayList<>();

    private Trace trace;
    private LBSTraceClient client;
    private LocRequest locRequest = null;


    boolean isFirstLoc = true;  //是否首次定位
    //   查询历史轨迹request选项定义
    boolean isNeedObjectStorage = false;
    long serviceId = 212031;    //轨迹服务id
    int gatherInterval = 3; //定位周期，秒
    int packInterval = 10;  //打包回传周期，秒
    int tag = 1;    //请求标识
    String entityName = "";


    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //mMapView 销毁后不再处理新接收的位置信息
            if (bdLocation == null || mBaiduMap == null) return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();

            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                //设置地图中心点以及缩放级别
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
                mBaiduMap.animateMapStatus(u);
               /* BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka4);
                 OverlayOptions option = new MarkerOptions()
                      .position(ll)
                     .icon(mCurrentMarker);
                  mBaiduMap.addOverlay(option); */
                //获取定位结果
                StringBuffer sb = new StringBuffer(256);

                sb.append("time : ");
                sb.append(bdLocation.getTime());    //获取定位时间

                sb.append("\nerror code : ");
                sb.append(bdLocation.getLocType());    //获取类型类型

                sb.append("\nlatitude : ");
                sb.append(bdLocation.getLatitude());    //获取纬度信息

                sb.append("\nlontitude : ");
                sb.append(bdLocation.getLongitude());    //获取经度信息

                sb.append("\nradius : ");
                sb.append(bdLocation.getRadius());    //获取定位精准度

                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {

                    // GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(bdLocation.getSpeed());    // 单位：公里每小时

                    sb.append("\nsatellite : ");
                    sb.append(bdLocation.getSatelliteNumber());    //获取卫星数

                    sb.append("\nheight : ");
                    sb.append(bdLocation.getAltitude());    //获取海拔高度信息，单位米

                    sb.append("\ndirection : ");
                    sb.append(bdLocation.getDirection());    //获取方向信息，单位度

                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());    //获取地址信息

                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");

                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {

                    // 网络定位结果
                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());    //获取地址信息

                    sb.append("\noperationers : ");
                    sb.append(bdLocation.getOperators());    //获取运营商信息

                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");

                } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {

                    // 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");

                } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {

                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，" +
                            "会有人追查原因");

                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {

                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");

                } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {

                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，" +
                            "处于飞行模式下一般会造成这种结果，可以试着重启手机");

                }

                sb.append("\nlocationdescribe : ");
                sb.append(bdLocation.getLocationDescribe());    //位置语义化信息

                List<Poi> list = bdLocation.getPoiList();    // POI数据
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                }

                Log.i("BaiduLocationApiDem", sb.toString());
                Toast.makeText(getActivity().getApplicationContext(), bdLocation.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }

        }

//        //@Override
//        public void onConnectHotSpotMessage(String s, int i) {
//        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getActivity().getApplicationContext());
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_trace, null);
        init(); //相关变量初始化
        initOnEntityListener(); //初始化实体监听器
        initOnStartTraceListener(); //轨迹监听器
        client.startTrace(trace, startTraceListener);
        return view;
    }

    private void init() {

       // locRequest = new LocRequest(serviceId);
        mMapView = view.findViewById(R.id.trace_mapView);
        staButton = view.findViewById(R.id.start_btn);    //记录轨迹按钮

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(getActivity().getApplicationContext()); //实例化LocationClient类
        mLocClient.registerLocationListener(myListener);  //注册监听函数
        this.setLocationOption();
        mLocClient.start();

        entityName = getImei(getActivity().getApplicationContext());
        client = new LBSTraceClient(getActivity().getApplicationContext());
//设置定位模式，这里我选择的是只用GPS定位，这样精度高些
        client.setLocationMode(LocationMode.Device_Sensors);
        trace = new Trace(serviceId, entityName, isNeedObjectStorage);  //实例化轨迹服务
        //设置位置采集和打包周期
        client.setInterval(gatherInterval, packInterval);
        //开启轨迹服务
        //client.startTrace(trace, startTraceListener);
        realtimeBitmap = BitmapDescriptorFactory.fromResource(R.drawable.holydizhi);
    }

    //初始化实体监听器
    private void initOnEntityListener() {
        //地图点击，标注显示
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(com.baidu.mapapi.model.LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        //实时显示轨迹按钮监听
        staButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTrace) {
                    //开始实时显示轨迹
                    startRefreshThread(true);
                    mBaiduMap.clear();
                    isFirstTrace = false;
                    //开始定位采集
                    client.startGather(startTraceListener);
                } else {
                    //结束显示实时轨迹
                    isFirstTrace = true;
                    pointList.clear();//清空点集
                    mBaiduMap.clear();
                    //停止收集轨迹点
                    client.stopGather(startTraceListener);
                    //停止刷新进程
                    startRefreshThread(false);
                }
            }
        });

        //实体状态监听器
        entityListener = new OnEntityListener() {

            @Override
//            接收最新的轨迹点
            public void onReceiveLocation(TraceLocation traceLocation) {
                super.onReceiveLocation(traceLocation);
                LatLng point = new LatLng(traceLocation.getLatitude(), traceLocation.getLongitude());
                if (pointList.size() == 0) {
                    overlay = new MarkerOptions().position(point)
                            .icon(realtimeBitmap).zIndex(9).draggable(true);
                    mBaiduMap.addOverlay(overlay);
                    pointList.add(point);
                } else {
                    LatLng last = pointList.get(pointList.size() - 1);
                    double distance = getDistance(point, last);
                    if (distance < 80 && distance > 0) {
                        pointList.add(point);
                        drawRealtimePoint(point, last);
                    }
                }
            }
        };
    }

    //计算两点之间的距离
    public static double getDistance(LatLng point1, LatLng point2) {
        double lat1 = point1.latitude * 100000;
        double lng1 = point1.longitude * 100000;
        double lat2 = point2.latitude * 100000;
        double lng2 = point2.longitude * 100000;
        return sqrt((lat1 - lat2) * (lat1 - lat2) + (lng1 - lng2) * (lng1 - lng2));
    }

    //追踪开始
    private void initOnStartTraceListener() {

        // 实例化开启轨迹服务回调接口
        startTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {

            }

            @Override
            public void onStartTraceCallback(int i, String s) {
                Log.i("TAG", "onTraceCallback=" + s);
                if (i == 0 || i == 10006) {
                }
            }

            @Override
            public void onStopTraceCallback(int i, String s) {

            }

            @Override
            public void onStartGatherCallback(int i, String s) {

            }

            @Override
            public void onStopGatherCallback(int i, String s) {

            }

            @Override
            public void onPushCallback(byte b, PushMessage pushMessage) {
                Log.i("TAG", "onTracePushCallback=" + pushMessage);
            }

            @Override
            public void onInitBOSCallback(int i, String s) {

            }
        };
    }

    private class RefreshThread extends Thread {
        protected boolean refresh = true;

        public void run() {
            while (refresh) {
                queryRealtimeTrack();
                System.out.println("线程更新" + pointList.size());
                try {
                    Thread.sleep(packInterval * 1000);
                } catch (InterruptedException e) {
                    System.out.println("线程休眠失败");
                }
            }
        }
    }

    // 查询实时线路
    private void queryRealtimeTrack() {
        locRequest = new LocRequest(tag, serviceId);
        client.queryRealTimeLoc(locRequest, entityListener);
    }

    //启动刷新线程
    private void startRefreshThread(boolean isStart) {

        if (refreshThread == null) {
            refreshThread = new RefreshThread();
        }

        refreshThread.refresh = isStart;

        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }
    }

    //画出实时线路点
    private void drawRealtimePoint(LatLng point, LatLng last) {

//           每次画两个点
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(last);
        latLngs.add(point);
        polyline = new PolylineOptions().width(10).color(Color.BLUE).points(latLngs);
        mBaiduMap.addOverlay(polyline);
    }

    //获取手机识别码
    //@SuppressLint("MissingPermission")
    private String getImei(Context context) {
        String mImei = "NULL";
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "TODO";
            }
            mImei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            mImei = "NULL";
        }
        return mImei;
    }

    //设置定位选项
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);  //打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //设置定位模式
        option.setCoorType("bd09ll"); //返回的定位结果是百度经纬度默认值gcj02
        //option.setScanSpan(2000);  //设置发起定位请求的间隔时间为2000ms
        option.setOpenAutoNotifyMode(); //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化
        // 就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        option.setIsNeedAddress(true);  //返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);  //返回的定位结果包含手机机头的方向
        mLocClient.setLocOption(option);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
        mLocClient.stop();
        client.stopTrace(trace,startTraceListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
