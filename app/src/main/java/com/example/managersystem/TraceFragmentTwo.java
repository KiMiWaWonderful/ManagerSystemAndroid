package com.example.managersystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.fence.AlarmPoint;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class TraceFragmentTwo extends Fragment {

    View view;
    // 轨迹服务ID
    long serviceId = 212031;
    // 设备标识
    String entityName = "myTrace";
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。
    // 支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    boolean isNeedObjectStorage = false;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private LBSTraceClient mTraceClient;
    private Trace mTrace;
    // 定位周期(单位:秒)
    private int gatherInterval = 5;
    // 打包回传周期(单位:秒)
    private int packInterval = 10;
    private List<LatLng> trackPoints = new ArrayList<>();

    int tag = 1;

    private Button btnStartServer;
    private Button btnStopServer;
    private Button btnStartGather;
    private Button btnStopGather;
    private Button btnQueryTrace;

    private LocationClient locationClient = null;
    private boolean firstLocation;
    private MyLocationConfiguration myLocationConfiguration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SDKInitializer.initialize(getActivity().getApplicationContext());
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_trace_two, null);
        mMapView = view.findViewById(R.id.texture_map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        OnClick onClick = new OnClick();
        btnStartServer = view.findViewById(R.id.btn_start_server);
        btnStopServer = view.findViewById(R.id.btn_stop_server);
        btnStartGather = view.findViewById(R.id.btn_start_gather);
        btnStopGather = view.findViewById(R.id.btn_stop_gather);
        btnQueryTrace = view.findViewById(R.id.btn_query_trace);

        btnStartServer.setOnClickListener(onClick);
        btnStopServer.setOnClickListener(onClick);
        btnStartGather.setOnClickListener(onClick);
        btnStopGather.setOnClickListener(onClick);
        btnQueryTrace.setOnClickListener(onClick);

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
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // map view 销毁后不在处理新接收的位置
                if (bdLocation == null || mMapView == null)
                    return;

                //构造定位数据,不要精度圈
                bdLocation.setRadius(0);
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();

                //设置定位数据
                mBaiduMap.setMyLocationData(myLocationData);

                // 第一次定位时，将地图位置移动到当前位置
                if(firstLocation){
                    firstLocation = false;
                    LatLng xy = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    //设置缩放中心点；缩放比例；
                    builder.target(xy).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build())  );
                }
            }
        });


        mTrace = new Trace(serviceId, entityName, isNeedObjectStorage); // 初始化轨迹服务

        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(getActivity().getApplicationContext());

        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);

        return view;
    }

    class OnClick implements View.OnClickListener{

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_start_server:
                    mTraceClient.startTrace(mTrace, mTraceListener);
                    break;
                case R.id.btn_stop_server:
                    mTraceClient.stopTrace(mTrace, mTraceListener);
                    break;
                case R.id.btn_start_gather:
                    mTraceClient.startGather(mTraceListener);
                    break;
                case R.id.btn_stop_gather:
                    mTraceClient.stopTrace(mTrace, mTraceListener);
                    break;
                case R.id.btn_query_trace:
                    // 创建历史轨迹请求实例
                    HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(tag, serviceId, entityName);

                    //设置轨迹查询起止时间
                    // 开始时间(单位：秒)
                    long startTime = System.currentTimeMillis() / 1000 - 12 * 60 * 60;
                    // 结束时间(单位：秒)
                    long endTime = System.currentTimeMillis() / 1000;
                    long timeMillis = System.currentTimeMillis();
                    // 设置开始时间
                    historyTrackRequest.setStartTime(startTime);
                    // 设置结束时间
                    historyTrackRequest.setEndTime(endTime);

                    historyTrackRequest.setPageSize(1000);
                    historyTrackRequest.setPageIndex(1);

                    // 查询历史轨迹
                    mTraceClient.queryHistoryTrack(historyTrackRequest, mHistoryListener);
                    break;
            }
        }
    }


    OnTrackListener mHistoryListener = new OnTrackListener() {
        // 历史轨迹回调
        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse response) {
            int total = response.getTotal();
            if (StatusCodes.SUCCESS != response.getStatus()) {
                Toast.makeText(getActivity().getApplicationContext(), "结果为：" + response.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (0 == total) {
                Toast.makeText(getActivity().getApplicationContext(), "未查询到历史轨迹", Toast.LENGTH_SHORT).show();
            } else {
                List<TrackPoint> points = response.getTrackPoints();
                if (null != points) {
                    for (TrackPoint trackPoint : points) {
                        if (!TraceUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                trackPoint.getLocation().getLongitude())) {
                            trackPoints.add(TraceUtil.convertTrace2Map(trackPoint.getLocation()));
                        }
                    }
                }
            }
            TraceUtil traceUtil=new TraceUtil();
            traceUtil.drawHistoryTrack(mBaiduMap,trackPoints, SortType.asc);
        }
    };

    OnTraceListener mTraceListener = new OnTraceListener() {

        public int notifyId = 0;

        @Override
        public void onBindServiceCallback(int i, String s) {
            if (i == StatusCodes.SUCCESS) {
                Toast.makeText(getActivity().getApplicationContext(), "绑定服务成功:" + "\r\n消息编码:" + i +
                        "\r\n消息内容:" + s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStartTraceCallback(int i, String s) {
            Toast.makeText(getActivity().getApplicationContext(), "绑定服务成功:" + "\r\n消息编码:" + i +
                    "\r\n消息内容:" + s, Toast.LENGTH_SHORT).show();
            if (StatusCodes.SUCCESS == i || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= i) {
                Toast.makeText(getActivity().getApplicationContext(), "绑定服务成功:" + "\r\n消息编码:" + i +
                        "\r\n消息内容:" + s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStopTraceCallback(int i, String s) {
            if (StatusCodes.SUCCESS == i || StatusCodes.CACHE_TRACK_NOT_UPLOAD == i) {
                Toast.makeText(getActivity().getApplicationContext(), "停止服务成功:" + "\r\n消息编码:" + i +
                        "\r\n消息内容:" + s, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onStartGatherCallback(int i, String s) {
            if (StatusCodes.SUCCESS == i || StatusCodes.GATHER_STARTED == i) {
                Toast.makeText(getActivity().getApplicationContext(), "开启采集成功:" + "\r\n消息编码:" + i +
                        "\r\n消息内容:" + s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStopGatherCallback(int i, String s) {
            if (StatusCodes.SUCCESS == i || StatusCodes.GATHER_STOPPED == i) {
                Toast.makeText(getActivity().getApplicationContext(), "停止采集成功:" + "\r\n消息编码:" + i +
                        "\r\n消息内容:" + s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPushCallback(byte messageType, PushMessage pushMessage) {
            if (messageType < 0x03 || messageType > 0x04) {
                Toast.makeText(getActivity().getApplicationContext(), pushMessage.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            FenceAlarmPushInfo alarmPushInfo = pushMessage.getFenceAlarmPushInfo();
            if (null == alarmPushInfo) {
                Toast.makeText(getActivity().getApplicationContext(), "onPushCallback:" + "\r\n状态码:" + messageType +
                        "\r\n消息内容:" + pushMessage, Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuffer alarmInfo = new StringBuffer();
            alarmInfo.append("您于")
                    .append(CommonUtil.getHMS(alarmPushInfo.getCurrentPoint().getLocTime() * 1000))
                    .append(alarmPushInfo.getMonitoredAction() == MonitoredAction.enter ? "进入" : "离开")
                    .append(messageType == 0x03 ? "云端" : "本地")
                    .append("围栏：").append(alarmPushInfo.getFenceName());

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                Notification notification = new Notification.Builder(getActivity().getApplicationContext())
                        .setContentTitle("百度鹰眼报警推送")
                        .setContentText(alarmInfo.toString())
                        .setWhen(System.currentTimeMillis()).build();
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(notifyId++, notification);
            }

            alarmPushInfo.getFenceId();//获取围栏id
            alarmPushInfo.getMonitoredPerson();//获取监控对象标识
            alarmPushInfo.getFenceName();//获取围栏名称
            alarmPushInfo.getPrePoint();//获取上一个点经度信息
            AlarmPoint alarmPoin = alarmPushInfo.getCurrentPoint();//获取报警点经纬度等信息
            alarmPoin.getCreateTime();//获取此位置上传到服务端时间
            alarmPoin.getLocTime();//获取定位产生的原始时间

        }

        @Override
        public void onInitBOSCallback(int i, String s) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        //initMap ();
        mBaiduMap.setMyLocationEnabled(true);
        if (!locationClient.isStarted())
        {
            locationClient.start();
        }
       // test ();
    }

    @Override
    public void onStop() {
        // 关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        super.onStop();
    }

    @Override
    public void onResume() {
        mMapView.onResume ();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy ();
        mMapView = null;
        super.onDestroy();
    }

}
