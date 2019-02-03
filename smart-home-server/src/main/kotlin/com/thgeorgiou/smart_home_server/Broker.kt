package com.thgeorgiou.smart_home_server

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

/**
 * Handles connection and I/O to the MQTT broker
 */
class Broker(private val url: String) {

    /** In-memory store for persistence */
    private val persistence = MemoryPersistence()

    /** MQTT client to the broker */
    private val mqtt = MqttClient(url, "com.thgeorgiou.smart_home_server", persistence)

    /** Connect to the MQTT Broker */
    fun connect(): Boolean {
        val connectionOptions = MqttConnectOptions()
        connectionOptions.isCleanSession = true

        try {
            mqtt.connect(connectionOptions)
        } catch (ex: MqttException) {
            System.err.println("Could not connect to $url:")
            System.err.println(ex.message)

            return false
        } catch (ex: MqttSecurityException) {
            System.err.println("Could not connect to $url, authentication required:")
            System.err.println(ex.message)

            return false
        }

        return true
    }

    /** Disconnect from the MQTT Broker */
    fun disconnect() {
        mqtt.disconnect()
    }

    /**
     * Publish a message to the MQTT broker.
     */
    fun publish(topic: String, payload: ByteArray = ByteArray(0), qos: Int = 0, retain: Boolean = false) {
        val message = MqttMessage(payload)
        message.qos = qos
        message.isRetained = retain

        mqtt.publish("com.thgeorgiou.smart-home-server/$topic", message)
    }

    /**
     * Subscribe to a topic. You should give a [listener] to forward any messages that come in.
     *
     * We prefix the topic with "com.thgeorgiou.smart-home-server/" so you can use this with
     * public brokers
     */
    fun subscribe(topic: String, listener: IMqttMessageListener) {
        mqtt.subscribe("com.thgeorgiou.smart-home-server/$topic", listener)
    }
}