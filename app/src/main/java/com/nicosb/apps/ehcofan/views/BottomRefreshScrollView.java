package com.nicosb.apps.ehcofan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Nico on 03.07.2016.
 */
public class BottomRefreshScrollView extends ScrollView {
    private ViewOnBottomListener viewOnBottomListener;

    public BottomRefreshScrollView(Context context) {
        super(context);
    }

    public BottomRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomRefreshScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewOnBottomListener(ViewOnBottomListener viewOnBottomListener) {
        this.viewOnBottomListener = viewOnBottomListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        View view = getChildAt(getChildCount() - 1);
        int diff = (view.getBottom()-(getHeight()+getScrollY()));

        if(diff <= 0){
            if(viewOnBottomListener != null){
                viewOnBottomListener.onBottomReached();
            }
        }
    }

    public interface ViewOnBottomListener{
        void onBottomReached();
    }
}
