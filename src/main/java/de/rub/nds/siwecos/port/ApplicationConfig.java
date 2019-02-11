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

import de.rub.nds.siwecos.port.ws.PoolManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("/")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();

        addRestResourceClasses(resources);
        return resources;
    }

    static {
        Properties p = new Properties(System.getProperties());
        if (new File("config.txt").exists()) {
            InputStream input = null;
            try {
                input = new FileInputStream("config.txt");
                // load a properties file

                p.load(input);

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (p.containsKey("portscanner.parallelScanJobs")) {
            PoolManager.getInstance().setPoolSize(Integer.parseInt(p.getProperty("portscanner.parallelScanJobs")));
        }
        if (p.containsKey("portscanner.debugMode")) {
            DebugManager.getInstance().setDebugEnabled(Boolean.parseBoolean(p.getProperty("portscanner.debugMode")));
        }
        System.out.println("################### WS-Port ###################");
        System.out
                .println("Properties are defined in a file called config.txt in the tomcat bin folder, or can be set as enviroment Variables");
        System.out.println("portscanner.parallelScanJobs="
                + PoolManager.getInstance().getService().getMaximumPoolSize());
        System.out.println("portscanner.debugMode=" + DebugManager.getInstance().isDebugEnabled());

    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(de.rub.nds.siwecos.port.ws.ScannerWS.class);
    }

}
