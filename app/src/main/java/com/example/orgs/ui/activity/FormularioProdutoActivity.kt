package com.example.orgs.ui.activity

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.constantes.CHAVE_PRODUTO
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

    private var url: String? = null
    private var idProduto = 0L

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

        val produtoData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CHAVE_PRODUTO, Produto::class.java)
        } else {
            intent.getParcelableExtra<Produto>(CHAVE_PRODUTO)
        }

        produtoData?.let { produtoCarregado ->
            title = "Alterar Produto"
            
            idProduto = produtoCarregado.id
            url = produtoCarregado.imagem

            binding.formularioProdutoImagem.tentaCarregarImagem(produtoCarregado.imagem)
            binding.formularioNome.setText(produtoCarregado.nome)
            binding.formularioDescricao.setText(produtoCarregado.descricao)
            binding.formularioValor.setText(produtoCarregado.valor.toPlainString())
        }
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
            if(idProduto > 0) {
                produtoDao.update(produto)
            } else {
                produtoDao.salva(produto)
            }
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
            id = idProduto,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
    }

}