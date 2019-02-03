package com.thgeorgiou.smart_home_server

import com.beust.klaxon.Klaxon
import com.thgeorgiou.smart_home_server.devices.ActuatorTypeConverter
import com.thgeorgiou.smart_home_server.devices.Device
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.MqttMessage


class DeviceManager(val broker: Broker) : IMqttMessageListener{

    /** JSON Serializer */
    val json = Klaxon().converter(ActuatorTypeConverter)

    /**
     * List of devices currently on the network, mapped by their IDs
     */
    val devices = mutableMapOf<String, Device>()

    init {
        broker.subscribe("devices/#", this)
        broker.subscribe("sensors/#", this)
        broker.subscribe("actuators/#", this)
    }

    /**
     * Sets the state of an actuator.
     *
     * We will not actually set the state in the in-memory list of actuators,
     * we just publish the state to the MQTT broker and wait for the device
     * to publish the new state back. This way we avoid having an inconsistent
     * state.
     */
    fun setActuatorStates(deviceId: String, states: Map<String, Int>): Boolean {
        // Try to find device & actuator
        // TODO Before setting state check if the mqtt packet actually got delivered?
        val device = devices[deviceId] ?: return false
        states.forEach {(actuatorId, newState) ->
            device.actuators[actuatorId]?.state = newState
        }

        // Create payload
        val payload = json.toJsonString(states).toByteArray(Charsets.UTF_8)

        broker.publish("devices/$deviceId/actuators", payload, 1)
        return true
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        // Ignore weird messages
        if (topic == null || message == null) return

        // Split topic to tokens
        var tokens = topic.split("/")
        tokens = tokens.subList(1, tokens.size) // Drop com.thgeorgiou.blah, TODO do this in Broker

        // Parse JSON payload, we use different target types depending on
        // the topic
        val payload = message.payload.toString(Charsets.UTF_8)

        // Ignore weird request
        if (tokens[0] != "devices" || tokens.size < 2) {
            return
        }

        // Device registration /devices/:deviceID
        if (tokens.size == 2) {
            val device = json.parse<DeviceRegistrationRequest>(payload) ?: return
            devices[tokens[1]] = device
            return
        }

        // Sensor value update
        if (tokens.size == 4 && tokens[2] == "sensor") {
            val newValue = json.parse<SensorValueChange>(payload)?.value ?: return

            val deviceId = tokens[1]
            val sensorId = tokens[3]

            // Try to set the new state
            val device = devices[deviceId] ?: return
            val sensor = device.sensors[sensorId] ?: return

            sensor.value = newValue

            return
        }
    }
}