
package com.xj.self.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.xj.self.R;
import com.xj.self.widget.LoadingDialog;

/**
 * 用于研究和学习Dialog 1.不依赖activity的dialaog实现方式一： 2.通过WindowManager实现
 * 
 * @author xj
 */
public class DialogActivity extends ActionBarActivity {

    private LoadingDialog mDialog;
    private WindowManager wm;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test1:
                showProgressDialog(R.string.dialog_msg);
                break;
            case R.id.btn_test2:
                showDiloagByWindow();
                break;
            default:
                break;
        }
    }

    private void showProgressDialog(final int msg) {

        if (mDialog == null) {
            mDialog = createLoadingDialog();
        }
        mDialog.setMessage(msg);
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        // finish();
        mDialog.show();

    }

    private LoadingDialog createLoadingDialog() {
        LoadingDialog loadingDialog = new LoadingDialog(getApplication(), R.style.Dialog_Fullscreen);
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                // onProgressCancelled();
            }
        });

        return loadingDialog;
    }

    private void showDiloagByWindow() {
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams param = new WindowManager.LayoutParams();
        param.height = -1;
        param.width = -1;
        param.format = -1;
        param.flags = LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        param.type = LayoutParams.TYPE_SYSTEM_OVERLAY;
        view = LayoutInflater.from(this).inflate(R.layout.loadingdialog, null);
        wm.addView(view, param);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                wm.removeView(view);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                wm.removeView(view);
                return true;
            }
        } catch (Exception e) {

        }

        return super.onKeyDown(keyCode, event);
    }

}
