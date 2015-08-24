package com.xj.self.view.lmj623565791;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.haha.self.R;

/**
 * 仿QQ的ListView滑动删除
 * @see blog.csdn.net/lmj623565791
 * @author xj
 *
 */
public class QQListView extends ListView {
    
    private static final String TAG = "QQListView";
    
    /**用户滑动的最小距离*/
    private int touchSlop;
    
    /**是否响应滑动*/
    private boolean isSliding;
    
    /**手指按下的x坐标*/
    private int xDown;
    
    /**手指按下的y坐标*/
    private int yDown;
    
    /**手指移动时的x坐标*/
    private int xMove;
    
    /**手指移动时的y坐标*/
    private int yMove;
    
    private LayoutInflater mInflater;
    
    private Button mDelBtn;
    
    private PopupWindow mPopupWindow;
    private int mPopupWindowHeight;
    private int mPopupWindowWidth;
    
    /**当前手指触摸的位置*/
    private int mCurrentViewPos;
    /**当前手指触摸的View*/
    private View mCurrentView;
    
    /**为删除按钮提供一个回掉监听*/
    private DelButtonClickListener mListener;

    public QQListView(Context context) {
        this(context,null,0);
    }

    public QQListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 必要的一些初始化
     * @param context
     * @param attrs
     * @param defStyle
     */
    public QQListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        mInflater = LayoutInflater.from(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop(); //一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件
        
        View view = mInflater.inflate(R.layout.delete_btn, null);
        mDelBtn = (Button) view.findViewById(R.id.item_btn);
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        
        /**先调用measure，否则拿不到宽和高*/
        mPopupWindow.getContentView().measure(0, 0);
        mPopupWindowHeight = mPopupWindow.getContentView().getMeasuredHeight();
        mPopupWindowWidth = mPopupWindow.getContentView().getMeasuredWidth();
    
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        
        int action = ev.getAction();
        int x = (int)ev.getX();
        int y = (int)ev.getY();
        
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown = x;
                yDown = y;
                
                /**如果当前的popupWindow显示，则直接隐藏，然后屏蔽ListView的touch事件的下传*/
                if(mPopupWindow!=null&&mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                    return false;
                }
                
                /**获取当前手指按下时的item的位置*/
                mCurrentViewPos = pointToPosition(xDown,yDown);//通过x、y坐标找到item在list中的position
                /**获取当前手指按下的item*/
                View view = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
                mCurrentView = view;
                break;
                
            case MotionEvent.ACTION_MOVE:
                xMove = x;
                yMove = y;
                int dx = xMove - xDown;
                int dy = yMove - yDown;
                /**判断是否是从右到左的滑动*/
                if(xMove<xDown&&Math.abs(dx)>touchSlop&&Math.abs(dy)<touchSlop){
                    Log.i(TAG, "touchSlop = "+touchSlop + ", dx = "+ dx + ",dy = "+dy);
                    isSliding = true;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 在dispatchTouchEvent中设置当前是否响应用户的滑动，然后在onTouchEvent中判断是否响应，如果响应则popupWindow以动画的形式展示出来。当然屏幕上如果存在PopupWindow则屏幕ListView的滚动与item的点击，以及从右到左滑动时屏幕item的click事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        
        int action = ev.getAction();
        /**如果是从右到左的滑动才相应*/
        if(isSliding){
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    
                    int[] location = new int[2];
                    /**获得当前item的位置x与y*/
                    mCurrentView.getLocationOnScreen(location);
                    /**设置popupWindow的动画*/
                    mPopupWindow.setAnimationStyle(R.style.popwindow_delete_btn_anim_style);
                    mPopupWindow.update();
                    mPopupWindow.showAtLocation(mCurrentView, Gravity.LEFT|Gravity.TOP, location[0]+mCurrentView.getWidth(), location[1]+mCurrentView.getHeight()/2-mPopupWindowHeight/2);
                    Log.i(TAG,"mPopupWindow.getHeight()="+mPopupWindowHeight);
                    
                    /**设置删除按钮的回调*/
                    mDelBtn.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View v) {
                            if(mListener != null){
                                mListener.onClick(mCurrentViewPos);
                                mPopupWindow.dismiss();
                            }
                            
                        }
                    });
                    break;
                case MotionEvent.ACTION_UP:
                    isSliding = false;
                    break;
                default:
                    break;
            }
            
            /**响应滑动期间屏幕itemClick事件，避免发生冲突*/
            return true;
        }
        return super.onTouchEvent(ev);
    }
    
    
    public void setDelButtonClickListener(DelButtonClickListener listener){
        mListener = listener;
    }
    
    public interface DelButtonClickListener{
        public void onClick(int position);
    }

}
