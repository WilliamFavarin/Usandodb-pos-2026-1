package com.willapphouse.usandodb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.willapphouse.usandodb.database.DatabaseHandler
import com.willapphouse.usandodb.databinding.ActivityMainBinding
import com.willapphouse.usandodb.entity.Cadastro

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var banco: DatabaseHandler

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

        banco = DatabaseHandler(this)

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

        val cadastro = Cadastro(
            0,
            binding.etNome.text.toString(),
            binding.etTelefone.text.toString()
        )

        banco.incluir(cadastro)

        Toast.makeText(this, "Inclusão efetuada com sucesso", Toast.LENGTH_LONG).show()

    }

    private fun alterar() {

        val cadastro = Cadastro(
            binding.etCod.text.toString().toInt(),
            binding.etNome.text.toString(),
            binding.etTelefone.text.toString()
        )

        banco.alterar(cadastro)

        Toast.makeText(
            this,
            "Alteração efetuada com sucesso",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun excluir() {

        banco.excluir(binding.etNome.text.toString().toInt())

        Toast.makeText(
            this,
            "Exclusão efetuada com sucesso!",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun pesquisar() {

        val cadastro = banco.pesquisar(binding.etCod.text.toString().toInt() )

        if ( cadastro != null ) {
            binding.etNome.setText( cadastro.nome )
            binding.etTelefone.setText( cadastro.telefone )
        } else {
            Toast.makeText(
                this,
                "Registro não encontrado",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun listar() {

        val registros = banco.listar()

        val saida = StringBuilder()

        registros.forEach { cadastro ->
            saida.append( cadastro.nome )
            saida.append("\n")
        }

        Toast.makeText(
            this,
            saida.toString(),
            Toast.LENGTH_LONG
        ).show()

    }

}