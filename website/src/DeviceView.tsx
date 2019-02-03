import { Empty, Layout, Slider, Switch } from 'antd';
import * as React from 'react';
import { IActuator, IDevice, ISensors } from './model/Device';

export interface IDeviceViewProps {
  device?: IDevice
}

export class DeviceView extends React.Component<IDeviceViewProps, {}> {

  constructor(props: IDeviceViewProps) {
    super(props);

    this.state = {};
  }

  public render() {
    if (this.props.device === undefined) {
      return (<Empty />);
    }

    const actuators: React.ReactNode[] = [];
    const sensors: React.ReactNode[] = [];

    for (const key of Object.keys(this.props.device.actuators)) {
      actuators.push(this.renderActuator(
        this.props.device.actuators[key], key
      ));
    }
    for (const key of Object.keys(this.props.device.sensors)) {
      sensors.push(this.renderSensor(
        this.props.device.sensors[key], key
      ));
    }

    return (
      <Layout>
        <h2>Actuators</h2>
        {actuators}
        <h2>Sensors</h2>
        {sensors}
      </Layout>
    );
  }

  private renderActuator = (actuator: IActuator, key: string) => {
    switch (actuator.type) {
      case "PWM": return (
        <Layout key={key}>
          <span>{actuator.name}</span>
          <Slider value={actuator.state} min={actuator.min} max={actuator.max} />
        </Layout>
      );
      case "SWITCH": return (
        <Layout>
          <span>{actuator.name}</span>
          <Switch />
        </Layout>
      );
      default: return <div key={key} />;
    }
  }

  private renderSensor = (sensor: ISensors, key: string) => {
    return <span key={key} />;
  }
}