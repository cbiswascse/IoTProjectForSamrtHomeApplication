package com.iot.alarmintrusion;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import java.util.Timer;
import java.util.TimerTask;

public class MotionSensor extends ConcurrentCoapResource {

    public static MotionSensorState motionSensorState;

    public MotionSensor(String name) {
        super(name);

        motionSensorState = new MotionSensorState();
        motionSensorState.setExist(false);
        motionSensorState.setOn(false);

        setObservable(true);
        setObserveType(CoAP.Type.CON);
        getAttributes().setObservable();
        Timer timer = new Timer();
        timer.schedule(new ContiniousTask(), 0, 10000);
    }

    private class ContiniousTask extends TimerTask {
        @Override
        public void run() {
            if(motionSensorState.isOn()) {
                motionSensorState.setExist(!motionSensorState.isExist());
                changed();
            }
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(motionSensorState);
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
                MotionSensorRequest request = mapper.readValue(jsonRequest, MotionSensorRequest.class);
                motionSensorState.setOn(request.isOn());

                String json = mapper.writeValueAsString(motionSensorState);
                exchange.respond(json);
                exchange.respond(CoAP.ResponseCode.CREATED, json);
                changed();
            } catch (Exception ex) {

            }
        } else {
            exchange.respond(CoAP.ResponseCode.CREATED);
        }
    }
}
