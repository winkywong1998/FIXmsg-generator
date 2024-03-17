package com.winky.fixmsggenerator.util;

import org.json.JSONArray;
import org.json.JSONObject;
import quickfix.DataDictionary;
import quickfix.Group;
import quickfix.StringField;
import quickfix.field.MsgType;
import quickfix.fix44.Message;

import java.io.InputStream;

public class JSONtoFIXmsg2 {

    private static DataDictionary dataDictionary;

    static {
        try (InputStream inputStream = JSONtoFIXmsg2.class.getResourceAsStream("/FIX44.xml")) {
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
                } else if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    Group group = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int fieldNum = getFieldNumber(key);
                        Object fieldValue = jsonArray.get(i);
                        if (group == null) {
                            int delimiterField = getFieldNumber(key) - 1;
                            group = new Group(field, delimiterField);
                        }
                        if (fieldValue instanceof String) {
                            group.setString(fieldNum, (String) fieldValue);
                        } else if (fieldValue instanceof Integer) {
                            group.setInt(fieldNum, (Integer) fieldValue);
                        } else if (fieldValue instanceof Double) {
                            group.setDouble(fieldNum, (Double) fieldValue);
                        }
                        if (group != null) {
                            message.addGroup(group);
                        }
                    }

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
