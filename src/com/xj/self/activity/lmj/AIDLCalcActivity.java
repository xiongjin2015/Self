
package com.xj.self.activity.lmj;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.haha.selfserver.aidl.ICalcAIDL;
import com.xj.self.R;

/**
 * 记得服务端生成R文件下生成的.java带包一起复制到客户端
 * 或者把ICalcAIDL.aidl带包一起拷贝到客户端；
 * 
 * 我们首先点击BindService按钮，查看log

[html] view plaincopy在CODE上查看代码片派生到我的代码片
08-09 22:56:38.959: E/server(29692): onCreate  
08-09 22:56:38.959: E/server(29692): onBind  
08-09 22:56:38.959: E/client(29477): onServiceConnected  
可以看到，点击BindService之后，服务端执行了onCreate和onBind的方法，并且客户端执行了onServiceConnected方法，标明服务器与客户端已经联通
然后点击12+12，50-12可以成功的调用服务端的代码并返回正确的结果

下面我们再点击unBindService

[html] view plaincopy在CODE上查看代码片派生到我的代码片
08-09 22:59:25.567: E/server(29692): onUnbind  
08-09 22:59:25.567: E/server(29692): onDestroy  
由于我们当前只有一个客户端绑定了此Service，所以Service调用了onUnbind和onDestory
然后我们继续点击12+12，50-12，通过上图可以看到，依然可以正确执行，也就是说即使onUnbind被调用，连接也是不会断开的，那么什么时候会端口呢？

即当服务端被异常终止的时候，比如我们现在在手机的正在执行的程序中找到该服务：



点击停止，此时查看log

[html] view plaincopy在CODE上查看代码片派生到我的代码片
08-09 23:04:21.433: E/client(30146): onServiceDisconnected  
可以看到调用了onServiceDisconnected方法，此时连接被断开，现在点击12+12，50-12的按钮，则会弹出Toast服务端断开的提示。
 *
 */
public class AIDLCalcActivity extends Activity {
    
    private final static String TAG = "Client";
    
    private ICalcAIDL mCalcAidl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_calc);
    }

    /**
     * 点击BindService按钮时调用
     * @param view
     */
    public void bindService(View view) {
        Intent intent = new Intent("com.haha.aidl.calc");
       startService(intent);
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
    }
    
    /**
     * 点击unBindService按钮时调用
     * @param view
     */
    public void unBindService(View view){
        unbindService(mServiceConn);
    }
    
    /**
     * 点击12+12按钮时调用
     * @param view
     */
    public void addInvoked(View view) throws Exception{
        if(mCalcAidl != null){
            int addResult = mCalcAidl.add(12, 12);
            Toast.makeText(this, addResult+"", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"服务器被异常杀死，请重新绑定服务端",Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 点击50-12按钮时调用
     * @param view
     */
    public void minInvoked(View view) throws Exception{
        if(mCalcAidl != null ){
            int minResult = mCalcAidl.min(50, 12);
            Toast.makeText(this, minResult+"", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"服务器被异常杀死，请重新绑定服务端",Toast.LENGTH_SHORT).show(); 
        }
    }
    
    
    private ServiceConnection mServiceConn = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
            mCalcAidl = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG,"onServiceConnected");
            mCalcAidl = ICalcAIDL.Stub.asInterface(service);
        }
    };

}
