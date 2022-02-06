package com.iot.alarmintrusion;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import java.util.Timer;
import java.util.TimerTask;

public class DangerAlarmSensor extends ConcurrentCoapResource {

    public static DangerAlarmState dangerAlarmState;

    public DangerAlarmSensor(String name) {
        super(name);

        dangerAlarmState = new DangerAlarmState();

        setObservable(true);
        setObserveType(CoAP.Type.CON);
        getAttributes().setObservable();
        Timer timer = new Timer();
        timer.schedule(new ContiniousTask(), 0, 1000);
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
            String json = mapper.writeValueAsString(dangerAlarmState);
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
                DangerAlarmRequest request = mapper.readValue(jsonRequest, DangerAlarmRequest.class);
                dangerAlarmState.setAlarm(request.isAlarm());

                String json = mapper.writeValueAsString(dangerAlarmState);
                exchange.respond(json);
                exchange.respond(CoAP.ResponseCode.CREATED, json);
            } catch (Exception ex) {

            }
        } else {
            exchange.respond(CoAP.ResponseCode.CREATED);
        }
    }
}
