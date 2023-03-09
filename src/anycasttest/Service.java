/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anycasttest;

/**
 *
 * @author Andrea
 */
class Service {

    private String serviceType;
    private String serviceId;
    private String SCPDURL;
    private String controlURL;
    private String eventSubURL;

    public Service(String serviceType, String serviceId, String SCPDURL, String controlURL, String eventSubURL) {
        this.serviceType = serviceType;
        this.serviceId = serviceId;
        this.SCPDURL = SCPDURL;
        this.controlURL = controlURL;
        this.eventSubURL = eventSubURL;
    }

    public Service() {

    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getSCPDURL() {
        return SCPDURL;
    }

    public String getControlURL() {
        return controlURL;
    }

    public String getEventSubURL() {
        return eventSubURL;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setSCPDURL(String SCPDURL) {
        this.SCPDURL = SCPDURL;
    }

    public void setControlURL(String controlURL) {
        this.controlURL = controlURL;
    }

    public void setEventSubURL(String eventSubURL) {
        this.eventSubURL = eventSubURL;
    }
}
