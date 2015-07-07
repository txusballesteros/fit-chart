/*
 * Copyright Txus Ballesteros 2015 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
package com.txusballesteros.widgets;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FitChart extends View {
    static final int ANIMATION_MODE_LINEAR = 0;
    static final int ANIMATION_MODE_OVERDRAW = 1;
    static final int DEFAULT_VIEW_RADIUS = 0;
    static final int DEFAULT_MIN_VALUE = 0;
    static final int DEFAULT_MAX_VALUE = 100;
    static final int START_ANGLE = -90;
    static final int ANIMATION_DURATION = 1000;
    static final float INITIAL_ANIMATION_PROGRESS = 0.0f;
    static final float MAXIMUM_SWEEP_ANGLE = 360f;
    static final int DESIGN_MODE_SWEEP_ANGLE = 216;
    private RectF drawingArea;
    private Paint backStrokePaint;
    private Paint valueDesignPaint;
    private int backStrokeColor;
    private int valueStrokeColor;
    private float strokeSize;
    private float minValue = DEFAULT_MIN_VALUE;
    private float maxValue = DEFAULT_MAX_VALUE;
    private List<FitChartValue> chartValues;
    private float animationProgress = INITIAL_ANIMATION_PROGRESS;
    private float maxSweepAngle = MAXIMUM_SWEEP_ANGLE;
    private AnimationMode animationMode = AnimationMode.LINEAR;

    public void setMinValue(float value) {
        minValue = value;
    }

    public void setMaxValue(float value) {
        maxValue = value;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setValue(float value) {
        chartValues.clear();
        FitChartValue chartValue = new FitChartValue(value, valueStrokeColor);
        chartValue.setPaint(buildPaintForValue());
        chartValue.setStartAngle(START_ANGLE);
        chartValue.setSweepAngle(calculateSweepAngle(value));
        chartValues.add(chartValue);
        maxSweepAngle = chartValue.getSweepAngle();
        playAnimation();
    }

    public void setValues(Collection<FitChartValue> values) {
        chartValues.clear();
        maxSweepAngle = 0;
        float offsetSweepAngle = START_ANGLE;
        for (FitChartValue chartValue : values) {
            float sweepAngle = calculateSweepAngle(chartValue.getValue());
            chartValue.setPaint(buildPaintForValue());
            chartValue.setStartAngle(offsetSweepAngle);
            chartValue.setSweepAngle(sweepAngle);
            chartValues.add(chartValue);
            offsetSweepAngle += sweepAngle;
            maxSweepAngle += sweepAngle;
        }
        playAnimation();
    }

    public void setAnimationMode(AnimationMode mode) {
        this.animationMode = mode;
    }

    void setAnimationSeek(float value) {
        animationProgress = value;
        invalidate();
    }

    private Paint buildPaintForValue() {
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeSize);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    public FitChart(Context context) {
        super(context);
        initializeView(null);
    }

    public FitChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(attrs);
    }

    public FitChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(attrs);
    }

    private void initializeView(AttributeSet attrs) {
        chartValues = new ArrayList<>();
        initializeBackground();
        readAttributes(attrs);
        preparePaints();
    }

    private void initializeBackground() {
        if (!isInEditMode()) {
            if (getBackground() == null) {
                setBackgroundColor(getContext().getResources().getColor(R.color.default_back_color));
            }
        }
    }

    private void calculateDrawableArea() {
        float drawPadding = (strokeSize / 2);
        float width = getWidth();
        float height = getHeight();
        float left = drawPadding;
        float top = drawPadding;
        float right = width - drawPadding;
        float bottom = height - drawPadding;
        drawingArea = new RectF(left, top, right, bottom);
    }

    private void readAttributes(AttributeSet attrs) {
        Resources resources = getContext().getResources();
        valueStrokeColor = resources.getColor(R.color.default_chart_value_color);
        backStrokeColor = resources.getColor(R.color.default_back_stroke_color);
        strokeSize = resources.getDimension(R.dimen.default_stroke_size);
        if (attrs != null) {
            TypedArray attributes = getContext()
                    .getTheme().obtainStyledAttributes(attrs, R.styleable.FitChart, 0, 0);
            strokeSize = attributes
                    .getDimensionPixelSize(R.styleable.FitChart_strokeSize, (int) strokeSize);
            valueStrokeColor = attributes
                    .getColor(R.styleable.FitChart_valueStrokeColor, valueStrokeColor);
            backStrokeColor = attributes
                    .getColor(R.styleable.FitChart_backStrokeColor, backStrokeColor);
            int attrAnimationMode = attributes.getInteger(R.styleable.FitChart_animationMode, ANIMATION_MODE_LINEAR);
            if (attrAnimationMode == ANIMATION_MODE_LINEAR) {
                animationMode = AnimationMode.LINEAR;
            } else {
                animationMode = AnimationMode.OVERDRAW;
            }
            attributes.recycle();
        }
    }

    private void preparePaints() {
        backStrokePaint = getPaint();
        backStrokePaint.setColor(backStrokeColor);
        backStrokePaint.setStyle(Paint.Style.STROKE);
        backStrokePaint.setStrokeWidth(strokeSize);
        valueDesignPaint = getPaint();
        valueDesignPaint.setColor(valueStrokeColor);
        valueDesignPaint.setStyle(Paint.Style.STROKE);
        valueDesignPaint.setStrokeCap(Paint.Cap.ROUND);
        valueDesignPaint.setStrokeWidth(strokeSize);
    }

    private Paint getPaint() {
        if (!isInEditMode()) {
            return new Paint(Paint.ANTI_ALIAS_FLAG);
        } else {
            return new Paint();
        }
    }

    private float getViewRadius() {
        if (drawingArea != null) {
            return (drawingArea.width() / 2);
        } else {
            return DEFAULT_VIEW_RADIUS;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDrawableArea();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = Math.max(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        renderBack(canvas);
        renderValues(canvas);
    }

    private void renderBack(Canvas canvas) {
        Path path = new Path();
        float viewRadius = getViewRadius();
        path.addCircle(drawingArea.centerX(), drawingArea.centerY(), viewRadius, Path.Direction.CCW);
        canvas.drawPath(path, backStrokePaint);
    }

    private void renderValues(Canvas canvas) {
        if (!isInEditMode()) {
            int valuesCounter = (chartValues.size() - 1);
            for (int index = valuesCounter; index >= 0; index--) {
                renderValue(canvas, chartValues.get(index));
            }
        } else {
            renderValue(canvas, null);
        }
    }

    private void renderValue(Canvas canvas, FitChartValue value) {
        if (!isInEditMode()) {
            float animationSeek = calculateAnimationSeek();
            Renderer renderer = RendererFactory.getRenderer(animationMode, value, drawingArea);
            Path path = renderer.buildPath(animationProgress, animationSeek);
            if (path != null) {
                canvas.drawPath(path, value.getPaint());
            }
        } else {
            Path path = new Path();
            path.addArc(drawingArea, START_ANGLE, DESIGN_MODE_SWEEP_ANGLE);
            canvas.drawPath(path, valueDesignPaint);
        }
    }

    private float calculateAnimationSeek() {
        return ((maxSweepAngle * animationProgress) + START_ANGLE);
    }

    private float calculateSweepAngle(float value) {
        float chartValuesWindow = Math.max(minValue, maxValue) - Math.min(minValue, maxValue);
        float chartValuesScale = (360f / chartValuesWindow);
        return (value * chartValuesScale);
    }

    private void playAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "animationSeek", 0.0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setTarget(this);
        animatorSet.play(animator);
        animatorSet.start();
    }
}
