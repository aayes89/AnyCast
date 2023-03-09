/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anycasttest;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Scanner;
import javax.swing.JFileChooser;

/**
 *
 * @author Andrea
 */
public class AnyCastTest {

    static final int PORT = 50000;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        UPNPUtils utils = new UPNPUtils();
        //System.out.println("Looking for alive UPNP/DLNA devices...");
        //utils.lookupDevices();
        System.out.println("Registering UPNP/DLNA devices founded...");
        utils.registerNewDevices("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + PORT);
        //utils.devices.getFirst().printAllData();
        utils.loadVideoLink("http://misc.commonsware.com/ed_hd_512kb.mp4");
        Scanner in = new Scanner(System.in);
        System.out.println("Insert the url to Play");
        //utils.loadVideoLink(in.nextLine());
        //utils.loadVideoLink("D:\\Peliculas\\Major Lazer – Light it Up (feat. Nyla & Fuse ODG).mp4");
             //JFileChooser chooser=new JFileChooser();
        //chooser.showOpenDialog(null);
        //File selected = chooser.getSelectedFile();
        /*
         *0-"SetAVTransportURI"
         1-"GetPositionInfo"
         2-"Play"
         3-"Pause"
         4-"Stop"
         5-"Next"
         6-"Previous"
         7-"Seek"
         8-"SetVolume"
         9-"GetVolumen"
         10-"GetMute"
         11-"GetProtocolInfo"
         12-"GetTransportInfo"
         */
        //utils.loadVideoLink("http://192.168.0.162:8080/C:/Users/Andrea/Documents/NetBeansProjects/AnyCastTest/screen.jpg");

        //utils.loadVideoLink("C:/Users/Andrea/Documents/NetBeansProjects/AnyCastTest/screen.jpg");//+selected.getAbsolutePath());
        String data;
        Device dev = utils.getFirstDevice();
        data = utils.getXmlHead() + utils.getXmlStop() + utils.getXmlFoot();
        //System.out.println("Result of Stop: " + utils.sendData(data, dev.getSoapActions()[4], dev));

        data = utils.getXmlHead() + utils.getUriLoad() + utils.getXmlFoot();
        System.out.println("Sending: \n" + data);
        System.out.println("Result of sending UriLoad: " + utils.sendData(data, dev.getSoapActions()[0], dev));

        data = utils.getXmlHead() + utils.getUriPlay() + utils.getXmlFoot();
        System.out.println("Sending: \n" + data);
        System.out.println("Result of sending UriPlay: " + utils.sendData(data, dev.getSoapActions()[2], dev));

        /*data = utils.getXmlHead() + utils.getXmlGetVolumen() + utils.getXmlFoot();
         System.out.println("Result: " + utils.sendData(data, dev.getSoapActions()[9], dev));

         data = utils.getXmlHead() + utils.getXmlVolumen() + utils.getXmlFoot();
         System.out.println("Result: " + utils.sendData(data, dev.getSoapActions()[8], dev));
         /*
         Robot rob = new Robot();
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
         Rectangle rect = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
         System.out.println("MyResolution: " + rect.getWidth() + "," + rect.getHeight());
         BufferedImage bimg = rob.createScreenCapture(rect);
         ImageIO.write(bimg, "JPG", new File("screen.jpg"));
         */
    }
}
