package com.thgeorgiou.smart_home_server

import com.thgeorgiou.smart_home_server.devices.Device

/** Represents a change of state of an actuator */
data class ActuatorStateChange(val state: Int)

/** Broadcasted by sensors when their value changes */
data class SensorValueChange(val value: Int)

/** New device registration, it sends us the whole descriptor */
typealias DeviceRegistrationRequest = Device
