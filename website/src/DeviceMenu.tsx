import { Menu } from 'antd';
import { ClickParam } from 'antd/lib/menu';
import * as React from 'react';
import { IDevice } from './model/Device';

export interface IDeviceMenuProps {
  devices: { [name: string]: IDevice };
  onSelect(key: string): void;
}

export class DeviceMenu extends React.Component<IDeviceMenuProps, {}> {

  constructor(props: IDeviceMenuProps) {
    super(props);

    this.state = {};
  }

  public render() {
    console.log(this.state);
    const items: React.ReactNode[] = [];
    for (const key of Object.keys(this.props.devices)) {
      const device = this.props.devices[key];

      items.push(
        <Menu.Item key={key}>
          {device.name}
        </Menu.Item>
      );
    }

    return (
      <Menu onClick={this.handleClick}>
        {items}
      </Menu>
    );
  }

  private handleClick = (param: ClickParam) => {
    this.props.onSelect(param.key);
  };
}

export default DeviceMenu;