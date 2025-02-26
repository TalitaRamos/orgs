package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.orgs.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId")
    fun buscaTodosByUsuario(usuarioId: String): Flow<List<Produto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salva(vararg produto: Produto)

    @Delete
    suspend fun remove(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun buscaPorId(id: Long): Flow<Produto?>

    @Query("SELECT * FROM Produto ORDER BY nome ASC")
    suspend fun buscaTodosOrdenadorPorNomeAsc(): List<Produto>
    @Query("SELECT * FROM Produto ORDER BY nome DESC")
    suspend fun buscaTodosOrdenadorPorNomeDesc(): List<Produto>
    @Query("SELECT * FROM Produto ORDER BY descricao ASC")
    suspend fun buscaTodosOrdenadorPorDescricaoAsc(): List<Produto>
    @Query("SELECT * FROM Produto ORDER BY descricao DESC")
    suspend fun buscaTodosOrdenadorPorDescricaoDesc(): List<Produto>
    @Query("SELECT * FROM Produto ORDER BY valor ASC")
    suspend fun buscaTodosOrdenadorPorValorAsc(): List<Produto>
    @Query("SELECT * FROM Produto ORDER BY valor DESC")
    suspend fun buscaTodosOrdenadorPorValorDesc(): List<Produto>
}