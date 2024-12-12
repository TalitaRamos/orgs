package com.example.orgs.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
}