package cz.home.interview.ubiquiti.device;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceService implements DeviceAPI {

    private final Map<String, Device> devices = new HashMap<>();
    private final Map<String, List<String>> topology = new HashMap<>();

    @Override
    public void registerDevice(DeviceType deviceType, String macAddress, String uplinkMacAddress) {
        Device device = new Device(deviceType, macAddress, uplinkMacAddress);
        devices.put(macAddress, device);
        if(uplinkMacAddress != null) topology.computeIfAbsent(uplinkMacAddress, k -> new ArrayList<>()).add(macAddress);
    }

    @Override
    public List<Device> getAllDevices() {
        List<Device> deviceList = new ArrayList<>(devices.values());
        deviceList.sort(Comparator.comparing(Device::getDeviceType));
        return deviceList;
    }

    @Override
    public Device getDeviceByMacAddress(String macAddress) {
        return devices.get(macAddress);
    }

    @Override
    public Map<String, List<String>> getDeviceTopology() {
        return topology;
    }

    @Override
    public Map<String, List<String>> getDeviceTopologyFrom(String macAddress) {
        Map<String, List<String>> subTopology = new HashMap<>();
        buildSubTree(macAddress, subTopology);
        return subTopology;
    }

    private void buildSubTree(String macAddress, Map<String, List<String>> subTopology) {
        if (topology.containsKey(macAddress)) {
            subTopology.put(macAddress, topology.get(macAddress));
            for (String childMac : topology.get(macAddress)) {
                buildSubTree(childMac, subTopology);
            }
        }
    }
}