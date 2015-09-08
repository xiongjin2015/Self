package com.xj.self.view.lmj623565791;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xj.self.R;

/***
 * 自定义View，实现圆角，圆形等效果:
 * 具体效果图间：assests/下的自定义圆角View.jpeg:第一个是原图，第二个是圆形效果，第三第四设置了不同的圆角大小。
 *
 */
public class CustomRoundImageView extends View {
    
    /**TYPE_CIRCLE / TYPE_ROUND*/
    private int type;
    private final static int TYPE_CIRCLE = 0;
    private final static int TYPE_ROUND = 1;
    
    /**源图片*/
    private Bitmap mSrc;
    
    /**圆角的大小*/
    private int mRadius;
    
    /**控件的宽度*/
    private int mWidth;
    
    /**控件的高度*/
    private int mHeight;

    public CustomRoundImageView(Context context) {
        this(context,null);
    }

    public CustomRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    
    /**
     * 初始化一些自定义参数
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomRoundImageView, defStyleAttr, 0);
        
        int n = a.getIndexCount();
        for(int i = 0;i<n;i++){
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomRoundImageView_src:
                    mSrc = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomRoundImageView_type:
                    type = a.getInt(attr,0);//默认为Circle
                    break;
                case R.styleable.CustomRoundImageView_borderRadius:
                    mRadius= a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f,  
                            getResources().getDisplayMetrics()));// 默认为10DP 
                    break;
                default:
                    break;
            }
        }
        a.recycle();
    }

    /**
     * 计算控件的高度和宽度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        /**设置高度*/
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        
        if(specMode == MeasureSpec.EXACTLY){//match_parent,accurate
            mWidth = specSize;
        }else{
            //由图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight() + mSrc.getWidth();
            if(specMode == MeasureSpec.AT_MOST){//wrap_content
                mWidth = Math.min(desireByImg, specSize);
            }else{
                mWidth = desireByImg;
            }
        }
        
        /**设置高度*/
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){//match_parent,accurate
            mHeight = specSize;
        }else{
            int desire = getPaddingTop() + getPaddingBottom() + mSrc.getHeight();
            if(specMode == MeasureSpec.AT_MOST){//wrap_content
                mHeight = Math.min(desire, specSize);
            }else{
                mHeight = desire;
            }
        }
        
        setMeasuredDimension(mWidth, mHeight);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        switch (type) {
            case TYPE_CIRCLE: //如果是TYPE_CIRCLE绘制圆形
                int min = Math.min(mWidth, mHeight);
                /**长度如果不一致，按小的值进行压缩*/
                mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
                canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
                break;
            case TYPE_ROUND:
                canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
                break;
            default:
                break;
        }
    }

    /**
     * 根据原图和变长绘制圆形图片
     * @param src
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap src, int min) {
        /**产生画笔*/
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        /**产生一个宽和高皆为min的空白的画纸，或者说相当于一个空白图片*/
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        /**产生一个同样大小的画布*/
        Canvas canvas = new Canvas(target);
        /**首先绘制圆形，在画布上的target画圆:前两个参数是圆心坐标，第三个是半径大小，第四个参数是画笔*/
        canvas.drawCircle(min/2, min/2, min/2, paint);
        /**使用SRC_IN模式，参照assest/相关说明.docx的1.关于”自定义圆角View.jpeg”的实现的说明*/
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        
        /**绘制图片:在canvas的target上绘制和src一样的图片*/
        canvas.drawBitmap(src, 0, 0, paint);
        return target;
    }
    
    /**
     * 根据原图添加圆角
     * @param src
     * @return
     */
    private Bitmap createRoundConerImage(Bitmap src) {
        /**产生画笔*/
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        /**产生目标位图*/
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
        /**产生画布*/
        Canvas canvas = new Canvas(target);
        RectF rectF = new RectF(0,0,src.getWidth(),src.getHeight());
        /**画圆角矩形,第一个参数指定画的矩形的大小（范围），第二第三个参数分别为x和y方向的圆角角度*/
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, 0, 0, paint);
        return target;
    }
}
