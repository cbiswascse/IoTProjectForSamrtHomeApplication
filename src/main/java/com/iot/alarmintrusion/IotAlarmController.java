package com.iot.alarmintrusion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("iot/")
public class IotAlarmController {

    @GetMapping("home")
    public String home() {
        return "alarm";
    }

    @GetMapping("get-data")
    @ResponseBody
    public ResponseData getData() {
        return AlarmintrusionApplication.responseData;
    }

}
