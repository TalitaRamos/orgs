package com.example.orgs.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.extensions.formataParaMoedaBrasileira
import com.example.orgs.constantes.CHAVE_PRODUTO
import com.example.orgs.databinding.ActivityDetalhesBinding
import com.example.orgs.model.Produto
import com.example.orgs.extensions.tentaCarregarImagem

class DetalhesActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tentaCarregarProduto()
    }

    private fun tentaCarregarProduto() {
        val userData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CHAVE_PRODUTO, Produto::class.java)
        } else {
            intent.getParcelableExtra<Produto>(CHAVE_PRODUTO)
        }
        userData?.let { produtoCarregado -> preencherCampos(produtoCarregado) } ?: finish()
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
                finish()
            }
            R.id.menu_detalhes_produto_deletar -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}