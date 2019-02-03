package com.thgeorgiou.smart_home_server.devices


data class Device (
    val name: String,

    val actuators: List<Actuator> = listOf(),
    val sensors: List<Sensor> = listOf()
)