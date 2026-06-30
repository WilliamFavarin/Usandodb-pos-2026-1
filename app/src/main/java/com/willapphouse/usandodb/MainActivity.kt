package com.willapphouse.usandodb

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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

        if (intent.getIntExtra("id", 0) > 0) {
            binding.etCod.setText(intent.getIntExtra("id", 0).toString())
            binding.etNome.setText(intent.getStringExtra("nome"))
            binding.etTelefone.setText(intent.getStringExtra("telefone"))
        } else {
            binding.btExcluir.visibility = View.GONE
            binding.btPesquisar.visibility = View.GONE
        }

        binding.btAlterar.setOnClickListener { alterar() }
        binding.btExcluir.setOnClickListener { excluir() }
        binding.btPesquisar.setOnClickListener { pesquisar() }
    }

    private fun alterar() {
        val id = binding.etCod.text.toString().toIntOrNull()

        if (id == null) {
            val cadastro = Cadastro(
                0,
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )
            banco.incluir(cadastro)
        } else {
            val cadastro = Cadastro(
                id,
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )
            banco.alterar(cadastro)
        }

        Toast.makeText(
            this,
            "Operação efetuada com sucesso",
            Toast.LENGTH_LONG
        ).show()

        finish()
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

        finish()
    }

    private fun pesquisar() {

        val etCodPesquisa = EditText(this)

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Informe o código da pessoa")
        dialog.setCancelable(false)
        dialog.setNegativeButton("Fechar", null)
        dialog.setPositiveButton("Pesquisar", { dialog, which ->

            val id = etCodPesquisa.text.toString().toIntOrNull()

            if (id == null) {
                Toast.makeText(this, "Informe o código para pesquisar", Toast.LENGTH_LONG).show()
            } else {

                val cadastro = banco.pesquisar(id)

                if (cadastro != null) {
                    binding.etCod.setText(etCodPesquisa.text.toString())
                    binding.etNome.setText(cadastro.nome)
                    binding.etTelefone.setText(cadastro.telefone)
                } else {
                    Toast.makeText(this, "Registro não encontrado", Toast.LENGTH_LONG).show()
                }
            }
        }
        )
        dialog.setView(etCodPesquisa)
        dialog.show()

    }

    private fun limparCampos() {
        binding.etCod.setText("")
        binding.etNome.setText("")
        binding.etTelefone.setText("")
        binding.etNome.requestFocus()
    }
}
