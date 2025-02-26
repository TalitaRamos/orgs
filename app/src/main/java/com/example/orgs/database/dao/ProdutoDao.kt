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

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY nome ASC ")
    fun buscaTodosOrdenadorPorNomeAsc(usuarioId: String): Flow<List<Produto>>
    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY nome DESC")
    fun buscaTodosOrdenadorPorNomeDesc(usuarioId: String): Flow<List<Produto>>
    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY descricao ASC")
    fun buscaTodosOrdenadorPorDescricaoAsc(usuarioId: String): Flow<List<Produto>>
    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY descricao DESC")
    fun buscaTodosOrdenadorPorDescricaoDesc(usuarioId: String): Flow<List<Produto>>
    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY valor ASC")
    fun buscaTodosOrdenadorPorValorAsc(usuarioId: String): Flow<List<Produto>>
    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY valor DESC")
    fun buscaTodosOrdenadorPorValorDesc(usuarioId: String): Flow<List<Produto>>
}