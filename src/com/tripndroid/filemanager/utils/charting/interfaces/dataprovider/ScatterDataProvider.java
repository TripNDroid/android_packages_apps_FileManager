package com.tripndroid.filemanager.utils.charting.interfaces.dataprovider;

import com.tripndroid.filemanager.utils.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
