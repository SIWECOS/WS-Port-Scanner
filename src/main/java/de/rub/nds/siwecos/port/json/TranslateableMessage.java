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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Robert Merget - robert.merget@rub.de
 */
public class TranslateableMessage {

    private String placeholder;

    private List<TestInfo> values;

    public TranslateableMessage(String placeholder, List<TestInfo> values) {
        this.placeholder = placeholder;
        this.values = values;
    }

    public TranslateableMessage(String placeholder, TestInfo value) {
        this.placeholder = placeholder;
        this.values = new LinkedList<>();
        values.add(value);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public List<TestInfo> getValues() {
        return values;
    }

    public void setValues(List<TestInfo> values) {
        this.values = values;
    }
}
