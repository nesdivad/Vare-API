package h577870.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TidSerializer : KSerializer<Instant> {
    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(
            "tidogdato", kind = PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }
}
