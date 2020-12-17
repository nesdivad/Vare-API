package h577870.utils

import h577870.entity.OppgaveType
import kotlinx.datetime.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
object Oppgavehjelper {

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