package com.tripndroid.filemanager.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.tripndroid.filemanager.materialdialogs.DialogAction;
import com.tripndroid.filemanager.materialdialogs.MaterialDialog;
import com.tripndroid.filemanager.R;
import com.tripndroid.filemanager.ui.dialogs.GeneralDialogCreation;
import com.tripndroid.filemanager.utils.PreferenceUtils;
import com.tripndroid.filemanager.utils.color.ColorUsage;
import com.tripndroid.filemanager.utils.theme.AppTheme;

/**
 * Created by arpitkh996 on 03-03-2016.
 */
public class BaseActivity extends BasicActivity {
    public SharedPreferences sharedPref;

    // Accent and Primary hex color string respectively
    /**
     * @deprecated use {@link #getColorPreference()#getColor(int)} and {@link ColorUsage#ACCENT}
     */
    public static String accentSkin;
    public static boolean rootMode;
    boolean checkStorage = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        accentSkin = getColorPreference().getColorAsString(ColorUsage.ACCENT);
        setTheme();

        rootMode = true;

        //requesting storage permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkStorage)
            if (!checkStoragePermission())
                requestStoragePermission();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public boolean checkStoragePermission() {

        // Verify that all required contact permissions have been granted.
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            final MaterialDialog materialDialog = GeneralDialogCreation.showBasicDialog(this, accentSkin, getAppTheme(), new String[]{getResources().getString(R.string.granttext), getResources().getString(R.string.grantper), getResources().getString(R.string.grant), getResources().getString(R.string.cancel), null});
            materialDialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat
                            .requestPermissions(BaseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 77);
                    materialDialog.dismiss();
                }
            });
            materialDialog.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            materialDialog.setCancelable(false);
            materialDialog.show();

        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 77);
        }
    }

    void setTheme() {
        AppTheme theme = getAppTheme();
        if (Build.VERSION.SDK_INT >= 21) {

            switch (accentSkin.toUpperCase()) {
                case "#F44336":
                    setTheme(R.style.pref_accent_light_red);
                    break;

                case "#E91E63":
                    setTheme(R.style.pref_accent_light_pink);
                    break;

                case "#9C27B0":
                    setTheme(R.style.pref_accent_light_purple);
                    break;

                case "#673AB7":
                    setTheme(R.style.pref_accent_light_deep_purple);
                    break;

                case "#3F51B5":
                    setTheme(R.style.pref_accent_light_indigo);
                    break;

                case "#0084C6":
                    setTheme(R.style.pref_accent_light_blue);
                    break;

                case "#5EAED6":
                    setTheme(R.style.pref_accent_light_light_blue);
                    break;

                case "#96CCE8":
                    setTheme(R.style.pref_accent_light_cyan);
                    break;

                case "#009688":
                    setTheme(R.style.pref_accent_light_teal);
                    break;

                case "#4CAF50":
                    setTheme(R.style.pref_accent_light_green);
                    break;

                case "#8BC34A":
                    setTheme(R.style.pref_accent_light_light_green);
                    break;

                case "#FFC107":
                    setTheme(R.style.pref_accent_light_amber);
                    break;

                case "#FF9800":
                    setTheme(R.style.pref_accent_light_orange);
                    break;

                case "#FF5722":
                    setTheme(R.style.pref_accent_light_deep_orange);
                    break;

                case "#795548":
                    setTheme(R.style.pref_accent_light_brown);
                    break;

                case "#212121":
                    setTheme(R.style.pref_accent_light_black);
                    break;

                case "#607D8B":
                    setTheme(R.style.pref_accent_light_blue_grey);
                    break;

                case "#004D40":
                    setTheme(R.style.pref_accent_light_super_su);
                    break;
            }
        } else {
            setTheme(R.style.appCompatLight);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTheme();
    }

}
