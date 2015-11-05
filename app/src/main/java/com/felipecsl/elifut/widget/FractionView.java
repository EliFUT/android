package com.felipecsl.elifut.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.felipecsl.elifut.util.BundleBuilder;

public class FractionView extends View {
  private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private Paint sectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private RectF sectorOval = new RectF();
  private int numerator = 1;
  private int denominator = 60;
  private OnChangeListener listener;

  public FractionView(Context context) {
    super(context);
    init();
  }

  public FractionView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public FractionView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    // Avoid allocating new memory in onDraw by initializing circlePaint
    // in the constructor.
    circlePaint.setColor(Color.WHITE);
    circlePaint.setStyle(Paint.Style.FILL);
    sectorPaint.setColor(0xffff5a5f);
    sectorPaint.setStyle(Paint.Style.FILL);
    strokePaint.setColor(0xffff5a5f);
    strokePaint.setStyle(Paint.Style.STROKE);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    int width = getWidth() - getPaddingLeft() - getPaddingRight();
    int height = getHeight() - getPaddingTop() - getPaddingBottom();
    int size = Math.min(width, height);
    int cx = width / 2 + getPaddingLeft();
    int cy = height / 2 + getPaddingTop();
    int radius = size / 2;

    canvas.drawCircle(cx, cy, radius, circlePaint);
    canvas.drawCircle(cx, cy, radius, strokePaint);

    sectorOval.top = (height - size) / 2 + getPaddingTop();
    sectorOval.left = (width - size) / 2 + getPaddingLeft();
    sectorOval.bottom = sectorOval.top + size;
    sectorOval.right = sectorOval.left + size;

    canvas.drawArc(sectorOval, 270, getSweepAngle(), true, sectorPaint);
  }

  private float getSweepAngle() {
    return numerator * 360f / denominator;
  }

  public void setFraction(int numerator, int denominator) {
    if (numerator < 0) {
      return;
    }
    if (denominator <= 0) {
      return;
    }
    if (numerator > denominator) {
      return;
    }

    this.numerator = numerator;
    this.denominator = denominator;

    // Request a redraw
    invalidate();

    if (listener != null) {
      listener.onChange(numerator, denominator);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_UP) {
      return true;
    }

    int width = getWidth() - getPaddingLeft() - getPaddingRight();
    int height = getHeight() - getPaddingTop() - getPaddingBottom();
    int size = Math.min(width, height);
    int cx = width / 2 + getPaddingLeft();
    int cy = height / 2 + getPaddingTop();
    int radius = size / 2;

    // Reject touch events outside of the circle
    float dx = event.getX() - cx;
    float dy = event.getY() - cy;
    if (dx * dx + dy * dy > radius * radius) {
      return true;
    }

    // Increment the numerator, cycling back to 0 when we have filled the
    // whole circle.
    int numerator = this.numerator + 1;
    if (numerator > denominator) {
      numerator = 0;
    }
    setFraction(numerator, denominator);

    return true;
  }

  public Parcelable onSaveInstanceState() {
    return new BundleBuilder()
        .putParcelable("superState", super.onSaveInstanceState())
        .putInt("numerator", numerator)
        .putInt("denominator", denominator)
        .toBundle();
  }

  public void onRestoreInstanceState(Parcelable state) {
    if (state instanceof Bundle) {
      Bundle bundle = (Bundle) state;
      numerator = bundle.getInt("numerator");
      denominator = bundle.getInt("denominator");
      super.onRestoreInstanceState(bundle.getParcelable("superState"));
    } else {
      super.onRestoreInstanceState(state);
    }
    setFraction(numerator, denominator);
  }

  public void setOnChangeListener(OnChangeListener listener) {
    listener = listener;
  }

  public interface OnChangeListener {
    public void onChange(int numerator, int denominator);
  }
}