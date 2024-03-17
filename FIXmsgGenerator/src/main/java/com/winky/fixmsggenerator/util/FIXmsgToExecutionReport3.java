package com.winky.fixmsggenerator.util;

import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FIXmsgToExecutionReport3 {
    public static ExecutionReport parseFIXMessage(String fixMessageString) throws Exception {
        String[] fields = fixMessageString.split("\\|");

        DataDictionary dataDictionary = new DataDictionary("FIX44.xml"); // Load your DataDictionary

        ExecutionReport executionReport = new ExecutionReport();
        executionReport.getHeader().setString(MsgType.FIELD, "8"); // MsgType is always 8 for ExecutionReport

        for (String field : fields) {
            String[] tagValuePair = field.split("=");
            int tag = Integer.parseInt(tagValuePair[0]);
            String value = tagValuePair[1];

            // Use the DataDictionary to get the field name
            String fieldName = dataDictionary.getFieldName(tag);
            if (fieldName != null) {
                if (executionReport.isSetField(tag)) {
                    // If the field is repeating, add it instead of setting

                } else {

                }
            }
        }

        return executionReport;
    }
}
