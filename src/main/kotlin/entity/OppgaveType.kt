package h577870.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Enum-klasse for Oppgavetype.
 * Enum-klasser kan ikke serialiseres av seg selv, så lager derfor en egendefinert
 * serialiseringsklasse, OppgaveTypeSerializer.
 * @author Kristoffer Davidsen
 * @param BESTILLING -> Oppgaven er en bestillingsoppgave.
 * @param TELLING -> Oppgaven er en tellingsoppgave.
 * @param KONTROLL -> Oppgaven er en kontrolloppgave, f.eks. ved ingen salg,
 * beholdning i minus eller stort avvik.
 */

@ExperimentalSerializationApi
@Serializable(with = OppgaveTypeSerializer::class)
enum class OppgaveType {
    BESTILLING, TELLING, KONTROLL
}

@ExperimentalSerializationApi
@Serializer(forClass = OppgaveType::class)
object OppgaveTypeSerializer : KSerializer<OppgaveType> {

    override fun deserialize(decoder: Decoder): OppgaveType {
        return OppgaveType.valueOf(decoder.decodeString().toUpperCase())
    }

    override fun serialize(encoder: Encoder, value: OppgaveType) {
        encoder.encodeString(value.name.toLowerCase())
    }
    override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("OppgaveType", PrimitiveKind.STRING)
}
