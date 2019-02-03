import { IDevice } from './model/Device';

const apiPath = "http://localhost:4567"

export interface IGetDevicesResponse {
  status: "server-error" | "success";
  devices: { [name: string]: IDevice };
}

export async function getDevices(): Promise<IGetDevicesResponse> {
  let response: Response;
  try {
    response = await fetch(`${apiPath}/devices`);
  } catch (ex) {
    return { status: "server-error", devices: {} };
  }

  if (!response.ok) { return { status: "server-error", devices: {} }; }
  const json = await response.json();
  return { status: "success", devices: json };
}