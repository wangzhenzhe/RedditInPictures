/*
 * Copyright (C) 2012 Antew | antewcode@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.antew.redditinpictures.library.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.antew.redditinpictures.library.R;
import com.antew.redditinpictures.library.ui.About;
import com.antew.redditinpictures.library.utils.Consts;

/**
 * Preferences screen used for Pre-Honeycomb, this can be subclassed to add additional preferences.
 * See For an example see RedditInPicturesPreferencesFree in the RedditInPictures-Free project
 * 
 * @author Antew
 * 
 */
public class RedditInPicturesPreferences extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
    public static final String TAG = RedditInPicturesPreferences.class.getSimpleName();
    private CheckBoxPreference useMobileInterface;
    private CheckBoxPreference showNsfwImages;
    private CheckBoxPreference loadHighQualityThumbs;
    private boolean            showNsfwImagesOldValue;
    private boolean            showNsfwImagesNewValue;

    /**
     * This uses the deprecated addPreferencesFromResource because fragment preferences aren't part
     * of the support library
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(Consts.EXTRA_SHOW_NSFW_IMAGES)) {
            showNsfwImagesOldValue = getIntent().getBooleanExtra(Consts.EXTRA_SHOW_NSFW_IMAGES, false);
            showNsfwImagesNewValue = showNsfwImagesOldValue;
        }

        addPreferencesFromResource(R.xml.preferences);
        useMobileInterface = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.pref_use_mobile_interface));
        showNsfwImages = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.pref_show_nsfw_images));
        loadHighQualityThumbs = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.pref_load_high_quality_thumbnails));

        getPreferenceScreen().findPreference(getString(R.string.pref_about)).setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(RedditInPicturesPreferences.this, About.class));
                return true;
            }
        });

        final ActionBar actionBar = getSupportActionBar();

        // Hide title text and set home as up
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.reddit_in_pictures);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String showNsfwImagesKey = getString(R.string.pref_show_nsfw_images);
        if (key.equals(showNsfwImagesKey)) {
            showNsfwImagesNewValue = sharedPreferences.getBoolean(showNsfwImagesKey, false);
        }

    }

    /**
     * Pass back whether the NSFW preference changed
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra(Consts.EXTRA_SHOW_NSFW_IMAGES_CHANGED, showNsfwImagesNewValue != showNsfwImagesOldValue);
        setResult(RESULT_OK, i);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Intent i = new Intent();
            i.putExtra(Consts.EXTRA_SHOW_NSFW_IMAGES_CHANGED, showNsfwImagesNewValue != showNsfwImagesOldValue);
            setResult(RESULT_OK, i);
            finish();
        }

        return true;
    }

}