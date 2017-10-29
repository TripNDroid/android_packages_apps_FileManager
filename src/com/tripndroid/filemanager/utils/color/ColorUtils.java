package com.tripndroid.filemanager.utils.color;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;

import com.tripndroid.filemanager.R;
import com.tripndroid.filemanager.utils.Utils;

import static com.tripndroid.filemanager.ui.icons.Icons.APK;
import static com.tripndroid.filemanager.ui.icons.Icons.ARCHIVE;
import static com.tripndroid.filemanager.ui.icons.Icons.AUDIO;
import static com.tripndroid.filemanager.ui.icons.Icons.CODE;
import static com.tripndroid.filemanager.ui.icons.Icons.GENERIC;
import static com.tripndroid.filemanager.ui.icons.Icons.PDF;
import static com.tripndroid.filemanager.ui.icons.Icons.PICTURE;
import static com.tripndroid.filemanager.ui.icons.Icons.TEXT;
import static com.tripndroid.filemanager.ui.icons.Icons.VIDEO;

/**
 * @author Emmanuel
 *         on 24/5/2017, at 18:56.
 */

public class ColorUtils {


    public static void colorizeIcons(Context context, int iconType, GradientDrawable background,
                                     @ColorInt int defaultColor) {
        switch (iconType) {
            case VIDEO:
            case PICTURE:
                background.setColor(Utils.getColor(context, R.color.video_item));
                break;
            case AUDIO:
                background.setColor(Utils.getColor(context, R.color.audio_item));
                break;
            case PDF:
                background.setColor(Utils.getColor(context, R.color.pdf_item));
                break;
            case CODE:
                background.setColor(Utils.getColor(context, R.color.code_item));
                break;
            case TEXT:
                background.setColor(Utils.getColor(context, R.color.text_item));
                break;
            case ARCHIVE:
                background.setColor(Utils.getColor(context, R.color.archive_item));
                break;
            case APK:
                background.setColor(Utils.getColor(context, R.color.apk_item));
                break;
            case GENERIC:
                background.setColor(Utils.getColor(context, R.color.generic_item));
                break;
            default:
                background.setColor(defaultColor);
                break;
        }
    }
}
