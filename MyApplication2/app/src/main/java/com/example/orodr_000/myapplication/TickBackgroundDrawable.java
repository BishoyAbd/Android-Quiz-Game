package com.example.orodr_000.myapplication;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class TickBackgroundDrawable extends Drawable {


    private Paint mBackgroundPaint;


    private final RectF mBounds = new RectF();

    private int mTickColor = Color.BLUE;




    public TickBackgroundDrawable( int tickColor) {

        mTickColor = tickColor;
        setupPaints();
    }

    private void setupPaints() {
        mBackgroundPaint = new Paint(ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mTickColor);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        int padding = bounds.centerX()/2;

        mBounds.left = bounds.left + padding;
        mBounds.right = bounds.right - padding;
        mBounds.top = bounds.top + padding;
        mBounds.bottom = bounds.bottom - padding;

        setupPlusMode();
    }

    private void setupPlusMode() {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mBounds.centerX(), mBackgroundPaint);

        /*canvas.save();
        canvas.rotate(180 * mRotation, (x(0) + x(1))/2, (y(0) + y(1))/2);
        canvas.drawLine(x(0), y(0), x(1), y(1), mLinePaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(180 * mRotation, (x(2) + x(3)) / 2, (y(2) + y(3)) / 2);
        canvas.drawLine(x(2), y(2), x(3), y(3), mLinePaint);
        canvas.restore();*/
    }

    @Override
    public void setAlpha(int alpha) {}

    @Override
    public void setColorFilter(ColorFilter cf) {}

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


}
