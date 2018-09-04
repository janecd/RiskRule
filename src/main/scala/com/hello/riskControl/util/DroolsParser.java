package com.hello.riskControl.util;

import com.hello.riskControl.drools.DroolsUtil;
import com.hello.utils.GwDataStruct;

import java.text.ParseException;

public class DroolsParser {

    public static void parseData_dymanic(String json) throws ParseException {
        GwDataStruct gwDataStruct = GwDataParser.parseGwTopicToGwDataStruct(json);
        DroolsUtil.execRule(gwDataStruct);
    }

}
