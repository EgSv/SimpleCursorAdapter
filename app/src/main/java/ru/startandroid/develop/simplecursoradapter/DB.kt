package ru.startandroid.develop.simplecursoradapter

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.view.ContextMenu


class DB(ctx:Context) {

    private var mCtx:Context

    private var mDBHelper:DBHelper? = null
    private var mDB:SQLiteDatabase? = null

    init {
        mCtx = ctx
    }

    fun open() {
        mDBHelper = DBHelper(mCtx, DB_NAME, null, DB_VERSION)
        mDB = mDBHelper!!.writableDatabase
    }

    fun close() {
        if (mDBHelper != null) mDBHelper!!.close()
    }

    val allDate: Cursor
    get() = mDB!!.query(DB_TABLE, null, null, null, null, null, null)

    fun addRec(txt: String, img:Int) {
        val cv = ContentValues()
        cv.put(COLUMN_TXT, txt)
        cv.put(COLUMN_IMG,  img)
        mDB!!.insert(DB_TABLE, null, cv)
    }

    fun delRec(id: ContextMenu.ContextMenuInfo?) {
        mDB!!.delete(DB_TABLE, "$COLUMN_ID = $id", null)
    }

    inner class DBHelper(
        context: Context,
        name: String,
        factory: CursorFactory?,
        version: Int) : SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(DB_CREATE)

            val cv = ContentValues()
            for(i in 1 .. 5) {
                cv.put(COLUMN_TXT, "sometext $i")
                cv.put(COLUMN_IMG, R.drawable.ic_launcher)
                db.insert(DB_TABLE, null, cv)
            }
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }
    }
    companion object {
        private const val DB_NAME = "mydb"
        private const val DB_VERSION = 1
        private const val DB_TABLE = "mytab"

        const val COLUMN_ID = "_id"
        const val COLUMN_IMG = "img"
        const val COLUMN_TXT = "txt"

        private const val DB_CREATE = "create table " + DB_TABLE + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_IMG + " integer, " +
                COLUMN_TXT + " text" +
                ")"
    }
}