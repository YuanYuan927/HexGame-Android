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

    public static final int BOARD_CENTER = 0x0;
    public static final int BOARD_TOP = 0x1;
    public static final int BOARD_BOTTOM= 0x2;
    public static final int BOARD_LEFT = 0x4;
    public static final int BOARD_RIGHT = 0x8;

    public static final int STROKE_SIZE = 1;
    private static final double SQRT_3 = Math.sqrt(3);
    private static final int PAINT_FILL_INIT_COLOR = 0x01010101;
    private static final int PAINT_FILL_INIT_ALPHA = 255;

    private int mType;
    private Player mOwner;
    private boolean isOccupied;

    private int mSize;
    private Paint mBoundPaint;
    private Paint mStrokePaint;
    private Paint mStrokePaintA;
    private Paint mStrokePaintB;
    private Paint mFillPaint;


    private Path mPath;
    private Path mPathALeft;
    private Path mPathARight;
    private Path mPathBTop;
    private Path mPathBBottom;

    public HexView(Context context, int size, int type) {
        super(context);
        mSize = size;
        mType = type;
        initPaint();
        initPath();
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
        int color = player.equals(Player.A) ? res.getColor(R.color.indigo_500) : res.getColor(R.color.pink_500);
        color &= ((PAINT_FILL_INIT_ALPHA << 24) | 0x00FFFFFF);
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
        mStrokePaint.setColor(getResources().getColor(R.color.gray_100));
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mFillPaint.setColor(PAINT_FILL_INIT_COLOR);
        mFillPaint.setStyle(Paint.Style.FILL);

        if ((mType & BOARD_LEFT) != 0 || (mType & BOARD_RIGHT) != 0) {
            intStrokeAPaint();
        }
        if ((mType & BOARD_TOP) != 0 || (mType & BOARD_BOTTOM) != 0) {
            intStrokeBPaint();
        }
        if (mType != 0) {
            initBoundPaint();
        }
    }

    private void initBoundPaint() {
        mBoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mBoundPaint.setStrokeWidth(STROKE_SIZE * 8);
        mBoundPaint.setStrokeCap(Paint.Cap.ROUND);
        mBoundPaint.setColor(getResources().getColor(R.color.gray_100));
        mBoundPaint.setStyle(Paint.Style.STROKE);
    }

    private void intStrokeAPaint() {
        mStrokePaintA = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePaintA.setStrokeWidth(STROKE_SIZE * 4);
        mStrokePaintA.setStrokeCap(Paint.Cap.ROUND);
        mStrokePaintA.setColor(getResources().getColor(R.color.indigo_500));
        mStrokePaintA.setStyle(Paint.Style.STROKE);
    }

    private void intStrokeBPaint() {
        mStrokePaintB = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePaintB.setStrokeWidth(STROKE_SIZE * 4);
        mStrokePaintB.setStrokeCap(Paint.Cap.ROUND);
        mStrokePaintB.setColor(getResources().getColor(R.color.pink_500));
        mStrokePaintB.setStyle(Paint.Style.STROKE);
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
        canvas.drawPath(mPath, mFillPaint);
        canvas.drawPath(mPath, mStrokePaint);
        if (mPathALeft != null) {
//            canvas.drawPath(mPathALeft, mBoundPaint);
            canvas.drawPath(mPathALeft, mStrokePaintA);
        } else if (mPathARight != null) {
//            canvas.drawPath(mPathARight, mBoundPaint);
            canvas.drawPath(mPathARight, mStrokePaintA);
        }
        if (mPathBTop != null) {
//            canvas.drawPath(mPathBTop, mBoundPaint);
            canvas.drawPath(mPathBTop, mStrokePaintB);
        } else if (mPathBBottom != null) {
//            canvas.drawPath(mPathBBottom, mBoundPaint);
            canvas.drawPath(mPathBBottom, mStrokePaintB);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Bitmap background = getDrawingCache();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x < 0 || x >= background.getWidth() || y < 0 || y >= background.getHeight())
            return false;
        int pixel = background.getPixel(x, y);
        if (Color.TRANSPARENT == pixel) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setAlpha(float alpha) {
        if (mOwner == null)
            return;
        Resources res = getResources();
        int color = mOwner.equals(Player.A) ? res.getColor(R.color.indigo_500) : res.getColor(R.color.pink_500);
        int paintAlpha = (int) (PAINT_FILL_INIT_ALPHA * alpha);
        if (paintAlpha != 0) {
            color &= ((paintAlpha << 24) | 0x00FFFFFF);
        } else {
            color = PAINT_FILL_INIT_COLOR;
        }
        mFillPaint.setColor(color);
        invalidate();
    }

    private void initPath() {
        int measuredWidth = (int) ((mSize * SQRT_3));
        int measuredHeight = 2 * mSize;
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

        mPath = new Path();
        mPath.moveTo(x0, y0);
        mPath.lineTo(x1, y1);
        mPath.lineTo(x2, y2);
        mPath.lineTo(x3, y3);
        mPath.lineTo(x4, y4);
        mPath.lineTo(x5, y5);
        mPath.close();

        if ((mType & BOARD_LEFT) != 0) {
            mPathALeft = new Path();
            mPathALeft.moveTo(x1, y1);
            mPathALeft.lineTo(x2, y2);
            mPathALeft.lineTo(x3, y3);
        }
        if ((mType & BOARD_RIGHT) != 0) {
            mPathARight = new Path();
            mPathARight.moveTo(x0, y0);
            mPathARight.lineTo(x5, y5);
            mPathARight.lineTo(x4, y4);
        }
        if ((mType & BOARD_TOP) != 0) {
            mPathBTop = new Path();
            mPathBTop.moveTo(x1, y1);
            mPathBTop.lineTo(x0, y0);
            mPathBTop.lineTo(x5, y5);
        }
        if ((mType & BOARD_BOTTOM) != 0) {
            mPathBBottom = new Path();
            mPathBBottom.moveTo(x2, y2);
            mPathBBottom.lineTo(x3, y3);
            mPathBBottom.lineTo(x4, y4);
        }
    }

    public static int type(int boardN, int id) {
        int row = (id - 1) / boardN;
        int col = (id - 1) % boardN;
        int boardNum = boardN * boardN;
        if (id == 1) { // top-left
            return BOARD_TOP | BOARD_LEFT;
        }
        if (id == boardN) { // top-right
            return BOARD_TOP | BOARD_RIGHT;
        }
        if (id == boardNum - boardN + 1) { // bottom-left
            return BOARD_BOTTOM | BOARD_LEFT;
        }
        if (id == boardNum) { // bottom-right
            return BOARD_BOTTOM | BOARD_RIGHT;
        }
        if (row == 0) { // top
            return BOARD_TOP;
        }
        if (row == boardN - 1) { // bottom
            return BOARD_BOTTOM;
        }
        if (col == 0) { // left
            return BOARD_LEFT;
        }
        if (col == boardN - 1) { // right
            return BOARD_RIGHT;
        }
        return BOARD_CENTER;
    }
}
