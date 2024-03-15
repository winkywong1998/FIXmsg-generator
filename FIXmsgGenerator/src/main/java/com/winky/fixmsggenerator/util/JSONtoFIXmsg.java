package com.winky.fixmsggenerator.util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONtoFIXmsg {
    private static final Map<String, Integer> FIELD_NUM_MAPPING = new HashMap<>();
    static {
        FIELD_NUM_MAPPING.put("OrderID", 37);
        FIELD_NUM_MAPPING.put("ExecID", 17);
        FIELD_NUM_MAPPING.put("ExecType", 150);
        FIELD_NUM_MAPPING.put("OrdStatus", 39);
        FIELD_NUM_MAPPING.put("Instrument", 0); // Component block tag
        FIELD_NUM_MAPPING.put("Side", 54);
        FIELD_NUM_MAPPING.put("SettlType", 63);
        FIELD_NUM_MAPPING.put("SettlDate", 64);
        FIELD_NUM_MAPPING.put("OrderQty", 38);
        FIELD_NUM_MAPPING.put("CumQty", 14);
        FIELD_NUM_MAPPING.put("LeavesQty", 151);
        FIELD_NUM_MAPPING.put("AvgPx", 6);
        FIELD_NUM_MAPPING.put("LastPx", 31);
        FIELD_NUM_MAPPING.put("TransactTime", 60);
        FIELD_NUM_MAPPING.put("TradeDate", 75);
        FIELD_NUM_MAPPING.put("LastQty", 32);
        FIELD_NUM_MAPPING.put("NoMiscFees", 136);
        FIELD_NUM_MAPPING.put("MiscFeeAmt", 137);
        FIELD_NUM_MAPPING.put("MiscFeeCurr", 138);
        FIELD_NUM_MAPPING.put("MiscFeeType", 139);
        FIELD_NUM_MAPPING.put("MiscFeeBasis", 891);
        FIELD_NUM_MAPPING.put("Currency", 15);
        FIELD_NUM_MAPPING.put("Text", 58);
        FIELD_NUM_MAPPING.put("SecurityID", 48);
        FIELD_NUM_MAPPING.put("SecurityIDSource", 22);
        FIELD_NUM_MAPPING.put("SecurityExchange", 207);
        FIELD_NUM_MAPPING.put("NoSecurityAltID", 454);
        FIELD_NUM_MAPPING.put("SecurityAltID", 455);
        FIELD_NUM_MAPPING.put("SecuritAltIDSource", 456);
    }

    public static String generateFIXMessage(JSONObject input) {
        StringBuilder fixMessage = new StringBuilder();
        // Hard code this part since none of them specify in the PRD
        // Note that:
        fixMessage.append("8=FIX.4.4|9=264|35=8|");
        for (String key : input.keySet()) {
            if (FIELD_NUM_MAPPING.containsKey(key)) {
                // If the ExecID exist no matter what it is it will be replaced with ex2- + orderId
                if(key.equals("ExecID")){
                    appendField(fixMessage, String.valueOf(FIELD_NUM_MAPPING.get(key)), "ex2-" + input.get("OrderID"));
                }else{
                    appendField(fixMessage, String.valueOf(FIELD_NUM_MAPPING.get(key)), input.get(key));
                }
            }
        }
        // Generating Checksum
        fixMessage.append("10=").append(calculateChecksum(fixMessage.toString())).append("|");
        return fixMessage.toString();
    }

    private static void appendField(StringBuilder message, String fieldName, Object value) {
        if (value != null) {
            message.append(fieldName).append("=").append(value).append("|");
        }
    }

    private static int calculateChecksum(String message) {
        int sum = 0;
        for (char c : message.toCharArray()) {
            sum += c;
        }
        return sum % 256;
    }
}
