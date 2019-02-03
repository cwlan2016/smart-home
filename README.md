Smart Home
---

This repo contains various components of a smart home platform, ranging from the server and the
management website to open-hardware schematics and firmware. It started as a school project but
hopefully I will have time to extend it in the future.


### Documentation

Currently only this document exists until the platform actually becomes useful.


### Architecture

The platform is managed by the server (`smart-home-server`) which communicates with a variety of smart
devices using MQTT. It also provides a REST API for websites and apps. There is no authentication
currently, both for devices and for users but it is planned.

Each smart device provides some services:

* **Actuators**: Devices that can act, like lamps, LEDs, relays, etc. Each actuator can have many states.
* **Sensors**: Devices that measure a physical quantity such as temperature, air quality, etc.
* **Controllers**: Devices that contain buttons and other input methods and can be used to control other
devices.


### Server

Inside the `smart-home-server` directory resides the server, written using Spark Java and Eclipse
Poho. For now it handles the registration of devices, publishing actuator events and collecting measurements
but it only keeps the latest one.

To run you also need an MQTT broker, in the example below I locally run
[Mosquitto](https://github.com/eclipse/mosquitto):

```sh
cd smart-home-server

mosquito &&
./gradlew run
```
