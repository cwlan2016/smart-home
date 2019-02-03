#include <Arduino.h>
#include <ESP8266WiFi.h>
#include "config.h"
#include "mqtt.h"

void wifi_setup();

void setup() {
  Serial.begin(115200);

  pinMode(D2, OUTPUT);

  wifi_setup();
  mqtt_setup();
  mqtt_connect();
}

void wifi_setup() {
  // Connect to the local network
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  // Wait for IP
  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());
}

void loop() {}