package com.iot.alarmintrusion;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iot/")
public class GatewayController {

    @PostMapping("/check-security")
    public SecurityDoorState setSecurity(@RequestBody SecurityDoorRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String requestJsonString = mapper.writeValueAsString(request);

            CoapClient coapClient = new CoapClient("coap://localhost:5683/door");
            CoapResponse coapResponse = coapClient.post(requestJsonString, MediaTypeRegistry.APPLICATION_JSON);
            String responseJsonString = coapResponse.getResponseText();

            SecurityDoorState securityDoorState = mapper.readValue(responseJsonString, SecurityDoorState.class);
            return securityDoorState;
        } catch (Exception ex) {

        }
        return null;
    }

    @PostMapping("/change-motion")
    public MotionSensorState setMotion(@RequestBody MotionSensorRequest request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String requestJsonString = mapper.writeValueAsString(request);

            CoapClient coapClient = new CoapClient("coap://localhost:5685/motion");
            CoapResponse coapResponse = coapClient.post(requestJsonString, MediaTypeRegistry.APPLICATION_JSON);
            String responseJsonString = coapResponse.getResponseText();

            MotionSensorState motionSensorState = mapper.readValue(responseJsonString, MotionSensorState.class);
            return motionSensorState;
        } catch (Exception ex) {

        }
        return null;
    }
}
