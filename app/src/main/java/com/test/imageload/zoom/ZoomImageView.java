package com.test.imageload.zoom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * 手势缩放
 * 1) 单指：
 * 1 点击关闭当前页面
 * 1.1 没有缩放的时候，move 过程，图片位移；
 * 1.2 缩放的情况下，move过程，拖动图片
 * <p>
 * 2）两指，根据手指放大、缩放
 */
public class ZoomImageView extends AppCompatImageView implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    //最大缩放比例
    private final float SCALE_MAX = 3.0f;
    //最小缩放比例
    private final float SCALE_MIN = 3.0f;

    /**
     * 是否可以拖动
     */
    private boolean canDrag;


    /**
     * 用于双手点击检测
     */
    private GestureDetector gestureDetector;

    /**
     * 缩放的手势检测
     */
    private ScaleGestureDetector scaleGestureDetector;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                return super.onDoubleTap(e);
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        setOnTouchListener(this);
    }


    //开始缩放手势
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }


    /**
     * 对图片进行缩放的控制，
     *
     * @param detector
     * @return
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }


    //缩放结束
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //判断手指触摸个数
        int pointerCount = event.getPointerCount();

        int actionIndex = event.getActionIndex();
        if (actionIndex == 1) {

        }

        return false;
    }

    /**
     * 根据图片的宽和高
     */
    @Override
    public void onGlobalLayout() {

    }
}
