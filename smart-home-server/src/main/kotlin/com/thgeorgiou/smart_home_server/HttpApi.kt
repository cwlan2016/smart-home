package com.thgeorgiou.smart_home_server

import com.beust.klaxon.Klaxon
import com.thgeorgiou.smart_home_server.utils.ApiError
import spark.Request
import spark.Response
import spark.Spark.*
import java.lang.IndexOutOfBoundsException

/** The HTTP API of the server */
class HttpApi(private val deviceManager: DeviceManager) {

    /** JSON Serializer */
    val json = Klaxon()

    /** Add all routes to Spark */
    fun init() {
        // We respond to everything with JSON
        before("*") { _: Request, response: Response ->
            response.header("Content-Type", "application/json")
        }

        // Route handlers
        get("/devices", ::getDevices)
        get("/devices/:deviceId", ::getDevice)
        get("/devices/:deviceId/actuators", ::getActuators)
        get("/devices/:deviceId/sensors", ::getSensors)
        get("/devices/:deviceId/actuators/:actuatorId", ::getActuator)
        get("/devices/:deviceId/sensors/:sensorId", ::getSensor)

        put("/devices/:deviceId/actuators", ::putActuator)
    }

    /** Returns all devices and their services */
    private fun getDevices(request: Request, response: Response): String {
        val body = json.toJsonString(deviceManager.devices)
        response.status(200)
        return body
    }

    /** Return one device by it's ID */
    private fun getDevice(request: Request, response: Response): String {
        // Get deviceId and check if device exists
        val deviceId = request.params(":deviceId")
        val device = deviceManager.devices[deviceId]

        if (device == null) {
            response.status(404)
            return json.toJsonString(
                ApiError("not_found", "Device with ID = $deviceId not found!")
            )
        }

        // Serialise and return device
        response.status(200)
        return json.toJsonString(device)
    }

    /** Return all actuators of a device */
    private fun getActuators(request: Request, response: Response): String {
        // Get deviceId and check if device exists
        val deviceId = request.params(":deviceId")
        val device = deviceManager.devices[deviceId]

        if (device == null) {
            response.status(404)
            return json.toJsonString(
                ApiError("not_found", "Device with ID = $deviceId not found!")
            )
        }

        // Serialise and return actuators
        response.status(200)
        return json.toJsonString(device.actuators)
    }

    /** Return all sensors of a device */
    private fun getSensors(request: Request, response: Response): String {
        // Get deviceId and check if device exists
        val deviceId = request.params(":deviceId")
        val device = deviceManager.devices[deviceId]

        if (device == null) {
            response.status(404)
            return json.toJsonString(
                ApiError("not_found", "Device with ID = $deviceId not found!")
            )
        }

        // Serialise and return actuators
        response.status(200)
        return json.toJsonString(device.sensors)
    }

    /** Return one actuator from a device */
    private fun getActuator(request: Request, response: Response): String {
        // Get deviceId and check if device exists
        val deviceId = request.params(":deviceId")
        val device = deviceManager.devices[deviceId]

        if (device == null) {
            response.status(404)
            return json.toJsonString(
                ApiError("not_found", "Device with ID = $deviceId not found!")
            )
        }

        // Check if actuator exists
        val actuatorId = request.params(":actuatorId")
        val actuator = device.actuators[actuatorId]
        if (actuator == null) {
            response.status(404)
            return json.toJsonString(
                ApiError("not_found", "Actuator with ID = $actuatorId not found!")
            )
        }

        // Serialise and return actuators
        response.status(200)
        return json.toJsonString(actuator)
    }

    /** Return one sensor from a device */
    private fun getSensor(request: Request, response: Response): String {
        // Get deviceId and check if device exists
        val deviceId = request.params(":deviceId")
        val device = deviceManager.devices[deviceId]

        if (device == null) {
            response.status(404)
            return json.toJsonString(
                ApiError("not_found", "Device with ID = $deviceId not found!")
            )
        }

        // Check if sensor exists
        val sensorId = request.params(":sensorId")
        val sensor = device.sensors[sensorId]

        if (sensor == null) {
            response.status(404)
            return json.toJsonString(
                ApiError("not_found", "Sensor with ID = $sensorId not found!")
            )
        }

        // Serialise and return actuators
        response.status(200)
        return json.toJsonString(sensor)
    }

    /** Update the state of an actuator */
    private fun putActuator(request: Request, response: Response): String {
        // Get deviceId and check if device exists
        val deviceId = request.params(":deviceId")
        val device = deviceManager.devices[deviceId]

        if (device == null) {
            response.status(404)
            return json.toJsonString(
                ApiError("not_found", "Device with ID = $deviceId not found!")
            )
        }

        // Verify new state
        val body = request.body()
        val states = json.parse<Map<String, Int>>(body)

        if (states == null) {
            response.status(400)
            return json.toJsonString(
                ApiError("body", "JSON Body could not be parsed")
            )
        }

        // Check if each actuator exists and verify min/max
        states.forEach { (actuatorId, state) ->
            val actuator = device.actuators[actuatorId]
            if (actuator == null) {
                response.status(404)
                return json.toJsonString(
                    ApiError("not_found", "Actuator with ID = $actuatorId not found!")
                )
            }

            if (state < actuator.min) {
                response.status(400)
                return json.toJsonString(
                    ApiError("state", "New State is smaller than the minimum value ($state < ${actuator.min})")
                )
            }

            if (state > actuator.max) {
                response.status(400)
                return json.toJsonString(
                    ApiError("state", "New State is larger than the maximum value ($state > ${actuator.max})")
                )
            }
        }

        // Set new states
        deviceManager.setActuatorStates(deviceId, states)

        response.status(200)
        return "{}"
    }
}