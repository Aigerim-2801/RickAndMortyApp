package com.example.retrofitapp.data.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.retrofitapp.domain.model.character.Location
import com.example.retrofitapp.domain.model.character.Origin
import com.example.retrofitapp.domain.model.character.ResultsCharacter

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db"
        private const val TABLE = "favoriteCharacters"
        private const val DATABASE_VERSION = 1
        private const val ID = "id"
        private const val NAME = "name"
        private const val STATUS = "status"
        private const val IMAGE = "image"
        private const val ISFAVORITE = "isFavorite"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE ($ID INTEGER PRIMARY KEY, $NAME TEXT, $STATUS TEXT, $IMAGE TEXT, $ISFAVORITE TEXT)"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun save(character: ResultsCharacter) {
        val values = ContentValues()
        values.put(ID, character.id)
        values.put(NAME, character.name)
        values.put(STATUS, character.status)
        values.put(IMAGE, character.image)
        values.put(ISFAVORITE, character.isFavorite.toString())

        val db = this.writableDatabase
        db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
    }

    fun remove(character: ResultsCharacter) {
        val db = this.writableDatabase
        db.delete(TABLE, "$ID=?", arrayOf(character.id.toString()))
        db.close()
    }

    fun getAllFavoriteItems(): List<ResultsCharacter> {
        val characterList = mutableListOf<ResultsCharacter>()
        val selectQuery = "SELECT DISTINCT * FROM $TABLE WHERE $ISFAVORITE = 'true'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null && cursor.count != 0) {
            cursor.moveToFirst()
            do {
                val character = ResultsCharacter(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    status = cursor.getString(2),
                    species = "",
                    type = "",
                    gender = "",
                    origin = Origin("", ""),
                    location = Location("", ""),
                    image = cursor.getString(3),
                    episode = listOf(),
                    url = "",
                    created = "",
                    isFavorite = cursor.getString(4) == "true"
                )
                characterList.add(character)
            } while (cursor.moveToNext())
        }
        return characterList.toList()
    }

}
