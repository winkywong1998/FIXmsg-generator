package com.winky.fixmsggenerator.util;

import org.json.JSONArray;
import org.json.JSONObject;
import quickfix.DataDictionary;
import quickfix.Group;
import quickfix.StringField;
import quickfix.field.MsgType;
import quickfix.fix44.Message;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    int fieldTag = getFieldNumber(key);
                    multipleValTag.add(fieldTag);
                    JSONArray jsonArray = (JSONArray) value;
                    Group group = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Object fieldValue = jsonArray.get(i);
                        if (group == null) {
                            int delimiterField = getFieldNumber(key) - 1;
                            group = new Group(field, delimiterField);
                        }
                        if (fieldValue instanceof String) {
                            group.setString(fieldTag, (String) fieldValue);
                        } else if (fieldValue instanceof Integer) {
                            group.setInt(fieldTag, (Integer) fieldValue);
                        } else if (fieldValue instanceof Double) {
                            group.setDouble(fieldTag, (Double) fieldValue);
                        }
                        message.addGroup(group);
                    }

                }
            }
        }
        String fixMessage = message.toString();
        // Replace SOH with |
        fixMessage = fixMessage.replaceAll("\u0001", "|");
        if(!multipleValTag.isEmpty()) fixMessage = removeFirstOccurrences(fixMessage, multipleValTag);
        return fixMessage;
    }

    private static int getFieldNumber(String fieldName) {
        return dataDictionary.getFieldTag(fieldName);
    }

    public static String removeFirstOccurrences(String fixMessage, List<Integer> fieldsToRemove) {
        String[] parts = fixMessage.split("\\|");
        StringBuilder result = new StringBuilder();
        for (int field : fieldsToRemove) {
            boolean removed = false;
            for (int i = 0; i < parts.length; i++) {
                if (!removed && parts[i].startsWith(field + "=")) {
                    // If the field matches and hasn't been removed yet, skip this part
                    removed = true;
                    continue;
                }
                // Add the part back to the result, including the delimiter if it's not the first part
                if (result.length() > 0) {
                    result.append("|");
                }
                result.append(parts[i]);
            }
            // Replace the original parts with the newly constructed parts for subsequent removals
            parts = result.toString().split("\\|");
            // Reset the StringBuilder for the next iteration
            result = new StringBuilder();
        }

        // Construct the final result from the remaining parts
        for (String part : parts) {
            if (result.length() > 0) {
                result.append("|");
            }
            result.append(part);
        }
        return result.toString();
    }

}
