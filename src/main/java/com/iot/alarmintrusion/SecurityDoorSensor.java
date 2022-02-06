package com.iot.alarmintrusion;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import java.util.Timer;
import java.util.TimerTask;

public class SecurityDoorSensor extends ConcurrentCoapResource {

    public static SecurityDoorState securityDoorState;
    private String myDoorPin = "1234";

    public SecurityDoorSensor(String name) {
        super(name);

        securityDoorState = new SecurityDoorState();
        securityDoorState.setSecurityNeed(false);
        securityDoorState.setSecurityBreak(false);

        setObservable(true);
        setObserveType(CoAP.Type.CON);
        getAttributes().setObservable();
        Timer timer = new Timer();
        timer.schedule(new ContiniousTask(), 0, 3000);
    }

    private class ContiniousTask extends TimerTask {
        @Override
        public void run() {
            changed();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(securityDoorState);
            exchange.respond(json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        exchange.accept();

        if (exchange.getRequestOptions().isContentFormat(MediaTypeRegistry.APPLICATION_JSON)) {
            String jsonRequest = exchange.getRequestText();
            try {
                ObjectMapper mapper = new ObjectMapper();
                SecurityDoorRequest request = mapper.readValue(jsonRequest, SecurityDoorRequest.class);

                securityDoorState.setDoorOpen(false);

                if(request.getDoorPin() == null || request.getDoorPin().isEmpty() == true) {
                    securityDoorState.setSecurityNeed(request.isSecurityNeed());

                    if(request.isSecurityNeed() == false) {
                        securityDoorState.setDoorOpen(false);
                    }
                }

                if (securityDoorState.isSecurityNeed() == true && (request.getDoorPin() != null && request.getDoorPin().isEmpty() == false)) {
                    if (request.getDoorPin().equals(myDoorPin) == true) {

                        securityDoorState.setSecurityBreak(false);
                        securityDoorState.setSecurityBreakCounter(0);
                        securityDoorState.setDoorOpen(true);

                    } else if (request.getDoorPin().equals(myDoorPin) == false) {

                        securityDoorState.setSecurityBreakCounter(securityDoorState.getSecurityBreakCounter() + 1);

                        if(securityDoorState.getSecurityBreakCounter() >= 3) {
                            securityDoorState.setSecurityBreak(true);
                        }

                    }
                }

                changed();

                String json = mapper.writeValueAsString(securityDoorState);
                exchange.respond(CoAP.ResponseCode.CREATED, json);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            exchange.respond(CoAP.ResponseCode.CREATED);
        }
    }
}
