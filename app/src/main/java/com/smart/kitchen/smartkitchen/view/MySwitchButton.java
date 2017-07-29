package com.smart.kitchen.smartkitchen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.utils.SingletonSB;

public class MySwitchButton extends LinearLayout implements OnClickListener {
    private boolean IS_OPEN;
    private boolean is_moving;
    OnChangeListener on;
    private RelativeLayout rl_container;
    private TextView tv_buttom;
    private View v_swicther;

    public interface OnChangeListener {
        void onChange(boolean z);
    }

    public MySwitchButton(Context context) {
        this(context, null);
    }

    public MySwitchButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MySwitchButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.IS_OPEN = false;
        this.is_moving = false;
        this.IS_OPEN = false;
        this.is_moving = false;
        View.inflate(context, R.layout.swicther, this);
        this.tv_buttom = (TextView) findViewById(R.id.tv_buttom);
        this.rl_container = (RelativeLayout) findViewById(R.id.rl_container);
        this.v_swicther = findViewById(R.id.v_swicther);
        this.rl_container.setOnClickListener(this);
        setText(false);
    }

    public boolean isChecked() {
        return this.IS_OPEN;
    }

    public void setText(boolean z) {
        this.IS_OPEN = z;
        SingletonSB.getInstance().setChecked(this.IS_OPEN);
        if (this.IS_OPEN) {
            this.tv_buttom.setText("开");
            this.tv_buttom.setBackgroundResource(R.drawable.circle_white);
            this.v_swicther.setBackgroundResource(R.drawable.corner_white);
        } else {
            this.tv_buttom.setText("关");
            this.tv_buttom.setBackgroundResource(R.drawable.circle_gray);
            this.v_swicther.setBackgroundResource(R.drawable.corner_gray);
        }
        if (this.on != null) {
            this.on.onChange(this.IS_OPEN);
        }
    }

    public void onClick(View view) {
        if (!this.is_moving) {
            if (this.IS_OPEN) {
                startAnimResove();
            } else {
                startAnim();
            }
        }
    }

    public void startAnim() {
        Animation translateAnimation = new TranslateAnimation(0.0f, 30.0f, 0.0f, 0.0f);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
                MySwitchButton.this.is_moving = true;
            }

            public void onAnimationEnd(Animation animation) {
                MySwitchButton.this.is_moving = false;
                MySwitchButton.this.setText(true);
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.tv_buttom.startAnimation(translateAnimation);
    }

    public void startAnimResove() {
        Animation translateAnimation = new TranslateAnimation(30.0f, 0.0f, 0.0f, 0.0f);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
                MySwitchButton.this.is_moving = true;
            }

            public void onAnimationEnd(Animation animation) {
                MySwitchButton.this.is_moving = false;
                MySwitchButton.this.setText(false);
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.tv_buttom.startAnimation(translateAnimation);
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.on = onChangeListener;
    }
}
