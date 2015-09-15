
package com.xj.self.view.lmj623565791;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.xj.self.R;

/**
 * 2048的每个Item
 */
public class Game2048Item extends View {

    /** 该View上的数字 */
    private int number;
    private String numberVal;
    private Paint paint;

    /** 绘制文字的区域 */
    private Rect bound;
    
    private static int[] mImgs = new int[] { R.drawable.d0, R.drawable.d2,
        R.drawable.d3, R.drawable.d4, R.drawable.d5, R.drawable.d6,
        R.drawable.d7, R.drawable.d8, R.drawable.d9, R.drawable.d10,
        R.drawable.d11 };

    private static Bitmap[] mBitmaps = null;
    {
        if (mBitmaps == null)
        {
            mBitmaps = new Bitmap[mImgs.length];
            for (int i = 0; i < mImgs.length; i++)
            {
                mBitmaps[i] = BitmapFactory.decodeResource(getResources(),
                        mImgs[i]);
            }
    
        }
    
    }

    public Game2048Item(Context context) {
        this(context, null);
    }

    public Game2048Item(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Game2048Item(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    public void setNumber(int number) {
        this.number = number;
        numberVal = number + "";
        paint.setTextSize(30.0f);
        bound = new Rect();
        paint.getTextBounds(numberVal, 0, numberVal.length(), bound);
        invalidate();
    }

    public int getNumber() {
        return number;
    }

    /**
     * 数字版
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String bgColor = "";
        switch (number) {
            case 0:
                bgColor = "#CCC0B3";
                break;
            case 2:
                bgColor = "#EEE4DA";
                break;
            case 4:
                bgColor = "#EDE0C8";
                break;
            case 8:
                bgColor = "#F2B179";
                break;
            case 16:
                bgColor = "#F49563";
                break;
            case 32:
                bgColor = "#F5794D";
                break;
            case 64:
                bgColor = "#F55D37";
                break;
            case 128:
                bgColor = "#EEE863";
                break;
            case 256:
                bgColor = "#EDB04D";
                break;
            case 512:
                bgColor = "#ECB04D";
                break;
            case 1024:
                bgColor = "#EB9437";
                break;
            case 2048:
                bgColor = "#EA7821";
                break;
            default:
                bgColor = "#EA7821";
                break;
        }

        paint.setColor(Color.parseColor(bgColor));
        paint.setStyle(Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        if (number != 0) {
            drawText(canvas);
        }
    }

    /**
     * 图片版
     */
//    @Override
//    protected void onDraw(Canvas canvas){
//
//        super.onDraw(canvas);
//        int index = -1;
//        switch (number){
//            case 2:
//                index = 0;
//                break;
//            case 4:
//                index = 1;
//                break;
//            case 8:
//                index = 2;
//                break;
//            case 16:
//                index = 3;
//                break;
//            case 32:
//                index = 4;
//                break;
//            case 64:
//                index = 5;
//                break;
//            case 128:
//                index = 6;
//                break;
//            case 256:
//                index = 7;
//                break;
//            case 512:
//                index = 8;
//                break;
//            case 1024:
//                index = 9;
//                break;
//            case 2048:
//                index = 10;
//                break;
//        }
//        if (number == 0){
//            paint.setColor(Color.parseColor("#EEE4DA"));
//            paint.setStyle(Style.FILL);
//            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
//        }
//        //
//        if (number != 0)
//            canvas.drawBitmap(mBitmaps[index], null, new Rect(0, 0, getWidth(),
//                    getHeight()), null);
//    }

    private void drawText(Canvas canvas) {
        paint.setColor(Color.BLACK);
        float x = (getWidth() - bound.width()) / 2;
        float y = getHeight() / 2 + bound.height() / 2;
        canvas.drawText(numberVal, x, y, paint);
    }
}
