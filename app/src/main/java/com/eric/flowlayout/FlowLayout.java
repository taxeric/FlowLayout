package com.eric.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * create by Eric
 * on 20-8-10
 */
public class FlowLayout extends ViewGroup implements BaseAdapter.OnRefresh {

    //默认margin值
    private static final int DEFAULT_MARGIN = 10;

    private void i(String msg){
        Log.i("EricLog", msg);
    }

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //由于是重写ViewGroup来自定义View，所以注释掉之前测量的值
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取自身建议的宽度
        int myWidth = MeasureSpec.getSize(widthMeasureSpec);
        int myHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        //测量完所有的子View后自身应该有多高
        int measureHeight = 0;
        if (myHeightMode == MeasureSpec.AT_MOST){
            //上一个子View的宽度
            int lastViewWidth = 0;

            //子View的数量
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i ++){

                //获取到子View
                View childView = getChildAt(i);

                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();

                int leftMargin = params.leftMargin == 0 ? dp2px(getContext(), DEFAULT_MARGIN) : params.leftMargin;
                int rightMargin = params.rightMargin == 0 ? dp2px(getContext(), DEFAULT_MARGIN) : params.rightMargin;
                int topMargin = params.topMargin == 0 ? dp2px(getContext(), DEFAULT_MARGIN) : params.topMargin;
                int bottomMargin = params.bottomMargin == 0 ? dp2px(getContext(), DEFAULT_MARGIN) : params.bottomMargin;

                //测量并保存子View的尺寸
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);

                //获取当前子View测量得到的宽度，需要加上左右的Margin值
                int childWidth = childView.getMeasuredWidth() + leftMargin + rightMargin;

                //如果当前子View宽度 + 上一个子View宽度 > 自身的宽度
                if (childWidth + lastViewWidth > myWidth){
                    //此时就要换行了，并且该值也需要加上当前子View的上下Margin值
                    measureHeight += childView.getMeasuredHeight() + topMargin + bottomMargin;
                    //并且换行后第一个子View的宽度就是当前子View的宽度+当前子View的左右Margin值
                    lastViewWidth = childView.getMeasuredWidth() + leftMargin + rightMargin;
                }else {
                    //否则就只加上当前子View宽度就好
                    lastViewWidth += childWidth;
                }
                //确保测量完所有子View后加上最后一行的高度，外带最后一个子View的左右Margin
                //其实这里写的有点问题，得看最后一行谁的高度最大，就加那个子View的高度，
                //这里的情况是所有的子View都用的相同高度和相同Margin值
                if (i == childCount - 1){
                    measureHeight += childView.getMeasuredHeight() + topMargin + bottomMargin;
                }
            }
        }else if (myHeightMode == MeasureSpec.EXACTLY){
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        }

        //最后保存自身应该有的宽度和高度
        setMeasuredDimension(widthMeasureSpec, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //子View的左边，也可以说是子View左上点的X轴坐标，也就是从左边哪里开始布局
        int layoutLeft = l;
        //子View的顶边，也可以说是子View左上点的Y轴坐标，也就是从顶边哪里开始布局
        int layoutHeight = 0;
        //获取ViewGroup测得的宽度
        int parentWidth = getMeasuredWidth();
        //摆放子View
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i ++){

            mCurrentItemIndex ++;

            //获取到子View
            View childView = getChildAt(i);
            setChildClick(childView);

            //获取到子View测量得到的宽高
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int leftMargin = params.leftMargin == 0 ? dp2px(getContext(), DEFAULT_MARGIN) : params.leftMargin;
            int rightMargin = params.rightMargin == 0 ? dp2px(getContext(), DEFAULT_MARGIN) : params.rightMargin;
            int topMargin = params.topMargin == 0 ? dp2px(getContext(), DEFAULT_MARGIN) : params.topMargin;
            int bottomMargin = params.bottomMargin == 0 ? dp2px(getContext(), DEFAULT_MARGIN) : params.bottomMargin;

            //这里依旧判断，是否超出ViewGroup的宽度，但是要加上子View的左右Margin值
            if (childWidth + layoutLeft + leftMargin + rightMargin > parentWidth){
                //如果超出就增加当前子View的高度，外带子View的上下Margin值
                layoutHeight += childHeight + topMargin + bottomMargin;
                //然后把下一行开始的子View左边位置设置为0
                //确保每次换行后都能从x轴坐标为0开始布局
                layoutLeft = 0;
            }

            //布局（子View的左边，子View的顶边，子View的右边 = 左边 + 当前子View的宽度，子View的底边 = 测得的高度 + 当前子View的高度）
            childView.layout(layoutLeft + leftMargin,
                    layoutHeight + topMargin,
                    //这里不需要添加子View右边Margin值，因为下面已经加上了
                    layoutLeft + childWidth + leftMargin,
                    layoutHeight + childHeight + topMargin);
            //每次布局后，需要把子View的左边加上当前子View的宽度，作为下一个子View左边的起始位置
            layoutLeft += childWidth + leftMargin + rightMargin;
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int dp2px(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }

    private OnTagCheckListener checkListener;

    public void setOnCheckListener(OnTagCheckListener checkListener){
        this.checkListener = checkListener;
    }

    public interface OnTagCheckListener{
        void onItemCheck(int position);
    }

    private BaseAdapter adapter;
    private int mCurrentItemIndex = -1;

    //设置点击事件
    private void setChildClick(final View view){
        if (view.getTag() == null){
            view.setTag(mCurrentItemIndex);
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkListener != null){
                    checkListener.onItemCheck((int) view.getTag());
                }
            }
        });
    }

    //设置适配器
    public void setAdapter(BaseAdapter adapter){
        BaseAdapter.addReObj(this);
        this.adapter = adapter;
        refresh();
    }

    @Override
    public void refresh(){
        if (adapter.getCount() != 0){
            mCurrentItemIndex = -1;
            removeAllViews();
            for (int i = 0; i < adapter.getCount(); i ++){
                View view = adapter.getView(i);
                addView(view);
            }
            requestLayout();
        }
    }
}
