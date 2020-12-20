package h577870.utils

import h577870.entity.OppgaveType
import h577870.entity.VareEgenskaperClass
import kotlinx.datetime.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.math.floor

@ExperimentalSerializationApi
object Oppgavehjelper {

    /*
    Konverterer set med varer til map med bestillingsverdier.
     */
    fun setToMap(set: Set<VareEgenskaperClass>) : MutableMap<Long, Double> {
        val map = mutableMapOf<Long, Double>()
        set.forEach { elem ->
            map[elem.ean] = computeOrder(elem)
        }
        assert(map.isNotEmpty())
        return map
    }
    /*
    Regner ut antall F-pk som skal bestilles inn. Foreløpig en enkel beregning.
     */
    private fun computeOrder(vareEC: VareEgenskaperClass) : Double {
        return floor((vareEC.beholdning + (vareEC.prestasjonslager * 1.5)))
    }

    /*
    Bestemmer neste bestillingsfrist.
     */
    fun bestemFrist(enum: OppgaveType) : Instant {
        val current = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        when (enum) {
            OppgaveType.BESTILLING -> {
                return when (current.dayOfWeek) {
                    DayOfWeek.MONDAY -> LocalDateTime(current.year, current.month, current.dayOfMonth + 2, 7, 0, 0).toInstant(
                        TimeZone.currentSystemDefault())
                    DayOfWeek.TUESDAY -> LocalDateTime(current.year, current.month, current.dayOfMonth + 1, 7, 0, 0).toInstant(
                        TimeZone.currentSystemDefault())
                    DayOfWeek.WEDNESDAY -> LocalDateTime(current.year, current.month, current.dayOfMonth + 2, 7, 0, 0).toInstant(
                        TimeZone.currentSystemDefault())
                    DayOfWeek.THURSDAY -> LocalDateTime(current.year, current.month, current.dayOfMonth + 1, 7, 0, 0).toInstant(
                        TimeZone.currentSystemDefault())
                    DayOfWeek.FRIDAY -> LocalDateTime(current.year, current.month, current.dayOfMonth + 3, 7, 0, 0).toInstant(
                        TimeZone.currentSystemDefault())
                    DayOfWeek.SATURDAY -> LocalDateTime(current.year, current.month, current.dayOfMonth + 2, 7, 0, 0).toInstant(
                        TimeZone.currentSystemDefault())
                    DayOfWeek.SUNDAY -> LocalDateTime(current.year, current.month, current.dayOfMonth + 1, 7, 0, 0).toInstant(
                        TimeZone.currentSystemDefault())
                }
            }
            OppgaveType.TELLING -> return LocalDateTime(current.year, current.month,
                current.dayOfMonth + (DayOfWeek.SUNDAY.value - current.dayOfWeek.value),
                7, 0, 0).toInstant(TimeZone.currentSystemDefault())
            OppgaveType.KONTROLL -> return LocalDateTime(current.year, 12, 31, 23, 59, 59).toInstant(TimeZone.currentSystemDefault())
        }
    }
}