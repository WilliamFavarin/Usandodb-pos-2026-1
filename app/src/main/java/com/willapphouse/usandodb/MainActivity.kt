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

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        banco = DatabaseHandler(this)

        binding.btIncluir.setOnClickListener { incluir() }
        binding.btAlterar.setOnClickListener { alterar() }
        binding.btExcluir.setOnClickListener { excluir() }
        binding.btPesquisar.setOnClickListener { pesquisar() }
        binding.btListar.setOnClickListener { listar() }
    }

    private fun incluir() {
        val nome = binding.etNome.text.toString()
        val telefone = binding.etTelefone.text.toString()

        if (nome.isNotEmpty() && telefone.isNotEmpty()) {
            val cadastro = Cadastro(0, nome, telefone)
            banco.incluir(cadastro)
            Toast.makeText(this, "Inclusão efetuada com sucesso", Toast.LENGTH_LONG).show()
            limparCampos()
        } else {
            Toast.makeText(this, "Preencha nome e telefone", Toast.LENGTH_SHORT).show()
        }
    }

    private fun alterar() {
        val idStr = binding.etCod.text.toString()
        if (idStr.isNotEmpty()) {
            val cadastro = Cadastro(
                idStr.toInt(),
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )
            banco.alterar(cadastro)
            Toast.makeText(this, "Alteração efetuada com sucesso", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Informe o código para alterar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun excluir() {
        val idStr = binding.etCod.text.toString() // Corrigido: pegando do campo de código
        if (idStr.isNotEmpty()) {
            banco.excluir(idStr.toInt())
            Toast.makeText(this, "Exclusão efetuada com sucesso!", Toast.LENGTH_LONG).show()
            limparCampos()
        } else {
            Toast.makeText(this, "Informe o código para excluir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pesquisar() {
        val idStr = binding.etCod.text.toString()
        if (idStr.isNotEmpty()) {
            val cadastro = banco.pesquisar(idStr.toInt())
            if (cadastro != null) {
                binding.etNome.setText(cadastro.nome)
                binding.etTelefone.setText(cadastro.telefone)
            } else {
                Toast.makeText(this, "Registro não encontrado", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Informe o código para pesquisar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listar() {
        val registros = banco.listar()
        if (registros.isEmpty()) {
            Toast.makeText(this, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val saida = StringBuilder()
        registros.forEach { cadastro ->
            saida.append("ID: ${cadastro.id} - ${cadastro.nome}\n")
        }

        Toast.makeText(this, saida.toString(), Toast.LENGTH_LONG).show()
    }

    private fun limparCampos() {
        binding.etCod.setText("")
        binding.etNome.setText("")
        binding.etTelefone.setText("")
        binding.etNome.requestFocus()
    }
}
