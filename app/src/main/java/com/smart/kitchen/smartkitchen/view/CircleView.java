package com.smart.kitchen.smartkitchen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.smart.kitchen.smartkitchen.R;

public class CircleView extends View {
    private int bg_color;
    private boolean isFILL;
    private boolean is_add;
    private int mHeight;
    private Paint mPaint;
    private int mWidth;

    public CircleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isFILL = true;
        this.bg_color = Color.parseColor("#FF6347");
        this.is_add = false;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CircleView, i, 0);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i2 = 0; i2 < indexCount; i2++) {
            int index = obtainStyledAttributes.getIndex(i2);
            switch (index) {
                case 0:
                    obtainStyledAttributes.getColor(index, Color.parseColor("#FF6347"));
                    break;
                case 1:
                    this.is_add = obtainStyledAttributes.getBoolean(index, false);
                    break;
                case 2:
                    this.isFILL = obtainStyledAttributes.getBoolean(index, true);
                    break;
                default:
                    break;
            }
        }
        obtainStyledAttributes.recycle();
        init(context);
    }

    public CircleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleView(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        this.mPaint = new Paint();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        canvas.drawCircle((float) (this.mWidth / 2), (float) (this.mWidth / 2), (float) (this.mWidth / 2), this.mPaint);
        if (this.isFILL) {
            this.mPaint.setColor(-1);
        }
        if (this.is_add) {
            canvas.drawLine(8.0f, (float) (this.mWidth / 2), (float) (this.mWidth - 8), (float) (this.mWidth / 2), this.mPaint);
            canvas.drawLine((float) (this.mWidth / 2), 8.0f, (float) (this.mWidth / 2), (float) (this.mWidth - 8), this.mPaint);
            return;
        }
        canvas.drawLine(4.0f, (float) (this.mWidth / 2), (float) (this.mWidth - 4), (float) (this.mWidth / 2), this.mPaint);
    }

    private void initPaint() {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(3.0f);
        this.mPaint.setStyle(Style.STROKE);
        if (this.isFILL) {
            this.mPaint.setStyle(Style.FILL);
        }
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setColor(this.bg_color);
    }

    private int dipToPx(int i) {
        return (int) ((((float) (i >= 0 ? 1 : -1)) * 0.5f) + (((float) i) * getContext().getResources().getDisplayMetrics().density));
    }

    protected void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = MeasureSpec.getSize(i2);
        if (mode == 1073741824 || mode == ExploreByTouchHelper.INVALID_ID) {
            this.mWidth = size;
        } else {
            this.mWidth = 0;
        }
        if (mode2 == ExploreByTouchHelper.INVALID_ID || mode2 == 0) {
            this.mHeight = dipToPx(15);
        } else {
            this.mHeight = size2;
        }
        setMeasuredDimension(this.mWidth, this.mHeight);
    }
}
