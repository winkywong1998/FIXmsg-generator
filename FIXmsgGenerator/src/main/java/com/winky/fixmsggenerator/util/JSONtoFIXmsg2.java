package com.winky.fixmsggenerator.util;

import org.json.JSONArray;
import org.json.JSONObject;
import quickfix.DataDictionary;
import quickfix.Group;

import quickfix.field.MsgType;
import quickfix.fix44.Message;

import java.io.InputStream;
import java.util.*;

public class JSONtoFIXmsg2 {

    private static DataDictionary dataDictionary;
    private static List<Integer> multipleValTag = new LinkedList();

    static {
        try (InputStream inputStream = JSONtoFIXmsg2.class.getResourceAsStream("/FIX44.xml")) {
            dataDictionary = new DataDictionary(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateFIXMessage(JSONObject input) {
        Message message = new Message();
        // Set message type. '8' is actually the MsgType for ExecutionReport in FIX 4.4, update as necessary
        message.getHeader().setString(MsgType.FIELD, "8");
        for (String key : input.keySet()) {
            int field = getFieldNumber(key);
            if (field > 0) {
                Object value = input.get(key);
                if (value instanceof String) {
                    message.setString(field, (String) value);
                } else if (value instanceof Integer) {
                    message.setInt(field, (Integer) value);
                } else if (value instanceof Double) {
                    message.setDouble(field, (Double) value);
                }
            }
        }
        // Handling NoSecurityAltID group
        if (input.has("NoSecurityAltID")) {
            handleSecurityAltIDGroup(input, message);
        }

        // Handling NoMiscFees group
        if (input.has("NoMiscFees")) {
            handleMiscFeesGroup(input, message);
        }

        String fixMessage = message.toString().replaceAll("\u0001", "|");
        return fixMessage;
    }

    private static void handleSecurityAltIDGroup(JSONObject input, Message message) {
        JSONArray altIds = input.getJSONArray("SecurityAltID");
        JSONArray altIdSources = input.getJSONArray("SecurityAltIDSource");
        Group securityAltIdGroup = new Group(454, 455);

        for (int i = 0; i < altIds.length(); i++) {
            securityAltIdGroup.setString(455, altIds.getString(i));
            securityAltIdGroup.setString(456, altIdSources.getString(i));
            message.addGroup(securityAltIdGroup);
        }
    }

    private static void handleMiscFeesGroup(JSONObject input, Message message) {
        JSONArray feeAmts = input.getJSONArray("MiscFeeAmt");
        JSONArray feeCurrs = input.getJSONArray("MiscFeeCurr");
        JSONArray feeTypes = input.getJSONArray("MiscFeeType");
        JSONArray feeBases = input.getJSONArray("MiscFeeBasis");
        Group miscFeesGroup = new Group(136, 137);

        for (int i = 0; i < feeAmts.length(); i++) {
            miscFeesGroup.setDouble(137, feeAmts.getDouble(i));
            miscFeesGroup.setString(138, feeCurrs.getString(i));
            miscFeesGroup.setString(139, feeTypes.getString(i));
            miscFeesGroup.setString(891, feeBases.getString(i));
            message.addGroup(miscFeesGroup);
        }
    }

    private static int getFieldNumber(String fieldName) {
        return dataDictionary.getFieldTag(fieldName);
    }
}
