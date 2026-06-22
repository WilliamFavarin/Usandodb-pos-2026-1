package com.willapphouse.usandodb

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.willapphouse.usandodb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var banco: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        banco = SQLiteDatabase.openOrCreateDatabase(
            this.getDatabasePath("banco.db"),
            null
        )

        banco.execSQL("CREATE TABLE IF NOT EXISTS cadastro (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT)")

        binding.btIncluir.setOnClickListener {
            incluir()
        }

        binding.btAlterar.setOnClickListener {
            alterar()
        }

        binding.btExcluir.setOnClickListener {
            excluir()
        }

        binding.btPesquisar.setOnClickListener {
            pesquisar()
        }

        binding.btListar.setOnClickListener {
            listar()
        }
    }

    private fun incluir() {

        val registro = ContentValues()
        registro.put( "nome", binding.etNome.text.toString() )
        registro.put( "telefone", binding.etTelefone.text.toString() )

        banco.insert( "cadastro", null, registro )

        Toast.makeText(this, "Inclusão efetuada com sucesso", Toast.LENGTH_LONG).show()

    }

    private fun alterar() {

        val registro = ContentValues()
        registro.put( "nome", binding.etNome.text.toString() )
        registro.put( "telefone", binding.etTelefone.text.toString() )

        banco.update(
            "cadastro",
            registro,
            "_id = ${binding.etCod.text.toString()}",
            null )

        Toast.makeText(this, "Alteração efetuada com sucesso", Toast.LENGTH_LONG).show()

    }

    private fun excluir() {

        banco.delete(
            "cadastro",
            "_id = ${binding.etCod.text.toString()}",
            null
        )

        Toast.makeText(
            this,
            "Exclusão efetuada com sucesso!",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun pesquisar() {
        TODO("Not yet implemented")
    }

    private fun listar() {
        TODO("Not yet implemented")
    }
}