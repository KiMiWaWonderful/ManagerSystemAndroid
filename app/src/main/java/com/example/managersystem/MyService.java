//package com.example.managersystem;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.os.Looper;
//import android.widget.Toast;
//
//import com.baidu.trace.LBSTraceClient;
//import com.baidu.trace.Trace;
//import com.baidu.trace.api.entity.OnEntityListener;
//import com.baidu.trace.model.LocationMode;
//
//public class MyService extends Service {
//
//    private static final String TAG = "MyService";
//
//    // 轨迹服务
//    protected static Trace trace = null;
//
//    // 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
//    public static final long serviceId = 212031;
//
//    // 轨迹服务类型
//    //0 : 不建立socket长连接，
//    //1 : 建立socket长连接但不上传位置数据，
//    //2 : 建立socket长连接并上传位置数据）
//    private int traceType = 2;
//
//    // 轨迹服务客户端
//    public static LBSTraceClient client = null;
//
//    // Entity监听器
//    public static OnEntityListener entityListener = null;
//
//    // 开启轨迹服务监听器
//    protected OnStartTraceListener startTraceListener = null;
//
//    // 停止轨迹服务监听器
//    protected static OnStopTraceListener stopTraceListener = null;
//
//    // 采集周期（单位 : 秒）
//    private int gatherInterval = 10;
//
//    // 设置打包周期(单位 : 秒)
//    private int packInterval = 20;
//
//    protected static boolean isTraceStart = false;
//
//    // 手机IMEI号设置为唯一轨迹标记号,只要该值唯一,就可以作为轨迹的标识号,使用相同的标识将导致轨迹混乱
//    private String imei;
//
//
//    public IBinder onBind(Intent arg0) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if(intent != null && intent.getExtras() != null){
//            imei= intent.getStringExtra("imei");
//        }
//        init();
//        return super.onStartCommand(intent, START_STICKY, startId);
//    }
//
//    //被销毁时反注册广播接收器
//    public void onDestroy() {
//        super.onDestroy();
//        stopTrace();
//    }
//
//    /**
//     * 初始化
//     */
//    private void init() {
//        // 初始化轨迹服务客户端
//        client = new LBSTraceClient(this);
//
//        // 设置定位模式
//        client.setLocationMode(LocationMode.High_Accuracy);
//
//        // 初始化轨迹服务
//        trace = new Trace(this, serviceId, imei, traceType);
//
//// 采集周期,上传周期
//        client.setInterval(gatherInterval, packInterval);
//
//        // 设置http请求协议类型0:http,1:https
//        client.setProtocolType(0);
//
//        // 初始化监听器
//        initListener();
//
//        // 启动轨迹上传
//        startTrace();
//    }
//    // 开启轨迹服务
//    private void startTrace() {
//        // 通过轨迹服务客户端client开启轨迹服务
//        client.startTrace(trace, startTraceListener);
//    }
//
//    // 停止轨迹服务
//    public static void stopTrace() {
//        // 通过轨迹服务客户端client停止轨迹服务
//        LogUtil.i(TAG, "stopTrace(), isTraceStart : " + isTraceStart);
//
//        if(isTraceStart){
//            client.stopTrace(trace, stopTraceListener);
//        }
//    }
//
//    // 初始化监听器
//    private void initListener() {
//
//        initOnEntityListener();
//
//        // 初始化开启轨迹服务监听器
//        initOnStartTraceListener();
//
//        // 初始化停止轨迹服务监听器
//        initOnStopTraceListener();
//    }
//
//
//    /**
//     * 初始化OnStartTraceListener
//     */
//    private void initOnStartTraceListener() {
//        // 初始化startTraceListener
//        startTraceListener = new OnStartTraceListener() {
//
//            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
//            public void onTraceCallback(int arg0, String arg1) {
//                LogUtil.i(TAG, "开启轨迹回调接口 [消息编码 : " + arg0 + "，消息内容 : " + arg1 + "]");
//                if (0 == arg0 || 10006 == arg0) {
//                    isTraceStart = true;
//                }
//            }
//
//            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
//            public void onTracePushCallback(byte arg0, String arg1) {
//                LogUtil.i(TAG, "轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]");
//            }
//        };
//    }
//
//    // 初始化OnStopTraceListener
//    private void initOnStopTraceListener() {
//        stopTraceListener = new OnStopTraceListener() {
//
//            // 轨迹服务停止成功
//            public void onStopTraceSuccess() {
//                LogUtil.i(TAG, "停止轨迹服务成功");
//                isTraceStart = false;
//                stopSelf();
//            }
//
//            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
//            public void onStopTraceFailed(int arg0, String arg1) {
//                LogUtil.i(TAG, "停止轨迹服务接口消息 [错误编码 : " + arg0 + "，消息内容 : " + arg1 + "]");
//            }
//        };
//    }
//
//    // 初始化OnEntityListener
//    private void initOnEntityListener() {
//
//        entityListener = new OnEntityListener() {
//
//            // 请求失败回调接口
//            @Override
//            public void onRequestFailedCallback(String arg0) {method stub
//                Looper.prepare();
//                LogUtil.i(TAG, "entity请求失败回调接口消息 : " + arg0);
//                Toast.makeText(getApplicationContext(), "entity请求失败回调接口消息 : " + arg0, Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            // 添加entity回调接口
//            @Override
//            public void onAddEntityCallback(String arg0) {
//                Looper.prepare();
//                LogUtil.i(TAG, "添加entity回调接口消息 : " + arg0);
//                Toast.makeText(getApplicationContext(), "添加entity回调接口消息 : " + arg0, Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            // 查询entity列表回调接口
//            @Override
//            public void onQueryEntityListCallback(String message) {
//                LogUtil.i(TAG, "onQueryEntityListCallback : " + message);
//            }
//
//            @Override
//            public void onReceiveLocation(TraceLocation location) {
//
//            }
//        };
//    }
//}
//
//}
