/**
 *  WS-Port-Scanner - A Webservice for NMAP in the SIWECOS-Project
 *
 *  Copyright 2019-2019 Ruhr University Bochum / Hackmanit GmbH
 *
 *  Licensed under Apache License 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package de.rub.nds.siwecos.port.config;

/**
 *
 * @author robert
 */
public class WsConfig {

    private int parallelScanJobs;
    private boolean debugMode;

    public WsConfig() {
        this.parallelScanJobs = parallelScanJobs;
        this.debugMode = debugMode;
    }

    public int getParallelScanJobs() {
        return parallelScanJobs;
    }

    public void setParallelScanJobs(int parallelScanJobs) {
        this.parallelScanJobs = parallelScanJobs;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

}
