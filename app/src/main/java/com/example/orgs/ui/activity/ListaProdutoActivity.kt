package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.constantes.CHAVE_PRODUTO_ID
import com.example.orgs.database.AppDatabase
import com.example.orgs.databinding.ActivityListaProdutoBinding
import com.example.orgs.model.Produto
import com.example.orgs.ui.recycler.adapter.ListaProdutosAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaProdutoActivity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(context = this)

    private val binding by lazy {
        ActivityListaProdutoBinding.inflate(layoutInflater)
    }

    private val produtoDao by lazy {
        AppDatabase.instance(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configuraRecyclerView()
        configuraFab()
        setContentView(binding.root)
        val db = AppDatabase.instance(this)
        val produtoDao = db.produtoDao()

        adapter.quandoClicaEmEditar = { produto ->
            Intent(this, FormularioProdutoActivity::class.java).apply {
                putExtra(CHAVE_PRODUTO_ID, produto.id)
                startActivity(this)
            }
        }

        adapter.quandoClicaEmRemover = { produto ->
            adapter.remove(produto)
            produtoDao.remove(produto)
        }
    }

    override fun onResume() {
        super.onResume()

        val scope = MainScope()
        scope.launch {
            val produtos = withContext(Dispatchers.IO) {
                produtoDao.buscaTodos()
            }
            adapter.atualiza(produtos)
        }
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
            putExtra(CHAVE_PRODUTO_ID, produto.id)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_order, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val produtoOrdenado: List<Produto>? = when (item.itemId) {
            R.id.menu_order_nome_desc ->
                produtoDao.buscaTodosOrdenadorPorNomeDesc()

            R.id.menu_order_nome_asc ->
                produtoDao.buscaTodosOrdenadorPorNomeAsc()

            R.id.menu_order_descricao_desc ->
                produtoDao.buscaTodosOrdenadorPorDescricaoDesc()

            R.id.menu_order_descricao_asc ->
                produtoDao.buscaTodosOrdenadorPorDescricaoAsc()

            R.id.menu_order_valor_desc ->
                produtoDao.buscaTodosOrdenadorPorValorDesc()

            R.id.menu_order_valor_asc ->
                produtoDao.buscaTodosOrdenadorPorValorAsc()

            R.id.menu_order_padrao ->
                produtoDao.buscaTodos()

            else -> null
        }

        produtoOrdenado?.let {
            adapter.atualiza(produtoOrdenado)
        }

        return super.onOptionsItemSelected(item)
    }
}