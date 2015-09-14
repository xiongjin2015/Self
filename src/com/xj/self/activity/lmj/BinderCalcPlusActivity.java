
package com.xj.self.activity.lmj;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xj.self.R;

public class BinderCalcPlusActivity extends Activity {

    private IBinder mPlusBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_calcplus);
    }

    /**
     * 调用bindService(intent, mServiceConnPlus, Context.BIND_AUTO_CREATE);绑定服务时，会调用mServiceConnPlus的
     * onServiceConnected方法得到Binder驱动,实际上即服务器端的CalcPlusService的onBind()方法返回的Binder；即MyBinder
     * @param view
     */
    public void bindService(View view) {
        Intent intent = new Intent("com.haha.aidl.calcplus");
        boolean plus = bindService(intent, mServiceConnPlus, Context.BIND_AUTO_CREATE);
        Log.e("plus", plus + "");
    }

    public void unBindService(View view) {
        unbindService(mServiceConnPlus);
    }

    /**
     * Binder实现进程间通信实际上是同过Linux底层的共享内存（Parcel）来实现的；
     * @param view
     */
    public void mulInvoked(View view) {
        if (mPlusBinder == null) {
            Toast.makeText(this, "未连接服务端或服务端被异常杀死", Toast.LENGTH_SHORT).show();
        } else {
            /**获取共享内存的_data区*/
            android.os.Parcel _data = android.os.Parcel.obtain();
            /**获取共享内存的_reply区*/
            android.os.Parcel _reply = android.os.Parcel.obtain();
            int _result;
            try {
                /**必须和服务器端的DESCRIPTOR一致*/
                _data.writeInterfaceToken("CalcPlusService");
                /**将12写入内存*/
                _data.writeInt(12);
                _data.writeInt(12);
                /**mPlusBinder即服务器端SelfService工程里的CalcPlusServie里的MyBinder
                 * 查看transact()方法的源码实现，调用transact()方法，会调用实现MyBinder类里实现的onTransact()方法；
                 */
                mPlusBinder.transact(0x110, _data, _reply, 0);
                _reply.readException();
                /**服务器端计算完成后，将结果从内存读出，返回结果*/
                _result = _reply.readInt();
                Toast.makeText(this, _result + "", Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }finally{
                _reply.recycle();  
                _data.recycle();  
            }
        }
    }
    
    public void divInvoked(View view){
        if(mPlusBinder == null){
            Toast.makeText(this, "未连接服务端或服务端被异常杀死", Toast.LENGTH_SHORT).show();  
        }else{
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            int _result;
            try{
                _data.writeInterfaceToken("CalcPlusService");
                _data.writeInt(36);
                _data.writeInt(12);
                mPlusBinder.transact(0x111, _data, _reply, 0);
                _reply.readException();
                _result = _reply.readInt();
                Toast.makeText(this, _result + "", Toast.LENGTH_SHORT).show();  
            }catch(RemoteException e){
                e.printStackTrace();
            }finally{
                _reply.recycle();
                _data.recycle();
            }
        }
    }

    private ServiceConnection mServiceConnPlus = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("client", "mServiceConnPlus onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.e("client", " mServiceConnPlus onServiceConnected");
            /**该方法第二参数传入的即服务器端的CalcPlusService类的mBinder对象*/
            mPlusBinder = service;
        }
    };
}
