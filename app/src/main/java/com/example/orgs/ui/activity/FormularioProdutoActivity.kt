package com.example.orgs.ui.activity

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.dao.ProdutosDao
import com.example.orgs.databinding.ActivityFormularioProdutoBinding
import com.example.orgs.extensions.tentaCarregarImagem
import com.example.orgs.model.Produto
import com.example.orgs.ui.dialog.FormularioImagemDialog
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    private var url: String? = null

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
    }

    private fun configuraBotaoSalvar(
        campoNome: EditText,
        campoDescricao: EditText,
        campoValor: EditText
    ) {
        val dao = ProdutosDao()
        val botaoSalvar = binding.botaoSalvar
        botaoSalvar.setOnClickListener {
            val produto = criaProduto(campoNome, campoDescricao, campoValor)
            dao.adiciona(produto = produto)
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
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
    }

}