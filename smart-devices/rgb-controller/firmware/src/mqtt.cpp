#include "mqtt.h"
#include <Ticker.h>
#include "config.h"

/** MQTT Client */
AsyncMqttClient mqtt_client;
/** Reconnect timer for mqtt */
Ticker mqtt_reconnect_timer;

/** MQTT Topic for changing actuator 0 state */
const char* topic_actuator_led =
    "com.thgeorgiou.smart-home-server/devices/" DEVICE_ID "/actuators";

/** JSON Buffer */
const size_t capacity = JSON_OBJECT_SIZE(1) + 20;
DynamicJsonBuffer json(capacity);

void mqtt_setup() {
  mqtt_client.onConnect(mqtt_on_connect);
  mqtt_client.onDisconnect(mqtt_on_disconnect);
  mqtt_client.onMessage(mqtt_on_message);
  mqtt_client.setServer(MQTT_HOST, MQTT_PORT);
}

void mqtt_connect() {
  Serial.println("Connecting to MQTT...");
  mqtt_client.connect();
}

void mqtt_on_connect(bool sessionPresent) {
  // Registration JSON
  const char* payload = "{\"name\":\"" DEVICE_NAME
                        "\",\"actuators\":{"
                        "\"red\":{\"type\":\"PWM\",\"name\":\"Red\","
                        "\"min\":0,\"max\":1023, \"state\":0},"
                        "\"green\":{\"type\":\"PWM\",\"name\":\"Green\","
                        "\"min\":0,\"max\":1023, \"state\":0},"
                        "\"blue\":{\"type\":\"PWM\",\"name\":\"Blue\","
                        "\"min\":0,\"max\":1023, \"state\":0}"
                        "}, \"sensors:\":[]}";

  Serial.println("Registering device!");
  mqtt_client.publish("com.thgeorgiou.smart-home-server/devices/" DEVICE_ID, 0,
                      true, payload);

  // Subscribe to actuator updates
  mqtt_client.subscribe(topic_actuator_led, 0);
}

void mqtt_on_disconnect(AsyncMqttClientDisconnectReason reason) {
  Serial.println("Disconnected from MQTT. Will retry to connect...");

  mqtt_reconnect_timer.once(2, mqtt_connect);
}

void mqtt_on_message(char* topic, char* payload,
                     AsyncMqttClientMessageProperties properties, size_t len,
                     size_t index, size_t total) {
  Serial.println("Message received! Topic:");
  Serial.println(topic);
  Serial.println("Payload:");
  Serial.println(payload);

  // Handle message
  if (strcmp(topic, topic_actuator_led) == 0) {
    Serial.println("Actuator state change!");

    // Deserialise message
    JsonObject& root = json.parseObject(payload);

    if (root.containsKey("red")) {
      int v = root["red"];
      analogWrite(PIN_RED, v);
    }
    if (root.containsKey("green")) {
      int v = root["green"];
      analogWrite(PIN_GREEN, v);
    }
    if (root.containsKey("blue")) {
      int v = root["blue"];
      analogWrite(PIN_BLUE, v);
    }
  }
}