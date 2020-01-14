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
import android.widget.OverScroller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.blankj.utilcode.util.LogUtils;


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
 * <p>
 * <p>
 * 根据手势缩放已完成
 * dan
 */
public class ZoomImageView extends AppCompatImageView implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

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
     * 缩放手势检测
     */
    private ScaleGestureDetector scaleGestureDetector;

    private boolean firstInit;

    private final float[] mMatrixValues = new float[9];
    private FlingRunnable flingRunnable;


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

            //单击事件
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (singleClickListener != null) {
                    singleClickListener.setSingleClick();
                }
                return super.onSingleTapConfirmed(e);
            }

            //双击放缩
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }

            //滑动
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                onTranslation(-distanceX, -distanceY);
                return true;
            }

            /**
             * 惯性滑动，在drawable 的宽、高 >= View的宽高的情况下，在滑动过程中，手指松开后 惯性往前滑动
             * @param e1
             * @param e2
             * @param velocityX
             * @param velocityY
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //惯性滑动处理
                flingRunnable = new FlingRunnable(getContext());
                flingRunnable.fling(getWidth(), getHeight(), -Math.round(velocityX), -Math.round(velocityY));
                ZoomImageView.this.post(flingRunnable);
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        /**
         * 手势滑动
         */
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            //缩放开始，返回值表示是否受理后续的缩放事件
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                //返回true,才会进入onScale()方法
                return true;
            }

            /**
             * 缩放进行中，返回值表示是否下次缩放需要重置，如果返回true，那么detector就会重置缩放事件，如果返回false，detector会在之前的缩放上继续进行计算。
             *
             * @param detector
             * @return
             */
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale(detector);
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });
        setOnTouchListener(this);
    }

    private void scale(ScaleGestureDetector detector) {
        //前一个伸缩事件至当前伸缩事件的伸缩比率；大于1 则放大，否则缩小
        float scaleFactor = detector.getScaleFactor();

        if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
            return;
        }

        //当前的放缩比例
        float curScale = getScale();

        if (getDrawable() == null) {
            return;
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
            setImageMatrix(matrix);

            //边界检测
            removeBorderAndTranslationCenter();
        }
    }


    //滑动手势操作（移动）
    private void onTranslation(float dx, float dy) {
        if (getDrawable() == null) {
            return;
        }

        RectF rectF = getMatrixRectF();
        if (rectF == null) {
            return;
        }

        //图片的宽高小于控件宽度时，不允许左右滑动
        if (rectF.width() <= getWidth()) {
            dx = 0.0F;
        }

        if (rectF.height() <= getHeight()) {
            dy = 0.0F;
        }

        //不能滑动
        if (dx == 0.0F && dy == 0.0F) {
            return;
        }

        matrix.postTranslate(dx, dy);
        removeBorderAndTranslationCenter();
        setImageMatrix(matrix);
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

    /**
     * 目前有问题
     * 消除图片缩放后周围的白边，并且将图片移动到中间位置
     * 图片的宽、高大于 View的宽高的情况下，图片在缩放、滑动、惯性滑动时进行边界控制，防止周围出现白边。
     */
    private void removeBorderAndTranslationCenter() {
        RectF rectF = getMatrixRectF();

        if (rectF == null) {
            return;
        }

        float translationX = 0;
        float translationY = 0;

        int width = getWidth();
        int height = getHeight();

        //图片的宽高大于View的宽高，缩放时进行边界检查,防止出现白边
        //防止左右两侧出现白边，
        if (rectF.width() >= width) {
            //在图片的宽度大于View的情况下，图片的左边界left>0,表示左侧有白边，需要移动-rectF.left距离。以下同理
            if (rectF.left >= 0) {
                translationX = -rectF.left;
            }

            //右侧有白边
            if (rectF.right < width) {
                translationX = width - rectF.right;
            }
        } else {
            //图片的宽、高 小于 View的宽高；
            //如果宽度和高度小于控件的宽、高；则让其居中（两个中线的差值就是需要移动的距离）
            translationX = width * 0.5f - (rectF.right - rectF.width() * 0.5f);
        }

        //防止上下两侧出现白边
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                translationY = -rectF.top;
            }

            if (rectF.bottom < height) {
                translationY = height - rectF.bottom;
            }
        } else {
            translationY = height * 0.5f - (rectF.bottom - rectF.height() * 0.5f);
        }

        //平移
        matrix.postTranslate(translationX, translationY);
        setImageMatrix(matrix);
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
        if (drawable == null) {
            return null;
        }

        rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //这一步的操作意义: 就是获取drawable 相对屏幕原点的坐标left,top,right,bottom,所以才会有边界校验
        mMatrix.mapRect(rectF);
        LogUtils.i("left：：" + rectF.left + ",,top：：" + rectF.top + ",,right：：" + rectF.right + ",,bottom：：" + rectF.bottom);
        return rectF;
    }

    private float getScale() {
        return (float) Math.sqrt((float) Math.pow(getValue(matrix, Matrix.MSCALE_X), 2) + (float) Math.pow
                (getValue(matrix, Matrix.MSKEW_Y), 2));
    }

    /**
     * Helper method that 'unpacks' a Matrix and returns the required value
     *
     * @param matrix     Matrix to unpack
     * @param whichValue Which value from Matrix.M* to return
     * @return returned value
     */
    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //把事件交给ScaleGestureDetector 使用 或 单双机点击
        return scaleGestureDetector.onTouchEvent(event) | gestureDetector.onTouchEvent(event);
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

    private class FlingRunnable implements Runnable {

        private OverScroller scroller;

        private int mCurX;
        private int mCurY;

        public FlingRunnable(Context context) {
            scroller = new OverScroller(context);
        }

        public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
            RectF rectF = getMatrixRectF();
            if (rectF == null) {
                return;
            }

            int minX, maxX, minY, maxY;

            //rectF.left 在drawable 的宽度大于 View的宽高的情况下，rectF.left <= 0 ,否则 >= 0;
            int startX = Math.round(-rectF.left); //四舍五入的原理是在参数上加0.5然后进行下取整
            if (viewWidth < rectF.width()) {
                minX = 0;
                maxX = Math.round(rectF.width() - getWidth());
            } else {
                minX = maxX = startX;
            }

            int startY = Math.round(-rectF.top);
            if (viewHeight < rectF.height()) {
                minY = 0;
                maxY = Math.round(rectF.height() - getHeight());
            } else {
                minY = maxY = startY;
            }

            mCurX = startX;
            mCurY = startY;

            if (startX != maxX || startY != maxY) {
                //调用fling方法，然后通过调用getCurX 和 getCurY来获得当前的x和y坐标，这个坐标的计算是模拟一个惯性滑动来计算出来的
                //我们根据这个x和y的变化可以模拟出图片的滑动惯性
                scroller.fling(startX, startY, Math.round(velocityX), Math.round(velocityY), minX, maxX, minY, maxY);
            }
        }

        @Override
        public void run() {
            if (scroller.isFinished()) {
                return;
            }

            //返回true 表示滑动动画还没有结束
            if (scroller.computeScrollOffset()) {
                int newX = scroller.getCurrX();
                int newY = scroller.getCurrY();
                onTranslation(mCurX - newX, mCurY - newY);

                mCurX = newX;
                mCurY = newY;

                postOnAnimation(ZoomImageView.this, this);
            }
        }
    }

    private void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.postOnAnimation(runnable);
        } else {
            view.postDelayed(runnable, 1000 / 60);
        }
    }

    //单击事件
    public interface SingleClickListener {
        void setSingleClick();
    }

    private SingleClickListener singleClickListener;

    public void setSingleClickListener(SingleClickListener singleClickListener) {
        this.singleClickListener = singleClickListener;
    }
}
