package com.insacvlasl.projet_final.modeles;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBSQlite extends SQLiteOpenHelper {

    private final String TAG = this.getClass().getName();

    // Database info
    private static final String DATABASE_NAME = "DBSQlite";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_POSTS = "posts";
    private static final String TABLE_RECENT = "recents";

    // Table posts info
    private static final String KEY_POST_ID = "id";
    private static final String KEY_POST_TITLE = "title";
    private static final String KEY_POST_MEDIA = "media";

    // Table recents info
    private static final String KEY_RECENT_ID = "id";
    private static final String KEY_RECENT_KEYWORD = "keyword";

    private static DBSQlite sInstance;

    public static synchronized DBSQlite getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBSQlite(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBSQlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS +
                "(" +
                KEY_POST_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_POST_TITLE + " TEXT" + "," +
                KEY_POST_MEDIA + " TEXT" +
                ")";

        String CREATE_RECENT_TABLE = "CREATE TABLE " + TABLE_RECENT +
                "(" +
                KEY_RECENT_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_RECENT_KEYWORD + " TEXT" +
                ")" ;

        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_RECENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT);
            onCreate(db);
        }
    }

    public void insertPost(PostItem item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_POST_TITLE, item.title);
            values.put(KEY_POST_MEDIA, item.media);
            db.insertOrThrow(TABLE_POSTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    @SuppressLint("Range")
    public List<PostItem> getAllPostItems() {
        List<PostItem> posts = new ArrayList<>();

        String POSTS_SELECT_QUERY = String.format("SELECT DISTINCT %s, %s FROM %s ", KEY_POST_MEDIA, KEY_POST_TITLE, TABLE_POSTS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    PostItem newPost = new PostItem();
                    newPost.title = cursor.getString(cursor.getColumnIndex(KEY_POST_TITLE));
                    newPost.media = cursor.getString(cursor.getColumnIndex(KEY_POST_MEDIA));
                    posts.add(newPost);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }

    public void deleteAllPostItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_POSTS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void deletePost(PostItem post) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_POSTS,
                    KEY_POST_TITLE + "=? AND " + KEY_POST_MEDIA + "=?",
                    new String[] {post.title, post.media});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    public void insertKeyword(String keyword) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_RECENT_KEYWORD, keyword);
            db.insertOrThrow(TABLE_RECENT, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    @SuppressLint("Range")
    public String getLastKeyword () {
        String keyword = "cats";

        String RECENT_SELECT_QUERY = String.format("SELECT %s FROM %s ORDER BY %s DESC LIMIT 1", KEY_RECENT_KEYWORD, TABLE_RECENT, KEY_RECENT_ID);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(RECENT_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    keyword = cursor.getString(cursor.getColumnIndex(KEY_RECENT_KEYWORD));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return keyword;
    }

    @SuppressLint("Range")
    public List<String> get10LastKeywords() {
        List<String> keywords = new ArrayList<>();

        String KEYWORDS_SELECT_QUERY = String.format("SELECT DISTINCT %s FROM %s ORDER BY %s DESC LIMIT 10", KEY_RECENT_KEYWORD, TABLE_RECENT, KEY_RECENT_ID);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(KEYWORDS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String keyword = cursor.getString(cursor.getColumnIndex(KEY_RECENT_KEYWORD));
                    keywords.add(keyword);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return keywords;
    }
}
