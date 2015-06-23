
package com.xj.self.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.haha.self.R;


/**
 * 自定义动画加载对话框实现
 * 同时实现不依赖activity的系统级弹出dialog；
 * @author xj
 *
 */
public class LoadingDialog extends Dialog {

    private TextView mTvMessage;
    private String mMsg;
    private ImageView mLoadingDots;
    private Context mContext;
    AnimationDrawable marqueeDots;

    private Runnable startAnimation = new Runnable() {
        @Override
        public void run() {
            marqueeDots.stop();
            marqueeDots.start();
        }
    };

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.loadingdialog);

        mTvMessage = (TextView) findViewById(R.id.loading_des);
        mLoadingDots = (ImageView) findViewById(R.id.loading_dots);
        marqueeDots = (AnimationDrawable) mLoadingDots.getDrawable();
    }

    private void startAnim() {
        mLoadingDots.removeCallbacks(startAnimation);
        mLoadingDots.post(startAnimation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnim();
        updateMessage();
    }

    public void setMessage(int resid) {
        mMsg = mContext.getString(resid);
        if (isShowing()) {
            updateMessage();
        }
    }

    private void updateMessage() {
        mTvMessage.setText(mMsg);
    }

    public void setMessage(String msg) {
        mMsg = msg;
        if (isShowing()) {
            updateMessage();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
