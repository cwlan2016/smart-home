package com.thgeorgiou.smart_home_server.devices

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue

enum class ActuatorType(val str: String) {
    PWM("PWM"), SWITCH("SWITCH"), UNKNOWN("?")
}

val ActuatorTypeConverter = object: Converter {
    override fun canConvert(cls: Class<*>)
            = cls == ActuatorType::class.java

    override fun toJson(value: Any): String =
        (value as ActuatorType).toString()


    override fun fromJson(jv: JsonValue): ActuatorType {
        val jsonString = jv.string ?: return ActuatorType.UNKNOWN
        return ActuatorType.valueOf(jsonString)
    }
}

data class Actuator(
    val type: ActuatorType,
    val name: String,
    val min: Int = 0,
    val max: Int = 1,
    var state: Int
)