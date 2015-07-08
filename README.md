Fit Chart
=====================

![](assets/overdraw_animation_mode.gif) ![](assets/linear_animation_mode.gif)

## Latest Version

[![Download](https://api.bintray.com/packages/txusballesteros/maven/FitChart/images/download.svg)](https://bintray.com/txusballesteros/maven/FitChart/_latestVersion) ![](https://img.shields.io/badge/platform-android-green.svg)

## How to use

### Configuring your project dependencies

Add the library dependency to your build.gradle file.

```groovy
dependencies {
    ...
    compile 'com.txusballesteros:FitChart:1.0'
}
```

### Adding the view to your layout

Add the view to your xml layout file.

```xml
<com.txusballesteros.widgets.FitChart
            android:layout_width="200dp"
            android:layout_height="200dp" />
```

### Styling the view

If you want to customize the view, you can set the next attributes.

```xml
<com.txusballesteros.widgets.FitChart
            ..
            app:strokeSize="10dp"
            app:valueStrokeColor="#ff0000"
            app:backStrokeColor="#00ff00"
            app:animationMode="overdraw" />
```

### Setting the values

Setting the minimum and maximum values of the scale of the chart.

```java
final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);
               fitChart.setMinValue(0f);
               fitChart.setMaxValue(100f);
```

Setting a single progress value.

```java
final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);
               fitChart.setValue(80f);
```

Setting a some progress values.

```java
Collection<FitChartValue> values = new ArrayList<>();
values.add(new FitChartValue(30f, 0x2d4302));
values.add(new FitChartValue(20f, 0x75a80d));
values.add(new FitChartValue(15f, 0x8fc026));
values.add(new FitChartValue(10f, 0xB5CC84));
final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);
               fitChart.setValues(values);
```

## License

Copyright Txus Ballesteros 2015 (@txusballesteros)

This file is part of some open source application.

Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
