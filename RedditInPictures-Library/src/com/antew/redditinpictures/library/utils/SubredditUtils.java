package com.antew.redditinpictures.library.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.antew.redditinpictures.library.R;
import com.antew.redditinpictures.library.json.JsonDeserializer;
import com.antew.redditinpictures.library.preferences.SharedPreferencesHelper;
import com.antew.redditinpictures.library.reddit.MySubreddits;
import com.antew.redditinpictures.library.reddit.SubredditChildren;
import com.antew.redditinpictures.library.reddit.SubredditData;
import com.antew.redditinpictures.library.subredditmanager.SubredditManager;
import com.antew.redditinpictures.sqlite.RedditContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class SubredditUtils {
    public static void setDefaultSubreddits(Context context) {
        if (context == null) {
            Ln.e("Got a Null Context");
            return;
        }

        Ln.d("Setting Default Subreddits");
        // Get the defaults subreddit response (static JSON).
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.default_subreddits)));

        String fakeJson = "";
        String line;
        try
        {
            line = reader.readLine();
            while (line != null)
            {
                fakeJson = fakeJson + line;
                line = reader.readLine();
            }
        }
        catch (IOException e)
        {
            Ln.e(e, "Failed to Load Default Subreddits");
            return;
        }

        ContentResolver resolver = context.getContentResolver();

        int userRowsDeleted = resolver.delete(RedditContract.Subreddits.CONTENT_URI, null, null);
        Ln.d("Deleted %d Subreddits", userRowsDeleted);

        MySubreddits mySubreddits = JsonDeserializer.deserialize(fakeJson, MySubreddits.class);

        if (mySubreddits == null) {
            Ln.e("Failed to Desearialize Default Subreddits");
            return;
        }

        ContentValues[] operations = mySubreddits.getContentValuesArray();

        int rowsInserted = resolver.bulkInsert(RedditContract.Subreddits.CONTENT_URI, operations);
        Ln.d("Inserted %d subreddits", rowsInserted);

        ArrayList<String> subReddits = new ArrayList<String>();

        for (SubredditChildren c : mySubreddits.getData().getChildren()) {
            SubredditData data = c.getData();
            subReddits.add(data.getDisplay_name());
        }

        Collections.sort(subReddits, StringUtil.getCaseInsensitiveComparator());
        SharedPreferencesHelper.saveArray(subReddits, SubredditManager.PREFS_NAME, SubredditManager.ARRAY_NAME, context);

        Intent intent = new Intent(Consts.BROADCAST_MY_SUBREDDITS);
        intent.putStringArrayListExtra(Consts.EXTRA_MY_SUBREDDITS, subReddits);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
