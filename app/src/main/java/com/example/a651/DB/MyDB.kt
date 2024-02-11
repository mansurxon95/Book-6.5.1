package com.example.a651.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.net.toUri
import com.example.a651.Usercontact
import com.example.a651.interfases.Usertab

class MyDB(context: Context): SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION),MyDBservis {


    companion object{
        const val DB_NAME = "YPX"
        const val DB_VERSION = 1


        const val TABLE_YPX_NAME = "name"

        const val YPX_ID = "ypxid"
        const val  YPX_NAME = "sarlavxa"
        const val YPX_TAVSIFI = "matni"
        const val YPX_IMAGE = "image"
        const val YPX_TAB_ID = "yt_id"
        const val YPX_SAVE = "nol"


        const val TABLE_TAB_NAME = "tablename"
        const val TAB_ID = "tabid"
        const val TAB_NAME = "tabname"
    }

    override fun onCreate(db: SQLiteDatabase?) {


        val query1 = "create table $TABLE_YPX_NAME($YPX_ID integer not null primary key autoincrement unique," +
                "$YPX_NAME text not null," +
                "$YPX_TAVSIFI text not null," +
                "$YPX_IMAGE text not null," +
                "$YPX_SAVE INTEGER not null," +
                "$YPX_TAB_ID INTEGER not null," +
                "FOREIGN KEY($YPX_TAB_ID) REFERENCES $TABLE_TAB_NAME($TAB_ID))"

        val query2 = "create table $TABLE_TAB_NAME ($TAB_ID integer not null primary key autoincrement," +
                "$TAB_NAME text not null)"

        db?.execSQL(query1)
        db?.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    override fun addypx(user: Usercontact) {
        val writableDatabase = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(YPX_NAME,user.name)
        contentValues.put(YPX_TAVSIFI,user.tavsif)

        contentValues.put(YPX_IMAGE,user.image.toString())     // TODO: image Int ?
        contentValues.put(YPX_TAB_ID,user.tableId)
        contentValues.put(YPX_SAVE,user.save.toString())
        writableDatabase.insert(TABLE_YPX_NAME,null,contentValues)
        writableDatabase.close()
    }

    override fun editypx(user: Usercontact): Int {
        val writableDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(YPX_NAME, user.name)
        contentValues.put(YPX_TAVSIFI, user.tavsif)

        contentValues.put(YPX_IMAGE, user.image.toString())
        contentValues.put(YPX_TAB_ID, user.tableId)
        contentValues.put(YPX_SAVE, user.save.toString())
        return writableDatabase.update(TABLE_YPX_NAME, contentValues, "${YPX_ID}=?", arrayOf(user.id.toString()))

    }


    override fun editypxsave(user: Usercontact,save:Int): Int {
        val writableDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(YPX_NAME, user.name)
        contentValues.put(YPX_TAVSIFI, user.tavsif)

        contentValues.put(YPX_IMAGE, user.image.toString())
        contentValues.put(YPX_TAB_ID, user.tableId)
        contentValues.put(YPX_SAVE, save.toString())
        return writableDatabase.update(TABLE_YPX_NAME,contentValues,"${YPX_ID}=?", arrayOf(user.id.toString()))

//        return writableDatabase.insert(YPX_SAVE, YPX_SAVE,contentValues).toInt()
    }

    override fun deletypx(user: Usercontact) {
        val writableDatabase = this.writableDatabase
        writableDatabase.delete(TABLE_YPX_NAME, "${YPX_ID}=?", arrayOf("${user.id}"))
    }

    override fun delettab(tab: Usertab) {
        val writableDatabase = this.writableDatabase
        writableDatabase.delete(TABLE_TAB_NAME, "${TAB_ID}=?", arrayOf("${tab.id_tab}"))
    }

    override fun getallypx(tableId: Int): ArrayList<Usercontact> {

        Log.d("1234", "table id: $tableId")
        var list2 = ArrayList<Usercontact>()
        val query = "SELECT $YPX_ID, $YPX_NAME, $YPX_TAVSIFI, $YPX_IMAGE, $YPX_SAVE, $YPX_TAB_ID from " +
                "$TABLE_YPX_NAME inner join $TABLE_TAB_NAME ON $YPX_TAB_ID == $tableId"

        val readableDatabase = this.readableDatabase
        val crusor = readableDatabase.rawQuery(query,null)
        if (crusor.moveToFirst()){
            do {
                val id = crusor.getInt(0)
                val name = crusor.getString(1)
                val tavsifi = crusor.getString(2)

                val image = crusor.getString(3)
                val save = crusor.getInt(4)
                val tableId = crusor.getInt(5)
                Log.d("1234", "id: $id  name: $name")

                list2.add(Usercontact(id,name,tavsifi,image.toUri(),save,tableId))      // TODO: save get boolean bo'lishi kerak
            } while ( crusor.moveToNext())
        }

        return list2
    }


    override fun getallypx2(idcount:Int): ArrayList<Usercontact> {
        var list2 = ArrayList<Usercontact>()
        val query = "SELECT * from $TABLE_YPX_NAME"

        val readableDatabase = this.readableDatabase
        val crusor = readableDatabase.rawQuery(query,null)
        if (crusor.moveToFirst()){
            do {
                val id = crusor.getInt(0)
                val name = crusor.getString(1)
                val tavsifi = crusor.getString(2)
                val image = crusor.getString(3)
                val save = crusor.getInt(4)
                val tableId = crusor.getInt(5)

                if (tableId==idcount){
                    list2.add(Usercontact(id,name,tavsifi,image.toUri(),save,tableId))
                }
            } while ( crusor.moveToNext())
        }

        return list2
    }

    override fun getallsaveypx(): ArrayList<Usercontact> {
        var list2 = ArrayList<Usercontact>()
        val query = "SELECT * from $TABLE_YPX_NAME"

        val readableDatabase = this.readableDatabase
        val crusor = readableDatabase.rawQuery(query,null)
        if (crusor.moveToFirst()){
            do {
                val id = crusor.getInt(0)
                val name = crusor.getString(1)
                val tavsifi = crusor.getString(2)
                val image = crusor.getString(3)
                val save = crusor.getInt(4)
                val tableId = crusor.getInt(5)
                list2.add(Usercontact(id,name,tavsifi,image.toUri(),save,tableId))
            } while ( crusor.moveToNext())
        }

        return list2
    }

    override fun getalltab(): ArrayList<Usertab> {
        var list2 = ArrayList<Usertab>()
        val query = "SELECT * from $TABLE_TAB_NAME"
        val readableDatabase = this.readableDatabase
        val crusor = readableDatabase.rawQuery(query,null)
        if (crusor.moveToFirst()){
            do {
                val id = crusor.getInt(0)
                val tabname = crusor.getString(1)
                list2.add(Usertab(id,tabname))
            } while ( crusor.moveToNext())
        }

        return list2
    }

    override fun addtab(tabname: String) {
        val writableDatabase = this.writableDatabase
        var contentValues = ContentValues()

        contentValues.put(TAB_NAME,tabname)
        writableDatabase.insert(TABLE_TAB_NAME,null,contentValues)
        writableDatabase.close()
    }


}
