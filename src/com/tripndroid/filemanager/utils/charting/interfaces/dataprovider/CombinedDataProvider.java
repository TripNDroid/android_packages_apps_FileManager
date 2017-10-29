package com.tripndroid.filemanager.utils.charting.interfaces.dataprovider;

import com.tripndroid.filemanager.utils.charting.data.CombinedData;

/**
 * Created by philipp on 11/06/16.
 */
public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {

    CombinedData getCombinedData();
}
