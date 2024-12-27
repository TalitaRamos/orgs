package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.constantes.CHAVE_PRODUTO_ID
import com.example.orgs.database.AppDatabase
import com.example.orgs.databinding.ActivityDetalhesBinding
import com.example.orgs.extensions.formataParaMoedaBrasileira
import com.example.orgs.extensions.tentaCarregarImagem
import com.example.orgs.model.Produto
import kotlinx.coroutines.launch

class DetalhesActivity : AppCompatActivity() {

    private var produtoId: Long = 0L
    private var produto: Produto? = null
    private val binding by lazy {
        ActivityDetalhesBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        AppDatabase.instance(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tentaCarregarProduto()
    }

    override fun onResume() {
        super.onResume()
        buscaProduto()
    }

    private fun buscaProduto() {
        lifecycleScope.launch {
            produto = produtoDao.buscaPorId(produtoId)
            produto?.let {
                preencherCampos(it)
            } ?: finish()
        }
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    private fun preencherCampos(produtoCarregado: Produto) {
        with(binding) {
            activityDetalhesCapa.tentaCarregarImagem(produtoCarregado.imagem)
            activityDetalhesTituloText.text = produtoCarregado.nome
            activityDetalhesTituloDetalhes.text = produtoCarregado.descricao
            activityDetalhesValorText.text = produtoCarregado.valor.formataParaMoedaBrasileira()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_detalhes_produto_editar -> {
                Intent(this, FormularioProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID, produtoId)
                    startActivity(this)
                }
            }

            R.id.menu_detalhes_produto_deletar -> {
                lifecycleScope.launch {
                    produto?.let {
                        produtoDao.remove(it)
                        finish()
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}