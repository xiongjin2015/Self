package com.xj.self.widget.gl;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xj.self.R;
import com.xj.self.manager.MyWindowManager;

public class FloatWindowSmallView extends LinearLayout {
    
    /**记录小悬浮窗的宽度*/
    public static int width;
    
    /**记录小悬浮窗的高度*/
    public static int height;
    
    /**记录系统状态栏的高度*/
    private static int statusBarHeight;
    
    /**用于更新小悬浮窗的位置*/
    private WindowManager windowManager;
    
    /**小悬浮窗的参数*/
    private WindowManager.LayoutParams mParams;
    
    /**记录当前手指位置在屏幕上的横坐标值*/
    private float xInScreen;
    
    /**记录当前手指位置屏幕上的纵坐标值*/
    private float yInScreen;
    
    /**记录手指按下时在屏幕上的横坐标值*/
    private float xDownInScreen;
    
    /**记录手指按下时在屏幕上的纵坐标值*/
    private float yDownInScreen;
    
    /**记录手指按下时在小悬浮窗的View上的横坐标值*/
    private float xInView;
    
    /**记录手指按下时在小悬浮窗的View上的纵坐标值*/
    private float yInView;

    public FloatWindowSmallView(Context context) {
        this(context,null);
    }
    
    public FloatWindowSmallView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FloatWindowSmallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.small_window_layout);
        width = view.getLayoutParams().width;
        height = view.getLayoutParams().height;
        TextView percentView = (TextView)findViewById(R.id.percent);
        percentView.setText(MyWindowManager.getUsedPercent(context));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**手指按下时记录必要的数据，纵坐标的值都需要减去状态烂高度*/
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                /**手指移动的时候更新小悬浮窗的位置*/
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                /**如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了点击事件*/
                if(xDownInScreen == xInScreen && yDownInScreen == yInScreen)
                    openBigWindow();
                break;
            default:
                break;
        }
        return true;
    }
    
    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置
     * @param params
     */
    public void setParams(WindowManager.LayoutParams params){
        mParams = params;
    }
    
    /**
     * 更新小悬浮窗在屏幕中的位置
     */
    private void updateViewPosition(){
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }
    
    /**
     * 打开大悬浮窗，同时关闭小悬浮窗
     */
    private void openBigWindow(){
        MyWindowManager.createBigWindow(getContext());
        MyWindowManager.removeSmallWindow(getContext());
    }
    
    /**
     * 用于获取状态栏的高度
     * @return 返回状态栏高度的像素值
     */
    private int getStatusBarHeight(){
        if(statusBarHeight == 0){
            try{
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = field.getInt(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

}
