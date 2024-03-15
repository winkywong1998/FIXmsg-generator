package com.winky.fixmsggenerator.controller;
import com.winky.fixmsggenerator.util.JSONtoFIXmsg;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenerationController {
    @PostMapping("/generateFIXMessage")
    public String generateFIXMessage(@RequestBody String requestBody) {
        try {
            JSONObject inputJson = new JSONObject(requestBody);
            String fixMessage = JSONtoFIXmsg.generateFIXMessage(inputJson);
            return fixMessage;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
