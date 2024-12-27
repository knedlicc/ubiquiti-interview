package cz.home.interview.ubiquiti;

import cz.home.interview.ubiquiti.device.Device;
import cz.home.interview.ubiquiti.device.DeviceService;
import cz.home.interview.ubiquiti.device.DeviceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DeviceServiceTest {

    @InjectMocks
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterDevice() {
        deviceService.registerDevice(DeviceType.GATEWAY, "00:11:22:33:44:55", null);
        Device device = deviceService.getDeviceByMacAddress("00:11:22:33:44:55");
        assertNotNull(device);
        assertEquals(DeviceType.GATEWAY, device.getDeviceType());
    }

    @Test
    void testRegisterDeviceWithUplinkAddress() {
        deviceService.registerDevice(DeviceType.GATEWAY, "00:11:22:33:44:55", null);
        deviceService.registerDevice(DeviceType.SWITCH, "66:77:88:99:AA:BB", "00:11:22:33:44:55");
        Device device = deviceService.getDeviceByMacAddress("66:77:88:99:AA:BB");
        assertNotNull(device);
        assertEquals(DeviceType.SWITCH, device.getDeviceType());
        assertEquals("00:11:22:33:44:55", device.getUplinkMacAddress());
    }

    @Test
    void testGetAllDevices() {
        deviceService.registerDevice(DeviceType.GATEWAY, "00:11:22:33:44:55", null);
        deviceService.registerDevice(DeviceType.SWITCH, "66:77:88:99:AA:BB", null);
        List<Device> devices = deviceService.getAllDevices();
        assertEquals(2, devices.size());
        assertEquals(DeviceType.GATEWAY, devices.get(0).getDeviceType());
        assertEquals(DeviceType.SWITCH, devices.get(1).getDeviceType());
    }

    @Test
    void testGetDeviceByMacAddress() {
        deviceService.registerDevice(DeviceType.GATEWAY, "00:11:22:33:44:55", null);
        Device device = deviceService.getDeviceByMacAddress("00:11:22:33:44:55");
        assertNotNull(device);
        assertEquals(DeviceType.GATEWAY, device.getDeviceType());
    }

    @Test
    void testGetDeviceByMacAddressNotFound() {
        Device device = deviceService.getDeviceByMacAddress("00:11:22:33:44:55");
        assertNull(device);
    }

    @Test
    void testGetDeviceTopology() {
        deviceService.registerDevice(DeviceType.GATEWAY, "00:11:22:33:44:55", null);
        deviceService.registerDevice(DeviceType.SWITCH, "66:77:88:99:AA:BB", "00:11:22:33:44:55");
        Map<String, List<String>> topology = deviceService.getDeviceTopology();
        assertEquals(1, topology.size());
        assertTrue(topology.containsKey("00:11:22:33:44:55"));
        assertEquals(1, topology.get("00:11:22:33:44:55").size());
        assertEquals("66:77:88:99:AA:BB", topology.get("00:11:22:33:44:55").get(0));
    }

    @Test
    void testGetDeviceTopologyFrom() {
        deviceService.registerDevice(DeviceType.GATEWAY, "00:11:22:33:44:55", null);
        deviceService.registerDevice(DeviceType.SWITCH, "66:77:88:99:AA:BB", "00:11:22:33:44:55");
        deviceService.registerDevice(DeviceType.ACCESS_POINT, "CC:DD:EE:FF:00:11", "66:77:88:99:AA:BB");
        Map<String, List<String>> subTopology = deviceService.getDeviceTopologyFrom("00:11:22:33:44:55");
        assertEquals(2, subTopology.size());
        assertTrue(subTopology.containsKey("00:11:22:33:44:55"));
        assertTrue(subTopology.containsKey("66:77:88:99:AA:BB"));
        assertEquals(1, subTopology.get("00:11:22:33:44:55").size());
        assertEquals("66:77:88:99:AA:BB", subTopology.get("00:11:22:33:44:55").get(0));
        assertEquals(1, subTopology.get("66:77:88:99:AA:BB").size());
        assertEquals("CC:DD:EE:FF:00:11", subTopology.get("66:77:88:99:AA:BB").get(0));
    }

    @Test
    void testRegisterMultipleDevicesWithSameUplink() {
        deviceService.registerDevice(DeviceType.GATEWAY, "00:11:22:33:44:55", null);
        deviceService.registerDevice(DeviceType.SWITCH, "66:77:88:99:AA:BB", "00:11:22:33:44:55");
        deviceService.registerDevice(DeviceType.ACCESS_POINT, "CC:DD:EE:FF:00:11", "00:11:22:33:44:55");
        Map<String, List<String>> topology = deviceService.getDeviceTopology();
        assertEquals(1, topology.size());
        assertTrue(topology.containsKey("00:11:22:33:44:55"));
        assertEquals(2, topology.get("00:11:22:33:44:55").size());
        assertTrue(topology.get("00:11:22:33:44:55").contains("66:77:88:99:AA:BB"));
        assertTrue(topology.get("00:11:22:33:44:55").contains("CC:DD:EE:FF:00:11"));
    }

    @Test
    void testGetDeviceTopologyMultipleLevels() {
        deviceService.registerDevice(DeviceType.GATEWAY, "00:11:22:33:44:55", null);
        deviceService.registerDevice(DeviceType.SWITCH, "66:77:88:99:AA:BB", "00:11:22:33:44:55");
        deviceService.registerDevice(DeviceType.ACCESS_POINT, "CC:DD:EE:FF:00:11", "66:77:88:99:AA:BB");
        deviceService.registerDevice(DeviceType.SWITCH, "11:22:33:44:55:66", "CC:DD:EE:FF:00:11");
        Map<String, List<String>> topology = deviceService.getDeviceTopology();
        assertEquals(3, topology.size());
        assertTrue(topology.containsKey("00:11:22:33:44:55"));
        assertTrue(topology.containsKey("66:77:88:99:AA:BB"));
        assertTrue(topology.containsKey("CC:DD:EE:FF:00:11"));
        assertEquals(1, topology.get("00:11:22:33:44:55").size());
        assertEquals("66:77:88:99:AA:BB", topology.get("00:11:22:33:44:55").get(0));
        assertEquals(1, topology.get("66:77:88:99:AA:BB").size());
        assertEquals("CC:DD:EE:FF:00:11", topology.get("66:77:88:99:AA:BB").get(0));
        assertEquals(1, topology.get("CC:DD:EE:FF:00:11").size());
        assertEquals("11:22:33:44:55:66", topology.get("CC:DD:EE:FF:00:11").get(0));
    }
}