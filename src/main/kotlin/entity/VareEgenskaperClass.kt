package h577870.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * Holder tilleggsinformasjon om en vare, som brukes ved generering av oppgaver osv.
 * @param ean Eankoden for den aktuelle varen, fremmednøkkel som refererer til ean i Vare-tabell.
 * @param beholdning Beholdning for den aktuelle varen.
 * @param prestasjonslager Minimumsantall som butikken skal ha til "enhver" tid.
 * @param dekningsperiode Neste levering skal levere x antall varer basert på dekningsperioden,
 * og tar prestasjonslager og snittsalg med i beregningen.
 * @param snittsalg Antall varer som selges i snitt pr dag, basert på siste 365 dager.
 */

@Serializable
data class VareEgenskaperClass(
        override val ean: Long,
        var beholdning: Double,
        var prestasjonslager: Int,
        var dekningsperiode: Int,
        var snittsalg: Double
) : Ean

object VareEgenskaper : Table() {
    val ean: Column<Long> = long("ean").references(Vare.ean)
    var beholdning: Column<Double> = double("beholdning")
    var prestasjonslager: Column<Int> = integer("prestasjonslager")
    var dekningsperiode: Column<Int> = integer("dekningsperiode")
    var snittsalg: Column<Double> = double("snittsalg")
}