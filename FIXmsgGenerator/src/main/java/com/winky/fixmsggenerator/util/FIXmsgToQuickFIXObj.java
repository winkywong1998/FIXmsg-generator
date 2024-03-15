package com.winky.fixmsggenerator.util;
import quickfix.*;

import java.util.Iterator;

public class FIXmsgToQuickFIXObj {
    public static Message convertFIXMessage(String FIXmsg) {
        Message message = null;
        try {
            // Replace '|' with SOH character (ASCII 1)
            String fixMessageWithSOH = FIXmsg.replace('|', '\001');
            message = new Message(fixMessageWithSOH);
            System.out.println("Parsed FIX Message: " + message);
        } catch (InvalidMessage e) {
            e.printStackTrace();
        }
        return message;
    }
    public static Message convertFIXMessage2(String FIXmsg) {
        Message quickFixMessage = null;
        try {
            String[] fields = FIXmsg.split("\\|");
            quickFixMessage = new Message();
            for (String field : fields) {
                String[] tagValue = field.split("=");
                int tag = Integer.parseInt(tagValue[0]);
                String value = tagValue[1];
                quickFixMessage.setField(new StringField(tag, value));
            }
            return quickFixMessage;
        } catch (Exception e) {
            System.out.println("Failed");
        }
        return quickFixMessage;
    }

    public static String printAllFields(Message message) {
        StringBuilder sb = new StringBuilder();
        FieldMap fieldMap = message.getHeader();
        sb.append(printFields(fieldMap));

        fieldMap = message;
        sb.append(printFields(fieldMap));

        fieldMap = message.getTrailer();
        sb.append(printFields(fieldMap));

        return sb.toString();
    }

    private static String printFields(FieldMap fieldMap) {
        StringBuilder sb = new StringBuilder();
        Iterator<Field<?>> iterator = fieldMap.iterator();
        while (iterator.hasNext()) {
            Field<?> field = iterator.next();
            sb.append("Tag: " + field.getTag() + ", Value: " + field.getObject() + "\n");
            System.out.println("Tag: " + field.getTag() + ", Value: " + field.getObject());
        }
        return sb.toString();
    }
}
