package com.xs.signboardwidget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.xs.signboardwidget.R;

/**
 * @version V1.0 <签名板>
 * @author: Xs
 * @date: 2016-07-02 16:08
 * @email Xs.lin@foxmail.com
 */
public class SignBoardView extends View {
    private static final String TAG = "SignBoardView";
    private static final int ENABLE_QUADTO_DISTANCE = 4;
    private Paint   mPaint;
    private Paint   mPaintBitmap;
    private Path    mPath;
    private Bitmap  mBitmap,mWriteBitmap;
    private Canvas  mCanvas;
    private float   _downX,_downY;//起始坐标点
    private float   mPaintX ,mPaintY ,mPaintWidth,mPaintHeight;//画笔图标显示
    private int mPaintColor;//画笔颜色
    private float mPaintStrokWidth;//画笔笔画粗细

    public SignBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.SignBoardView);
        mPaintColor = mTypeArray.getColor(R.styleable.SignBoardView_paintColor,0x00000000);
        mPaintStrokWidth = mTypeArray.getFloat(R.styleable.SignBoardView_paintStrokeWidth,5f);
        mTypeArray.recycle();
        initView(context);
    }

    private void initView(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mPaintStrokWidth);
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final int screenWidth = manager.getDefaultDisplay().getWidth();
        try {
            mBitmap = Bitmap.createBitmap(screenWidth - 40,1000, Bitmap.Config.ARGB_4444);
            mWriteBitmap = BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_menu_edit);
            mPaintHeight = mWriteBitmap.getHeight();
            mPaintWidth = mWriteBitmap.getWidth();
            mPaintX = mPaintWidth;
            mPaintY = mPaintHeight;
        }catch (OutOfMemoryError error) {
            Log.e(TAG, "initView: OOM" );
        }
        mCanvas = new Canvas(mBitmap);
        mPaintBitmap = new Paint(Paint.DITHER_FLAG);
        mPath = new Path();
    }

    /**
     * first touch board
     * @param x
     * @param y
     */
    private void touchDown(float x,float y) {
        mPath.reset();
        mPath.moveTo(x,y);
        _downX = x;
        _downY = y;
        mPaintX = x;
        mPaintY = y;
    }

    /**
     * end
     */
    private void touchUp() {
        mPath.lineTo(_downX,_downY);
        mCanvas.drawPath(mPath,mPaint);
        mPath.reset();
    }

    /**
     * move on board
     * @param x
     * @param y
     */
    private void touchMove(float x,float y) {
        float dx = Math.abs(x - _downX);
        float dy = Math.abs(y - _downY);
        if (dx >= ENABLE_QUADTO_DISTANCE || dy >= ENABLE_QUADTO_DISTANCE) {//两点之间的距离大于等于3时，生成贝塞尔绘制曲线
            //设置贝塞尔曲线的操作点为起点和终点的一半
            //二次贝塞尔，实现平滑曲线
            mPath.quadTo(_downX,_downY,(x + _downX) / 2,(y + _downY) / 2);
            _downX = x;
            _downY = y;
            mPaintX = x;
            mPaintY = y;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFFFFFFF);
        canvas.drawBitmap(mBitmap,0,0,mPaintBitmap);
        canvas.drawPath(mPath,mPaint);
        canvas.drawBitmap(mWriteBitmap,mPaintX,mPaintY - mPaintHeight,mPaintBitmap);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(x,y);
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x,y);
                postInvalidate();
                break;
        }

        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
        if (mWriteBitmap != null && !mWriteBitmap.isRecycled()) {
            mWriteBitmap.recycle();
            mWriteBitmap = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * 橡皮擦
     */
    public void clearView() {
        mBitmap.eraseColor(Color.TRANSPARENT);
        postInvalidate();
    }

    /**
     * 设置画笔颜色
     * @param color
     */
    public void setPaintColor(int color) {
        mPaintColor = color;
        mPaint.setColor(getResources().getColor(mPaintColor));
        postInvalidate();
    }

    /**
     * 减小画笔粗细
     */
    public void setmPaintStrokWidthSmaller() {
        mPaintStrokWidth -= 3f;
        if (mPaintStrokWidth <= 5f) {
            mPaintStrokWidth = 5f;
            return;
        }
        mPaint.setStrokeWidth(mPaintStrokWidth);
    }

    /**
     * 增大画笔粗细
     */
    public void setmPaintStrokWidthLarger() {
        mPaintStrokWidth += 3f;
        if (mPaintStrokWidth > 20f) {
            mPaintStrokWidth = 20f;
            return;
        }
        mPaint.setStrokeWidth(mPaintStrokWidth);
    }

    public Bitmap getSignBoardBitmamp() {
        return mBitmap;
    }
}
