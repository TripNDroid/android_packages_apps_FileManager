package com.tripndroid.filemanager.utils.provider;

import com.tripndroid.filemanager.utils.files.Futils;
import com.tripndroid.filemanager.utils.color.ColorPreference;
import com.tripndroid.filemanager.utils.theme.AppTheme;
import com.tripndroid.filemanager.utils.theme.AppThemeManagerInterface;

/**
 * Created by Rémi Piotaix <remi.piotaix@gmail.com> on 2016-10-17.
 */
public interface UtilitiesProviderInterface {
    Futils getFutils();

    ColorPreference getColorPreference();

    AppTheme getAppTheme();

    AppThemeManagerInterface getThemeManager();
}
