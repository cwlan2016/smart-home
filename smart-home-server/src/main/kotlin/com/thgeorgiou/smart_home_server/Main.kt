package com.thgeorgiou.smart_home_server

import spark.Spark.*

fun main() {
    // Setup MQTT
    val broker = Broker("tcp://localhost:1883")
    broker.connect()

    // Create a device manager
    val deviceManager = DeviceManager(broker)

    // HTTP APIs
    val api = HttpApi(deviceManager)
    path("/") {
        api.init()
    }
}