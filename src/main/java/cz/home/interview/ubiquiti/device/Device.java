package cz.home.interview.ubiquiti.device;

public class Device {
    private DeviceType deviceType;
    private String macAddress;
    private String uplinkMacAddress;

    public Device(DeviceType deviceType, String macAddress, String uplinkMacAddress) {
        this.deviceType = deviceType;
        this.macAddress = macAddress;
        this.uplinkMacAddress = uplinkMacAddress;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getUplinkMacAddress() {
        return uplinkMacAddress;
    }
}