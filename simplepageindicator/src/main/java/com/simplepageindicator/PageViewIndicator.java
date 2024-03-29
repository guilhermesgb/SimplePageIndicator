package com.simplepageindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
/**
 * Created by renan on 12/11/15.
 */
public class PageViewIndicator extends LinearLayout {

    private int currentPage = 0;
    private int pageQuantity = 1;
    private int primaryColor = 0;
    private int secondaryColor = 0;
    private int indicadorWidth = 0;
    private int indicadorHeight = 0;
    private Context mContext;
    private boolean overflowMode = false;

    public PageViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PageViewIndicator, 0, 0);
        try {
            //get attrs
            this.primaryColor = a.getInteger(R.styleable.PageViewIndicator_indicatorPrimaryColor, 0);
            this.secondaryColor = a.getInteger(R.styleable.PageViewIndicator_indicatorSecondaryColor, 0);
            this.indicadorWidth = a.getDimensionPixelSize(R.styleable.PageViewIndicator_indicatorWidth, 5);
            this.indicadorHeight = a.getDimensionPixelSize(R.styleable.PageViewIndicator_indicatorHeight, 5);
            this.pageQuantity = a.getInteger(R.styleable.PageViewIndicator_pageQuantity, 1);
            this.overflowMode = a.getBoolean(R.styleable.PageViewIndicator_overflowMode, false);
            layoutWidthAdjust();
        } finally {
            a.recycle();
        }
        setPageQuantity(pageQuantity);
    }

    private void layoutWidthAdjust() {
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        if ((indicadorWidth * pageQuantity) >= width) {
            this.indicadorWidth = width / pageQuantity;
            this.indicadorHeight = width / pageQuantity;
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void nextPage() {
        clearPageIndicator();
        if (currentPage < pageQuantity - 1) currentPage++;
        else if (overflowMode) currentPage = 0;
        setCurrentPage(currentPage);
    }

    public void previousPage() {
        clearPageIndicator();
        if (currentPage > 0) currentPage --;
        else if (overflowMode) currentPage = pageQuantity - 1;
        setCurrentPage(currentPage);
    }

    public void setCurrentPage(int currentPage) {
        clearPageIndicator();
        if (currentPage >= pageQuantity || currentPage < 0) return;
        ViewGroup mViewGroup = this;
        this.currentPage = currentPage;
        Indicator indicator = (Indicator) mViewGroup.getChildAt(currentPage);
        indicator.setCircleColor(primaryColor);
    }

    public void clearPageIndicator() {
        ViewGroup mViewGroup = this;
        Indicator indicator = (Indicator) mViewGroup.getChildAt(this.currentPage);
        indicator.setCircleColor(secondaryColor);
    }

    public void setPageQuantity(int quantity) {
        ViewGroup mViewGroup = this;
        mViewGroup.removeAllViews();
        this.pageQuantity = quantity;
        layoutWidthAdjust();
        for (int i = 0; i < this.pageQuantity; i++) {
            Indicator indicator = new Indicator(getContext());
            indicator.setLayoutParams(new LayoutParams(indicadorWidth, indicadorHeight));
            indicator.setCircleColor(secondaryColor);
            mViewGroup.addView(indicator, i);
        }
        setCurrentPage(getCurrentPage());
    }

}
