package com.winky.fixmsggenerator.util;

import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FIXmsgToExecutionReport2 {
    public static ExecutionReport parse(String messageString) {
        ExecutionReport executionReport = new ExecutionReport();
        // break message into field, format: number tag=value
        String[] fields = messageString.split("\\|");
        String[] firstField = fields[0].split("=");
        String[] lastField = fields[fields.length-1].split("=");
        if (firstField.length != 2 || Integer.parseInt(firstField[0]) != 8 || !firstField[1].equals("FIX.4.4")) {
            //first field is not version number or version number is not correct.
            //throw an exception

            return null;
        }
        if (lastField.length != 2 || Integer.parseInt(lastField[0]) != 10 ) {
            //last field is not checkSum
            //throw an exception
            return null;
        }

        try {
            DefaultMessageFactory messageFactory = new DefaultMessageFactory();
            DataDictionary dataDictionary = new DataDictionary("FIX44.xml");
            Message fixMessage = MessageUtils.parse(messageFactory,dataDictionary ,messageString.replace('|', '\u0001'));

            if(fixMessage.isSetField(37)){
                String OrderID = fixMessage.getString(37);
                executionReport.setField(new OrderID(OrderID));
            }

            if(fixMessage.isSetField(17)){
                String ExecID = fixMessage.getString(17);
                executionReport.setField(new ExecID(ExecID));
            }

            if(fixMessage.isSetField(150)){
                String ExecType = fixMessage.getString(150);
                executionReport.setField(new ExecType(ExecType.charAt(0)));
            }

            if(fixMessage.isSetField(19)){
                String ExecRefID = fixMessage.getString(19);
                executionReport.setField(new ExecRefID(ExecRefID));
            }

            if(fixMessage.isSetField(11)){
                String ClOrdID = fixMessage.getString(11);
                executionReport.setField(new ClOrdID(ClOrdID));
            }

            if(fixMessage.isSetField(39)){
                String OrdStatus = fixMessage.getString(39);
                executionReport.setField(new OrdStatus(OrdStatus.charAt(0)));
            }

            if(fixMessage.isSetField(460)){
                String Product = fixMessage.getString(460);
                executionReport.setField(new Product(Integer.parseInt(Product)));
            }

            if(fixMessage.isSetField(461)){
                String CFICode = fixMessage.getString(461);
                executionReport.setField(new CFICode(CFICode));
            }

            if(fixMessage.isSetField(48)){
                String SecurityID = fixMessage.getString(48);
                executionReport.setField(new SecurityID(SecurityID));
            }

            if(fixMessage.isSetField(22)){
                String SecurityIDSource = fixMessage.getString(22);
                executionReport.setField(new SecurityIDSource(SecurityIDSource));
            }

            if(fixMessage.isSetField(207)){
                String SecurityExchange = fixMessage.getString(207);
                executionReport.setField(new SecurityExchange(SecurityExchange));
            }

            if(fixMessage.isSetField(454)){
                String NoSecurityAltID = fixMessage.getString(454);
                executionReport.setField(new NoSecurityAltID(Integer.parseInt(NoSecurityAltID)));
            }

            if(fixMessage.isSetField(455)){
                String SecurityAltID = fixMessage.getString(455);
                executionReport.setField(new SecurityAltID(SecurityAltID));
            }

            if(fixMessage.isSetField(456)){
                String SecurityAltIDSource = fixMessage.getString(456);
                executionReport.setField(new SecurityAltIDSource(SecurityAltIDSource));
            }

            if(fixMessage.isSetField(54)){
                String Side = fixMessage.getString(54);
                executionReport.setField(new Side(Side.charAt(0)));
            }

            if(fixMessage.isSetField(63)){
                String SettlType = fixMessage.getString(63);
                executionReport.setField(new SettlType(SettlType));
            }

            if(fixMessage.isSetField(64)){
                String SettlDate = fixMessage.getString(64);
                executionReport.setField(new SettlType(SettlDate));
            }

            if(fixMessage.isSetField(38)){
                String OrderQty = fixMessage.getString(38);
                executionReport.setField(new OrderQty(Double.parseDouble(OrderQty)));
            }

            if(fixMessage.isSetField(14)){
                String CumQty = fixMessage.getString(14);
                executionReport.setField(new CumQty(Double.parseDouble(CumQty)));
            }

            if(fixMessage.isSetField(151)){
                String LeavesQty = fixMessage.getString(151);
                executionReport.setField(new LeavesQty(Double.parseDouble(LeavesQty)));
            }

            if(fixMessage.isSetField(6)){
                String AvgPx = fixMessage.getString(6);
                executionReport.setField(new AvgPx(Double.parseDouble(AvgPx)));
            }

            if(fixMessage.isSetField(31)){
                String LastPx = fixMessage.getString(31);
                executionReport.setField(new LastPx(Double.parseDouble(LastPx)));
            }

            if(fixMessage.isSetField(60)){
                String TransactTime = fixMessage.getString(60);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");
                LocalDateTime localDateTime = LocalDateTime.parse(TransactTime, formatter);
                executionReport.setField(new TransactTime(localDateTime));
            }


            if(fixMessage.isSetField(75)){
                String TradeDate = fixMessage.getString(75);
                executionReport.setField(new TradeDate(TradeDate));
            }

            if(fixMessage.isSetField(32)){
                String LastQty = fixMessage.getString(32);
                executionReport.setField(new LastQty(Double.parseDouble(LastQty)));
            }

            if(fixMessage.isSetField(136)){
                String NoMiscFees = fixMessage.getString(136);
                executionReport.setField(new NoMiscFees(Integer.parseInt(NoMiscFees)));
            }

            if(fixMessage.isSetField(137)){
                String MiscFeeAmt = fixMessage.getString(137);
                executionReport.setField(new MiscFeeAmt(Double.parseDouble(MiscFeeAmt)));
            }

            if(fixMessage.isSetField(138)){
                String MiscFeeCurr = fixMessage.getString(138);
                executionReport.setField(new MiscFeeCurr(MiscFeeCurr));
            }

            if(fixMessage.isSetField(139)){
                String MiscFeeType = fixMessage.getString(139);
                executionReport.setField(new MiscFeeType(MiscFeeType));
            }

            if(fixMessage.isSetField(891)){
                String MiscFeeBasis = fixMessage.getString(891);
                executionReport.setField(new MiscFeeBasis(Integer.parseInt(MiscFeeBasis)));
            }

            if(fixMessage.isSetField(15)){
                String Currency = fixMessage.getString(15);
                executionReport.setField(new Currency(Currency));
            }

            if(fixMessage.isSetField(58)){
                String Text = fixMessage.getString(58);
                executionReport.setField(new Text(Text));
            }

            if(fixMessage.isSetField(30)){
                String LastMkt = fixMessage.getString(30);
                executionReport.setField(new LastMkt(LastMkt));

            }





        }catch (ConfigError | InvalidMessage | FieldNotFound e){
            e.printStackTrace();
        }


        return executionReport;
    }



}
