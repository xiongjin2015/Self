
package com.xj.self.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xj.self.R;

public class ImageView extends View {

    private static final int IMAGE_SCALE_FITXY = 0;

    /** 展示的Image */
    private Bitmap mImage;

    /** Image展示的裁剪方式 */
    private int mImageScale;

    /** 该View上展示的文本域 */
    private String mText;

    /** 该View上显示的文本的颜色 */
    private int mTextColor;

    /** 该View上显示的文本的大小 */
    private float mTextSize;

    private Rect mRect;

    private Paint mPaint;

    private Rect mTextBound;

    /** 该View的宽 */
    private int mWidth;

    /** 该View的高 */
    private int mHeight;

    public ImageView(Context context) {
        this(context, null);
    }

    public ImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 初始化所有自定义类型
     * 
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomImageView, defStyleAttr, 0);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {

            int attr = a.getIndex(i);

            switch (attr) {
                case R.styleable.CustomImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale = a.getInt(attr, 0);
                    break;
                case R.styleable.CustomImageView_titleText:
                    mText = a.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor:
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleTextSize:
                    mTextSize = a.getDimension(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }

        a.recycle();

        mRect = new Rect();// 该view显示的范围/边界
        mPaint = new Paint();
        mTextBound = new Rect();// 该view里的文本域显示的范围/边界

        mPaint.setTextSize(mTextSize);// 设置画笔画文本的大小
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);// 计算了绘制字体需要的范围
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /** 设置宽度 */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) { // match_parent、accurate
            mWidth = specSize;
        } else {
            int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();// 由图片决定的宽
            int desireByTitle = getPaddingLeft() + getPaddingRight() + mTextBound.width(); // 由字体决定的宽
            if (specMode == MeasureSpec.AT_MOST) {// wrap_content
                int desire = Math.max(desireByImg, desireByTitle);
                mWidth = Math.max(desire, specSize);
            }
        }

        /** 设置高度 */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) { // match_parent,accurate
            mHeight = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight()
                    + mTextBound.height();
            if (specMode == MeasureSpec.AT_MOST) { // wrap_content
                mHeight = Math.min(desire, specSize);
            }
        }

        setMeasuredDimension(mWidth, mHeight); // 设置该View的宽和高；使用measue出来的值；

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        /** 边框 */
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.top = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Style.FILL);

        /** 当前设置的宽度小于字体所需的宽度，将字体改为xxx... */
        if (mTextBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mText, paint, mWidth-getPaddingLeft()-getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight, mPaint);
        }else{
            
            /**正常情况，将字体居中*/ 
            canvas.drawText(mText, mWidth/2-mTextBound.width()*1.0f/2, mHeight-getPaddingBottom(), mPaint);
        }
        
        mRect.bottom -= mTextBound.height(); //取消使用掉的块
        
        if(mImageScale == IMAGE_SCALE_FITXY){
            canvas.drawBitmap(mImage, null, mRect,mPaint);
        }else{
            //计算居中的矩形范围
            mRect.left = mWidth / 2 - mImage.getWidth() / 2;
            mRect.right = mWidth /2 + mImage.getWidth() / 2;
            mRect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight() / 2;
            mRect.bottom = (mHeight - mTextBound.height()) / 2 + mImage.getHeight() / 2;
            
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }
    }
}
