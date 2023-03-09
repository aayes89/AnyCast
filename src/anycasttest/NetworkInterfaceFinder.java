/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anycasttest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * @author Andrea
 */
public class NetworkInterfaceFinder {

    NetworkInterface ni = null;

    public NetworkInterfaceFinder() {

    }

    public NetworkInterface getMyNetworkInterface() throws SocketException {
        Enumeration<NetworkInterface> listNI = NetworkInterface.getNetworkInterfaces();
        while (listNI.hasMoreElements()) {
            NetworkInterface networkInterface = listNI.nextElement();
            if (networkInterface.isUp() && networkInterface.supportsMulticast()) {
                //System.out.println(networkInterface.toString());
                Enumeration<InetAddress> subIA = networkInterface.getInetAddresses();
                while (subIA.hasMoreElements()) {
                    InetAddress inetAddress = subIA.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ni = networkInterface;
                    }
                    //System.out.println(ni.getName()+" "+inetAddress.toString());
                }
            }
        }
        return ni;
    }
}
