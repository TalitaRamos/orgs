package com.example.orgs.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converter {
    @TypeConverter
    fun fromDouble(valor: Double?): BigDecimal {
        return valor?.let {
            BigDecimal(valor.toString())
        } ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun toDouble(valor: BigDecimal?): Double? {
        return valor?.let {
            valor.toDouble()
        }
    }

}