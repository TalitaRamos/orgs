package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import br.com.alura.orgs.ui.activity.UsuarioBaseActivity
import com.example.orgs.database.AppDatabase
import com.example.orgs.databinding.ActivityTodosProdutosBinding
import com.example.orgs.extensions.vaiPara
import com.example.orgs.model.Produto
import com.example.orgs.ui.recycler.adapter.CabecalhoAdapter
import com.example.orgs.ui.recycler.adapter.ListaProdutosAdapter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TodosProdutosActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityTodosProdutosBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDatabase.instancia(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyclerview = binding.activityTodosProdutosRecyclerview
        lifecycleScope.launch {
            dao.buscaTodos()
                .map { produtos ->
                    produtos
                        .sortedBy {
                            it.usuarioId
                        }
                        .groupBy {
                            it.usuarioId
                        }.map { produtosUsuario ->
                            criaAdapterDeProdutosComCabecalho(produtosUsuario)
                        }.flatten()
                }
                .collect { adapter ->
                    recyclerview.adapter = ConcatAdapter(adapter)
                }
        }
    }

    private fun criaAdapterDeProdutosComCabecalho(produtosUsuario: Map.Entry<String?, List<Produto>>) =
        listOf(
            CabecalhoAdapter(this, listOf(produtosUsuario.key)),
            ListaProdutosAdapter(
                this,
                produtosUsuario.value
            ) { produtoClicado ->
                vaiPara(DetalhesProdutoActivity::class.java) {
                    putExtra(CHAVE_PRODUTO_ID, produtoClicado.id)
                }
            }
        )

}