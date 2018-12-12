package com.example.sandpickle.music.db

import android.provider.BaseColumns

// This essentially holds static variables that we will need to reference when working with
// the database

class DbSettings {
    companion object {
        const val DB_NAME = "favourites.db"
        const val DB_VERSION = 1
    }

    class DBEntry: BaseColumns {
        companion object {
            const val TABLE = "favourites"
            const val ID = BaseColumns._ID
            const val COL_FAV_TITLE = "title"
            const val COL_FAV_DATE = "date"
            const val COL_FAV_BODY = "body"
            const val COL_FAV_CATEGORY = "category"
            const val COL_FAV_COMPLETE = "complete"
            const val COL_FAV_IMPORTANT = "important"
            const val COL_FAV_DUEDATE = "duedate"
        }
    }
}