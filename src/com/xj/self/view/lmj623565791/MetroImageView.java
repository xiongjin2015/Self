
package com.xj.self.view.lmj623565791;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 实现柔和点击效果的imageView：自定义点击动画，先放大在缩回；
 * 
 * 代码不算复杂，主要就是对onTouchEvent的Action_Down和Action_Up的监听，
 * 然后通过Handler结合matrix完成缩放的效果。这里简单说一个mScaleHandler里面代码的逻辑，
 * 当检测到ACTION_DOWN事件，会判断当前缩放是否完成，如果完成了则添加缩小的效果，如果没有，
 * 则一直检测。ACTION_UP也是同样的过程。缩放的梯度参见src/xj/algorithm/GradientMirrorData.java
 * 有人会觉得使用Handler比较麻烦，这里一直使用Handler.sendMsg的原因是，利用了这个消息队列，
 * 队列先进先出，保证动画效果的流畅。因为ACTION_DOWN_与ACTION_UP一瞬点完成的，其实动画还在进行。
 * 如果你在onTouchEvent中用while集合sleep完成动画，会出现卡死，监听不到Up事件等问题。
 * 
 * @author xj
 */
public class MetroImageView extends ImageView {

    private final static String TAG = "MetroImageView";

    private final static int SCALE_REDUCE_INIT = 0;
    private final static int SCALING = 1;
    private final static int SCALE_ADD_INIT = 6;

    /** 控件的宽 */
    private int mWidth;

    /** 控件的高 */
    private int mHeight;

    /** 控件宽的1/2 */
    private int mCenterWidth;

    /** 控件高的1/2 */
    private int mCenterHeight;

    /** 设置一个缩放量 */
    private float mMinScale = 0.85f;

    /** 缩放是否结束 */
    private boolean isFinish = true;

    public MetroImageView(Context context) {
        this(context, null);
    }

    public MetroImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MetroImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 必要的初始化
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            mHeight = getHeight() - getPaddingTop() - getPaddingBottom();

            mCenterWidth = mWidth / 2;
            mCenterHeight = mHeight / 2;

            Drawable drawable = getDrawable();
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bd.setAntiAlias(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // float X = event.getX();
                // float Y = event.getY();
                mScaleHandler.sendEmptyMessage(SCALE_REDUCE_INIT);
                break;
            case MotionEvent.ACTION_UP:
                mScaleHandler.sendEmptyMessage(SCALE_ADD_INIT);
                break;
            default:
                break;
        }
        return true;
    }

    /** 控制缩放的Handler */
    private Handler mScaleHandler = new Handler() {

        private Matrix matrix = new Matrix();// 图形变换矩阵
        private int count = 0;
        private float s;

        /** 是否已经调用了点击事件 */
        private boolean isClicked;

        @Override
        public void handleMessage(Message msg) {

            matrix.set(getImageMatrix());
            switch (msg.what) {
                case SCALE_REDUCE_INIT:
                    if (!isFinish) {
                        mScaleHandler.sendEmptyMessage(SCALE_REDUCE_INIT);
                    } else {
                        isFinish = false;
                        count = 0;
                        s = (float) Math.sqrt(Math.sqrt(mMinScale));
                        beginScale(matrix, s);
                        mScaleHandler.sendEmptyMessage(SCALING);
                    }
                    break;
                case SCALING:
                    beginScale(matrix, s);
                    if (count < 4) {
                        mScaleHandler.sendEmptyMessage(SCALING);
                    } else {
                        isFinish = true;
                        if (MetroImageView.this.mOnViewClickListener != null && !isClicked) {
                            isClicked = true;
                            MetroImageView.this.mOnViewClickListener
                                    .onViewClick(MetroImageView.this);
                        } else {
                            isClicked = false;
                        }
                    }
                    count++;
                    break;
                case SCALE_ADD_INIT:
                    if (!isFinish) {
                        mScaleHandler.sendEmptyMessage(SCALE_ADD_INIT);
                    } else {
                        isFinish = false;
                        count = 0;
                        s = (float) Math.sqrt(Math.sqrt(1.0f / mMinScale));
                        beginScale(matrix, s);
                        mScaleHandler.sendEmptyMessage(SCALING);
                    }
                    break;
                default:
                    break;
            }
        }

    };

    protected void sleep(int i)
    {
        try{
            Thread.sleep(i);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * 缩放
     * 
     * @param matrix
     * @param scale
     */
    private synchronized void beginScale(Matrix matrix, float scale) {
        matrix.postScale(scale, scale, mCenterWidth, mCenterHeight);
        setImageMatrix(matrix);
    }

    private OnViewClickListener mOnViewClickListener;

    /**
     * 利用提供的回调接口注册了点击事件。这里说明一下，现在为ImageView设置OnClickLIstener是没有作用的，
     * 因为自定义的ImageView的onTouchEvent直接返回了true,不会往下执行click事件，
     * 如果你希望通过OnClickLIstener进行注册，你可以把ontouchevent里面返回值改成super.ontouchevent(event)，
     * 并且需要将ImageView的clickable设置为true。这些都是Ontouch事件的传播机制，不了解的google下，还是很有必要的。
     * @param onViewClickListener
     */
    public void setOnClickListener(OnViewClickListener onViewClickListener) {
        this.mOnViewClickListener = onViewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(MetroImageView view);
    }

}
