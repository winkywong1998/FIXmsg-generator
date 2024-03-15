package com.winky.fixmsggenerator.util;

import quickfix.field.*;
import quickfix.fix40.ExecutionReport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FIXmsgToExecutionReport {
    public static ExecutionReport parse(String messageString) {
        ExecutionReport executionReport = new ExecutionReport();
        // break message into field, format: number tag=value
        String[] fields = messageString.split("\\|");
        String[] firstField = fields[0].split("=");
        String[] lastField = fields[fields.length-1].split("=");
        if (firstField.length != 2 || Integer.parseInt(firstField[0]) != 8 || !firstField[1].equals("FIX.4.4")) {
            //first field is not version number or version number is not correct.
            //throw an exception
            System.out.println(firstField.length);
            System.out.println(Integer.parseInt(firstField[0]));
            System.out.println(firstField[1]);
            System.out.println(firstField[1].equals("FIX.4.4"));
            return null;
        }
        if (lastField.length != 2 || Integer.parseInt(lastField[0]) != 10 ) {
            //last field is not checkSum
            //throw an exception
            return null;
        }

        for (String field : fields) {
            String[] keyValue = field.split("=");
            if (keyValue.length == 2) {
                int tag = Integer.parseInt(keyValue[0]);
                String value = keyValue[1];
                switch (tag) {
                    case 8:
                        executionReport.getHeader().setString(tag, value);
                        break;
                    case 37:
                        executionReport.set(new OrderID(value));
                        break;
                    case 17:
                        executionReport.set(new ExecID(value));
                        break;
                        //check this
                    case 150:
                        executionReport.setField(new ExecType(value.charAt(0)));
                        break;
                    case 19:
                        executionReport.set(new ExecRefID(value));
                        break;
                    case 11:
                        executionReport.set(new ClOrdID(value));
                        break;
                    case 39:
                        executionReport.set(new OrdStatus(value.charAt(0)));
                        break;
                    case 460:
                        executionReport.setField(new Product(Integer.parseInt(value)));
                        break;
                    case 461:
                        executionReport.setField(new CFICode(value));
                        break;
                    case 48:
                        executionReport.set(new SecurityID(value));
                        break;
                    case 22:
                        executionReport.setField(new SecurityIDSource(value));
                        break;
                    case 207:
                        executionReport.setField(new SecurityExchange(value));
                        break;
                    case 454:
                        executionReport.setField(new NoSecurityAltID(Integer.parseInt(value)));
                        break;
                    case 455:
                        executionReport.setField(new SecurityAltID(value));
                        break;
                    case 456:
                        executionReport.setField(new SecurityAltIDSource(value));
                        break;
                    case 54:
                        executionReport.setField(new Side(value.charAt(0)));
                        break;
                    case 63:
                        executionReport.setField(new SettlType(value));
                        break;
                    case 64:
                        executionReport.setField(new SettlDate(value));
                        break;
                    case 38:
                        executionReport.setField(new OrderQty(Double.parseDouble(value)));
                        break;
                    case 14:
                        executionReport.setField(new CumQty(Double.parseDouble(value)));
                        break;
                    case 151:
                        executionReport.setField(new LeavesQty(Double.parseDouble(value)));
                        break;
                    case 6:
                        executionReport.setField(new AvgPx(Double.parseDouble(value)));
                        break;
                    case 31:
                        executionReport.setField(new LastPx(Double.parseDouble(value)));
                        break;
                    case 60:
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");
                        LocalDateTime localDateTime = LocalDateTime.parse(value, formatter);
                        executionReport.set(new TransactTime(localDateTime));
                        break;
                    case 75:
                        executionReport.setField(new TradeDate(value));
                        break;
                    case 32:
                        executionReport.setField(new LastQty(Double.parseDouble(value)));
                        break;
                    case 136:
                        executionReport.setField(new NoMiscFees(Integer.parseInt(value)));
                        break;
                    case 137:
                        executionReport.setField(new MiscFeeAmt(Double.parseDouble(value)));
                        break;
                    case 138:
                        executionReport.setField(new MiscFeeCurr(value));
                        break;
                    case 139:
                        executionReport.setField(new MiscFeeType(value));
                        break;
                    case 891:
                        executionReport.setField(new MiscFeeBasis(Integer.parseInt(value)));
                        break;
                    case 15:
                        executionReport.setField(new Currency(value));
                        break;
                    case 58:
                        executionReport.setField(new Text(value));
                        break;
                    case 30:
                        executionReport.setField(new LastMkt(value));
                        break;
                    default:
                        executionReport.setString(tag, value);
                        break;
                }
            }
        }

        return executionReport;
    }

}
