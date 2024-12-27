package cz.home.interview.ubiquiti.device;

import cz.home.interview.ubiquiti.device.exception.DeviceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DeviceController {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/devices")
    public void registerDevice(@RequestBody RegisterDeviceRequest deviceRequest) {
        logger.info("Registering {} device with MAC address: {}", deviceRequest.getDeviceType(), deviceRequest.getMacAddress());
        deviceService.registerDevice(deviceRequest.getDeviceType(), deviceRequest.getMacAddress(), deviceRequest.getUplinkMacAddress());
    }

    @GetMapping("/devices")
    public List<Device> getAllDevices() {
        logger.info("Retrieving all devices");
        return deviceService.getAllDevices();
    }

    @GetMapping("/devices/{macAddress}")
    public Device getDeviceByMacAddress(@PathVariable String macAddress) {
        logger.info("Retrieving device with MAC address: {}", macAddress);
        Device device = deviceService.getDeviceByMacAddress(macAddress);
        if (device == null) {
            logger.warn("Device with MAC address {} not found", macAddress);
            throw new DeviceNotFoundException(macAddress);
        }
        return device;
    }

    @GetMapping("/topology")
    public Map<String, List<String>> getDeviceTopology() {
        logger.info("Retrieving device topology");
        return deviceService.getDeviceTopology();
    }

    @GetMapping("/topology/{macAddress}")
    public Map<String, List<String>> getDeviceTopologyFrom(@PathVariable String macAddress) {
        logger.info("Retrieving device topology from MAC address: {}", macAddress);
        return deviceService.getDeviceTopologyFrom(macAddress);
    }
}