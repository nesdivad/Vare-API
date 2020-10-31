package h577870.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class OppgaveStatus {
    IKKESTARTET, PAAGAAR, FULLFOERT
}

object OppgaveStatusSerializer : KSerializer<OppgaveStatus> {
    override fun deserialize(decoder: Decoder): OppgaveStatus {
        return OppgaveStatus.valueOf(decoder.decodeString().toUpperCase())
    }

    override fun serialize(encoder: Encoder, value: OppgaveStatus) {
        encoder.encodeString(value.name.toLowerCase())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(
                "OppgaveStatus", PrimitiveKind.STRING
        )

}
