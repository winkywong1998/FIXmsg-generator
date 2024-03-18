package com.winky.fixmsggenerator.util;

import org.json.JSONArray;
import org.json.JSONObject;
import quickfix.DataDictionary;
import quickfix.Group;

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
        // Set message type. '8' is actually the MsgType for ExecutionReport in FIX 4.4, update as necessary
        message.getHeader().setString(MsgType.FIELD, "8");
        for (String key : input.keySet()) {
            int field = getFieldNumber(key);
            Object value = input.get(key);
            if(field > 0){
                if (value instanceof String) {
                    message.setString(field, (String) value);
                } else if (value instanceof Integer) {
                    message.setInt(field, (Integer) value);
                } else if (value instanceof Double) {
                    message.setDouble(field, (Double) value);
                }
            }else{
                // Deal with the group:
                // Note that There will be two groups
                // First is key 454(NoSecurityAltID) having 455(SecurityAltID) as the delim Order should be [455(SecurityAltID), 456(SecuritAltIDSource)]
                // Second is key 136(NoMiscFees) having 137(MiscFeeAmt) as the delim Order should be [137(MiscFeeAmt) , 138(MiscFeeCurr), 139(MiscFeeType), 891(MiscFeeBasis)]
                if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    if (!jsonArray.isEmpty()) {
                        if (key.equals("SecurityAlt")) {
                            handleSecurityAltIDGroup(jsonArray, message);
                        } else if (key.equals("MiscFee")) {
                            handleMiscFeesGroup(jsonArray, message);
                        }
                    }
                }
                // Might want to throw error here because when value is not an JSONArray and field is <=0 then invalid
            }
            // Note that the trailer  will be generated automatically
        }
        String fixMessage = message.toString().replaceAll("\u0001", "|");
        return fixMessage;
    }

        private static void handleSecurityAltIDGroup (JSONArray input, Message message){
            Group securityAltIdGroup = new Group(454, 455);
            for (int i = 0; i < input.length(); i++) {
                JSONObject altIdObj = input.getJSONObject(i);
                securityAltIdGroup.setString(455, altIdObj.getString("SecurityAltID"));
                securityAltIdGroup.setString(456, altIdObj.getString("SecurityAltIDSource"));
                message.addGroup(securityAltIdGroup);
            }
        }

        private static void handleMiscFeesGroup (JSONArray input, Message message){
            Group miscFeesGroup = new Group(136, 137);
            for (int i = 0; i < input.length(); i++) {
                JSONObject feeObj = input.getJSONObject(i);
                miscFeesGroup.setDouble(137, feeObj.getDouble("MiscFeeAmt"));
                miscFeesGroup.setString(138, feeObj.getString("MiscFeeCurr"));
                miscFeesGroup.setString(139, feeObj.getString("MiscFeeType"));
                miscFeesGroup.setString(891, feeObj.getString("MiscFeeBasis"));
                message.addGroup(miscFeesGroup);
            }
        }

        private static int getFieldNumber (String fieldName){
            return dataDictionary.getFieldTag(fieldName);
        }
    }
