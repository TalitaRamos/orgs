package com.example.orgs.ui.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.databinding.ProdutoItemBinding
import com.example.orgs.extensions.formataParaMoedaBrasileira
import com.example.orgs.extensions.tentaCarregarImagem
import com.example.orgs.model.Produto

class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto>,
    var quandoClicaNoItemListener : (produto: Produto) -> Unit = {}
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private  val produtos = produtos.toMutableList()

    inner class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var produto: Produto

        init {
            itemView.setOnClickListener {
                if(::produto.isInitialized) {
                    quandoClicaNoItemListener(produto)
                }
            }
        }
        val nome = binding.produtoItemNome
        val descricao = binding.produtoItemDescricao
        val valor = binding.produtoItemValor
        val imagem = binding.imageView

        fun vincula(produto: Produto) {
            this.produto = produto
            nome.text = this.produto.nome
            descricao.text = this.produto.descricao
            valor.text = this.produto.valor.formataParaMoedaBrasileira()

            val visibilidade = if (produto.imagem != null) {
                View.VISIBLE
            } else{
                View.GONE
            }

            imagem.visibility = visibilidade

            imagem.tentaCarregarImagem(this.produto.imagem)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProdutoItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.vincula(produto)
    }

    override fun getItemCount(): Int = produtos.size

    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()
    }

}
