package org.gongming.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

    private static final int leftMargin = 6;
    private static final int rightMargin = 6;
    private static final int topMargin = 10;
    private static final int bottomMargin = 10;


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int maxHeight = 0;
        int childState = 0;
        int calcHeight = 0;

        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int count = getChildCount();

        for (int index = 0; index < count; index++) {
            final View child = getChildAt(index);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                int calcWidth = child.getMeasuredWidth() + leftMargin + rightMargin;
                calcHeight = Math.max(calcHeight, child.getMeasuredHeight() + topMargin + bottomMargin);
                if (maxWidth + calcWidth > totalWidth) {
                    maxWidth = totalWidth;
                    maxHeight += calcHeight;
                } else {
                    maxWidth += calcWidth;
                }
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        maxHeight += calcHeight;
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int totalWidth = getMeasuredWidth();
        int count = getChildCount();
        int width = 0;
        int height = 0;

        for (int index = 0; index < count; index++) {
            View view = getChildAt(index);
            int calcHeight = view.getMeasuredHeight() + topMargin + bottomMargin;
            int calcWidth = view.getMeasuredWidth() + leftMargin + rightMargin;
            if (width + calcWidth > totalWidth) {
                height += calcHeight;
                width = calcWidth;
            } else {
                width += calcWidth;
                height = Math.max(calcHeight, height);
            }
            view.layout(width - calcWidth + leftMargin, height - calcHeight + topMargin, width - rightMargin, height - bottomMargin);
        }
    }

    public void setOnItemClickListener(OnClickListener listener) {
        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);
            child.setOnClickListener(listener);
        }
    }
}
