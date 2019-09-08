//package com.example.managersystem;
//
//import android.app.Activity;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.graphics.Color;
//import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.baidu.mapapi.SDKInitializer;
//
//public class MainnnActivity extends Activity implements View.OnClickListener {
//
//    //TAG标识
//    private int TAG;
//    private int selindex=0;
//    private static final  String[] FRAGMENT_TAG = {"homeFragment","demandFragment","newsFragment","personalFragment"};
//    private static final  String[] FRAGMENT_TITLE_TAG={"homeTitleFragment","demandTitleFragment","newsTitleFragment","personalTitleFragment"};
//
//    //Fragment控件
//    private HomeFragment homeFragment;
//    private DemandFragment demandFragment;
//    private NewsFragment newsFragment;
//    private PersonalFragment personalFragment;
//
//    private HomeTitleFragment homeTitleFragment;
//    private DemandTitleFragment demandTitleFragment;
//    private NewsTitleFragment newsTitleFragment;
//    private PersonalTitleFragment personalTitleFragment;
//
//    private View homeLayout;
//    private View demandLayout;
//    private View newsLayout;
//    private View personalLayout;
//
//    private ImageView homeImage;
//    private ImageView demandImage;
//    private ImageView newsImage;
//    private ImageView personalImage;
//
//    private TextView homeText;
//    private TextView demandText;
//    private TextView newsText;
//    private TextView personalText;
//
//    //Fragmnet管理
//
//    private FragmentManager fragmentManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        //状态栏
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            Window window = getWindow();
////            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
////            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////            window.setStatusBarColor(Color.TRANSPARENT);
////        }
//
//        super.onCreate(savedInstanceState);
//
//        //百度地图
//        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
//        //注意该方法要再setContentView方法之前实现
//
//        SDKInitializer.initialize(getApplicationContext());
//        //requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_main);
//
//        // 状态栏初始化布局元素
////        View statusBar = findViewById(R.id.statusBarView);
////        statusBar.setBackgroundColor(getResources().getColor(R.color.limegreen));
////        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
////        layoutParams.height = getStatusBarHeight();
//
//        initViews();
//
//        fragmentManager = getFragmentManager();
//
//        //savedInstanceState不为空
//        if(savedInstanceState!=null){
//            homeFragment=(HomeFragment)fragmentManager.findFragmentByTag("homeFragment");
//            demandFragment=(DemandFragment) fragmentManager.findFragmentByTag("demandFragment");
//            newsFragment=(NewsFragment)fragmentManager.findFragmentByTag("newsFragment");
//            personalFragment=(PersonalFragment)fragmentManager.findFragmentByTag("personalFragment");
//            homeTitleFragment=(HomeTitleFragment)fragmentManager.findFragmentByTag("homeTitleFragment");
//            demandTitleFragment=(DemandTitleFragment)fragmentManager.findFragmentByTag("demandTitleFragment");
//            newsTitleFragment=(NewsTitleFragment)fragmentManager.findFragmentByTag("newsTitleFragment");
//            personalTitleFragment=(PersonalTitleFragment)fragmentManager.findFragmentByTag("personalTitleFragment");
//
//            //打开关闭前的Fragment
//            setTabSelection(savedInstanceState.getInt("TAG"));
//        }else{
//            //打开第一个Fragment
//            setTabSelection(selindex);
//        }
//    }
//
//    //动态获取状态栏高度
////    private int getStatusBarHeight() {
////        int result = 0;
////        //获取状态栏高度的资源id
////        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
////        if (resourceId > 0) {
////            result = getResources().getDimensionPixelSize(resourceId);
////        }
////        return result;
////    }
//
//    //初始化布局控件
//
//    private void initViews() {
//
//        homeLayout = findViewById(R.id.home_layout);
//        demandLayout = findViewById(R.id.demand_layout);
//        newsLayout = findViewById(R.id.news_layout);
//        personalLayout = findViewById(R.id.personal_layout);
//
//        homeImage = (ImageView) findViewById(R.id.home_image);
//
//        demandImage = (ImageView) findViewById(R.id.demand_image);
//
//        newsImage = (ImageView) findViewById(R.id.news_image);
//
//        personalImage = (ImageView) findViewById(R.id.personal_image);
//
//        homeText = (TextView) findViewById(R.id.home_text);
//
//        demandText = (TextView) findViewById(R.id.demand_text);
//
//        newsText = (TextView) findViewById(R.id.news_text);
//
//        personalText = (TextView) findViewById(R.id.personal_text);
//
//        //点击监听
//
//        homeLayout.setOnClickListener(this);
//
//        demandLayout.setOnClickListener(this);
//
//        newsLayout.setOnClickListener(this);
//
//        personalLayout.setOnClickListener(this);
//
//    }
//
//    @Override
//
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//
//            case R.id.home_layout:
//
//                // 当点击了首页tab时，选中第1个tab
//
//                setTabSelection(0);
//
//                break;
//
//            case R.id.demand_layout:
//
//                // 当点击了供求tab时，选中第2个tab
//
//                setTabSelection(1);
//
//                break;
//
//            case R.id.news_layout:
//
//                // 当点击了发现tab时，选中第3个tab
//
//                setTabSelection(2);
//
//                break;
//
//            case R.id.personal_layout:
//
//                // 当点击了我的tab时，选中第4个tab
//
//                setTabSelection(3);
//
//                break;
//
//            default:
//
//                break;
//
//        }
//
//    }
//
//
//
//    /**
//
//     * 根据传入的index参数来设置选中的tab页。
//
//     */
//
//    private void setTabSelection(int index) {
//
//        // 每次选中之前先清楚掉上次的选中状态
//
//        //clearSelection();
//
//        // 开启一个Fragment事务
//
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
//
//        hideFragments(transaction);
//
//        switch (index) {
//
//            case 0:
//
//                //     messageImage.setImageResource(R.drawable.message_selected);
//
//                TAG=0;
//                homeText.setTextColor(homeText.getResources().getColor(R.color.limegreen));
//                if (homeFragment == null) {
//
//                    // 如果MessageFragment为空，则创建一个并添加到界面上
//                    homeFragment = new HomeFragment();
//                    homeTitleFragment=new HomeTitleFragment();
//                    transaction.add(R.id.content, homeFragment, FRAGMENT_TAG[index]);
//                   // transaction.add(R.id.title,homeTitleFragment,FRAGMENT_TITLE_TAG[index]);
//
//                } else {
//
//                    // 如果MessageFragment不为空，则直接将它显示出来
//
//                    transaction.show(homeFragment);
//
//                    transaction.show(homeTitleFragment);
//
//                }
//
//                break;
//
//            case 1:
//
//                //  contactsImage.setImageResource(R.drawable.contacts_selected);
//
//                TAG=1;
//
//                demandText.setTextColor(demandText.getResources().getColor(R.color.limegreen));
//
//                if (demandFragment == null) {
//
//                    // 如果ContactsFragment为空，则创建一个并添加到界面上
//
//                    demandFragment = new DemandFragment();
//
//                    demandTitleFragment=new DemandTitleFragment();
//
//                    transaction.add(R.id.content, demandFragment, FRAGMENT_TAG[index]);
//
//                    transaction.add(R.id.title,demandTitleFragment,FRAGMENT_TITLE_TAG[index]);
//
//                } else {
//
//                    // 如果ContactsFragment不为空，则直接将它显示出来
//
//                    transaction.show(demandFragment);
//
//                    transaction.show(demandTitleFragment);
//
//                }
//
//                break;
//
//            case 2:
//
//                //     newsImage.setImageResource(R.drawable.news_selected);
//
//                TAG=2;
//
//                newsText.setTextColor(newsText.getResources().getColor(R.color.limegreen));
//
//                if (newsFragment == null) {
//
//                    // 如果NewsFragment为空，则创建一个并添加到界面上
//
//                    newsFragment = new NewsFragment();
//
//                    newsTitleFragment=new NewsTitleFragment();
//
//                    transaction.add(R.id.content, newsFragment, FRAGMENT_TAG[index]);
//
//                    transaction.add(R.id.title,newsTitleFragment,FRAGMENT_TITLE_TAG[index]);
//
//                } else {
//
//                    // 如果NewsFragment不为空，则直接将它显示出来
//
//                    transaction.show(newsFragment);
//
//                    transaction.show(newsTitleFragment);
//
//                }
//
//                break;
//
//            case 3:
//
//            default:
//
//                //    settingImage.setImageResource(R.drawable.setting_selected);
//
//                TAG=3;
//
//                personalText.setTextColor(personalText.getResources().getColor(R.color.limegreen));
//
//                if (personalFragment == null) {
//
//                    // 如果SettingFragment为空，则创建一个并添加到界面上
//
//                    personalFragment = new PersonalFragment();
//
//                    personalTitleFragment=new PersonalTitleFragment();
//
//                    transaction.add(R.id.content, personalFragment, FRAGMENT_TAG[index]);
//
//                    transaction.add(R.id.title,personalTitleFragment,FRAGMENT_TITLE_TAG[index]);
//
//                } else {
//
//                    // 如果SettingFragment不为空，则直接将它显示出来
//
//                    transaction.show(personalFragment);
//
//                    transaction.show(personalTitleFragment);
//
//                }
//
//                break;
//
//        }
//
//        transaction.commit();
//
//    }
//
//
//
//    /**
//
//     * 清除掉所有的选中状态。
//
//     */
//
////    private void clearSelection() {
////
////        homeImage.setImageResource(R.drawable.home);
////
////        homeText.setTextColor(Color.parseColor("#82858b"));
////
////        demandImage.setImageResource(R.drawable.demand);
////
////        demandText.setTextColor(Color.parseColor("#82858b"));
////
////        newsImage.setImageResource(R.drawable.news);
////
////        newsText.setTextColor(Color.parseColor("#82858b"));
////
////        personalImage.setImageResource(R.drawable.personal);
////
////        personalText.setTextColor(Color.parseColor("#82858b"));
////
////    }
//
//
//
//    /**
//
//     * 将所有的Fragment都置为隐藏状态。
//
//     * 用于对Fragment执行操作的事务
//
//     */
//
//    private void hideFragments(FragmentTransaction transaction) {
//
//        if (homeFragment != null) {
//
//            transaction.hide(homeFragment);
//
//            transaction.hide(homeTitleFragment);
//
//        }
//
//        if (demandFragment != null) {
//
//            transaction.hide(demandFragment);
//
//            transaction.hide(demandTitleFragment);
//
//        }
//
//        if (newsFragment != null) {
//
//            transaction.hide(newsFragment);
//
//            transaction.hide(newsTitleFragment);
//
//        }
//
//        if (personalFragment != null) {
//
//            transaction.hide(personalFragment);
//
//            transaction.hide(personalTitleFragment);
//
//        }
//
//    }
//
//    /*
//
//     * 保存TAB选中状态
//
//     * */
//
//    @Override
//
//    protected void onSaveInstanceState(Bundle outState) {
//
//        //如果用以下这种做法则不保存状态，再次进来的话会显示默认tab
//
//        //总是执行这句代码来调用父类去保存视图层的状态
//
//        //保存tab选中的状态;
//
//        super.onSaveInstanceState(outState);
//
//        outState.putInt("TAG",TAG);
//
//    }
//
//}
//
