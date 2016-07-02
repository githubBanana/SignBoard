package com.xs.signboardwidget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * @version V1.0 <签名板>
 * @author: Xs
 * @date: 2016-07-02 16:08
 * @email Xs.lin@foxmail.com
 */
public class SignBoardView extends View {
    private static final String TAG = "SignBoardView";
    private static final int ENABLE_QUADTO_DISTANCE = 3;
    private Paint   mPaint;
    private Paint   mPaintBitmap;
    private Path    mPath;
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private float   _downX,_downY;//起始坐标点


    public SignBoardView(Context context) {
        super(context);
        initView(context);
    }

    public SignBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SignBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.rgb(0,0,0));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5f);

        mPath = new Path();

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final int screenWidth = manager.getDefaultDisplay().getWidth();
        try {
            mBitmap = Bitmap.createBitmap(screenWidth - 50,1000, Bitmap.Config.ARGB_4444);
        }catch (OutOfMemoryError error) {
            Log.e(TAG, "initView: OOM" );
        }
        mCanvas = new Canvas(mBitmap);
        mPaintBitmap = new Paint(Paint.DITHER_FLAG);
    }

    /**
     * first touch board
     * @param x
     * @param y
     */
    private void touchDown(float x,float y) {
        _downX = x;
        _downY = y;
        mPath.reset();
        mPath.moveTo(x,y);
    }

    private void touchUp(float x,float y) {
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
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath,mPaint);
//        canvas.drawBitmap(mBitmap,0,0,mPaintBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event.getX(),event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchMove(event.getX(),event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchUp(event.getX(),event.getY());
                invalidate();
                break;
            default:break;
        }

        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
