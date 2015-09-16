package com.xj.self.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.xj.self.R;
import com.xj.self.widget.gl.FloatWindowBigView;
import com.xj.self.widget.gl.FloatWindowSmallView;

/**
 * 基本的实现原理，这种桌面悬浮窗的效果很类似与Widget，但是它比Widget要灵活的多。主要是通过WindowManager这个类来实现的，
 * 调用这个类的addView方法用于添加一个悬浮窗，updateViewLayout方法用于更新悬浮窗的参数，removeView用于移除悬浮窗。其中悬浮窗的参数有必要详细说明一下。

 * WindowManager.LayoutParams这个类用于提供悬浮窗所需的参数，其中有几个经常会用到的变量：

 * type值用于确定悬浮窗的类型，一般设为2002，表示在所有应用程序之上，但在状态栏之下。

 * flags值用于确定悬浮窗的行为，比如说不可聚焦，非模态对话框等等，属性非常多，大家可以查看文档。

 * gravity值用于确定悬浮窗的对齐方式，一般设为左上角对齐，这样当拖动悬浮窗的时候方便计算坐标。

 * x值用于确定悬浮窗的位置，如果要横向移动悬浮窗，就需要改变这个值。

 * y值用于确定悬浮窗的位置，如果要纵向移动悬浮窗，就需要改变这个值。

 * width值用于指定悬浮窗的宽度。

 * height值用于指定悬浮窗的高度。

 * 创建悬浮窗这种窗体需要向用户申请权限才可以的，因此还需要在AndroidManifest.xml中加入<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
 *
 */
public final class MyWindowManager {
    
    /**小悬浮窗view的实例*/
    private static FloatWindowSmallView smallWindow;
    
    /**大悬浮窗View的实例*/
    private static FloatWindowBigView bigWindow;
    
    /**小悬浮窗View的参数*/
    private static LayoutParams smallWindowParams;
    
    /**大悬浮窗的View的参数*/
    private static LayoutParams bigWindowParams;
    
    /**用于控制在屏幕上添加或者移除悬浮窗*/
    private static WindowManager mWindowManager;
    
    /**用于获取手机可用内存*/
    private static ActivityManager mActivityManager;
    
    /**
     * 创建一个小悬浮窗，初始位置为屏幕的右部中间位置
     * @param context
     */
    public static void createSmallWindow(Context context){
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if(smallWindow == null){
            smallWindow = new FloatWindowSmallView(context);
            if(smallWindowParams == null){
                smallWindowParams = new LayoutParams();
                smallWindowParams.type = LayoutParams.TYPE_PHONE;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = FloatWindowSmallView.width;
                smallWindowParams.height = FloatWindowSmallView.height;
                smallWindowParams.x = screenWidth / 2;
                smallWindowParams.y = screenHeight / 2;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
        }
    }
    
    /** 
     * 将小悬浮窗从屏幕上移除。 
     *  
     * @param context 
     *            必须为应用程序的Context. 
     */  
    public static void removeSmallWindow(Context context) {  
        if (smallWindow != null) {  
            WindowManager windowManager = getWindowManager(context);  
            windowManager.removeView(smallWindow);  
            smallWindow = null;  
        }  
    }  
    
    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     * 
     * @param context
     *            必须为应用程序的Context.
     */
    public static void createBigWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(context);
            if (bigWindowParams == null) {
                bigWindowParams = new LayoutParams();
                bigWindowParams.x = screenWidth / 2 - FloatWindowBigView.width / 2;
                bigWindowParams.y = screenHeight / 2 - FloatWindowBigView.height / 2;
                bigWindowParams.type = LayoutParams.TYPE_PHONE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigWindowParams.width = FloatWindowBigView.width;
                bigWindowParams.height = FloatWindowBigView.height;
            }
            windowManager.addView(bigWindow, bigWindowParams);
        }
    }

    /**
     * 将大悬浮窗从屏幕上移除。
     * 
     * @param context
     *            必须为应用程序的Context.
     */
    public static void removeBigWindow(Context context) {
        if (bigWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(bigWindow);
            bigWindow = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     * 
     * @param context
     *            可传入应用程序上下文。
     */
    public static void updateUsedPercent(Context context) {
        if (smallWindow != null) {
            TextView percentView = (TextView) smallWindow.findViewById(R.id.percent);
            percentView.setText(getUsedPercent(context));
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     * 
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return smallWindow != null || bigWindow != null;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     * 
     * @param context
     *            必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     * 
     * @param context
     *            可传入应用程序上下文。
     * @return ActivityManager的实例，用于获取手机可用内存。
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    /**
     * 计算已使用内存的百分比，并返回。
     * 
     * @param context
     *            可传入应用程序上下文。
     * @return 已使用内存的百分比，以字符串形式返回。
     */
    public static String getUsedPercent(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "悬浮窗";
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     * 
     * @param context
     *            可传入应用程序上下文。
     * @return 当前可用内存。
     */
    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

}
