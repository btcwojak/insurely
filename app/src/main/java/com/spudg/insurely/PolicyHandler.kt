package com.spudg.insurely

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class PolicyHandler(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "SIOPolicies.db"
        private const val TABLE_POLICIES = "policies"

        private const val KEY_ID = "_id"
        private const val KEY_NOTE = "note"
        private const val KEY_TAG = "tag"
        private const val KEY_PRICE = "amount"
        private const val KEY_NEXT_MONTH = "next_month"
        private const val NOTIF_RC = "notif_rc"
        private const val KEY_NEXT_DAY = "next_day"
        private const val KEY_NEXT_YEAR = "next_year"
        private const val KEY_NEXT_DATE_MS = "next_date_millis"
        private const val KEY_FREQUENCY = "frequency"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createPolicyTable =
            ("CREATE TABLE $TABLE_POLICIES($KEY_ID INTEGER PRIMARY KEY,$KEY_NOTE TEXT,$KEY_TAG INT,$KEY_PRICE TEXT,$KEY_NEXT_MONTH INTEGER,$NOTIF_RC INTEGER,$KEY_NEXT_DAY INTEGER,$KEY_NEXT_YEAR INTEGER,$KEY_NEXT_DATE_MS TEXT,$KEY_FREQUENCY TEXT)")
        db?.execSQL(createPolicyTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_POLICIES")
        onCreate(db)
    }

    fun addPolicy(policy: PolicyModel): Long {
        val strDate = "${policy.nextDay}-${policy.nextMonth}-${policy.nextYear}"
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val nextDateMillis = sdf.parse(strDate)?.time

        val values = ContentValues()
        values.put(KEY_NOTE, policy.note)
        values.put(KEY_TAG, policy.tag)
        values.put(KEY_PRICE, policy.price)
        values.put(KEY_NEXT_MONTH, policy.nextMonth)
        values.put(NOTIF_RC, policy.notifRC)
        values.put(KEY_NEXT_DAY, policy.nextDay)
        values.put(KEY_NEXT_YEAR, policy.nextYear)
        values.put(KEY_NEXT_DATE_MS, nextDateMillis)
        values.put(KEY_FREQUENCY, policy.frequency)
        val db = this.writableDatabase
        val success = db.insert(TABLE_POLICIES, null, values)
        db.close()
        return success
    }

    fun updatePolicy(policy: PolicyModel): Int {
        val strDate = "${policy.nextDay}-${policy.nextMonth}-${policy.nextYear}"
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val nextDateMillis = sdf.parse(strDate)?.time

        val values = ContentValues()
        values.put(KEY_NOTE, policy.note)
        values.put(KEY_TAG, policy.tag)
        values.put(KEY_PRICE, policy.price)
        values.put(KEY_NEXT_MONTH, policy.nextMonth)
        values.put(NOTIF_RC, policy.notifRC)
        values.put(KEY_NEXT_DAY, policy.nextDay)
        values.put(KEY_NEXT_YEAR, policy.nextYear)
        values.put(KEY_NEXT_DATE_MS, nextDateMillis)
        values.put(KEY_FREQUENCY, policy.frequency)
        val db = this.writableDatabase
        val success = db.update(TABLE_POLICIES, values, KEY_ID + "=" + policy.id, null)
        db.close()
        return success
    }

    fun deletePolicy(policy: PolicyModel): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_POLICIES, KEY_ID + "=" + policy.id, null)
        db.close()
        return success
    }

    fun filterPolicies(sortBy: Int = 0): ArrayList<PolicyModel> {
        val list = ArrayList<PolicyModel>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_POLICIES",
            null
        )

        var id: Int
        var tag: Int
        var price: String
        var note: String
        var nextMonth: Int
        var notifRC: Int
        var nextDay: Int
        var nextYear: Int
        var nextDateMillis: String
        var frequency: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                tag = cursor.getInt(cursor.getColumnIndex(KEY_TAG))
                price = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                note = cursor.getString(cursor.getColumnIndex(KEY_NOTE))
                nextMonth = cursor.getInt(cursor.getColumnIndex(KEY_NEXT_MONTH))
                notifRC = cursor.getInt(cursor.getColumnIndex(NOTIF_RC))
                nextDay = cursor.getInt(cursor.getColumnIndex(KEY_NEXT_DAY))
                nextYear = cursor.getInt(cursor.getColumnIndex(KEY_NEXT_YEAR))
                nextDateMillis = cursor.getString(cursor.getColumnIndex(KEY_NEXT_DATE_MS))
                frequency = cursor.getString(cursor.getColumnIndex(KEY_FREQUENCY))
                val policy = PolicyModel(
                    id = id,
                    tag = tag,
                    price = price,
                    note = note,
                    nextMonth = nextMonth,
                    notifRC = notifRC,
                    nextDay = nextDay,
                    nextYear = nextYear,
                    nextDateMillis = nextDateMillis,
                    frequency = frequency
                )
                list.add(policy)
            } while (cursor.moveToNext())
        }

        if (sortBy == -1) {
            list.sortByDescending {
                it.nextDateMillis
            }
        }

        if (sortBy == 1) {
            list.sortBy {
                it.nextDateMillis
            }
        }

        cursor.close()
        db.close()

        return list

    }

    fun changePolicyTagDueToTagDeletion(tag: TagModel) {
        val values = ContentValues()
        values.put(KEY_TAG, 5)
        val db = this.writableDatabase
        db.update(TABLE_POLICIES, values, KEY_TAG + "=" + tag.id, null)
        db.close()
    }


}