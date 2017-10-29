/*
 * Copyright (C) 2014 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tripndroid.filemanager.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tripndroid.filemanager.R;
import com.tripndroid.filemanager.fragments.preference_fragments.ColorPref;
import com.tripndroid.filemanager.fragments.preference_fragments.FoldersPref;
import com.tripndroid.filemanager.fragments.preference_fragments.Preffrag;
import com.tripndroid.filemanager.utils.PreferenceUtils;
import com.tripndroid.filemanager.utils.color.ColorUsage;
import com.tripndroid.filemanager.utils.SystemBarTintManager;

import static android.os.Build.VERSION.SDK_INT;

public class PreferencesActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    //Start is the first activity you see
    public static final int START_PREFERENCE = 0;
    public static final int FOLDERS_PREFERENCE = 1;

    private boolean changed = false;
    //The preference fragment currently selected
    private int selectedItem = 0;

    private static final String KEY_CURRENT_FRAG_OPEN = "current_frag_open";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences Sp = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefsfrag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (SDK_INT >= 21) {
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription("FileManager",
                    ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap(),
                    getColorPreference().getColor(ColorUsage.getPrimary(MainActivity.currentTab)));
            setTaskDescription(taskDescription);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP | android.support.v7.app.ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setBackgroundDrawable(getColorPreference().getDrawable(ColorUsage.getPrimary(MainActivity.currentTab)));

        if (SDK_INT == 20 || SDK_INT == 19) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getColorPreference().getColor(ColorUsage.getPrimary(MainActivity.currentTab)));

            FrameLayout.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) findViewById(R.id.preferences).getLayoutParams();
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            p.setMargins(0, config.getStatusBarHeight(), 0, 0);
        } else if (SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(PreferenceUtils.getStatusColor(getColorPreference().getColorAsString(ColorUsage.getPrimary(MainActivity.currentTab))));
        }
        if (savedInstanceState != null){
            selectedItem = savedInstanceState.getInt(KEY_CURRENT_FRAG_OPEN, 0);
        }
        selectItem(selectedItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_FRAG_OPEN, selectedItem);
    }

    @Override
    public void onBackPressed() {
        if (selectedItem != START_PREFERENCE && changed)
            restartPC(this);
        else if (selectedItem != START_PREFERENCE) {
            selectItem(START_PREFERENCE);
        } else {
            Intent in = new Intent(PreferencesActivity.this, MainActivity.class);
            in.setAction(Intent.ACTION_MAIN);
            in.setAction(Intent.CATEGORY_LAUNCHER);
            this.startActivity(in);
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                if (selectedItem != START_PREFERENCE && changed)
                    restartPC(this);
                else if (selectedItem != START_PREFERENCE) {
                    selectItem(START_PREFERENCE);
                } else {
                    Intent in = new Intent(PreferencesActivity.this, MainActivity.class);
                    in.setAction(Intent.ACTION_MAIN);
                    in.setAction(Intent.CATEGORY_LAUNCHER);

                    final int enter_anim = android.R.anim.fade_in;
                    final int exit_anim = android.R.anim.fade_out;
                    Activity activity = this;
                    activity.overridePendingTransition(enter_anim, exit_anim);
                    activity.finish();
                    activity.overridePendingTransition(enter_anim, exit_anim);
                    activity.startActivity(in);
                }
                return true;
        }
        return true;
    }

    public void setChanged() {
        changed = true;
    }

    public void restartPC(final Activity activity) {
        if (activity == null)
            return;
        final int enter_anim = android.R.anim.fade_in;
        final int exit_anim = android.R.anim.fade_out;
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.finish();
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.startActivity(activity.getIntent());
    }

    /**
     * When a Preference (that requires an independent fragment) is selected this is called.
     * @param i the Preference in question
     */
    public void selectItem(int i) {
        selectedItem = i;
        switch (i) {
            case START_PREFERENCE:
                loadPrefFragment(new Preffrag(), R.string.setting);
                break;
            case FOLDERS_PREFERENCE:
                loadPrefFragment(new FoldersPref(), R.string.sidebarfolders_title);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 66) {
            Preffrag preffrag = (Preffrag) getFragmentManager().findFragmentById(R.id.prefsfragment);

        }
    }

    private void loadPrefFragment(PreferenceFragment fragment, @StringRes int titleBarName) {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.prefsfragment, fragment);
        t.commit();
        getSupportActionBar().setTitle(titleBarName);
    }
}
