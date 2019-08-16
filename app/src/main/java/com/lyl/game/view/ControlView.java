package com.lyl.game.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lyl.game.R;

/**
 * create lyl on 2019-08-16
 * </p>
 */
public class ControlView extends FrameLayout {

    private SteeringWheelView wheelView;
    private Button btnStart, btnSelect;
    private Button btnA, btnB;

    public ControlView(@NonNull Context context) {
        super(context);
        init();
    }

    public ControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_control, this, false);
        wheelView = view.findViewById(R.id.steeringWheelView);
        btnStart = view.findViewById(R.id.btnStart);
        btnSelect = view.findViewById(R.id.btnSelect);
        btnA = view.findViewById(R.id.btnA);
        btnB = view.findViewById(R.id.btnB);
        addView(view);
    }

    public void setDirectionListener(SteeringWheelView.SteeringWheelListener listener) {
        wheelView.notifyInterval(16).listener(listener).interpolator(new OvershootInterpolator());
    }

    public void setActionListener(OnClickListener onClickListener) {
        btnStart.setOnClickListener(onClickListener);
        btnSelect.setOnClickListener(onClickListener);
        btnA.setOnClickListener(onClickListener);
        btnB.setOnClickListener(onClickListener);
    }
}
