package com.yuan.hexgame.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.yuan.hexgame.R;

/**
 * Created by Yuan Sun on 2015/10/2.
 */
public class Avatar extends View {

    public static final int STROKE_SIZE = 2;

    private static Path sPath;
    private int mDiameter;
    private int mColor;

    private Paint mFillPaint;
    private Paint mStrokePaint;
    private Bitmap mAvatar;


    public Avatar(Context context, int diameter, int color, Bitmap avatar) {
        super(context);
        mDiameter = diameter;
        mColor = color;
        initPaint();
//        mAvatar = avatar;
        Matrix matrix = new Matrix();
        int width = mDiameter - 2 * STROKE_SIZE;
        matrix.postScale((float) width / avatar.getWidth(), (float) width / avatar.getHeight());
        mAvatar = Bitmap.createBitmap(avatar, 0, 0, avatar.getWidth(), avatar.getWidth(), matrix, false);
    }

    public Avatar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Avatar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint() {
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePaint.setColor(getResources().getColor(R.color.gray_100));
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(2 * STROKE_SIZE);

        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mFillPaint.setColor(mColor);
        mFillPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mDiameter, mDiameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (sPath == null) {
            sPath = new Path();
            int center = mDiameter / 2;
            int radius = mDiameter / 2 - STROKE_SIZE;
            sPath.addCircle(center, center, radius, Path.Direction.CW);
        }
        canvas.drawPath(sPath, mStrokePaint);
        canvas.drawPath(sPath, mFillPaint);
        canvas.drawBitmap(mAvatar, STROKE_SIZE, STROKE_SIZE, null);
    }
}
