package com.example.sandpickle.music.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.sandpickle.music.SharedPrefKeys
import com.example.sandpickle.music.model.Favourite
import com.example.sandpickle.music.db.DbSettings
import com.example.sandpickle.music.db.NotesDatabase

// Implements all necessary functions for a Favourite entity in the database

class FavouriteViewModel(application: Application): AndroidViewModel(application) {
    private var _favouriteDBHelper: NotesDatabase = NotesDatabase(application)
    private var _favouritesList: MutableLiveData<ArrayList<Favourite>> = MutableLiveData() // Here's where the magic happens
    private var _categoriesList: MutableLiveData<ArrayList<String>> = MutableLiveData() // Here's where the magic happens

    //observer detects when there's change and updates the ui

    fun getFavourites(prefs: SharedPreferences, retrieveStrings: String):  MutableLiveData<ArrayList<Favourite>> {
        loadFavourites(prefs,retrieveStrings)
        return _favouritesList
    }
    fun getCategories(): MutableLiveData<ArrayList<String>> {
        loadCategories()
        return _categoriesList
    }
    private fun loadCategories(){
        val newCategories: ArrayList<String> = ArrayList()

        val cursor: Cursor //select certain things from a database
        val database: SQLiteDatabase = this._favouriteDBHelper.readableDatabase
        cursor = database.query(
                DbSettings.DBEntry.TABLE,
                arrayOf(
                        DbSettings.DBEntry.ID,
                        DbSettings.DBEntry.COL_FAV_TITLE,
                        DbSettings.DBEntry.COL_FAV_DATE,
                        DbSettings.DBEntry.COL_FAV_BODY,
                        DbSettings.DBEntry.COL_FAV_CATEGORY,
                        DbSettings.DBEntry.COL_FAV_COMPLETE,
                        DbSettings.DBEntry.COL_FAV_IMPORTANT,
                        DbSettings.DBEntry.COL_FAV_DUEDATE

                        //added to the array for DbSettings.DBEntry.COL_FAV_TEXT,
                ),
                null, null, null, null, DbSettings.DBEntry.COL_FAV_TITLE
        )
        while (cursor.moveToNext()) {
            val cursorCategory = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_CATEGORY)
            //if does not contain current category (checking for duplicates)
                if(!newCategories.contains(cursor.getString(cursorCategory))) {
                    newCategories.add(
                            cursor.getString(cursorCategory)
                    )
                }
        }
        cursor.close() //if not could lead to memory leaks
        database.close()
        this._categoriesList.value = newCategories
    }

    private fun loadFavourites(prefs: SharedPreferences, retrieveStrings: String) {
        val newFavourites: ArrayList<Favourite> = ArrayList()
        val database: SQLiteDatabase = this._favouriteDBHelper.readableDatabase

        // Check Shared Preferences
        val cursor: Cursor //select certain things from a database

        if (prefs.getInt(SharedPrefKeys.sortOption, 0) == 0) { //0 if you want to sort by url
            cursor = database.query(
                    DbSettings.DBEntry.TABLE,
                    arrayOf(
                            DbSettings.DBEntry.ID,
                            DbSettings.DBEntry.COL_FAV_TITLE,
                            DbSettings.DBEntry.COL_FAV_DATE,
                            DbSettings.DBEntry.COL_FAV_BODY,
                            DbSettings.DBEntry.COL_FAV_CATEGORY,
                            DbSettings.DBEntry.COL_FAV_COMPLETE,
                            DbSettings.DBEntry.COL_FAV_IMPORTANT,
                            DbSettings.DBEntry.COL_FAV_DUEDATE
                            //added to the array for DbSettings.DBEntry.COL_FAV_TEXT,
                    ),
                    null, null, null, null, DbSettings.DBEntry.COL_FAV_TITLE
            )
        }

        else if (prefs.getInt(SharedPrefKeys.sortOption, 0) == 2){
            cursor = database.query(
                    DbSettings.DBEntry.TABLE,
                    arrayOf(
                            DbSettings.DBEntry.ID,
                            DbSettings.DBEntry.COL_FAV_TITLE,
                            DbSettings.DBEntry.COL_FAV_DATE,
                            DbSettings.DBEntry.COL_FAV_BODY,
                            DbSettings.DBEntry.COL_FAV_CATEGORY,
                            DbSettings.DBEntry.COL_FAV_COMPLETE,
                            DbSettings.DBEntry.COL_FAV_IMPORTANT,
                            DbSettings.DBEntry.COL_FAV_DUEDATE

                            //added to the array TEXT
                    ),
                    null, null, null, null, DbSettings.DBEntry.COL_FAV_IMPORTANT
            )
        }

        else{
            cursor = database.query(
                    DbSettings.DBEntry.TABLE,
                    arrayOf(
                            DbSettings.DBEntry.ID,
                            DbSettings.DBEntry.COL_FAV_TITLE,
                            DbSettings.DBEntry.COL_FAV_DATE,
                            DbSettings.DBEntry.COL_FAV_BODY,
                            DbSettings.DBEntry.COL_FAV_CATEGORY,
                            DbSettings.DBEntry.COL_FAV_COMPLETE,
                            DbSettings.DBEntry.COL_FAV_IMPORTANT,
                            DbSettings.DBEntry.COL_FAV_DUEDATE

                            //added to the array TEXT
                    ),
                    null, null, null, null, null
            )
        }

        while (cursor.moveToNext()) {
            val cursorId = cursor.getColumnIndex(DbSettings.DBEntry.ID)
            val cursorTitle = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_TITLE)
            val cursorBody = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_BODY)
            val cursorCategory = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_CATEGORY)
            val cursorDate = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_DATE)
            val cursorComplete = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_COMPLETE)
            val cursorImportant = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_IMPORTANT)
            val cursorDueDate = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_DUEDATE)
            if (cursor.getString(cursorCategory) == retrieveStrings){
                newFavourites.add( //in this case, adding to a list of favortes.
                        Favourite(
                                cursor.getLong(cursorId),
                                cursor.getString(cursorTitle),
                                cursor.getString(cursorBody),
                                cursor.getString(cursorCategory),
                                cursor.getLong(cursorDate),
                                cursor.getString(cursorComplete),
                                cursor.getInt(cursorImportant),
                                cursor.getLong(cursorDueDate)
                        )
                )
            }
        }

        cursor.close() //if not could lead to memory leaks
        database.close()
        this._favouritesList.value = newFavourites
    }

    fun addFavourite(title: String, body: String, category: String, date: Long, complete: String, important: Int, dueDate: Long) {
        val database: SQLiteDatabase = _favouriteDBHelper.writableDatabase
        val values = ContentValues()
        values.put(DbSettings.DBEntry.COL_FAV_TITLE, title)
        values.put(DbSettings.DBEntry.COL_FAV_BODY, body)
        values.put(DbSettings.DBEntry.COL_FAV_CATEGORY, category)
        values.put(DbSettings.DBEntry.COL_FAV_DATE, date)
        values.put(DbSettings.DBEntry.COL_FAV_COMPLETE, complete)
        values.put(DbSettings.DBEntry.COL_FAV_IMPORTANT, important)
        values.put(DbSettings.DBEntry.COL_FAV_DUEDATE, dueDate)

        val id = database.insertWithOnConflict( // general method of inserting into the database
                DbSettings.DBEntry.TABLE,
                null,             // allows insertion of null values into the db in the event that values doesn't contain something
                values,
                SQLiteDatabase.CONFLICT_REPLACE // an algorithm for resolving value conflicts
        )
        database.close()

        var favouritesList: ArrayList<Favourite>? = this._favouritesList.value
        if (favouritesList == null) {
            favouritesList = ArrayList()
        }

        favouritesList.add(
                Favourite(
                        id,
                        title,
                        body,
                        category,
                        date,
                        complete,
                        important,
                        dueDate
                )
        )
        this._favouritesList.value = favouritesList
        loadCategories()
    }

    fun retrieve(id: String): Favourite {
        val cursor: Cursor //select certain things from a database
        val database: SQLiteDatabase = this._favouriteDBHelper.readableDatabase
        cursor = database.query(
                DbSettings.DBEntry.TABLE,
                arrayOf(
                        DbSettings.DBEntry.ID,
                        DbSettings.DBEntry.COL_FAV_TITLE,
                        DbSettings.DBEntry.COL_FAV_DATE,
                        DbSettings.DBEntry.COL_FAV_BODY,
                        DbSettings.DBEntry.COL_FAV_CATEGORY,
                        DbSettings.DBEntry.COL_FAV_COMPLETE,
                        DbSettings.DBEntry.COL_FAV_IMPORTANT,
                        DbSettings.DBEntry.COL_FAV_DUEDATE

                ),
                null, null, null, null, null
        )
        while (cursor.moveToNext()) {
            val cursorId = cursor.getColumnIndex(DbSettings.DBEntry.ID)
            val cursorTitle = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_TITLE)
            val cursorBody = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_BODY)
            val cursorCategory = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_CATEGORY)
            val cursorDate = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_DATE)
            val cursorComplete = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_COMPLETE)
            val cursorImportant = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_IMPORTANT)
            val cursorDueDate = cursor.getColumnIndex(DbSettings.DBEntry.COL_FAV_DUEDATE)


            cursor.getString(cursorTitle)
            if (id == cursor.getLong(cursorId).toString()) {
                return Favourite(
                        cursor.getLong(cursorId),
                        cursor.getString(cursorTitle),
                        cursor.getString(cursorBody),
                        cursor.getString(cursorCategory),
                        cursor.getLong(cursorDate),
                        cursor.getString(cursorComplete),
                        cursor.getInt(cursorImportant),
                        cursor.getLong(cursorDueDate)
                )
            }

        }
        return null!!
    }

    fun removeFavourite(id: Long) {
        val database: SQLiteDatabase = _favouriteDBHelper.writableDatabase
        database.delete(
                DbSettings.DBEntry.TABLE,
                DbSettings.DBEntry.ID + " = ?",
                arrayOf(id.toString())
        )
        database.close()

        var index = 0
        val favourites: ArrayList<Favourite>? = this._favouritesList.value
        if (favourites != null) {
            for (i in 0 until favourites.size) {
                if (favourites[i].id == id) {
                    index = i
                }
            }
            favourites.removeAt(index)
            this._favouritesList.value = favourites
        }
    }
}