package org.rdm.kc.usb;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;

import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbPort;
import javax.usb.UsbServices;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import org.usb4java.javax.Services;

public class USBControls {

	private UsbDevice device;
	private UsbInterface iface;
	private short vendorId, productId;
	
	private boolean open;

	public USBControls( short vendorId, short productId) {
		super();
		this.vendorId = vendorId;
		this.productId = productId;
		this.open = false;
	}

	/**
	 * Find a devices with the given product and verndor
	 * @param hub
	 * @param vendorId
	 * @param productId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public UsbDevice findDevice( UsbHub hub, short vendorId, short productId )
	{
		Collection<UsbDevice> devices = hub.getAttachedUsbDevices();
		for (UsbDevice device : devices )
		{
			UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
			if (desc.idVendor() == vendorId && desc.idProduct() == productId) 
				return device;
			if (device.isUsbHub())
			{
				device = findDevice((UsbHub) device, vendorId, productId);
				if (device != null) return device;
			}
		}
		return null;
	}


	public void open(){
		try{
			UsbServices services = UsbHostManager.getUsbServices();
			this.device = findDevice( services.getRootUsbHub(), vendorId, productId);
			UsbConfiguration configuration = device.getActiveUsbConfiguration();
			this.iface = configuration.getUsbInterface((byte) 1);
			iface.claim();
			this.open = true;
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}
	
	public boolean isOpen() {
		return open;
	}

	public void close(){
		try{
			iface.release();
			this.open = false;
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}

	public static void listen (UsbServices services ) throws SecurityException, UsbException{
		services.addUsbServicesListener(new UsbServicesListener() {
		    @Override
			public
		    void usbDeviceAttached(UsbServicesEvent event) {
	            System.out.println("connect " + event.getUsbDevice());
		    }

		    @Override
			public
		    void usbDeviceDetached(UsbServicesEvent event) {
	            System.out.println("diisconnect " + event.getUsbDevice());
		    }
		});
		//while(true) { ... }	
	}
	
	public static void listPeripherique(UsbHub hub) {
		 Collection<UsbDevice> perepheriques = hub.getAttachedUsbDevices();
		 Iterator<UsbDevice> iterator = perepheriques.iterator();
		 while (iterator.hasNext()) {
		   UsbDevice perepherique = (UsbDevice) iterator.next();
		   perepherique.addUsbDeviceListener(new UsbDeviceListener() {

		        @Override
		        public void usbDeviceDetached(UsbDeviceEvent arg0) {
		            System.out.println("connect " + arg0);

		        }

		        @Override
		        public void errorEventOccurred(UsbDeviceErrorEvent arg0) {
		            System.out.println("disconect " + arg0);

		        }

		        @Override
		        public void dataEventOccurred(UsbDeviceDataEvent arg0) {
		            System.out.println("new data on " + arg0);  
		        }
		    });
		  
		  if (perepherique.isUsbHub()) {
		    listPeripherique((UsbHub) perepherique);
		  }
		} 
	}
	/**
	 * Dumps the specified device and its sub devices.
	 * 
	 * @param device
	 *            The USB device to dump.
	 * @param level
	 *            The indentation level.
	 */
	@SuppressWarnings("unchecked")
	public static void dump(UsbDevice device, int level)
	{
		for (int i = 0; i < level; i += 1)
			System.out.print("  ");
		System.out.println(device);
		if (device.isUsbHub())
		{
			final UsbHub hub = (UsbHub) device;
			Collection<UsbDevice> devices = hub.getAttachedUsbDevices();
			for (UsbDevice child: devices )
			{
				dump(child, level + 1);
			}
		}
	}

	/**
	 * Print the available services
	 * 
	 * @param args
	 *            Command-line arguments (Ignored).
	 * @throws UsbException
	 *             When USB communication fails.
	 */
	public static void printServices() throws UsbException
	{
		UsbServices services = UsbHostManager.getUsbServices();
		dump(services.getRootUsbHub(), 0);
	}
	
	@SuppressWarnings("unchecked")
	public static void printDevices( UsbDevice device ) throws UsbException
	{
		// Dump information about the device itself
		System.out.println(device);
		final UsbPort port = device.getParentUsbPort();
		if (port != null)
		{
			System.out.println("Connected to port: " + port.getPortNumber());
			System.out.println("Parent: " + port.getUsbHub());
		}

		// Dump device descriptor
		System.out.println(device.getUsbDeviceDescriptor());

		// Process all configurations
		Collection<UsbConfiguration> configurations = device.getUsbConfigurations();
		for (UsbConfiguration configuration: configurations )
		{
			// Dump configuration descriptor
			System.out.println(configuration.getUsbConfigurationDescriptor());

			// Process all interfaces
			Collection<UsbInterface> interfaces = configuration.getUsbInterfaces();
			for (UsbInterface iface: interfaces )
			{
				// Dump the interface descriptor
				System.out.println(iface.getUsbInterfaceDescriptor());

				// Process all endpoints
				Collection<UsbEndpoint> endpoints = iface.getUsbEndpoints();
				for (UsbEndpoint endpoint:endpoints)
				{
					// Dump the endpoint descriptor
					System.out.println(endpoint.getUsbEndpointDescriptor());
				}
			}
		}

		System.out.println();

		// Dump child devices if device is a hub
		if (device.isUsbHub())
		{
			final UsbHub hub = (UsbHub) device;
			Collection<UsbDevice> devices = hub.getAttachedUsbDevices(); 
			for (UsbDevice child: devices )
			{
				printDevices(child);
			}
		}
	}

	/**
	 * Dumps the names of all USB devices by using the javax-usb API. On
	 * Linux this can only work when your user has write permissions on all the USB
	 * device files in /dev/bus/usb (Running this example as root will work). On
	 * Windows this can only work for devices which have a libusb-compatible driver
	 * installed. On OSX this usually works without problems.
	 * 
	 * @author Klaus Reimer <k@ailis.de>
	 */
	private static void dumpName(final UsbDevice device)
			throws UnsupportedEncodingException, UsbException
	{
		// Read the string descriptor indices from the device descriptor.
		// If they are missing then ignore the device.
		final UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
		final byte iManufacturer = desc.iManufacturer();
		final byte iProduct = desc.iProduct();
		if (iManufacturer == 0 || iProduct == 0) return;

		// Dump the device name
		System.out.println(device.getString(iManufacturer) + " "
				+ device.getString(iProduct));
	}
	
    /**
     * Processes the specified USB device.
     * 
     * @param device
     *            The USB device to process.
     */
    @SuppressWarnings("unchecked")
	public static void processDevice(final UsbDevice device)
    {
        // When device is a hub then process all child devices
        if (device.isUsbHub())
        {
            final UsbHub hub = (UsbHub) device;
    		Collection<UsbDevice> devices = hub.getAttachedUsbDevices();
           for (UsbDevice child: devices )
            {
                processDevice(child);
            }
        }

        // When device is not a hub then dump its name.
        else
        {
            try
            {
                dumpName(device);
            }
            catch (Exception e)
            {
                // On Linux this can fail because user has no write permission
                // on the USB device file. On Windows it can fail because
                // no libusb device driver is installed for the device
                System.err.println("Ignoring problematic device: " + e);
            }
        }
    }	
}