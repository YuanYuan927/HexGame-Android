package com.yuan.hexgame.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yuan.hexgame.R;
import com.yuan.hexgame.game.Player;

/**
 * Created by Yuan Sun on 2015/9/10.
 */
public class HexView extends View implements HexChess {

    public static final int STROKE_SIZE = 1;
    private static final double SQRT_3 = Math.sqrt(3);
    private static final int PAINT_FILL_INIT_COLOR = 0xFF010101;

    private Player mOwner;
    private boolean isOccupied;

    private int mSize;
    private Paint mStrokePaint;
    private Paint mFillPaint;

    public HexView(Context context, int size) {
        super(context);
        mSize = size;
        initPaint();
        setDrawingCacheEnabled(true);
    }

    private HexView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private HexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOwner(Player player) {
        if (player == null)
            throw new NullPointerException();
        mOwner = player;
        isOccupied = true;
        Resources res = getResources();
        int color = player.equals(Player.A) ? res.getColor(R.color.player_a_chess_color) : res.getColor(R.color.player_b_chess_color);
        mFillPaint.setColor(color);
        invalidate();
    }

    @Override
    public Player getOwner() {
        return mOwner;
    }

    @Override
    public boolean isOccupied() {
        return isOccupied;
    }

    @Override
    public void reset() {
        mOwner = null;
        isOccupied = false;
        mFillPaint.setColor(PAINT_FILL_INIT_COLOR);
        invalidate();
    }

    private void initPaint() {
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePaint.setStrokeWidth(STROKE_SIZE * 2);
        mStrokePaint.setStrokeCap(Paint.Cap.ROUND);
        mStrokePaint.setColor(0xFFFFFFFF);
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mFillPaint.setColor(PAINT_FILL_INIT_COLOR);
        mFillPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = (int) ((mSize * SQRT_3));
        int measuredHeight = 2 * mSize;
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     *     0
     * 1       5
     * 2       4
     *     3
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        Path path = new Path();
        int x0 = measuredWidth / 2;
        int y0 = (int) (STROKE_SIZE * 2 / SQRT_3);

        int x1 = STROKE_SIZE;
        int y1 = (int) (measuredHeight / 4 + STROKE_SIZE / SQRT_3);

        int x2 = x1;
        int y2 = (int) (measuredHeight * 3 / 4 - STROKE_SIZE / SQRT_3);

        int x3 = x0;
        int y3 = (int) (measuredHeight - STROKE_SIZE * 2 / SQRT_3);

        int x4 = measuredWidth - STROKE_SIZE;
        int y4 = y2;

        int x5 = x4;
        int y5 = y1;

        path.moveTo(x0, y0);
        path.lineTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        path.lineTo(x5, y5);
        path.close();


        canvas.drawPath(path, mFillPaint);
        canvas.drawPath(path, mStrokePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Bitmap background = getDrawingCache();
        int pixel = background.getPixel((int) event.getX(), (int) event.getY());
        if (Color.TRANSPARENT == pixel) {
            return false;
        }
        return super.onTouchEvent(event);
    }
}
