package com.smart.kitchen.smartkitchen.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import com.smart.kitchen.smartkitchen.R;

public class SwipeRefreshView extends SwipeRefreshLayout {
    private boolean isLoading;
    private boolean isNoMore = false;
    private float mDownY;
    private final View mFooterView;
    private ListView mListView;
    private OnLoadListener mOnLoadListener;
    private final int mScaledTouchSlop;
    private float mUpY;

    public interface OnLoadListener {
        void onLoad();
    }

    public SwipeRefreshView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFooterView = View.inflate(context, R.layout.view_footer, null);
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        System.out.println("====" + this.mScaledTouchSlop);
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mListView == null && getChildCount() > 0 && (getChildAt(0) instanceof ListView)) {
            this.mListView = (ListView) getChildAt(0);
            setListViewOnScroll();
        }
    }

    public void setNoMore(boolean z) {
        this.isNoMore = z;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.mDownY = motionEvent.getY();
                break;
            case 1:
                this.mUpY = getY();
                break;
            case 2:
                if (canLoadMore()) {
                    loadData();
                    break;
                }
                break;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    private boolean canLoadMore() {
        boolean z = true;
        if (this.isNoMore) {
            return false;
        }
        boolean z2;
        boolean z3;
        boolean z4 = this.mDownY - this.mUpY >= ((float) this.mScaledTouchSlop);
        if (z4) {
            System.out.println("是上拉状态");
        }
        if (this.mListView == null || this.mListView.getAdapter() == null) {
            z2 = false;
        } else {
            if (this.mListView.getLastVisiblePosition() == this.mListView.getAdapter().getCount() - 1) {
                z3 = true;
            } else {
                z3 = false;
            }
            z2 = z3;
        }
        if (z2) {
            System.out.println("是最后一个条目");
        }
        if (this.isLoading) {
            z3 = false;
        } else {
            z3 = true;
        }
        if (z3) {
            System.out.println("不是正在加载状态");
        }
        if (!(z4 && z2 && z3)) {
            z = false;
        }
        return z;
    }

    private void loadData() {
        System.out.println("加载数据...");
        if (this.mOnLoadListener != null) {
            setLoading(true);
            this.mOnLoadListener.onLoad();
        }
    }

    public void setLoading(boolean z) {
        this.isLoading = z;
        if (this.isLoading) {
            this.mListView.addFooterView(this.mFooterView);
            return;
        }
        this.mListView.removeFooterView(this.mFooterView);
        this.mDownY = 0.0f;
        this.mUpY = 0.0f;
    }

    private void setListViewOnScroll() {
        this.mListView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (SwipeRefreshView.this.canLoadMore()) {
                    SwipeRefreshView.this.loadData();
                }
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }
        });
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }
}
