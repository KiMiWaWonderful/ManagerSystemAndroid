//package com.example.managersystem;
//
//import android.app.ProgressDialog;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.InfoWindow;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.SupportMapFragment;
//import com.baidu.mapapi.model.LatLng;
//
//public class InfoWindowFragment extends Fragment {
//
//    private static final int   REQUEST_CHECKMAP = 200;
//    private View rootview;
//    private ProgressDialog mDialog;
//    private Handler mHandler;
//
//    private MapView mapView          = null;
//    private SupportMapFragment map;
//    private BaiduMap mBaiduMap;
//    private BitmapDescriptor descriptor;
//
//    /**
//     *@Fields mInfoWindow : 弹出的窗口
//     */
//    private InfoWindow mInfoWindow;
//    private LinearLayout baidumap_infowindow;
//    private MarkerOnInfoWindowClickListener markerListener;
//
//    private void initView(View rootView){
//        mapView = (MapView) rootView.findViewById (R.id.mv_baidumap);
//        baidumap_infowindow = (LinearLayout) LayoutInflater.from (getActivity ()).inflate (R.layout.baidumap_infowindow, null);
//        // 构建Marker图标
//        descriptor = BitmapDescriptorFactory.fromResource (R.drawable.icon_point);
//        mHandler = new AsyncHandler ();
//    }
//
//    private void initMap(){
//        // 获取BaiduMap类 BaiduMap类才可以添加自定义的图层
//        mBaiduMap = mapView.getMap ();
//
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo (14.0f);
//        mBaiduMap.setMapStatus (msu);
//
//        markerListener = new MarkerOnInfoWindowClickListener ();
//
//        //对Marker的点击事件
//        mBaiduMap.setOnMarkerClickListener (new BaiduMap.OnMarkerClickListener() {
//
//            @Override
//            public boolean onMarkerClick(Marker marker){
//                //获得marker中的数据
//                CheckRecordBean bean = (CheckRecordBean) marker.getExtraInfo ().get ("marker");
//
//                createInfoWindow(baidumap_infowindow, bean);
//
//                //将marker所在的经纬度的信息转化成屏幕上的坐标
//                final LatLng ll = marker.getPosition();
//
//                mInfoWindow = new InfoWindow (BitmapDescriptorFactory.fromView (baidumap_infowindow), ll, -47, markerListener);
//                //显示InfoWindow
//                mBaiduMap.showInfoWindow(mInfoWindow);
//
//                return true;
//            }
//        });
//    }
//
//    private void initData(){
//
//        mDialog = ProgressDialogUtils.showProgressDialog (getActivity (), R.string.progress_title, R.string.progress_message);
//
//        AsyncCheckMap checkMap = new AsyncCheckMap (getActivity (),mDialog,mHandler,REQUEST_CHECKMAP);
//        checkMap.execute ();
//
//    }
//
//
//    private final class  MarkerOnInfoWindowClickListener implements InfoWindow.OnInfoWindowClickListener {
//
//        @Override
//        public void onInfoWindowClick(){
//            //隐藏InfoWindow
//            mBaiduMap.hideInfoWindow();
//        }
//
//    }
//
//    /**
//     *@Description: 创建 弹出窗口
//     *@Author:杨攀
//     *@Since: 2016年1月20日上午11:18:33
//     *@param baidumap_infowindow
//     *@param bean
//     */
//    private void createInfoWindow(LinearLayout baidumap_infowindow,CheckRecordBean bean){
//
//        InfoWindowHolder holder = null;
//        if(baidumap_infowindow.getTag () == null){
//            holder = new InfoWindowHolder ();
//
//            holder.tv_entname = (TextView) baidumap_infowindow.findViewById (R.id.tv_entname);
//            holder.tv_checkdept = (TextView) baidumap_infowindow.findViewById (R.id.tv_checkdept);
//            holder.tv_checkuser = (TextView) baidumap_infowindow.findViewById (R.id.tv_checkuser);
//            holder.tv_checktime = (TextView) baidumap_infowindow.findViewById (R.id.tv_checktime);
//            baidumap_infowindow.setTag (holder);
//        }
//
//        holder = (InfoWindowHolder) baidumap_infowindow.getTag ();
//
        //holder.tv_entname.setText (String.format (getString (R.string.checkmap_entname_format), bean.getEntName ()));
