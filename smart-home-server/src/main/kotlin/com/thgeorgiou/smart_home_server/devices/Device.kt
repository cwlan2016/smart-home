package com.thgeorgiou.smart_home_server.devices


data class Device (
    val name: String,

    val actuators: Map<String, Actuator> = mapOf(),
    val sensors: Map<String, Sensor> = mapOf()
)