package com.hjh.baselib.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.LayoutInflaterFactory;

import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.hjh.baselib.R;
import com.hjh.baselib.constants.Constants;
import com.hjh.baselib.constants.IConfig;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.entity.MessageEvent;
import com.hjh.baselib.entity.ResponseJson;
import com.hjh.baselib.listener.BasicViewInterface;
import com.hjh.baselib.listener.ITitleClickListener;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.utils.CustomToast;
import com.hjh.baselib.widget.AppActivityManager;
import com.hjh.baselib.widget.BasicDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class LBaseActivity extends AppCompatActivity implements BasicViewInterface,ITitleClickListener{

    protected Unbinder mUnBinder;
    protected LBaseActivity mActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected BasicDialog basicDialog;
    private View mRootView,globalView;
    private LinearLayout mRootLayout;
    private BroadcastReceiver receiver;
    protected ImageLoader mImageLoader;
    protected Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            dispatchHandlerMessage(msg);
        }
    };

    public void dispatchHandlerMessage(Message msg){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setAppTypeFace();
        super.onCreate(savedInstanceState);
        onPreCreate();
        initImmersionBar();
        onProcessData();
        if(getActionBar() != null){
            getActionBar().hide();
        }

        getScreenSize();
        int rootResId = getRootResId();
        if(rootResId != 0) {
            super.setContentView(rootResId);
            mRootLayout = super.findViewById(getRootLayoutId());
            initRootView();
        }

        mContext = getApplicationContext();
        mActivity = this;
        mImageLoader = ImageLoader.getInstance();
        setContentView(getContentLayout());
        registerBroadrecevicer();
        mUnBinder = ButterKnife.bind(this);
        AppActivityManager.getInstance().addActivity(this);
        mInflater = LayoutInflater.from(this);
        if (basicDialog == null)
            basicDialog = new BasicDialog(mActivity, R.style.Dialog);
        EventBus.getDefault().register(this);
        switchSkin();
        onAfterCreate(savedInstanceState);
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        //设置共同沉浸式样式
//        ImmersionBar.with(this).navigationBarColor(R.color.white).init();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ImmersionBar.with(this).init();
    }

    @Override
    public void onRefreshView() {

    }

    //主题等的改变
    public void onPreCreate(){

    }

    //处理上一界面传输的数据
    protected void onProcessData(){

    }

    public void onAfterCreate(Bundle savedInstanceState){
        onLoadDefaultTitle();
        onLoadDefaultData();
        onLoadDefaultView(savedInstanceState);
    }

    /**
     * 标题定制改变
     */
    public void onLoadDefaultTitle(){

    }

    /**
     * 加载默认数据，从数据库查询
     */
    public void onLoadDefaultData(){

    }

    /**
     * 根据查询的数据加载view
     * @param savedInstanceState
     */
    public void onLoadDefaultView(Bundle savedInstanceState){

    }

    /**
     * onresume后重新获取数据,singleTask 调用onNewIntent()后执行
     */
    protected void onResumeProcessData(){

    }

    public LinearLayout getRootLayout() {
        return mRootLayout;
    }

    protected void setAppTheme(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //xml
    public int getRootResId(){
        return 0;
    }

    //xml下面的根id
    public int getRootLayoutId(){
        return 0;
    }

    public void initRootView(){

    }

    public void switchSkin(){

    }

    public void addLayoutAnimation(ViewGroup view,int animId) {
        Animation animation = AnimationUtils.loadAnimation(mContext,animId);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setDelay(0.3f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        view.setLayoutAnimation(layoutAnimationController);
    }

    public void showLoadDialog(){
        if(basicDialog != null) {
            basicDialog.show();
        }
    }

    public void hideLoadDialog(){
        if(basicDialog != null) {
            basicDialog.dismiss();
        }
    }

    public void setLoadHint(String hint){
        if(basicDialog != null) {
            basicDialog.setHintText(hint);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        mRootView = LayoutInflater.from(this).inflate(layoutResID, null);
        if(mRootLayout != null) {
            mRootLayout.removeAllViews();
            mRootLayout.addView(mRootView);
//            mRootLayout.addView(mRootView,IConfig.SCREEN_WIDTH,IConfig.SCREEN_HEIGHT);
        }else {
            setContentView(mRootView);
            addGlobalView();
        }
    }

    public void addGlobalView() {

    }

    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //接收到发布者发布的事件后，进行相应的处理操作
        if(event.getType() == MessageEvent.EventType.RESET_TYPE_SIZE){
//            resetAppTypeFace(mRootView);
//            recreate();//会闪屏，体验不好
            resetActivity();
        }else if(event.getType() == MessageEvent.EventType.RESET_BACKGROUND){
//            if(getRootResId() != 0) {
//                mRootLayout.setBackgroundResource(getRootResId());
//            }

            switchSkin();
        }else if(event.getType() == MessageEvent.EventType.REFRESH_MEETING_FILE){
            dispatchRefreshTask(event);
        }else if(event.getType() == MessageEvent.EventType.DELETE_ALL_FILES){
            onDeleteAllFiles(event);
        }else{
            dispatchEvent(event);
        }
    }

    public void dispatchEvent(MessageEvent messageEvent){

    }

    //分发会议文件改变任务
    public void dispatchRefreshTask(MessageEvent event){

    }

    public void onDeleteAllFiles(MessageEvent event){

    }

    public void resetActivity(){

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onResumeProcessData();
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy(this,null);
        basicDialog.dismiss();
        if (mUnBinder != null)
            mUnBinder.unbind();
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }

    @Override
    public void onClickLeftView() {
        back(mActivity);
    }

    @Override
    public void onClickRightView() {

    }

    @Override
    public void onClickOtherView() {

    }

    public int  getMipmapResource(String imageName){
        Context ctx=getBaseContext();
        int resId = getResources().getIdentifier(imageName, "mipmap", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }

    protected void checkCameraPermission() {
        Constants.REFUSED_PERMISSION.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < Constants.APP_PERMISSION.length; i++) {
                if (ActivityCompat.checkSelfPermission(this, Constants.APP_PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                    Constants.REFUSED_PERMISSION.add(Constants.APP_PERMISSION[i]);
                }
            }
            if (Constants.REFUSED_PERMISSION.size() == 0) {

            } else {
                ActivityCompat.requestPermissions(this, Constants.REFUSED_PERMISSION.toArray(new String[Constants.REFUSED_PERMISSION.size()]), Constants.REQUEST_CAMERA);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.REQUEST_CAMERA) {
            boolean flag = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Toast.makeText(this, "已授权", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Deprecated
    private synchronized void resetAppTypeFace(View view){
        if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for(int index = 0;index < childCount;index++){
                resetAppTypeFace(viewGroup.getChildAt(index));
            }
        }else if(view instanceof TextView || isExtendTextView(view.getClass().getName())){
            TextView textView = (TextView) view;
            float size = textView.getTextSize();
            float o = AppPresences.getInstance().getFloat(Constants.KEY_SET_TYPESIZE);
            boolean flag = AppPresences.getInstance().getBoolean(Constants.KEY_ADD);
            if(o != -1 && o!= 0) {
                if(o == 0.3f){
                    if(flag){
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,size* (o+1));
                    }else {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (size/ (1+0.6)*(1+0.3)));
                    }
                }else {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (size/(1+0.3)*(0.6+1)));
                }
            }else if(o == 0 && !flag){
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (size/ (1+0.3)));
            }
        }
    }

    public boolean isExtendTextView(String name){
        return name.equals("com.scrd.meet.widget.ScrollTextView");
    }

    @Deprecated
    private void setAppTypeFace(){
//        if (typeface == null){
//            typeface = Typeface.createFromAsset(getAssets(), "hwxk.ttf");
//        Typeface.createFromAsset(getAssets(), "fonts/huawen_songti.ttf");
//        }
//
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory(){
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs){
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                if(name.equals("com.scrd.meet.widget.ScrollTextView")){
                    try {
                        view = LayoutInflater.from(LBaseActivity.this).createView(name,null,attrs);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if ( view!= null && (view instanceof TextView || isExtendTextView(name))){
                    TextView textView = (TextView) view;
                    float size = textView.getTextSize();
                    float o = AppPresences.getInstance().getFloat(Constants.KEY_SET_TYPESIZE);
                    if(o != -1 && o!= 0) {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,size * (o+1));
                    }
//                    ((TextView) view).setTypeface(typeface);
                }
                return view;
            }
        });
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @return
     */
    public  int px2sp(float pxValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @return
     */
    public  int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int dp2px(Context context, float dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private void registerBroadrecevicer() {
        //获取广播对象
        receiver = new IntenterBoradCastReceiver();
        //创建意图过滤器
        IntentFilter filter=new IntentFilter();
        //添加动作，监听网络
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }
    //监听网络状态变化的广播接收器
    public class IntenterBoradCastReceiver extends BroadcastReceiver{

        private ConnectivityManager mConnectivityManager;
        private NetworkInfo netInfo;

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){
                        /////WiFi网络
                        onNetWorkConnect(netInfo.getType());
                    }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){
                        /////有线网络
                        onNetWorkConnect(netInfo.getType());
                    }else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                        /////////3g网络
                        onNetWorkConnect(netInfo.getType());
                    }
                } else {
                    ////////网络断开
                    onNetWorkDisConnect();
                }
            }

        }
    }

    public void onNetWorkConnect(int type){

    }

    public void onNetWorkDisConnect(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (basicDialog != null) {
            basicDialog.dismiss();
        }
        back(mActivity);
    }

    protected void showToast(String text){
        showToast(text, 3000);
    }

    protected void showToast(String text,int duration){
        if(text.endsWith("!")){
            text = text.substring(0,text.length()-1);
        }
        CustomToast.showToast(mContext, text, duration, isPad());
    }

    protected void showToast(String text,int duration,CustomToast.OnToastListener onToastListener){
        CustomToast.showToast(mContext, text, duration, isPad(),onToastListener);
    }

    public void showToast(int strId){
        CustomToast.showToast(mContext, strId, 3000, isPad());
    }

    protected boolean isPad(){
        return (mContext.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    protected int getPixel(int id) {
        return	getResources().getDimensionPixelSize(id);
    }

    /**
     * @param id
     * @return
     */
    protected int getAppColor(int id){
        return getResources().getColor(id);
    }

    protected String getStringById(int id){
        return getResources().getString(id);
    }


    /**
     * 取得状态栏高度
     * @return
     */
    protected int getStatusBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ModuleConfig.STATUS_BAR_HEIGHT = statusBarHeight;
        return statusBarHeight;
    }


    /**
     * @category 渐变加载
     * @param imageView
     * @param bitmap
     */
    protected void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
        final TransitionDrawable transitionDrawable = new TransitionDrawable(
                new Drawable[] { TRANSPARENT_DRAWABLE,
                        new BitmapDrawable(imageView.getResources(), bitmap) });
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(500);
    }

    @SuppressLint("ResourceAsColor")
    private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(
            android.R.color.transparent);

    public void getScreenSize(){
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        IConfig.SCREEN_WIDTH = outMetrics.widthPixels;
        IConfig.SCREEN_HEIGHT = outMetrics.heightPixels;
        getStatusBarHeight();
    }

    protected boolean findBoolean(String key,boolean defaultValue) {
        return getIntent().getBooleanExtra(key, defaultValue);
    }

    protected <T> T findObject(String key,Class<T> clazz){
        Object o = getIntent().getSerializableExtra(key);
        String json = ResponseJson.objectToJson(o);
        T t = new Gson().fromJson(json,clazz);
        return t;
    }

    protected String findString(String key){
        return getIntent().getStringExtra(key);
    }

    protected int findInteger(String key,int defaultValue) {
        return getIntent().getIntExtra(key, defaultValue);
    }

    protected float findFloat(String key,float defaultValue) {
        return getIntent().getFloatExtra(key, defaultValue);
    }

    protected void selectLast(EditText editText){
        String text = editText.getText().toString().trim();
        if(text != null && text.length() >= 1){
            editText.setSelection(text.length());
        }
    }

    /** start activity with left in anim */
    public  void startActivityWithAnim(Activity old, Intent intent){

        if(old == null || intent == null){
            return ;
        }

        // start activity
        old.startActivity(intent) ;
        // set adnim
        old.overridePendingTransition(getIdByName(old,"anim", "push_left_in"),getIdByName(old,"anim", "push_not_move"));
    }

    public  void startActivityWithAnim(Activity old, Intent intent, int requestCode){

        if(old == null || intent == null){
            return ;
        }

        // start activity
        old.startActivityForResult(intent, requestCode);
        // set adnim
        old.overridePendingTransition(getIdByName(old,"anim", "push_left_in"),getIdByName(old,"anim", "push_not_move"));
    }

    /** finish activity with left out anim */
    public  void finishWithAnim(Activity old){
        old.finish() ;
        old.overridePendingTransition(getIdByName(old,"anim", "push_not_move"),getIdByName(old,"anim", "push_left_out"));
    }

    public  void finishWithAnim(Activity old,int resultCode){
        old.setResult(resultCode);
        old.finish() ;
        old.overridePendingTransition(getIdByName(old,"anim", "push_not_move"),getIdByName(old,"anim", "push_left_out"));
    }

    public  int getIdByName(Activity mActivity,String className, String name) {
        String packageName = mActivity.getPackageName();
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }



    public  void back(Activity mActivity){
        mActivity.finish();
        AppActivityManager.getInstance().removeActivity(mActivity);
        if(AppActivityManager.getInstance().getActivityStack().size() != 0){
            mActivity.overridePendingTransition(getIdByName(mActivity,"anim", "push_not_move"),getIdByName(mActivity,"anim", "push_left_out"));
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public  void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    /**
     * 扩大点击范围
     * @param view 点击的视图
     * @param expandTouchWidth 扩大多大 200
     */
    public  void setTouchDelegate(final View view, final int expandTouchWidth) {
        final View parentView = (View) view.getParent();
        parentView.post(new Runnable() {
            @Override
            public void run() {
                final Rect rect = new Rect();
                view.getHitRect(rect);
                rect.top -= expandTouchWidth;
                rect.bottom += expandTouchWidth;
                rect.left -= expandTouchWidth;
                rect.right += expandTouchWidth;
                TouchDelegate touchDelegate = new TouchDelegate(rect, view);
                parentView.setTouchDelegate(touchDelegate);
            }
        });
    }


    private static final double EARTH_RADIUS = 6378137.0;
    // 返回单位是米
    public double getDistance(double longitude1, double latitude1,
                              double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private  double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public void showLoadSuccess(){

    }

    public void showLoadFailed(){

    }

    private void testStatusBar(){
        ImmersionBar.with(this)
                .transparentStatusBar()  //透明状态栏，不写默认透明色
                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .navigationBarColor(R.color.colorPrimary) //导航栏颜色，不写默认黑色
                .barColor(R.color.colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
                .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
                .autoDarkModeEnable(true) //自动状态栏字体和导航栏图标变色，必须指定状态栏颜色和导航栏颜色才可以自动变色哦
                .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .autoNavigationBarDarkModeEnable(true, 0.2f) //自动导航栏图标变色，必须指定导航栏颜色才可以自动变色哦
//                .flymeOSStatusBarFontColor(R.color.btn3)  //修改flyme OS状态栏字体颜色
                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//                .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
//                .titleBar(view)    //解决状态栏和布局重叠问题，任选其一
//                .titleBarMarginTop(view)     //解决状态栏和布局重叠问题，任选其一
//                .statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
                .supportActionBar(true) //支持ActionBar使用
                .statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
                .navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
                .barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
//                .removeSupportView(toolbar)  //移除指定view支持
                .removeSupportAllView() //移除全部view支持
                .navigationBarEnable(true)   //是否可以修改导航栏颜色，默认为true
                .navigationBarWithKitkatEnable(true)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
//                .fixMarginAtBottom(true)   //已过时，当xml里使用android:fitsSystemWindows="true"属性时,解决4.4和emui3.1手机底部有时会出现多余空白的问题，默认为false，非必须
                .addTag("tag")  //给以上设置的参数打标记
                .getTag("tag")  //根据tag获得沉浸式参数
                .reset()  //重置所以沉浸式参数
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
//                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
//                    @Override
//                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
//                        LogUtils.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
//                    }
//                })
                .init();  //必须调用方可沉浸式
    }
}
