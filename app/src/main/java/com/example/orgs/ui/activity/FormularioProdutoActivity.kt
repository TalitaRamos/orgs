package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.constantes.CHAVE_PRODUTO_ID
import com.example.orgs.database.AppDatabase
import com.example.orgs.databinding.ActivityFormularioProdutoBinding
import com.example.orgs.extensions.tentaCarregarImagem
import com.example.orgs.model.Produto
import com.example.orgs.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    private val produtoDao by lazy {
        AppDatabase.instance(this).produtoDao()
    }

    private var url: String? = null
    private var produtoId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar Produto"

        configuraBotaoSalvar()

        binding.formularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this).show(url) { imagem ->
                url = imagem
                binding.formularioProdutoImagem.tentaCarregarImagem(url)
            }
        }

        tentaCarregarProduto()
    }

    override fun onResume() {
        super.onResume()

        tentaBuscarProduto()
    }

    private fun tentaBuscarProduto() {
        lifecycleScope.launch {
            produtoDao.buscaPorId(produtoId)?.let {
                title = "Alterar Produto"
                preencheCampos(it)
            }
        }
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    private fun preencheCampos(produto: Produto) {
        url = produto.imagem

        binding.formularioProdutoImagem.tentaCarregarImagem(produto.imagem)
        binding.formularioNome.setText(produto.nome)
        binding.formularioDescricao.setText(produto.descricao)
        binding.formularioValor.setText(produto.valor.toPlainString())
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.botaoSalvar
        botaoSalvar.setOnClickListener {
            lifecycleScope.launch {
                val produto = criaProduto()
                produtoDao.salva(produto)
                finish()
            }
        }
    }

    private fun criaProduto(
    ): Produto {
        val nome = binding.formularioNome.text.toString()
        val descricao = binding.formularioDescricao.text.toString()
        val valorTexto = binding.formularioValor.text.toString()
        val valor = if (valorTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorTexto)
        }
        return Produto(
            id = produtoId,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
    }
}