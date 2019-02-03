export interface IDevice {
  name: string;

  actuators: { [name: string]: IActuator };
  sensors: { [name: string]: ISensors };
}

type ActuatorType = "PWM" | "SWITCH" | "?";

export interface IActuator {
  type: ActuatorType;
  name: string;
  min: number;
  max: number;
  state: number;
}

export interface ISensors {
  type: string;
  name: string;
  unit: string;
  value: number;
}