package com.xj.self.view.lmj623565791;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * 2048的每个Item
 *
 */
public class Game2048Item extends View {
    
    /**该View上的数字*/
    private int number;
    private String numberVal;
    private Paint paint;
    
    /**绘制文字的区域*/
    private Rect bound;

    public Game2048Item(Context context) {
        this(context,null);
    }
    
    public Game2048Item(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Game2048Item(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    public void setNumber(int number){
        this.number = number;
        numberVal = number+"";
        paint.setTextSize(30.0f);
        bound = new Rect();
        paint.getTextBounds(numberVal, 0, numberVal.length(), bound);
        invalidate();
    }
    
    public int getNumber(){
        return number;
    }
    
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
        if(number != 0){
            drawText(canvas);
        }
    }
    
    private void drawText(Canvas canvas){
        paint.setColor(Color.BLACK);
        float x = (getWidth() - bound.width())/2;
        float y = getHeight() / 2 + bound.height()/2;
        canvas.drawText(numberVal, x, y, paint);
    }
}
