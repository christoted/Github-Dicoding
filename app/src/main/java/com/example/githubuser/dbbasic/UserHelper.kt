package com.example.githubuser.dbbasic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.githubuser.dbbasic.DatabaseContract.UserColumns.Companion.LOGIN
import com.example.githubuser.dbbasic.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuser.dbbasic.DatabaseContract.UserColumns.Companion._ID
import java.sql.SQLException

class UserHelper(context: Context) {
    private val databaseHelper:DatabaseHelper = DatabaseHelper(context)

    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: UserHelper? = null

        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }

    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
        database = databaseHelper.readableDatabase
    }

    fun close(){
        databaseHelper.close()

        if ( database.isOpen) {
            database.close()
        }
    }

    fun queryAll() : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues) {
        database.insert(DATABASE_TABLE,null, values)
    }

    fun insertForContentProvider(values: ContentValues?) : Long {
        return database.insert(DATABASE_TABLE,null, values)
    }

    fun deleteByLoginName(loginName: String) {
        database.delete(DATABASE_TABLE, "$LOGIN = '$loginName'", null)
    }

    fun deleteById(id: String) : Int{
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    fun queryById(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "${BaseColumns._ID} = ?", arrayOf(id), null, null, null, null)
    }
}