package com.willapphouse.usandodb.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.willapphouse.usandodb.entity.Cadastro

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_NOME TEXT, " +
                "$COL_TELEFONE TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun incluir(cadastro: Cadastro) {
        val banco = this.writableDatabase
        val registro = ContentValues().apply {
            put(COL_NOME, cadastro.nome)
            put(COL_TELEFONE, cadastro.telefone)
        }
        banco.insert(TABLE_NAME, null, registro)
    }

    fun alterar(cadastro: Cadastro) {
        val banco = this.writableDatabase
        val registro = ContentValues().apply {
            put(COL_NOME, cadastro.nome)
            put(COL_TELEFONE, cadastro.telefone)
        }
        banco.update(TABLE_NAME, registro, "$COL_ID = ?", arrayOf(cadastro.id.toString()))
    }

    fun excluir(id: Int) {
        val banco = this.writableDatabase
        banco.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun pesquisar(id: Int): Cadastro? {
        val banco = this.readableDatabase
        val cursor = banco.query(
            TABLE_NAME, null, "$COL_ID = ?", arrayOf(id.toString()),
            null, null, null
        )

        return cursor.use {
            if (it.moveToNext()) {
                Cadastro(
                    id = it.getInt(it.getColumnIndexOrThrow(COL_ID)),
                    nome = it.getString(it.getColumnIndexOrThrow(COL_NOME)),
                    telefone = it.getString(it.getColumnIndexOrThrow(COL_TELEFONE))
                )
            } else {
                null
            }
        }
    }

    fun listar():  MutableList<Cadastro> {
        val banco = this.readableDatabase
        val saida = mutableListOf<Cadastro>()
        val cursor = banco.query(TABLE_NAME, null, null, null, null, null, null)

        cursor.use {
            while (it.moveToNext()) {
                saida.add(
                    Cadastro(
                        id = it.getInt(it.getColumnIndexOrThrow(COL_ID)),
                        nome = it.getString(it.getColumnIndexOrThrow(COL_NOME)),
                        telefone = it.getString(it.getColumnIndexOrThrow(COL_TELEFONE))
                    )
                )
            }
        }
        return saida
    }

    fun listarCursor(): Cursor {
        return readableDatabase.query(TABLE_NAME, null, null, null, null, null, null)
    }

    companion object {
        private const val DATABASE_NAME = "banco.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "cadastro"
        
        private const val COL_ID = "_id"
        private const val COL_NOME = "nome"
        private const val COL_TELEFONE = "telefone"
    }
}
