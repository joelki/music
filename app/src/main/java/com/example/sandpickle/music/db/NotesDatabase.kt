package com.example.sandpickle.music.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sandpickle.music.db.DbSettings

// This essentially creates and upgrades

class NotesDatabase(context: Context): SQLiteOpenHelper(context, DbSettings.DB_NAME, null, DbSettings.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE " + DbSettings.DBEntry.TABLE + " ( " +
                DbSettings.DBEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbSettings.DBEntry.COL_FAV_TITLE + " TEXT NOT NULL, " +
                DbSettings.DBEntry.COL_FAV_BODY + " TEXT NOT NULL, " +
                DbSettings.DBEntry.COL_FAV_CATEGORY + " TEXT NOT NULL, " +
                DbSettings.DBEntry.COL_FAV_DATE + " INTEGER NOT NULL, " +
                DbSettings.DBEntry.COL_FAV_COMPLETE + " STRING NOT NULL, " +
                DbSettings.DBEntry.COL_FAV_IMPORTANT + " INTEGER NOT NULL, " +
                DbSettings.DBEntry.COL_FAV_DUEDATE + " INTEGER NOT NULL);"




// added DbSettings.DBEntry.COL_FAV_TEXT + " TEXT NOT NULL, " +
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + DbSettings.DBEntry.TABLE)
        onCreate(db)
    }
}