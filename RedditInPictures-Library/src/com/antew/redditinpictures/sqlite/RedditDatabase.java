package com.antew.redditinpictures.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.antew.redditinpictures.sqlite.RedditContract.PostColumns;
import com.antew.redditinpictures.sqlite.RedditContract.RedditDataColumns;

public class RedditDatabase extends SQLiteOpenHelper {

    private static final int    DATABASE_VERSION      = 1;
    private static final String DATABASE_NAME         = "redditinpictures.db";

    public interface Tables {
        String POSTDATA    = "post_data";
        String REDDIT_DATA = "reddit_data";
        
        interface CreateTableSql {
            //@formatter:off
            String POSTDATA = "CREATE TABLE " + Tables.POSTDATA + 
                    " (" 
                    + BaseColumns._ID                               + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
                    + PostColumns.DOMAIN                 + " TEXT NOT NULL, "
                    + PostColumns.BANNED_BY              + " TEXT, "
                    + PostColumns.SUBREDDIT              + " TEXT NOT NULL, "
                    + PostColumns.SELFTEXT_HTML          + " TEXT, "
                    + PostColumns.SELFTEXT               + " TEXT, "
                    + PostColumns.VOTE                   + " TEXT NOT NULL, "
                    + PostColumns.SAVED                  + " INTEGER, "
                    + PostColumns.POST_ID                + " TEXT NOT NULL, "
                    + PostColumns.CLICKED                + " INTEGER, "
                    + PostColumns.TITLE                  + " TEXT, "
                    + PostColumns.COMMENTS               + " INTEGER, "
                    + PostColumns.SCORE                  + " INTEGER, "
                    + PostColumns.APPROVED_BY            + " TEXT, "
                    + PostColumns.OVER_18                + " INTEGER, "
                    + PostColumns.HIDDEN                 + " INTEGER, "
                    + PostColumns.THUMBNAIL              + " TEXT, "
                    + PostColumns.SUBREDDIT_ID           + " TEXT NOT NULL, "
                    + PostColumns.AUTHOR_FLAIR_CSS_CLASS + " TEXT, "
                    + PostColumns.DOWNS                  + " INTEGER, "
                    + PostColumns.IS_SELF                + " INTEGER, "
                    + PostColumns.PERMALINK              + " TEXT NOT NULL, "
                    + PostColumns.NAME                   + " TEXT NOT NULL, "
                    + PostColumns.CREATED                + " INTEGER, "
                    + PostColumns.URL                    + " TEXT, "
                    + PostColumns.AUTHOR_FLAIR_TEXT      + " TEXT, "
                    + PostColumns.AUTHOR                 + " TEXT NOT NULL, "
                    + PostColumns.CREATED_UTC            + " INTEGER, "
                    + PostColumns.LINK_FLAIR_TEXT        + " TEXT, "
                    + PostColumns.DECODED_URL            + " TEXT "
//                    + PostDataColumns.MODHASH                + " TEXT NOT NULL, "
//                    + "FOREIGN KEY (" + PostDataColumns.MODHASH + ")" + " REFERENCES " + Tables.REDDIT_DATA + " (" + RedditDataColumns.MODHASH + ")"
                    + " );";
            
            String REDDIT_DATA = "CREATE TABLE " + Tables.REDDIT_DATA +
                    " ("
                    + BaseColumns._ID           + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RedditDataColumns.MODHASH + " TEXT NOT NULL, "
                    + RedditDataColumns.AFTER   + " TEXT, "
                    + RedditDataColumns.BEFORE  + " TEXT "
                    + ");";
            //@formatter:on
        }
    }

    public RedditDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tables.CreateTableSql.POSTDATA);
        db.execSQL(Tables.CreateTableSql.REDDIT_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.POSTDATA);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.REDDIT_DATA);
        onCreate(db);
    }
    
    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

}