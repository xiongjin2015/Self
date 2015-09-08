
package com.xj.self.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.xj.self.R;

/**
 * handler study
 * 1.如何让handler运行在子线程——与子线程相关联
 * 2.don't forget handlerThread.start();
 * @author xj
 */
public class HandlerActivity extends ActionBarActivity implements Handler.Callback {

    private Handler mHandler;
    private final static int MSG_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_test);
    }

    public void onClick(View view) {
        HandlerThread handlerThread = new HandlerThread("handlerTest",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);   
        handlerThread.start(); //if not start，will has NullPointerException
        mHandler = new Handler(handlerThread.getLooper(), this);
        //mHandler = new Handler(handlerThread.getLooper(), new CallbackImpl());
        mHandler.sendEmptyMessage(MSG_ID);
    }

    /**
     * see this method is be invoked in non-UI Thread or Main Thread;
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_ID:
                handle();
                break;

            default:
                break;
        }
        return false;
    }

    private void handle() {
        Log.i("XJ", "Current Thread is:" + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class CallbackImpl implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ID:
                    handle();
                    break;

                default:
                    break;
            }
            return false;
        }

    }

}
