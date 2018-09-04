package com.hello.riskControl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hello.utils.GwDataStruct;

public class GwDataParser {
    public static GwDataStruct parseGwTopicToGwDataStruct(String json){
        GwDataStruct gwDataStruct = new GwDataStruct();
        JSONObject jsonObject = JSON.parseObject(json.substring(json.indexOf("{")));
        String userGuid = jsonObject.getString("userGuid");
        gwDataStruct.setUserGuid(userGuid);
        String action = jsonObject.getString("action");
        gwDataStruct.setActionType(action);
        String remoteIp = jsonObject.getString("remoteIp");
        gwDataStruct.setRemoteIp(remoteIp);
        String logTime = jsonObject.getString("logTime");
        gwDataStruct.setLogTime(logTime);
        String type = jsonObject.getString("type");
        gwDataStruct.setType(type);
        if(type.equals("metrics-userGw-tcp")){
            String clientId = jsonObject.getString("clientId");
            gwDataStruct.setClientId(clientId);
        }
        if(type.equals("metrics-userGw-https")){
            String userAgent = jsonObject.getString("userAgent");
            gwDataStruct.setUserAgent(userAgent);
        }
        String requestJson = jsonObject.getString("requestJson");
        gwDataStruct.setRequestJson(requestJson);
        return gwDataStruct;
    }
}
