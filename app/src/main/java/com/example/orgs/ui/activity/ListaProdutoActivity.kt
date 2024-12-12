package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.constantes.CHAVE_PRODUTO
import com.example.orgs.dao.ProdutosDao
import com.example.orgs.databinding.ActivityListaProdutoBinding
import com.example.orgs.model.Produto
import com.example.orgs.ui.recycler.adapter.ListaProdutosAdapter

class ListaProdutoActivity : AppCompatActivity() {

    private val dao = ProdutosDao()
    private  val adapter = ListaProdutosAdapter(context = this, produtos = dao.buscaTodos())

    private  val binding by lazy {
        ActivityListaProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuraRecyclerView()
        configuraFab()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        adapter.atualiza(dao.buscaTodos())
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