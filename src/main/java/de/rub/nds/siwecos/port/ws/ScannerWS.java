/**
 *  WS-Port-Scanner - A Webservice for NMAP in the SIWECOS-Project
 *
 *  Copyright 2019-2019 Ruhr University Bochum / Hackmanit GmbH
 *
 *  Licensed under Apache License 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package de.rub.nds.siwecos.port.ws;

import de.rub.nds.siwecos.port.DebugManager;
import de.rub.nds.siwecos.port.PortScannerCallback;
import java.net.URISyntaxException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Robert Merget - robert.merget@rub.de
 */
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
public class ScannerWS {

    protected static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(ScannerWS.class.getName());

    @Context
    private UriInfo context;

    public ScannerWS() {
    }

    @POST
    @Path("/start")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startScan(ScanRequest request) throws URISyntaxException {
        LOGGER.info("Received a request to scan: " + request.getUrl());
        if (request.getCallbackurls() == null || request.getCallbackurls().length == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No callback urls provided").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        PoolManager
                .getInstance()
                .getService()
                .submit(new PortScannerCallback(request, new DebugOutput(PoolManager.getInstance().getService()
                        .getQueue().size(), System.currentTimeMillis())));
        return Response.status(Response.Status.OK).entity("Success").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @POST
    @Path("/poolconfig")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePoolSize(PoolsizeChangeRequest poolsizeChangeRequest) throws URISyntaxException {
        LOGGER.info("Changed Poolsize to: " + poolsizeChangeRequest.getSize());
        PoolManager.getInstance().setPoolSize(poolsizeChangeRequest.getSize());

        return Response.status(Response.Status.OK).entity("Poolsize Changed to " + poolsizeChangeRequest.getSize())
                .type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @POST
    @Path("/taskpool")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTaskPoolSize() throws URISyntaxException {
        long poolsize = PoolManager.getInstance().getService().getQueue().size();
        LOGGER.info("Returning requested task pool size: " + poolsize);
        return Response.status(Response.Status.OK).entity("Current Tasks in queue: " + poolsize)
                .type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @POST
    @Path("/toggleDebug")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getToggleDebug() throws URISyntaxException {
        DebugManager.getInstance().setDebugEnabled(!DebugManager.getInstance().isDebugEnabled());
        if (DebugManager.getInstance().isDebugEnabled()) {
            LOGGER.info("Switched DebugMode on");
            return Response.status(Response.Status.OK).entity("Switched DebugMode on").type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        } else {
            LOGGER.info("Switched DebugMode off");
            return Response.status(Response.Status.OK).entity("Switched DebugMode off").type(MediaType.TEXT_PLAIN_TYPE)
                    .build();
        }
    }
}
