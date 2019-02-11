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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author robert
 */
public class PortScanner {

    private final Logger LOGGER = LogManager.getLogger();

    private final String host;

    public PortScanner(String host) {
        host = host.replaceAll("https://", "");
        host = host.replaceAll("http://", "");

        this.host = host;
    }

    public PortScannerReport scan() {
        try {
            InetAddress address = InetAddress.getByName(host);
            String hostAddress = address.getHostAddress();
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("nmap -sT --open " + hostAddress);
            process.waitFor(10, TimeUnit.MINUTES);
            InputStream scannerOutput = process.getInputStream();
            PortScannerReport report = processScannerOutput(scannerOutput);
            return report;
        } catch (IOException | InterruptedException ex) {
            LOGGER.warn("Could not scan Host", ex);
            PortScannerReport portScannerReport = new PortScannerReport(host);
            portScannerReport.setHostIsUp(Boolean.FALSE);
            return portScannerReport;
        }
    }

    private PortScannerReport processScannerOutput(InputStream scannerOutput) {
        String text = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(scannerOutput));
            String line = null;
            boolean serviceMode = false;
            List<PortResult> portResultList = new LinkedList<>();
            boolean isUp = false;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.contains("host up")) {
                    isUp = true;
                }
                if (line.startsWith("Nmap done")) {
                    break;
                }
                if (line.trim().startsWith("PORT")) {

                    serviceMode = true;
                    continue;
                }
                if (line.trim().equals("")) {
                    serviceMode = false;
                    continue;
                }
                if (serviceMode) {
                    PortResult result = createPortResult(line);
                    portResultList.add(result);
                }
            }
            PortScannerReport portScannerReport = new PortScannerReport(host);
            portScannerReport.setHostIsUp(isUp);
            portScannerReport.setPortResultList(portResultList);
            adjustReport(portScannerReport);
            return portScannerReport;
        } catch (IOException ex) {
            LOGGER.warn("Caught IOException", ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                LOGGER.warn("Error while closing IO Stream", ex);
            }
        }
        return null;
    }

    private PortResult createPortResult(String line) throws IOException {
        String[] split = line.split(" ");
        if (split.length < 2) {
            LOGGER.error("Unparseable output line");
            throw new IOException("Unparsable Service");
        }
        Integer port = Integer.parseInt(split[0].replace("/tcp", ""));
        String serviceName = null;
        if (split.length > 2) {
            serviceName = split[2];
        }
        return new PortResult(port, serviceName);
    }

    private void adjustReport(PortScannerReport portScannerReport) {
        List<PortResult> questionablePortList = new LinkedList<>();
        for (PortResult result : portScannerReport.getPortResultList()) {
            if (null != result.getPort()) {
                switch (result.getPort()) {
                    case 23:
                        portScannerReport.setTelnetIsOpen(true);
                        break;
                    case 194:
                        portScannerReport.setIrcIsOpen(true);
                        break;
                    case 1433:
                        portScannerReport.setMsSqlIsOpen(true);
                        break;
                    case 3306:
                        portScannerReport.setMySqlIsOpen(true);
                        break;
                    case 3389:
                        portScannerReport.setRdpIsOpen(true);
                        break;
                    case 80: // HTTP
                    case 443: // HTTPS
                    case 22: // SSH
                    case 20: // FTP
                    case 21: // FTP
                    case 25: // SMTP
                    case 110: // Pop3
                    case 143: // IMAP
                    case 989: // FTPS
                    case 990: // FTPS
                    case 115: // SFTP
                    case 465: // SMTPS
                    case 993: // IMAPS
                    case 995: // POP3S

                        // Probably intended open ports
                        break;
                    default:
                        questionablePortList.add(result);
                        break;
                }
            }

        }
        portScannerReport.setQuestionableResultList(questionablePortList);
    }
}
