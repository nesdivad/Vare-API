package h577870.messagequeue

import com.rabbitmq.client.*
import java.nio.charset.StandardCharsets

/*
Consumer for salgsmeldinger fra kasse.
 */

object Consumer {
    var connectionFactory: ConnectionFactory = ConnectionFactory()
    lateinit var connection: Connection
    lateinit var channel: Channel
    lateinit var consumertag: String

    init {
        try {
            consumertag = "Salgsconsumer"
            connection = connectionFactory.newConnection("ampg://vare-api:varer@localhost:5672")
            channel = connection.createChannel()
            channel.queueDeclare("Salgsmelding", false, true, false, null)
            channel.basicQos(1)
        } catch (e: Exception) { print(e.stackTrace) }
    }

    private val deliverCallback = DeliverCallback { s, delivery ->
        val message = String(delivery.body, StandardCharsets.UTF_8)
        println("$s mottok melding: $message")
        this.channel.basicAck(0, false)
    }
    private val cancelCallback = CancelCallback { s ->
        println("$s ble kansellert")
        this.channel.basicNack(0, false, true)
    }

    fun consume() {
        channel.basicConsume("Salgsmelding", true, consumertag, deliverCallback, cancelCallback)
    }

}