//        holder.tv_checkdept.setText (String.format (getString (R.string.checkmap_checkdept_format), bean.getRemark1 ()));
//        holder.tv_checkuser.setText (String.format (getString (R.string.checkmap_checkuser_format), bean.getCheckUserNames ()));
//        holder.tv_checktime.setText (String.format (getString (R.string.checkmap_checktime_format), bean.getCheckTime ()));
//    }
//
//    @Override
//    public void onStart(){
//        super.onStart ();
//        initMap ();
//        test ();
//    }
//
//    @Override
//    public void onResume(){
//        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
//        mapView.onResume ();
//        super.onResume ();
//    }
//
//    private final class AsyncHandler extends Handler {
//
//        @Override
//        public void handleMessage(Message msg){
//
//            String result = (String) msg.obj;
//
//            switch (msg.what) {
//                case REQUEST_CHECKMAP:// 检查地图
//
//                    List<CheckRecordBean> list = JSONUtils.parseArray (result, CheckRecordBean.class);
//                    if (list != null) {
//                        showData (list);
//                    } else {
//                        DialogUtils.showDialog (getActivity (), R.string.app_serviceReturnError);
//                    }
//                    break;
//            }
//        }
//    }
//
//    private void test(){
//
//        List<CheckRecordBean> list = new ArrayList<CheckRecordBean> ();
//
//        CheckRecordBean bean = new CheckRecordBean ();
//        bean.setEntName ("杭州鸿雁电器有限公司");
//        bean.setCheckTime ("2015-09-19");
//        bean.setCheckUserNames ("杨攀");
//        bean.setRemark1 ("综合科");
//        bean.setCheckX ("39.963175");
//        bean.setCheckY ("116.400244");
//
//        list.add (bean);
//
//        CheckRecordBean bean2 = new CheckRecordBean ();
//        bean2.setEntName ("杭州图讯科技有限公司");
//        bean2.setCheckTime ("2015-09-19");
//        bean2.setCheckUserNames ("赵云");
//        bean2.setRemark1 ("管理科");
//        bean2.setCheckX ("39.962173");
//        bean2.setCheckY ("116.410294");
//
//        list.add (bean2);
//
//        showData (list);
//    }
//
//    /**
//     *@Description: 显示 数据
//     *@Author:杨攀
//     *@Since: 2016年1月20日上午10:33:39
//     *@param list
//     */
//    private void showData(List<CheckRecordBean> list){
//        mBaiduMap.clear ();
//        addMarker (list);
//    }
//
//    /**
//     *@Description: 添加 标记
//     *@Author:杨攀
//     *@Since: 2016年1月20日上午10:34:17
//     *@param list
//     */
//    private void addMarker(List<CheckRecordBean> list){
//        for ( int i = 0 ; i < list.size () ; i++ ) {
//            CheckRecordBean bean = list.get (i);
//            // 经度
//            double longitude = parseLatLng (bean.getCheckY ());
//            // 纬度
//            double latitude = parseLatLng (bean.getCheckX ());
//
//            if (longitude > 0 && latitude > 0) {
//                // 定义Maker坐标点
//                LatLng ll = new LatLng (latitude,longitude);
//                // 构建MarkerOption，用于在地图上添加Marker
//                MarkerOptions options = new MarkerOptions ().position (ll).icon (descriptor);
//                // 在地图上添加Marker，并显示
//                Marker marker = (Marker) mBaiduMap.addOverlay (options);
//                // 将信息保存
//                Bundle bundle = new Bundle ();
//                bundle.putSerializable ("marker", bean);
//                marker.setExtraInfo (bundle);
//
//                if (i == 0) {
//                    // 把第一个默认为当前的位置图层
//                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng (ll);
//                    mBaiduMap.setMapStatus (u);
//                }
//            }
//        }
//    }
//
//    private double parseLatLng(String latlng){
//
//        if (StringUtils.isNotEmpty (latlng)) { return Double.parseDouble (latlng); }
//        return -1;
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig){
//        super.onConfigurationChanged (newConfig);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        // 在使用这个view之前首先判断其是否存在parent view，这调用getParent()方法可以实现。
//        // 如果存在parent view，那么就调用removeAllViewsInLayout()方法
//        /*-ViewGroup perentView = (ViewGroup) rootview.getParent ();
//        if (perentView != null) {
//            perentView.removeAllViewsInLayout ();
//        }*/
//
//        rootview = LayoutInflater.from (getActivity ()).inflate (R.layout.fragment_check_map, null);
//
//        initView (rootview);
//
//        // initData ();
//        return rootview;
//    }
//
//
//    @Override
//    public void onDestroy(){
//        mapView.onDestroy ();
//        mapView = null;
//        super.onDestroy ();
//    }
//}
