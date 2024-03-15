package com.winky.fixmsggenerator.controller;
import com.winky.fixmsggenerator.util.FIXmsgToExecutionReport;
import com.winky.fixmsggenerator.util.FIXmsgToQuickFIXObj;
import com.winky.fixmsggenerator.util.JSONtoFIXmsg;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import quickfix.Message;
import quickfix.fix40.ExecutionReport;

import static com.winky.fixmsggenerator.util.FIXmsgToQuickFIXObj.printAllFields;

@RestController
public class GenerationController {
    @PostMapping("/generate")
    public String generateFIXMessage(@RequestBody String requestBody) {
        try {
            JSONObject inputJson = new JSONObject(requestBody);
            String fixMessage = JSONtoFIXmsg.generateFIXMessage(inputJson);
            return fixMessage;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/convert")
    public String convertFIXMessage(@RequestBody String requestBody) {
        try {
            ExecutionReport executionReport = FIXmsgToExecutionReport.parse(requestBody);
            return executionReport.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
