/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anycasttest;

import java.util.LinkedList;

/**
 *
 * @author Andrea
 */
public class Device {

    private String name;
    private String manufacturer;
    private String manufacturerUrl;
    private String modelDescription;
    private String modelName;
    private String modelNumber;
    private String modelURL;
    private String host;
    private int port;
    private LinkedList<Service> serviceList;
    private String UDN;
    private String deviceType;
    private String dlna;
    private String[] SoapActions;

    public Device(String name, String manufacturer, String manufacturerUrl, String modelDescription, String modelName, String modelNumber, String modelURL, String host, int port, LinkedList<Service> serviceList, String UDN, String deviceType, String dlna) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.manufacturerUrl = manufacturerUrl;
        this.modelDescription = modelDescription;
        this.modelName = modelName;
        this.modelNumber = modelNumber;
        this.modelURL = modelURL;
        this.host = host;
        this.port = port;
        this.serviceList = serviceList;
        this.UDN = UDN;
        this.deviceType = deviceType;
        this.dlna = dlna;
        this.SoapActions=new String[]{
            "AVTransport:1#SetAVTransportURI",
            "AVTransport:1#GetPositionInfo",
            "AVTransport:1#Play",
            "AVTransport:1#Pause",
            "AVTransport:1#Stop",
            "AVTransport:1#Next",
            "AVTransport:1#Previous",
            "AVTransport:1#Seek",
            "RenderingControl:1#SetVolume",
            "RenderingControl:1#GetVolume",
            "RenderingControl:1#GetMute",
            "ConnectionManager:1#GetProtocolInfo",
            "AVTransport:1#GetTransportInfo"};
    }

    public String[] getSoapActions() {
        return SoapActions;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturerUrl() {
        return manufacturerUrl;
    }

    public void setManufacturerUrl(String manufacturerUrl) {
        this.manufacturerUrl = manufacturerUrl;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getModelURL() {
        return modelURL;
    }

    public void setModelURL(String modelURL) {
        this.modelURL = modelURL;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUDN() {
        return UDN;
    }

    public void setUDN(String UDN) {
        this.UDN = UDN;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDlna() {
        return dlna;
    }

    public void setDlna(String dlna) {
        this.dlna = dlna;
    }

    public LinkedList<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(LinkedList<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public void printAllData() {
        System.out.println("Device Type:    " + getDeviceType() + "\n"
                + "Familiar Name:   " + getName() + "\n"
                + "Manufacturer:    " + getManufacturer() + "\n"
                + "Manufacturer URL:    " + getManufacturerUrl() + "\n"
                + "Model Name:  " + getModelName() + "\n"
                + "Model Description:   " + getModelDescription() + "\n"
                + "Model Number:    " + getModelNumber() + "\n"
                + "Model URL:   " + getModelURL() + "\n"
                + "DLNA:    " + getDlna() + "\n"
                + "UDN: " + getUDN() + "\n"
                + "URL Server:  http://" + getHost() + ":" + getPort());
        System.out.println("\nService List:\n");
        for (Service service : serviceList) {
            System.out.println("Service Type: " + service.getServiceType() + "\n"
                    + "Service ID: " + service.getServiceId() + "\n"
                    + "SCPURL: " + service.getSCPDURL() + "\n"
                    + "Control URL: " + service.getControlURL() + "\n"
                    + "Event URL: " + service.getEventSubURL());
        }

    }
}
