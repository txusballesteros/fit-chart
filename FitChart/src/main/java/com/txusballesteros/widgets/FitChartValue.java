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

import android.graphics.Paint;
import android.graphics.SweepGradient;

public class FitChartValue {
    private final float value;
    private final int color;
    private final SweepGradient gradientShader;
    private Paint paint;
    private float startAngle;
    private float sweepAngle;

    float getValue() {
        return this.value;
    }

    int getColor() {
        return this.color;
    }

    void setPaint(Paint paint) {
        this.paint = paint;
        if (gradientShader != null) {
            this.paint.setDither(true);
            this.paint.setShader(gradientShader);

        } else {
            this.paint.setColor(color);

        }
    }

    void setStartAngle(float angle) {
        this.startAngle = angle;
    }

    void setSweepAngle(float sweep) {
        this.sweepAngle = sweep;
    }

    float getStartAngle() {
        return this.startAngle;
    }

    float getSweepAngle() {
        return this.sweepAngle;
    }

    Paint getPaint() {
        return this.paint;
    }

    public FitChartValue(float value, int color, SweepGradient gradientShader) {
        this.value = value;
        this.color = color;
        this.gradientShader = gradientShader;
    }
}
