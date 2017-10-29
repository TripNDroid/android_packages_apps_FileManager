package com.tripndroid.filemanager.utils.charting.interfaces.dataprovider;

import com.tripndroid.filemanager.utils.charting.components.YAxis.AxisDependency;
import com.tripndroid.filemanager.utils.charting.data.BarLineScatterCandleBubbleData;
import com.tripndroid.filemanager.utils.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
