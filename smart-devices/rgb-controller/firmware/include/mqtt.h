#include <Arduino.h>
#include <ArduinoJson.h>
#include <AsyncMqttClient.h>

/**
 * Configures the MQTT client, call before anything else MQTT related
 */
void mqtt_setup();

/**
 * Connect to the MQTT broker
 */
void mqtt_connect();

/**
 * Called after a successful connection to the broker, we will
 * register ourselves here
 */
void mqtt_on_connect(bool sessionPresent);

/**
 * Called after a disconnect from the broker. Will start a reconnect timer here
 */
void mqtt_on_disconnect(AsyncMqttClientDisconnectReason reason);

/** Called when receiving a message, should handle actuator states here */
void mqtt_on_message(char* topic, char* payload,
                     AsyncMqttClientMessageProperties properties, size_t len,
                     size_t index, size_t total);