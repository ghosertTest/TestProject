import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import junit.framework.TestCase;

public class JavaBlueToothTester extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testJavaBlueTooth() throws Exception {
        LocalDevice localDevice=null;
        DiscoveryAgent discoveryAgent=null;
        try {
	        localDevice = LocalDevice.getLocalDevice();
	        localDevice.setDiscoverable(DiscoveryAgent.GIAC);
	        discoveryAgent = localDevice.getDiscoveryAgent();
	        discoveryAgent.startInquiry(DiscoveryAgent.GIAC,new DiscoveryListener() {
	            public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
                    System.out.println(remoteDevice.getBluetoothAddress());
	            }
	            public void inquiryCompleted(int arg0) {
                    System.out.println(arg0);
	            }
	            public void servicesDiscovered(int arg0, ServiceRecord[] serviceRecords) {
                    System.out.println(arg0);
	            }
	            public void serviceSearchCompleted(int arg0, int arg1) {
                    System.out.println(arg0);
	            }
	        });
        } catch (BluetoothStateException c) {
            System.out.println(c.getMessage());
            c.printStackTrace();
        }
    }
}
