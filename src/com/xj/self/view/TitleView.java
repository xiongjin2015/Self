
package com.xj.self.view;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.haha.self.R;

/**
 * the first custom view example 自定义view的步骤如下： 1.自定义view的属性； 2.在View的构造方法中获取我们自定义的属性 3.重写onMeasure
 * //非必须，但一般都要重写 4.重写onDraw
 * 
 * @author xj
 */
public class TitleView extends TextView {

    /**
     * 文本文字
     */
    private String mTitleText;
    /**
     * 文本颜色
     */
    private int mTitleTextColor;
    /**
     * 文本大小
     */
    private int mTitleTextSize;

    /**
     * 绘制时控制文本绘制的范围,可参见该类源码 定义了left、top、right、bottom四个属性
     */
    private Rect mBound;

    /**
     * The Paint class holds the style and color information about how to draw geometries, text and
     * bitmaps 相当于画笔；
     */
    private Paint mPaint;

    /**
     * 一般重写该构造方法；通用写法 重写了3个构造方法，默认的布局文件调用的是两个参数的构造方法，所以记得让所有的构造调用我们的三个参数的构造，我们在三个参数的构造中获得自定义属性
     * 
     * @param context
     */
    public TitleView(Context context) {
        this(context, null);
    }

    /**
     * 一般重写该构造方法；通用写法
     * 
     * @param context
     * @param attrs
     */
    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 获取自定义的样式的属性 布局文件里定义的控件会调用这个构造方法；同时系统把所有定义的属性都封装进attrs里：包括系统和自定义的属性
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public TitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        /** 获取自定义样式属性：通过样式文件查找出对应的自定义的属性 */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomTitleView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTitleView_titleText:
                    mTitleText = a.getString(attr); // 获取布局文件里定义的文本；
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    mTitleTextColor = a.getColor(attr, Color.BLACK);// 获取布局文件里的定义的文本颜色，默认设置为黑色
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension( // 默认设置为16sp，TypeValue也可以把sp转化为px;
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        a.recycle();// 这句话一定要调用；

        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);// 可以认为画笔画出的文本大小；
        // mPaint.setColor(mTitleTextColor);//设置画笔画出的颜色；
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound); // 设置绘制文本的范围：宽和高；

        /**
         * 设置点击的监听事件 让它随机生成一个4位的随机数， 有兴趣的可以在onDraw中添加一点噪点，然后改写为验证码
         */
        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mTitleText = randomText();
                postInvalidate();
            }
        });
    }

    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }

        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append(i);
        }

        return sb.toString();
    }

    /**
     * 重写之前先了解MeasureSpec的specMode,一共三种类型： EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
     * AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT UNSPECIFIED：表示子布局想要多大就多大，很少使用
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height); // 设置该自定义view的宽和高；
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2,
                getHeight() / 2 + mBound.height() / 2, mPaint);
    }

}
