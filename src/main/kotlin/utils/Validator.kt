package h577870.utils

import java.util.regex.Pattern
import kotlin.math.ceil

class Validator {

    fun validateEan(ean: Any) : Boolean {
        val converted = ean as String
        if (converted.contains("[^0-9]") ||
                converted.length != 13) {
            return false
        }
        return true
    }

    fun validatePris(pris: Any) : Boolean {
        var converted = pris as String
        if (converted.contains('.')) {
            converted = converted.substringBefore('.')
        }
        if (converted.contains("^0-9") || converted.toInt() > 5000) return false
        return true
    }

}