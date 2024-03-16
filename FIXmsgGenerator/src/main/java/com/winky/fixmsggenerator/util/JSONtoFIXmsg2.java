package com.winky.fixmsggenerator.util;

import org.json.JSONObject;
import quickfix.DataDictionary;
import quickfix.field.MsgType;
import quickfix.fix44.Message;

import java.io.InputStream;

public class JSONtoFIXmsg2 {

    private static DataDictionary dataDictionary;

    static {
        try (InputStream inputStream = JSONtoFIXmsg.class.getResourceAsStream("/FIX44.xml")) {
            dataDictionary = new DataDictionary(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateFIXMessage(JSONObject input) {
        Message message = new Message();
        message.getHeader().setString(MsgType.FIELD, "8"); // Message Type
        for (String key : input.keySet()) {
            int field = getFieldNumber(key);
            if (field > 0) {
                Object value = input.get(key);
                if (value instanceof String) {
                    message.setString(field, (String) value);
                } else if (value instanceof Integer) {
                    message.setInt(field, (int) value);
                } else if (value instanceof Double) {
                    message.setDouble(field, (double) value);
                }
            }
        }
        String fixMessage = message.toString();
        // Replace SOH with |
        fixMessage = fixMessage.replaceAll("\u0001", "|");
        return fixMessage;
    }

    private static int getFieldNumber(String fieldName) {
        return dataDictionary.getFieldTag(fieldName);
    }
}
