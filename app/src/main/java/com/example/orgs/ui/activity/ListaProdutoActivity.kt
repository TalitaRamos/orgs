package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.constantes.CHAVE_PRODUTO
import com.example.orgs.database.AppDatabase
import com.example.orgs.databinding.ActivityListaProdutoBinding
import com.example.orgs.model.Produto
import com.example.orgs.ui.recycler.adapter.ListaProdutosAdapter

class ListaProdutoActivity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(context = this)

    private val binding by lazy {
        ActivityListaProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuraRecyclerView()
        configuraFab()
        setContentView(binding.root)
        val db = AppDatabase.instance(this)
        val produtoDao = db.produtoDao()

        adapter.quandoClicaEmEditar = { produto ->

        }

        adapter.quandoClicaEmRemover = { produto ->
            adapter.remove(produto)
            produtoDao.remove(produto)
        }
    }

    override fun onResume() {
        super.onResume()
        val db = AppDatabase.instance(this)
        val produtoDao = db.produtoDao()

        adapter.atualiza(produtoDao.buscaTodos())
    }

    private fun configuraFab() {
        val fab = binding.listaProdutosFab

        fab.setOnClickListener {
            toFormProduto()
        }
    }

    private fun toFormProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun toDetalhesProduto(produto: Produto) {
        val intent = Intent(this, DetalhesActivity::class.java).apply {
            putExtra(CHAVE_PRODUTO, produto)
        }
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.listaProdutosRecyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItemListener = {
            toDetalhesProduto(it)
        }
    }
}