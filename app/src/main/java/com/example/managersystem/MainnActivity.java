//package com.example.managersystem;
//
//
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.view.MenuItem;
//import android.widget.FrameLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
//
//
//    private FrameLayout contentLayout;//容器
//    private BottomNavigationView mainBottomView;//底部导航
//
//
//    private HomeFragment homeFragment;
//    private DashboardFragment dashboardFragment;
//    private NoticeFragment noticeFragment;
//
//
//    private static final String HOME_FRAGMENT_KEY = "homeFragment";
//    private static final String DASHBOARD_FRAGMENT_KEY = "DashboardFragment";
//    private static final String NOTICE_FRAGMENT_KEY = "NoticeFragment";
//
//
//    private List<Fragment> fragmentList = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        L.i("MainActivity  onCreate");
//        initView();
//
//
//        if (savedInstanceState != null) {
//
//            /*获取保存的fragment  没有的话返回null*/
//            homeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, HOME_FRAGMENT_KEY);
//            dashboardFragment = (DashboardFragment) getSupportFragmentManager().getFragment(savedInstanceState, DASHBOARD_FRAGMENT_KEY);
//            noticeFragment = (NoticeFragment) getSupportFragmentManager().getFragment(savedInstanceState, NOTICE_FRAGMENT_KEY);
//
//
//            addToList(homeFragment);
//            addToList(dashboardFragment);
//            addToList(noticeFragment);
//
//
//        } else {
//            initFragment();
//        }
//
//
//    }
//
//    private void addToList(Fragment fragment) {
//        if (fragment != null) {
//            fragmentList.add(fragment);
//        }
//
//        L.i("fragmentList数量" + fragmentList.size());
//    }
//
//    private void initView() {
//        contentLayout = (FrameLayout) findViewById(R.id.bottom_nav_content);
//        mainBottomView = (BottomNavigationView) findViewById(R.id.mainBottomView);
//
//        mainBottomView.setOnNavigationItemSelectedListener(this);
//    }
//
//    private void initFragment() {
//        /* 默认显示home  fragment*/
//        homeFragment = new HomeFragment();
//        addFragment(homeFragment);
//        showFragment(homeFragment);
//
//
//    }
//
//    /*添加fragment*/
//    private void addFragment(Fragment fragment) {
//
//        /*判断该fragment是否已经被添加过  如果没有被添加  则添加*/
//        if (!fragment.isAdded()) {
//            getSupportFragmentManager().beginTransaction().add(R.id.bottom_nav_content, fragment).commit();
//            /*添加到 fragmentList*/
//            fragmentList.add(fragment);
//        }
//    }
//
//
//    /*显示fragment*/
//    private void showFragment(Fragment fragment) {
//
//
//        L.i("fragmentList數量：" + fragmentList.size());
//        for (Fragment frag : fragmentList) {
//
//            if (frag != fragment) {
//                /*先隐藏其他fragment*/
//                L.i("隱藏" + fragment);
//                getSupportFragmentManager().beginTransaction().hide(frag).commit();
//            }
//        }
//        getSupportFragmentManager().beginTransaction().show(fragment).commit();
//
//    }
//
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.navigation_home:
//
//                if (homeFragment == null) {
//                    L.i("homeFragment 为空  创建");
//                    homeFragment = new HomeFragment();
//                }
//                addFragment(homeFragment);
//                showFragment(homeFragment);
//                break;
//
//            case R.id.navigation_dashboard:
//                if (dashboardFragment == null) {
//                    L.i("dashboardFragment 为空  创建");
//                    dashboardFragment = new DashboardFragment();
//                }
//                addFragment(dashboardFragment);
//                showFragment(dashboardFragment);
//                break;
//
//            case R.id.navigation_notifications:
//
//                if (noticeFragment == null) {
//                    L.i("noticeFragment 为空  创建");
//                    noticeFragment = new NoticeFragment();
//                }
//                addFragment(noticeFragment);
//                showFragment(noticeFragment);
//                break;
//        }
//        return true;
//    }
//
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        L.i("MainActivity onSaveInstanceState");
//        /*fragment不为空时 保存*/
//        if (homeFragment != null) {
//            getSupportFragmentManager().putFragment(outState, HOME_FRAGMENT_KEY, homeFragment);
//        }
//        if (dashboardFragment != null) {
//            getSupportFragmentManager().putFragment(outState, DASHBOARD_FRAGMENT_KEY, dashboardFragment);
//        }
//        if (noticeFragment != null) {
//            getSupportFragmentManager().putFragment(outState, NOTICE_FRAGMENT_KEY, noticeFragment);
//        }
//        super.onSaveInstanceState(outState);
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        L.i("MainActivity onStart");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        L.i("MainActivity onRestart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        L.i("MainActivity onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        L.i("MainActivity onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        L.i("MainActivity onStop");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        L.i("MainActivity onDestroy");
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//
//        super.onConfigurationChanged(newConfig);
//        L.i("MainActivity onConfigurationChanged");
//    }
//}
//
//
