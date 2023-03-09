/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anycasttest;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.*;
import org.w3c.dom.*;

/**
 *
 * @author Andrea
 */
public class UPNPUtils {

    String uriLoad;
    String uriPlay;
    String xmlHead;
    String xmlFoot;
    String xmlGetPos;
    String xmlStop;
    String xmlPause;
    String xmlVolumen;
    String xmlGetTransportInfo;
    String xmlGetPositionInfo;
    String xmlGetProtocolInfo;
    String xmlGetVolumen;
    String xmlGetMute;
    URL link;
    HttpURLConnection urlCon;
    NetworkInterfaceFinder nif;
    String SEARCH_TARGET;
    String sendMsg;
    MulticastSocket ms;
    Socket sock;
    InetAddress iaddrs;
    LinkedList<Device> devices = new LinkedList<>();
    LinkedList<String> devListed = new LinkedList<>();

    public UPNPUtils() {
        this.xmlHead = "<?xml version=\"1.0\"?>\n<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n<SOAP-ENV:Body>\n";
        this.uriPlay = "<u:Play xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><Speed>1</Speed></u:Play>\n";
        this.xmlGetPos = "<m:GetPositionInfo xmlns:m=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"ui4\">0</InstanceID></m:GetPositionInfo>\n";
        this.xmlStop = "<u:Stop xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID></u:Stop>\n";
        this.xmlPause = "<u:Pause xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID></u:Pause>\n";
        this.xmlVolumen = "<u:SetVolume xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\"><InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>100</DesiredVolume></u:SetVolume>\n";
        this.xmlGetTransportInfo = "<m:GetTransportInfo xmlns:m=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"ui4\">0</InstanceID></m:GetTransportInfo>";
        this.xmlGetPositionInfo = "<m:GetPositionInfo xmlns:m=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"ui4\">0</InstanceID></m:GetPositionInfo>";
        this.xmlGetProtocolInfo = "<m:GetProtocolInfo xmlns:m=\"urn:schemas-upnp-org:service:ConnectionManager:1\"/>";
        this.xmlGetVolumen = "<m:GetVolume xmlns:m=\"urn:schemas-upnp-org:service:RenderingControl:1\"><InstanceID xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"ui4\">0</InstanceID><Channel xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"string\">Master</Channel></m:GetVolume>";
        this.xmlGetMute = "<m:GetMute xmlns:m=\"urn:schemas-upnp-org:service:RenderingControl:1\"><InstanceID xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"ui4\">0</InstanceID><Channel xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"string\">Master</Channel></m:GetMute>";
        this.xmlFoot = "</SOAP-ENV:Body>\n</SOAP-ENV:Envelope>\n";
        this.SEARCH_TARGET = "urn:schemas-upnp-org:service:AVTransport:1";
        this.sendMsg = "M-SEARCH * HTTP/1.1\r\n";
        sendMsg += "HOST:239.255.255.250:1900\r\n";
        sendMsg += "ST:" + SEARCH_TARGET + "\r\n";
        sendMsg += "MX:2\r\n";
        sendMsg += "MAN:\"ssdp:alive\"\r\n\r\n";
        try {
            nif = new NetworkInterfaceFinder();
            ms = new MulticastSocket(1900);
            iaddrs = InetAddress.getByName("239.255.255.250");
        } catch (IOException ex) {
            Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lookupDevices() {
        try {
            NetworkInterface ni = nif.getMyNetworkInterface();
            ms.setReuseAddress(true);
            ms.joinGroup(new InetSocketAddress(iaddrs, 1900), ni);
            ms.send(new DatagramPacket(sendMsg.getBytes(StandardCharsets.UTF_8), sendMsg.length(), iaddrs, 1900));
            boolean stop = false;
            while (!stop) {
                byte[] buff = new byte[Byte.MAX_VALUE];
                ms.receive(new DatagramPacket(buff, buff.length, iaddrs, 1900));
                String data = (ByteConverter(buff));
                if (data.contains("Location")) {
                    String urlDev = "";
                    for (int i = data.indexOf("Location:") + 10; i < data.length(); i++) {
                        char let = data.charAt(i);
                        if (let == '\n') {
                            break;
                        } else {
                            urlDev += let;
                        }
                    }
                    devListed.add(urlDev);
                }
                if (!devListed.isEmpty()) {
                    System.out.println("Devices founded: " + devListed.size());
                    stop = true;
                }

            }
            ms.leaveGroup(new InetSocketAddress(iaddrs, 1900), ni);

        } catch (SocketException ex) {
            Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void registerNewDevices(String url) {
        devListed.add(url);
        //devListed.add("http://192.168.0.112:50000/");
        

        for (String links : devListed) {
            System.out.println("Start scanning on: " + links);
            if (openConnectionWell(links) == true) {
                try {
                    DocumentBuilderFactory docbfact = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dbuild = docbfact.newDocumentBuilder();
                    //InputStream is = urlCon.getInputStream();
                    //org.w3c.dom.Document xmldoc = dbuild.parse(is);
                    org.w3c.dom.Document xmldoc = dbuild.parse(new File("AnyCast.xml"));
                    String devType = xmldoc.getElementsByTagName("deviceType").item(0).getTextContent();
                    String name = xmldoc.getElementsByTagName("friendlyName").item(0).getTextContent();
                    String manufucturer = xmldoc.getElementsByTagName("manufacturer").item(0).getTextContent();
                    String manufucturerURL = xmldoc.getElementsByTagName("manufacturerURL").item(0).getTextContent();
                    String modelDesc = xmldoc.getElementsByTagName("modelDescription").item(0).getTextContent();
                    String modelName = xmldoc.getElementsByTagName("modelName").item(0).getTextContent();
                    String modelNumber = xmldoc.getElementsByTagName("modelNumber").item(0).getTextContent();
                    String modelURL = xmldoc.getElementsByTagName("modelURL").item(0).getTextContent();
                    String udn = xmldoc.getElementsByTagName("UDN").item(0).getTextContent();
                    String dlna = xmldoc.getElementsByTagName("dlna:X_DLNADOC").item(0).getTextContent();
                    String[] tmp = links.split(":");
                    String host = tmp[1].substring(2);
                    int port = Integer.valueOf(tmp[2].replace('/', '\n').trim());
                    LinkedList<Service> listServices = new LinkedList<Service>();
                    dom(xmldoc, listServices);
                    Device d = new Device(name, manufucturer, manufucturerURL, modelDesc, modelName, modelNumber, modelURL, host, port, listServices, udn, devType, dlna);
                    addDevice(d);
                } catch (ProtocolException ex) {
                    Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    break;
                }
            }
        }
    }

    public void dom(org.w3c.dom.Document doc, LinkedList<Service> servList) {
        org.w3c.dom.Document xmldoc = doc;
        org.w3c.dom.Element root = xmldoc.getDocumentElement();
        NodeList serviceList = root.getElementsByTagName("service");
        for (int i = 0; i < serviceList.getLength(); i++) {
            Node service = serviceList.item(i);
            Service serv = new Service();
            NodeList serviceData = service.getChildNodes();
            for (int j = 0; j < serviceData.getLength(); j++) {
                Node data = serviceData.item(j);
                if (data.getNodeType() == Node.ELEMENT_NODE) {
                    //System.out.print(data.getNodeName() + ": ");
                    Node datoContent = data.getFirstChild();
                    if (datoContent != null && datoContent.getNodeType() == Node.TEXT_NODE) {
                        //System.out.println(j+": "+datoContent.getNodeValue());
                        switch (j) {
                            case 1:
                                serv.setServiceType(datoContent.getNodeValue());
                                break;
                            case 3:
                                serv.setServiceId(datoContent.getNodeValue());
                                break;
                            case 5:
                                serv.setSCPDURL(datoContent.getNodeValue());
                                break;
                            case 7:
                                serv.setControlURL(datoContent.getNodeValue());
                                break;
                            case 9:
                                serv.setEventSubURL(datoContent.getNodeValue());
                                break;
                        }
                    }
                }
            }
            servList.add(serv);
        }
    }

    public boolean openConnectionWell(String host) {
        boolean result = false;
        try {
            link = new URL(host);
            sock = new Socket(host, 50000);
            result = sock.isConnected();
            /*urlCon = (HttpURLConnection) link.openConnection();
             urlCon.setRequestProperty("Method:", "GET");            
             urlCon.setReadTimeout(5000);
             result = (urlCon.getResponseCode() == HttpURLConnection.HTTP_OK);            
             urlCon.disconnect();
             */
        } catch (MalformedURLException ex) {
            Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void addDevice(Device dev) {
        devices.add(dev);
    }

    public Device getDevice(String ip) {
        Device tmpDev = null;
        for (Device dev : devices) {
            if (dev.getHost().equalsIgnoreCase(ip)) {
                tmpDev = dev;
                break;
            }
        }
        return tmpDev;
    }

    public Device getFirstDevice() {
        return devices.getFirst();
    }

    public boolean sendData(String dataSend, String SoapAction, Device dev) {
        boolean result = false;
        try {
            //sock.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));            
            if (SoapAction.contains("AVTransport")) {
                link = new URL("http://" + dev.getHost() + ":" + dev.getPort() + dev.getServiceList().getFirst().getControlURL());
            } else if (SoapAction.contains("RenderingControl")) {
                link = new URL("http://" + dev.getHost() + ":" + dev.getPort() + "/RenderingControl/" + dev.getUDN().substring(5) + "/control.xml");
            } else if (SoapAction.contains("ConnectionManager")) {
                link = new URL("http://" + dev.getHost() + ":" + dev.getPort() + "/ConnectionManager/" + dev.getUDN().substring(5) + "/control.xml");
            }
            urlCon = (HttpURLConnection) link.openConnection();
            //String data ="POST "+dev.getServiceList().getFirst().getControlURL()+" HTTP/1.1\n"+dataSend;
            //urlCon.setRequestProperty("Method:", "POST");
            urlCon.setRequestProperty("HOST", dev.getHost() + ":" + dev.getPort());
            urlCon.setRequestProperty("Content-Length", dataSend.length() + "");
            urlCon.setRequestProperty("Connection", "Close");
            urlCon.setRequestProperty("Accept-Ranges", "bytes");
            urlCon.setRequestProperty("Content-type", "text/xml; charset=utf-8;");
            urlCon.setRequestProperty("Pragma", "no-cache");
            urlCon.setRequestProperty("USER-AGENT", "Java/6.3 UPnP/1.0 JVM-DLNA DLNADOC/1.0");
            //"urn:schemas-upnp-org:service:AVTransport:1#SetAVTransportURI"
            //"urn:schemas-upnp-org:service:ConnectionManager:1#GetProtocolInfo"\r\n
            urlCon.setRequestProperty("SOAPAction", "urn:schemas-upnp-org:service:" + SoapAction);
            urlCon.setDoOutput(true);
            urlCon.getOutputStream().write(dataSend.getBytes(StandardCharsets.UTF_8));
            int respCode = urlCon.getResponseCode();
            System.out.println("Response Code: " + respCode);
            result = (respCode == HttpURLConnection.HTTP_OK);
        } catch (IOException ex) {
            Logger.getLogger(UPNPUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    static String ByteConverter(byte[] dataIn) {
        String result = "";
        for (byte b : dataIn) {
            result += (char) b;
        }
        return result;
    }

    public void loadVideoLink(String url) {
        this.uriLoad = "<u:SetAVTransportURI xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><CurrentURI>" + url + "</CurrentURI><CurrentURIMetaData>URL: " + url + "</CurrentURIMetaData></u:SetAVTransportURI>\n";
    }

    public String getXmlGetTransportInfo() {
        return xmlGetTransportInfo;
    }

    public String getXmlGetPositionInfo() {
        return xmlGetPositionInfo;
    }

    public String getUriLoad() {
        return uriLoad;
    }

    public String getUriPlay() {
        return uriPlay;
    }

    public String getXmlHead() {
        return xmlHead;
    }

    public String getXmlFoot() {
        return xmlFoot;
    }

    public String getXmlGetPos() {
        return xmlGetPos;
    }

    public String getXmlStop() {
        return xmlStop;
    }

    public String getXmlPause() {
        return xmlPause;
    }

    public String getXmlVolumen() {
        return xmlVolumen;
    }

    public void setXmlVolumen(int Volumen) {
        this.xmlVolumen = "<u:SetVolume xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\"><InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>" + Volumen + "</DesiredVolume></u:SetVolume>\n";
        //this.xmlVolumen = "<u:SetVolume xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><Channel>0</Channel><DesiredVolume>" + Volumen + "</DesiredVolume></u:SetVolume>\n";;
    }

    public String getXmlGetProtocolInfo() {
        return xmlGetProtocolInfo;
    }

    public String getXmlGetVolumen() {
        return xmlGetVolumen;
    }

    public String getXmlGetMute() {
        return xmlGetMute;
    }

    public void setMute() {
        this.xmlVolumen = "<u:SetVolume xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\"><InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>0</DesiredVolume></u:SetVolume>\n";
    }

}
