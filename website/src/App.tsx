import { Layout } from 'antd';
import * as React from 'react';
import { getDevices } from './API';
import './App.css';
import DeviceMenu from './DeviceMenu';
import { DeviceView } from './DeviceView';
import { IDevice } from './model/Device';

const { Header, Content, Footer } = Layout;

export interface IAppState {
  devices: { [name: string]: IDevice };
  selectedDevice?: IDevice;
}

class App extends React.Component<{}, IAppState> {

  constructor(props: {}) {
    super(props);

    this.state = {
      devices: {},
      selectedDevice: undefined
    };
  }

  public async componentDidMount() {
    const response = await getDevices();
    if (response.status === "success") {
      this.setState({ devices: response.devices });
    }
  }

  public render() {
    return (
      <Layout>
        <Header>
          <h1 className="logo">Smart Home</h1>
        </Header>
        <Content style={{ padding: '0 50px' }}>
          <DeviceMenu devices={this.state.devices} onSelect={this.onSelectDevice} />
          <DeviceView device={this.state.selectedDevice} />
        </Content>
        <Footer style={{ textAlign: 'center' }}>
          Ant Design ©2018 Created by Ant UED
        </Footer>
      </Layout>
    );
  }

  private onSelectDevice = (name: string) => {
    this.setState({
      selectedDevice: this.state.devices[name]
    });
  }
}

export default App;
