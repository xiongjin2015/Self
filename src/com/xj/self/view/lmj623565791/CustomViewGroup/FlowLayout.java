package com.xj.self.view.lmj623565791.CustomViewGroup;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 1.需要指定LayoutParams,目前只需要能够识别margin即可，即使用MarginLayoutParams
 * 2.onMeasure中计算所有childView的宽和高，然后根据childView的宽和高，计算自己的宽和高。
 *   （当然，如果不是wrap_content,直接使用父ViewGroup传入的计算值即可）
 * 3.onLayout中对所有的childView进行布局
 *
 */
public class FlowLayout extends ViewGroup {
    
    private final static String TAG = "FlowLayout";
    
    /**
     * 存储所有的View,按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    
    /**记录每一行的最大高度*/
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 我们只需要支持margin,所有直接使用系统的MarginLayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {

        return new MarginLayoutParams(getContext(),attrs);
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
    
    /**
     * 负责设置子控件的测量模式和大小，根据所有子控件设置自己的宽和高
     * 
     * 首先得到其父容器传入的测量模式和宽高的计算值，然后遍历所有的childView,使用measureChild方法对所有的childView
     * 进行测量。然后根据所有的childView的测量得出的宽和高得到该ViewGroup如果设置为wrap_content时的宽和高。最后根据模式，如果是MeasureSpec.EXACTLY
     * 则直接使用父ViewGroup传入的宽和高，否则设置为自己计算的宽和高
     * 
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        /**获得它的父容器为它设置的测量模式和大小*/
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        
        Log.e(TAG, sizeWidth + "," + sizeHeight);
        
        /**如果是wrap_content情况下，记录宽和高*/
        int width = 0;
        int height = 0;
        
        /**记录每一行的宽度，width不断取最大宽度*/
        int lineWidth = 0;
        /**每一行的高度，累加至height*/
        int lineHeight = 0;
        
        int cCount = getChildCount();
        
        /**遍历每个子元素*/
        for(int i=0;i<cCount;i++){
            View child = getChildAt(i);
            /**测量每一个child的宽和高*/
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            /**得到child的lp*/
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            /**当前子控件实际占据的宽度*/
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            /**当前子控件实际占据的高度*/
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            
            /**如果加入当前child超出最大宽度，则得到目前最大宽度给width，累加height，然后开启新行*/
            if(lineWidth + childWidth > sizeWidth){
                width = Math.max(lineHeight, childWidth);
                lineWidth = childWidth;//重新开启新行，开始记录
                height += lineHeight;//叠加当前高度
                lineHeight = childHeight;//开启记录下一行的高度
            }else{
                /**否则累加值lineWidth,lineheight取最大高度*/
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            
            /**如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较*/
            if(i == cCount-1){
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        
        setMeasuredDimension((modeWidth==MeasureSpec.EXACTLY)?sizeWidth:width,(modeHeight == MeasureSpec.EXACTLY)?sizeHeight:height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
        mAllViews.clear();
        mLineHeight.clear();
        
        int width = getWidth();
        
        int lineWidth = 0;
        int lineHeight = 0;
        //存储每一行所有的childView
        List<View> lineViews  = new ArrayList<View>();
        int cCount = getChildCount();
        //遍历所有的孩子
        for(int i=0;i<cCount;i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            
            //如果已经需要换行
            if(childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width){
                //记录这一行所有的View以及最大高度
                mLineHeight.add(lineHeight);
                //将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add(lineViews);
                lineWidth = 0; //重置行宽
                lineViews = new ArrayList<View>();
            }
            
            /**如果不需要换行，则累加*/
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }
        
        //记录最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
        
        int left = 0;
        int top = 0;
        //得到总行数
        int lineNums = mAllViews.size();
        for(int i=0;i<lineNums;i++){
            //每一行的所有views
            lineViews = mAllViews.get(i);
            //当前行的最大高度
            lineHeight = mLineHeight.get(i);
            
            Log.e(TAG, "第" + i + "行 ：" + lineViews.size() + " , " + lineViews);  
            Log.e(TAG, "第" + i + "行， ：" + lineHeight); 
            
            //变量当前行所有的View
            for(int j=0;j<lineViews.size();j++){
                View child = lineViews.get(j);
                if(child.getVisibility() == View.GONE){
                    continue;
                }
                
                MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
                
                //计算childView的left,top,right,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                
                Log.e(TAG, child + " , l = " + lc + " , t = " + t + " , r ="  
                        + rc + " , b = " + bc);  
                
                child.layout(lc, tc, rc, bc);
                
                left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            }
            
            left = 0;
            top += lineHeight;
        }
    }

}
