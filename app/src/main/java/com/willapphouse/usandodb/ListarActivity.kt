package com.willapphouse.usandodb

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.willapphouse.usandodb.database.DatabaseHandler
import com.willapphouse.usandodb.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        banco = DatabaseHandler( this )

        val registros = banco.listarCursor()

        val adapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_2,
            registros,
            arrayOf( "nome", "telefone" ),
            intArrayOf( android.R.id.text1, android.R.id.text2 ),
            0
        )

        binding.lvCadastro.adapter = adapter

    }
}