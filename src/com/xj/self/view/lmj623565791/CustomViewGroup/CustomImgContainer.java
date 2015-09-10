package com.xj.self.view.lmj623565791.CustomViewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

/**
 * 该容器需求：自定义一个ViewGroup,内部可以传入0到4个childView,分别依次显示在左上角，右上角，左下角和右下角
 * 
 *
 */
public class CustomImgContainer extends ViewGroup {

    public CustomImgContainer(Context context) {
        super(context);
    }

    public CustomImgContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public CustomImgContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }    
    
    /**
     * 决定该ViewGroup的LayoutParams
     * 只需要ViewGroup能够支持margin即可，那么直接使用系统的MarginLayoutParams
     * 重写父类的该方法，返回MarginLayoutParams的实例，这样就为我们的ViewGroup指定了其LayoutParams为MarginLayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * onMeasure中计算childView的测量值以及模式，以及设置自己的宽和高,该方法的两个参数是root布局（父布局）传入的；
     * 计算所有Childview的宽度和高度，然后根据ChildView的计算结果，设置自己的宽和高；
     * 
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        
        /**获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         * 
         * 获取该ViewGroup父容器为其设置的计算模式和尺寸，大多情况下，只要不是wrap_content，父容器都能正确的计算其尺寸。
         * 所以我们自己需要计算如果设置为wrap_content时的宽和高，如何计算呢？那就是通过其childView的宽和高来进行计算。
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        /**计算所有的childView的宽和高
         * 该方法是父类ViewGroup中的方法；该方法会测量每个子view的大小；
         * for循环调用measureChild——>child.measure-->onMeasure(widthMeasureSpec, heightMeasureSpec)
         * 
         * 通过ViewGroup的measureChildren方法为其所有的孩子设置宽和高，此行执行完成后，childView的宽和高都已经正确的计算过了
         */
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        
        /**记录如果是wrap_content，设置的宽和高*/
        int width = 0;
        int height = 0;
        
        int cCount = getChildCount();
        
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        
        /**用于计算左边两个childView的高度*/
        int leftHeight = 0;
        /**用于计算右边两个childView的高度，最终高度取两者之间的大值*/
        int rightHeight = 0;
        
        /**用于计算上边两个childView的宽度*/
        int topWidth = 0;
        /**用于计算下面两个childView的宽度，最终宽度取两者之间大值*/
        int bottomWidth = 0;
        
        /**
         * 根据childView计算出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是wrap_content时
         * 
         */
        for(int i=0;i<cCount;i++){
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            
            /**上边的两个childView*/
            if(i==0 || i ==1){
                topWidth +=  cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            
            if(i == 2 || i == 3){
                bottomWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            
            if(i == 0 || i ==2){
                leftHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
            
            if(i == 1 || i ==3){
                rightHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
            
        }
        
        width = Math.max(topWidth, bottomWidth);
        height = Math.max(leftHeight, rightHeight);
        
        /**如果是wrap_content设置为我们计算的值，否则：直接设置为父容器计算的值*/
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth  
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight  
                : height);
    }
    
    /**
     * abstract method in ViewGroup
     * 对其所有的childView进行定位（设置childView的绘制区域）
     * 
     *  Overrides: onLayout(...) in ViewGroup
     *  Parameters:
     *   changed This is a new size or position for this view
     *     l Left position, relative to parent
     *     t Top position, relative to parent
     *     r Right position, relative to parent
     *     b Bottom position, relative to parent
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
        /**
         * 遍历所有的childView，根据childView的宽和高以及margin，然后分别将0，1，2，3位置的childView依次设置到左上、右上、左下、右下的位置。
         *  如果是第一个View(index=0) ：则childView.layout(cl, ct, cr, cb); cl为childView的leftMargin , ct 为topMargin , cr 为cl+ cWidth , cb为 ct + cHeight
         *  如果是第二个View(index=1) ：则childView.layout(cl, ct, cr, cb); 
         *  cl为getWidth() - cWidth - cParams.leftMargin- cParams.rightMargin;
         *  ct 为topMargin , cr 为cl+ cWidth , cb为 ct + cHeight
         *  剩下两个类似~
         */
        
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        
        /**遍历所有childView，根据其宽和高，以及margin进行布局*/
        for(int i=0; i<cCount; i++){
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            
            int cl=0, ct=0, cr=0, cb=0;
            
            switch (i) {
                case 0:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin;
                    break;
                case 1:
                    cl = getWidth() - cWidth - cParams.rightMargin;
                    ct = cParams.topMargin;
                    break;
                case 2:
                    cl = cParams.leftMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - cWidth - cParams.leftMargin - cParams.rightMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                   break; 
                default:
                    break;
            }
            
            cr = cl + cWidth;
            cb = cHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }

    }
    
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams(){

        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p){
        return new MarginLayoutParams(p);
    }

    /*
     * if (heightMode == MeasureSpec.UNSPECIFIED)
        {
            int tmpHeight = 0 ;
            LayoutParams lp = getLayoutParams();
            if (lp.height == LayoutParams.MATCH_PARENT)
            {
                Rect outRect = new Rect();
                getWindowVisibleDisplayFrame(outRect);
                tmpHeight = outRect.height();
            }else
            {
                tmpHeight = getLayoutParams().height ; 
            }
            height = Math.max(height, tmpHeight);

        }
     */

}
