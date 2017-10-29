package com.tripndroid.filemanager.utils.charting.interfaces.dataprovider;

import com.tripndroid.filemanager.utils.charting.components.YAxis;
import com.tripndroid.filemanager.utils.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
