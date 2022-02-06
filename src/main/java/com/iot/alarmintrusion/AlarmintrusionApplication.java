package com.iot.alarmintrusion;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlarmintrusionApplication {

    @Autowired
    public static ResponseData responseData;

    public static void main(String[] args) {

        SpringApplication.run(AlarmintrusionApplication.class, args);
        responseData = new ResponseData();

        CoapServer securityDoorCoapServer = new CoapServer(5683);
        securityDoorCoapServer.add(new SecurityDoorSensor("door"));
        securityDoorCoapServer.start();

        CoapServer securityAlarmCoapServer = new CoapServer(5684);
        securityAlarmCoapServer.add(new DangerAlarmSensor("alarm"));
        securityAlarmCoapServer.start();

        CoapServer motionSensorCoapServer = new CoapServer(5685);
        motionSensorCoapServer.add(new MotionSensor("motion"));
        motionSensorCoapServer.start();

        CoapClient securityDoorClient = new CoapClient("coap://localhost:5683/door");
        CoapClient securityAlarmClient = new CoapClient("coap://localhost:5684/alarm");
        CoapClient motionSensorClient = new CoapClient("coap://localhost:5685/motion");

        CoapObserveRelation relation = securityDoorClient.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                String jsonResponse = coapResponse.getResponseText();
                System.out.println(jsonResponse);
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    SecurityDoorState securityDoorState = mapper.readValue(jsonResponse, SecurityDoorState.class);
                    responseData.setDoorState(securityDoorState);

                    if(responseData.getMotionSensorState().isOn() == false) {
                        new Thread() {
                            public void run() {
                                try {
                                    DangerAlarmRequest request = new DangerAlarmRequest();
                                    request.setAlarm(securityDoorState.isSecurityBreak());
                                    String requestJson = mapper.writeValueAsString(request);

                                    CoapResponse response = securityAlarmClient.post(requestJson, MediaTypeRegistry.APPLICATION_JSON);
                                    DangerAlarmState dangerAlarmState = mapper.readValue(requestJson, DangerAlarmState.class);

                                    dangerAlarmState.setAlarm(securityDoorState.isSecurityBreak());
                                    responseData.setAlarmState(dangerAlarmState);

                                } catch (Exception ex) {

                                }
                            }
                        }.start();
                    }

                    System.out.println(mapper.writeValueAsString(responseData));

                } catch (Exception ex) {

                }
            }

            @Override
            public void onError() {

            }
        });


        CoapObserveRelation motionSensorRelation = motionSensorClient.observe(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                String jsonResponse = coapResponse.getResponseText();
                System.out.println(jsonResponse);
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    MotionSensorState motionSensorState = mapper.readValue(jsonResponse, MotionSensorState.class);
                    responseData.setMotionSensorState(motionSensorState);

                    if(motionSensorState.isOn() == true && motionSensorState.isExist() == true) {
                        new Thread() {
                            public void run() {
                                try {
                                    DangerAlarmRequest request = new DangerAlarmRequest();
                                    request.setAlarm(true);
                                    String requestJson = mapper.writeValueAsString(request);

                                    CoapResponse response = securityAlarmClient.post(requestJson, MediaTypeRegistry.APPLICATION_JSON);
                                    DangerAlarmState dangerAlarmState = mapper.readValue(requestJson, DangerAlarmState.class);

                                    dangerAlarmState.setAlarm(true);
                                    responseData.setAlarmState(dangerAlarmState);

                                } catch (Exception ex) {

                                }
                            }
                        }.start();
                    }

                    if(motionSensorState.isOn() == true && motionSensorState.isExist() == false) {
                        new Thread() {
                            public void run() {
                                try {
                                    DangerAlarmRequest request = new DangerAlarmRequest();
                                    request.setAlarm(false);
                                    String requestJson = mapper.writeValueAsString(request);

                                    CoapResponse response = securityAlarmClient.post(requestJson, MediaTypeRegistry.APPLICATION_JSON);
                                    DangerAlarmState dangerAlarmState = mapper.readValue(requestJson, DangerAlarmState.class);

                                    dangerAlarmState.setAlarm(false);
                                    responseData.setAlarmState(dangerAlarmState);

                                } catch (Exception ex) {

                                }
                            }
                        }.start();
                    }

                    System.out.println(mapper.writeValueAsString(responseData));

                } catch (Exception ex) {

                }
            }

            @Override
            public void onError() {

            }
        });
    }

}
