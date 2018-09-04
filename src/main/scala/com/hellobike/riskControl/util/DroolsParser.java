package com.hellobike.riskControl.util;

import com.hellobike.riskControl.drools.DroolsUtil;
import com.hellobike.utils.GwDataStruct;

import java.text.ParseException;

public class DroolsParser {

    public static void parseData_dymanic(String json) throws ParseException {
        GwDataStruct gwDataStruct = GwDataParser.parseGwTopicToGwDataStruct(json);
        DroolsUtil.execRule(gwDataStruct);
    }

}
