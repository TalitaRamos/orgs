package com.example.orgs.ui.activity

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.constantes.CHAVE_PRODUTO_ID
import com.example.orgs.database.AppDatabase
import com.example.orgs.databinding.ActivityFormularioProdutoBinding
import com.example.orgs.extensions.tentaCarregarImagem
import com.example.orgs.model.Produto
import com.example.orgs.ui.dialog.FormularioImagemDialog
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

        val campoNome = binding.formularioNome
        val campoDescricao = binding.formularioDescricao
        val campoValor = binding.formularioValor

        configuraBotaoSalvar(campoNome, campoDescricao, campoValor)

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

        produtoDao.buscaPorId(produtoId)?.let {
            title = "Alterar Produto"
            preencheCampos(it)
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

    private fun configuraBotaoSalvar(
        campoNome: EditText,
        campoDescricao: EditText,
        campoValor: EditText
    ) {
        val db = AppDatabase.instance(this)
        val produtoDao = db.produtoDao()

        val botaoSalvar = binding.botaoSalvar
        botaoSalvar.setOnClickListener {
            val produto = criaProduto(campoNome, campoDescricao, campoValor)
            produtoDao.salva(produto)
            finish()
        }
    }

    private fun criaProduto(
        campoNome: EditText,
        campoDescricao: EditText,
        campoValor: EditText
    ): Produto {
        val nome = campoNome.text.toString()
        val descricao = campoDescricao.text.toString()
        val valorTexto = campoValor.text.toString()
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