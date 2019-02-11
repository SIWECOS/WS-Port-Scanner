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

import java.util.List;

/**
 *
 * @author robert
 */
public class PortScannerReport {

    private String host;

    private String ip;

    private Boolean hostIsUp;

    private List<PortResult> portResultList;

    private Boolean telnetIsOpen = false;
    private Boolean mySqlIsOpen = false;
    private Boolean rdpIsOpen = false;
    private Boolean msSqlIsOpen = false;
    private Boolean ircIsOpen = false;

    private List<PortResult> questionableResultList;

    public PortScannerReport(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getHostIsUp() {
        return hostIsUp;
    }

    public void setHostIsUp(Boolean hostIsUp) {
        this.hostIsUp = hostIsUp;
    }

    public String getHost() {
        return host;
    }

    public List<PortResult> getPortResultList() {
        return portResultList;
    }

    public void setPortResultList(List<PortResult> portResultList) {
        this.portResultList = portResultList;
    }

    public Boolean getTelnetIsOpen() {
        return telnetIsOpen;
    }

    public void setTelnetIsOpen(Boolean telnetIsOpen) {
        this.telnetIsOpen = telnetIsOpen;
    }

    public Boolean getMySqlIsOpen() {
        return mySqlIsOpen;
    }

    public void setMySqlIsOpen(Boolean mySqlIsOpen) {
        this.mySqlIsOpen = mySqlIsOpen;
    }

    public Boolean getRdpIsOpen() {
        return rdpIsOpen;
    }

    public void setRdpIsOpen(Boolean rdpIsOpen) {
        this.rdpIsOpen = rdpIsOpen;
    }

    public Boolean getMsSqlIsOpen() {
        return msSqlIsOpen;
    }

    public void setMsSqlIsOpen(Boolean msSqlIsOpen) {
        this.msSqlIsOpen = msSqlIsOpen;
    }

    public List<PortResult> getQuestionableResultList() {
        return questionableResultList;
    }

    public void setQuestionableResultList(List<PortResult> questionableResultList) {
        this.questionableResultList = questionableResultList;
    }

    public Boolean getIrcIsOpen() {
        return ircIsOpen;
    }

    public void setIrcIsOpen(Boolean ircIsOpen) {
        this.ircIsOpen = ircIsOpen;
    }
}
