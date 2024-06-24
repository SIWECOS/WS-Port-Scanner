/**
 *  WS-Port-Scanner - A Webservice for NMAP in the SIWECOS-Project
 *
 *  Copyright 2019-2019 Ruhr University Bochum / Hackmanit GmbH
 *
 *  Licensed under Apache License 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package de.rub.nds.siwecos.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.rub.nds.siwecos.port.json.HostAliveInfo;
import de.rub.nds.siwecos.port.json.ScanResult;
import de.rub.nds.siwecos.port.json.TestResult;
import de.rub.nds.siwecos.port.json.TranslateableMessage;
import de.rub.nds.siwecos.port.scanner.PortScanner;
import de.rub.nds.siwecos.port.scanner.PortScannerReport;
import de.rub.nds.siwecos.port.ws.DebugOutput;
import de.rub.nds.siwecos.port.ws.PoolManager;
import de.rub.nds.siwecos.port.ws.ScanRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Robert Merget <robert.merget@rub.de>
 */
public class PortScannerCallback implements Runnable {

    protected static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(PortScannerCallback.class
            .getName());

    private final ScanRequest request;

    private DebugOutput debugOutput;

    public PortScannerCallback(ScanRequest request, DebugOutput debugOutput) {
        this.request = request;
        this.debugOutput = debugOutput;
    }

    @Override
    public void run() {
        debugOutput.setLeftQueueAt(System.currentTimeMillis());
        debugOutput.setScanStartedAt(System.currentTimeMillis());
        debugOutput.setTimeInQueue(debugOutput.getLeftQueueAt() - debugOutput.getEnteredQueueAt());
        LOGGER.info("Scanning: " + request.getUrl());
        try {
            PortScanner scanner = new PortScanner(request.getUrl());
            PortScannerReport portScannerReport = scanner.scan();
            ScanResult result = reportToScanResult(portScannerReport);
            LOGGER.info("Finished scanning: " + request.getUrl());
            debugOutput.setScanFinisedAt(System.currentTimeMillis());
            debugOutput.setFinalQueueSize(PoolManager.getInstance().getService().getQueue().size());
            if (DebugManager.getInstance().isDebugEnabled()) {
                result.setDebugOutput(debugOutput);
            }
            answer(result);
        } catch (Throwable T) {
            LOGGER.warn("Failed to scan:" + request.getUrl());
            T.printStackTrace();
        }
    }

    public String scanResultToJson(ScanResult result) {
        ObjectMapper ow = new ObjectMapper();
        String json = "";
        try {
            json = ow.writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            LOGGER.warn("Could not convert to json");
            ex.printStackTrace();
        }
        return json;
    }

    public void answer(ScanResult result) {
        String json = scanResultToJson(result);
        for (String callback : request.getCallbackurls()) {
            LOGGER.info("Calling back: " + callback + " for " + result.getName());
            try {
                URL url = new URL(callback);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                con.setConnectTimeout(10000);
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setFixedLengthStreamingMode(json.getBytes().length);
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();
                try (OutputStream os = http.getOutputStream()) {
                    os.write(json.getBytes("UTF-8"));
                    os.flush();
                }
                LOGGER.debug(json);
                http.disconnect();
            } catch (Exception ex) {
                LOGGER.warn("Failed to callback:" + callback, ex);
            }
        }
    }

    public ScanResult reportToScanResult(PortScannerReport report) {
        if (report.getHostIsUp() != Boolean.TRUE) {
            return new ScanResult("PORT", true, getHostAlive(report), 0, new LinkedList<TestResult>());
        }

        List<TestResult> resultList = new LinkedList<>();
        resultList.add(getIrcIsOpen(report));
        resultList.add(getMsSqlIsOpen(report));
        resultList.add(getMySqlIsOpen(report));
        resultList.add(getRdpIsOpen(report));
        resultList.add(getTelnetIsOpen(report));
        resultList.add(getVncIsOpen(report));
        resultList.add(getSmbIsOpen(report));

        int max = 100;
        boolean hasError = false;
        boolean hasCritical = false;
        boolean hasWarning = false;
        int count = 0;
        int score = 0;
        for (TestResult result : resultList) {
            if (result.getScoreType().equals("hidden")) {
                continue;
            }
            if (result.getScore() < max
                    && (result.getScoreType().equals("fatal") || result.getScoreType().equals("critical"))) {
                max = result.getScore();
                hasCritical = true;
            }

            hasError |= result.isHasError();
            if (!hasError) {
                score += result.getScore();
                count++;
            }
        }

        if (count != 0) {
            score = score / count;
        } else {
            score = 0;
        }
        if (score > max && (hasCritical || hasWarning)) {
            score = (int) (score * (((double) max) / 100));
        }
        // Rewrite critical fatal
        for (TestResult result : resultList) {
            if (result.getScoreType().equals("critical")) {
                result.setScoreType("warning");
            } else if (result.getScoreType().equals("fatal")) {
                result.setScoreType("critical");
            }
        }
        ScanResult result = new ScanResult("PORT", false, null, score, resultList);
        return result;
    }

    private TranslateableMessage getHostAlive(PortScannerReport report) {
        return new TranslateableMessage("HOST_ALIVE", new HostAliveInfo(report.getHost()));
    }

    private TestResult getTelnetIsOpen(PortScannerReport report) {
        return new TestResult("TELNET", report.getTelnetIsOpen() == null, null,
                report.getTelnetIsOpen() == Boolean.TRUE ? 0 : 100,
                !(report.getTelnetIsOpen() == Boolean.TRUE) ? "success" : "warning", null);
    }

    private TestResult getRdpIsOpen(PortScannerReport report) {
        return new TestResult("RDP", report.getRdpIsOpen() == null, null, report.getRdpIsOpen() == Boolean.TRUE ? 0
                : 100, !(report.getRdpIsOpen() == Boolean.TRUE) ? "success" : "warning", null);
    }

    private TestResult getMySqlIsOpen(PortScannerReport report) {
        return new TestResult("MYSQL", report.getMySqlIsOpen() == null, null,
                report.getMySqlIsOpen() == Boolean.TRUE ? 0 : 100,
                !(report.getMySqlIsOpen() == Boolean.TRUE) ? "success" : "warning", null);
    }

    private TestResult getMsSqlIsOpen(PortScannerReport report) {
        return new TestResult("MSSQL", report.getMsSqlIsOpen() == null, null,
                report.getMsSqlIsOpen() == Boolean.TRUE ? 0 : 100,
                !(report.getMsSqlIsOpen() == Boolean.TRUE) ? "success" : "warning", null);
    }

    private TestResult getIrcIsOpen(PortScannerReport report) {
        return new TestResult("IRC", report.getIrcIsOpen() == null, null, report.getIrcIsOpen() == Boolean.TRUE ? 0
                : 100, !(report.getIrcIsOpen() == Boolean.TRUE) ? "success" : "warning", null);
    }

    private TestResult getVncIsOpen(PortScannerReport report) {
        return new TestResult("VNC", report.getVncIsOpen() == null, null, report.getVncIsOpen() == Boolean.TRUE ? 0
                : 100, !(report.getVncIsOpen() == Boolean.TRUE) ? "success" : "warning", null);
    }

    private TestResult getSmbIsOpen(PortScannerReport report) {
        return new TestResult("SMB", report.getSmbIsOpen() == null, null, report.getSmbIsOpen() == Boolean.TRUE ? 0
                : 100, !(report.getSmbIsOpen() == Boolean.TRUE) ? "success" : "warning", null);
    }
}
