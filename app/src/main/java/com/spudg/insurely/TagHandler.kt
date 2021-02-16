package com.spudg.insurely

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TagHandler(context: Context, factory: SQLiteDatabase.CursorFactory?) :
        SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "SIOTags.db"
        private const val TABLE_TAGS = "tags"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_COLOUR = "colour"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTagsTable =
                ("CREATE TABLE $TABLE_TAGS($KEY_ID INTEGER PRIMARY KEY,$KEY_NAME TEXT,$KEY_COLOUR TEXT)")
        db?.execSQL(createTagsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TAGS")
        onCreate(db)
    }

    fun addTag(tag: TagModel) {
        val values = ContentValues()
        values.put(KEY_NAME, tag.name)
        values.put(KEY_COLOUR, tag.colour)
        val existingTitles = getAllTagTitles()

        val db = this.writableDatabase

        var alreadyExists = false
        for (title in existingTitles) {
            if (title == tag.name) {
                alreadyExists = true
            }
        }

        if (!alreadyExists) {
            db.insert(TABLE_TAGS, null, values)
            Constants.TAG_UNIQUE_TITLE = 1
        } else {
            Constants.TAG_UNIQUE_TITLE = 0
        }

        db.close()
    }

    fun updateTag(tag: TagModel) {
        val values = ContentValues()
        values.put(KEY_NAME, tag.name)
        values.put(KEY_COLOUR, tag.colour)
        val existingTitles = getAllTagTitles()

        val dbForSearch = this.readableDatabase
        val dbForUpdate = this.writableDatabase

        if (existingTitles.contains(tag.name)) {
            Constants.TAG_UNIQUE_TITLE = 0
        } else {
            dbForUpdate.update(TABLE_TAGS, values, KEY_ID + "=" + tag.id, null)
            Constants.TAG_UNIQUE_TITLE = 1
        }

        if (Constants.TAG_UNIQUE_TITLE == 0) {
            val cursor =
                    dbForSearch.rawQuery(
                            "SELECT * FROM $TABLE_TAGS WHERE _id = ${tag.id}",
                            null
                    )
            if (cursor.moveToFirst()) {
                val oldName = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val newName = tag.name
                if (oldName == newName) {
                    dbForUpdate.update(
                            TABLE_TAGS,
                            values,
                            KEY_ID + "=" + tag.id,
                            null
                    )
                    Constants.TAG_UNIQUE_TITLE = 1
                } else {
                    Constants.TAG_UNIQUE_TITLE = 0
                }
            }

            cursor.close()
            dbForSearch.close()
            dbForUpdate.close()

        }

    }

    fun deleteTag(tag: TagModel): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_TAGS, KEY_ID + "=" + tag.id, null)
        db.close()
        return success
    }

    fun getAllTags(): ArrayList<TagModel> {
        val list = ArrayList<TagModel>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TAGS", null)

        var id: Int
        var name: String
        var colour: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                colour = cursor.getString(cursor.getColumnIndex(KEY_COLOUR))
                val tag = TagModel(
                        id = id,
                        name = name,
                        colour = colour,
                )
                list.add(tag)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list

    }

    fun getAllTagTitles(): ArrayList<String> {
        val list = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TAGS", null)

        var name: String

        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                list.add(name)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list

    }

    fun getTagColour(tagId: Int): Int {
        val db = this.readableDatabase

        val cursor =
                db.rawQuery("SELECT * FROM $TABLE_TAGS WHERE $KEY_ID = '$tagId'", null)

        val colour: Int = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(KEY_COLOUR)).toInt()
        } else {
            0
        }

        cursor.close()
        db.close()

        return colour

    }

    fun getTagId(tagTitle: String): Int {
        val db = this.readableDatabase

        val cursor =
                db.rawQuery("SELECT * FROM $TABLE_TAGS WHERE $KEY_NAME = '$tagTitle'", null)

        val id: Int = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndex(KEY_ID))
        } else {
            0
        }

        cursor.close()
        db.close()

        return id

    }

    fun getTagTitle(tagId: Int): String {
        val db = this.readableDatabase

        val cursor =
                db.rawQuery("SELECT * FROM $TABLE_TAGS WHERE $KEY_ID = $tagId", null)

        val name: String = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(KEY_NAME))
        } else {
            "Error"
        }

        cursor.close()
        db.close()

        return name

    }


}