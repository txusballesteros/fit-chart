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

import android.graphics.Path;
import android.graphics.RectF;

class LinearValueRenderer extends BaseRenderer implements Renderer {
    public LinearValueRenderer(RectF drawingArea, FitChartValue value) {
        super(drawingArea, value);
    }

    @Override
    public Path buildPath(float animationProgress, float animationSeek) {
        Path path = null;
        if (getValue().getStartAngle() <= animationSeek) {
            path = new Path();
            float sweepAngle = calculateSweepAngle(animationSeek, getValue());
            path.addArc(getDrawingArea(), getValue().getStartAngle(), sweepAngle);
        }
        return path;
    }

    private float calculateSweepAngle(float animationSeek, FitChartValue value) {
        float result;
        float totalSizeOfValue = value.getStartAngle() + value.getSweepAngle();
        if (totalSizeOfValue > animationSeek) {
            result = animationSeek - value.getStartAngle();
        } else {
            result = value.getSweepAngle();
        }
        return result;
    }
}
