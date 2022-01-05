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

    // Table info
    private static final String KEY_POST_ID = "id";
    private static final String KEY_POST_TITLE = "title";
    private static final String KEY_POST_MEDIA = "media";

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
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
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

        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s ", TABLE_POSTS);

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
}
