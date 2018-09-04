package com.hello.utils;

//import com.hellobike.riskControl.util.RedisUtil;
import com.hello.riskControl.util.JedisPoolUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class GwDataStructService {

    public Long deltaTime(String startTime, String updateTime) throws ParseException {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat fs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long startTimeLong = 0L;
        Long updateTimeLong = 0L;
        try{
            startTimeLong = f.parse(startTime).getTime();
        }
        catch (Exception e){
            startTimeLong = fs.parse(startTime).getTime();
        }
        try {
            updateTimeLong = f.parse(updateTime).getTime();
        }
        catch (Exception e){
            updateTimeLong = fs.parse(updateTime).getTime();
        }
        return (updateTimeLong-startTimeLong);
    }

    public boolean exist(String key,String field) {

        if(JedisPoolUtil.exist(key,field)){
            return true;
        }
        else
            return false;
    }

    public Long insertData(String key,String field,String value){
        return JedisPoolUtil.insert(key,field,value);
    }
    public Long insertIndexData(int index,String key,String field,String value){
        return JedisPoolUtil.insertindex(index,key,field,value);
    }

    public Long increaseData(String key,String field,Long value){
        return JedisPoolUtil.increase(key,field,value);
    }

    public Long updateDate(String key,String field,String value){
        return JedisPoolUtil.update(key,field,value);
    }

    public String getData(String key,String field){
        return JedisPoolUtil.get(key,field);
    }
    public Map<String,String> getAllData(String key){
        return JedisPoolUtil.getAll(key);
    }

    public Long deleteData(String key,String field){
        return JedisPoolUtil.delete(key,field);
    }

    public static void main(String[] args) throws ParseException{
        GwDataStructService gwDataStructService = new GwDataStructService();
        Long test = gwDataStructService.deltaTime("2018-08-31 14:59:56.468","2018-08-31 15:03:12.277");
        System.out.println(test);
    }
}
