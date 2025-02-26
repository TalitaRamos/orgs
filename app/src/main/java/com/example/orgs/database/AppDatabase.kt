package com.example.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.orgs.database.converter.Converter
import com.example.orgs.database.dao.ProdutoDao
import com.example.orgs.database.dao.UsuarioDao
import com.example.orgs.database.migrations.MIGRATION_1_2
import com.example.orgs.database.migrations.MIGRATION_2_3
import com.example.orgs.model.Produto
import com.example.orgs.model.Usuario

@Database(
    entities = [
        Produto::class,
        Usuario::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var db: AppDatabase? = null
        fun instancia(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db"
            ).addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3
            ).build().also {
                db = it
            }
        }
    }
}