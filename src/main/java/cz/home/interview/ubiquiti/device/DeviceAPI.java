package cz.home.interview.ubiquiti.device;

import java.util.List;
import java.util.Map;

public interface DeviceAPI {

    /**
     * Registers a new device with the given details.
     *
     * @param deviceType the type of the device
     * @param macAddress the MAC address of the device
     * @param uplinkMacAddress the MAC address of the uplink device
     */
    void registerDevice(DeviceType deviceType, String macAddress, String uplinkMacAddress);

    /**
     * Retrieves a list of all registered devices.
     *
     * @return a list of all devices
     */
    List<Device> getAllDevices();

    /**
     * Retrieves a device by its MAC address.
     *
     * @param macAddress the MAC address of the device
     * @return the device with the specified MAC address
     */
    Device getDeviceByMacAddress(String macAddress);

    /**
     * Retrieves the topology of all devices.
     *
     * @return a map representing the device topology
     */
    Map<String, List<String>> getDeviceTopology();

    /**
     * Retrieves the topology starting from a specific device.
     *
     * @param macAddress the MAC address of the starting device
     * @return a map representing the device topology from the specified device
     */
    Map<String, List<String>> getDeviceTopologyFrom(String macAddress);
}