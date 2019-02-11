/**
 *  WS-Port-Scanner - A Webservice for NMAP in the SIWECOS-Project
 *
 *  Copyright 2019-2019 Ruhr University Bochum / Hackmanit GmbH
 *
 *  Licensed under Apache License 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 */
/*
 */
package de.rub.nds.siwecos.port.scanner;

/**
 *
 * @author robert
 */
public class PortResult {

    private Integer port;

    private String serviceName;

    public PortResult(Integer port, String serviceName) {
        this.port = port;
        this.serviceName = serviceName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
