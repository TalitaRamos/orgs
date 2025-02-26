package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDatabase
import com.example.orgs.databinding.ActivityFormularioCadastroUsuarioBinding
import com.example.orgs.extensions.toast
import com.example.orgs.model.Usuario
import kotlinx.coroutines.launch

class FormularioCadastroUsuarioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
    }
    private val usuarioDao by lazy {
        AppDatabase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
    }

    private fun configuraBotaoCadastrar() {
        binding.activityFormularioCadastroBotaoCadastrar.setOnClickListener {
            val novoUsuario = criaUsuario()
            cadastro(novoUsuario)
        }
    }

    private fun cadastro(usuario: Usuario) {
        lifecycleScope.launch {
            try {
                usuarioDao.salva(usuario)
                finish()
            } catch (e: Exception) {
                Log.e("CadastroUsuario", "configuraBotaoCadastrar: ", e)
                toast("Falha ao cadastrar usuário")
            }
        }
    }

    private fun criaUsuario(): Usuario {
        val usuario = binding.activityFormularioCadastroUsuario.text.toString()
        val nome = binding.activityFormularioCadastroNome.text.toString()
        val senha = binding.activityFormularioCadastroSenha.text.toString()
        return Usuario(usuario, nome, senha)
    }
}