package com.willapphouse.usandodb.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.willapphouse.usandodb.entity.Cadastro

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(bd: SQLiteDatabase?) {
        bd?.execSQL("CREATE TABLE IF NOT EXISTS " +
                "${TABLE_NAME}(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, telefone TEXT, email TEXT)"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    private fun incluir(cadastro : Cadastro) {

        val banco = this.writableDatabase

        val registro = ContentValues()
        registro.put( "nome", cadastro.nome )
        registro.put( "telefone", cadastro.telefone )

        banco.insert( TABLE_NAME, null, registro )
    }

    private fun alterar(cadastro: Cadastro) {

        val banco = this.writableDatabase

        val registro = ContentValues()
        registro.put( "nome", cadastro.nome )
        registro.put( "telefone", cadastro.telefone )

        banco.update(
            TABLE_NAME,
            registro,
            "_id = ${cadastro.id}",
            null )

    }

    private fun excluir(id: Int) {

        val banco = this.writableDatabase

        banco.delete(
            TABLE_NAME,
            "_id = $id",
            null
        )

    }

    private fun pesquisar(id: Int): Cadastro? {

        val banco = this.writableDatabase

        val registros = banco.query(
            TABLE_NAME,
            null,
            "_id = $id",
            null,
            null,
            null,
            null
        )

        if (registros.moveToNext()) {

            val cadastro = Cadastro(
                registros.getInt(ID),
                registros.getString(NOME),
                registros.getString(TELEFONE)
            )

            registros.close()

            return cadastro

        } else {
            return null
        }
    }

    private fun listar(): MutableList<Cadastro> {

        val banco = this.writableDatabase

        val registros = banco.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val saida = mutableListOf<Cadastro>()

        while ( registros.moveToNext() ) {

            val cadastro = Cadastro (
                registros.getInt(ID),
                registros.getString(NOME),
                registros.getString(TELEFONE)
            )
            saida.add( cadastro )
        }

        registros.close()

        return saida
    }

    private fun listarCursor(): Cursor {

        val banco = this.writableDatabase

        val registros = banco.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        return registros
    }

    companion object {
        private const val DATABASE_NAME = "banco.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "cadastro"
        private const val ID = 0
        private const val NOME = 1
        private const val TELEFONE = 2
    }
}