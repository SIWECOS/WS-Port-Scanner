/**
 *  WS-Port-Scanner - A Webservice for NMAP in the SIWECOS-Project
 *
 *  Copyright 2019-2019 Ruhr University Bochum / Hackmanit GmbH
 *
 *  Licensed under Apache License 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package de.rub.nds.siwecos.port.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import de.rub.nds.siwecos.port.ws.DebugOutput;
import java.util.List;

/**
 *
 * @author Robert Merget - robert.merget@rub.de
 */
public class ScanResult {

    private String name;

    private String version;
    private boolean hasError;

    private TranslateableMessage errorMessage;

    private int score;

    private List<TestResult> tests;

    @JsonInclude(Include.NON_EMPTY)
    private DebugOutput debugOutput;

    public ScanResult(String name, boolean hasError, TranslateableMessage errorMessage, int score,
            List<TestResult> tests) {
        this.name = name;
        this.version = "1.0";
        this.hasError = hasError;
        this.errorMessage = errorMessage;
        this.score = score;
        this.tests = tests;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DebugOutput getDebugOutput() {
        return debugOutput;
    }

    public void setDebugOutput(DebugOutput debugOutput) {
        this.debugOutput = debugOutput;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public TranslateableMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(TranslateableMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<TestResult> getTests() {
        return tests;
    }

    public void setTests(List<TestResult> tests) {
        this.tests = tests;
    }
}
