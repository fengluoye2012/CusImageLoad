package com.test.imageload.picture.zoom;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;


/**
 * 图片缩放：根据缩放比例，通过Matrix 类对图片进行缩放
 * <p>
 * 1) 单指：
 * 1 点击关闭当前页面
 * 1.1 没有缩放的时候，move 过程，图片位移；
 * 1.2 缩放的情况下，move过程，拖动图片
 * <p>
 * 2）两指，根据手指放大、缩放
 * <p>
 * ImageView 的各种ScaleType 的实现方式，是否也通过 Matrix 来实现的
 */
public class ZoomImageView extends AppCompatImageView implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    //最大缩放比例
    private float maxScale;

    //最小缩放比例
    private float minScale;

    //初始化的图片缩放值
    private float mBaseScale;

    //图片缩放工具操作类
    private Matrix matrix;

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

    private boolean firstInit;

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
        matrix = new Matrix();
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


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);

    }

    //开始缩放手势
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        //返回true,才会进入onScale()方法
        return true;
    }

    /**
     * 手势发生时监听器，对图片进行缩放的控制，
     *
     * @param detector
     * @return
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //前一个伸缩事件至当前伸缩事件的伸缩比率；大于1 则放大，否则缩小
        float scaleFactor = detector.getScaleFactor();

        //当前的放缩比例
        float curScale = getScale();

        if (getDrawable() == null) {
            return true;
        }

        //放大，但是当前的放缩比例小于最大比率；缩小，当前的放缩比率大于最小放缩比率
        if ((scaleFactor > 1 && curScale < maxScale) || (scaleFactor < 1 && curScale > minScale)) {
            //修正scaleFactor
            if (scaleFactor * curScale > maxScale) {
                scaleFactor = maxScale / curScale;
            }

            if (scaleFactor * curScale < minScale) {
                scaleFactor = minScale / curScale;
            }

            //在手指缩在地方进行缩放
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            //边界检测
            broaderAndCenterCheck();

            setImageMatrix(matrix);
        }

        return false;
    }


    /**
     * 图片在缩放时进行边界控制 todo
     */
    private void broaderAndCenterCheck() {
        RectF rectF = getMatrixRectF();

        float deltaX = 0;
        float delaY = 0;

        int width = getWidth();
        int height = getHeight();

        //缩放时进行边界检查,防止出现白边
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;
            }

            if (rectF.right < width) {
                deltaX = width - rectF.right;
            }
        }

        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                delaY = -rectF.top;
            }

            if (rectF.bottom < height) {
                delaY = height - rectF.bottom;
            }
        }

        //如果宽度和高度小于控件的宽、高；则让其居中
        if (rectF.width() < width) {
            deltaX = width / 2F - rectF.right + rectF.width() / 2F;
        }

        if (rectF.height() < height) {
            delaY = height / 2F - rectF.bottom + rectF.height() / 2F;
        }

        matrix.postTranslate(deltaX, delaY);
    }

    /**
     * 获取图片放大、缩小以后的宽和高
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix mMatrix = matrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            mMatrix.mapRect(rectF);
        }
        return rectF;
    }

    private float getScale() {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }


    //缩放结束
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //把事件交给ScaleGestureDetector 使用
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 根据图片和View 的宽和高，让图片居中显示
     */
    @Override
    public void onGlobalLayout() {
        if (!firstInit) {
            firstInit = true;
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            int dWidth = drawable.getIntrinsicWidth();
            int dHeight = drawable.getIntrinsicHeight();

            float scale;

            //根据图片和View的宽高来确定图片缩放比例
//            if (dWidth < width || dHeight < height) {
//                //将图片放大，能够填充满 View
//                scale = Math.max(width * 1.0f / dWidth, height * 1.0f / dHeight);
//            } else {
//                //将图片缩小，尽可能让图片多展示
//                scale = Math.max(width * 1.0f / dWidth, height * 1.0f / dHeight);
//            }

            //以View的宽度为基准，宽度填充满，高度等比例缩放
            scale = width * 1.0f / dWidth;

            mBaseScale = scale;
            maxScale = mBaseScale * 3;
            minScale = mBaseScale / 2;

            float dx = width * 1.0f / 2F - dWidth / 2F;
            float dy = height * 1.0f / 2F - dHeight / 2F;

            //将图片移动到View的中心位置
            //dx，dy 分别是x，y上的平移量。
            matrix.postTranslate(dx, dy);

            //sx，sy代表了缩放的倍数，px,py代表缩放的中心
            matrix.postScale(scale, scale, width / 2F, height / 2F);
            setImageMatrix(matrix);
        }

    }
}
