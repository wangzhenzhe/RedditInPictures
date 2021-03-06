package com.antew.redditinpictures.library.ui.base;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.android.debug.hv.ViewServer;
import com.antew.redditinpictures.library.BuildConfig;

/**
 * Base activity for an activity which does not use fragments.
 */
public abstract class BaseActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            ViewServer.get(this).addWindow(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BuildConfig.DEBUG) {
            ViewServer.get(this).removeWindow(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            ViewServer.get(this).setFocusedWindow(this);
        }
    }
}