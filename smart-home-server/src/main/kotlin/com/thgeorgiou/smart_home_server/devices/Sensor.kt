package com.thgeorgiou.smart_home_server.devices

data class Sensor(
    val type: String,
    val name: String,
    val unit: String,
    var value: Int
)