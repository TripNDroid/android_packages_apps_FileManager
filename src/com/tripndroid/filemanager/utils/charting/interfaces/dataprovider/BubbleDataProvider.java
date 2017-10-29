package com.tripndroid.filemanager.utils.charting.interfaces.dataprovider;

import com.tripndroid.filemanager.utils.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
