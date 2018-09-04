package com.hello.riskControl.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

public class JedisPoolUtil {

    private volatile static JedisPool jedisPool;
    private JedisPoolUtil(){}

    /**
     * 单例模式, 获取连接池
     * @return
     */
    public static JedisPool getJedisPoolInstance(){
        if (jedisPool == null) {
            synchronized (JedisPoolUtil.class) {
                if (jedisPool == null) {
                    //连接池配置
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
                    // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
                    poolConfig.setMaxTotal(500);
                    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
                    poolConfig.setMaxIdle(10);
                    // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
                    poolConfig.setMaxWaitMillis(100 * 1000);
                    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
                    poolConfig.setTestOnBorrow(true);

                    jedisPool = new JedisPool(poolConfig, "10.111.40.148",6379,2000,"123456");
//                    jedisPool = new JedisPool(poolConfig, "127.0.0.1",6379);
                }
            }
        }
        return jedisPool;
    }

    /**
     * 资源返回连接池
     * @param jedis
     */
    public static void returnResource(Jedis jedis){
        if (jedis != null) {
//            System.out.println("关闭连接池");
            jedis.close();
        }
    }

    //my
    public static boolean exist(String key, String field){
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.select(1);
        boolean result = jedis.hexists(key,field);
        JedisPoolUtil.returnResource(jedis);
        return result;
    }

    public static Long insert(String key,String field,String value){
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.select(1);
        Long result = jedis.hset(key,field,value);
//        jedis.expire(key,4*60*60);
        JedisPoolUtil.returnResource(jedis);
        return result;
    }

    public static Long insertindex(int index,String key,String field,String value){
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.select(index);
        Long result = jedis.hset(key,field,value);
//        jedis.expire(key,4*60*60);
        JedisPoolUtil.returnResource(jedis);
        return result;
    }

    public static Long increase(String key,String field,Long value){
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.select(1);
        Long result = jedis.hincrBy(key,field,value);
        JedisPoolUtil.returnResource(jedis);
        return result;
    }

    public static Long update(String key,String field,String value){
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.select(1);
        Long result = jedis.hset(key,field,value);
        JedisPoolUtil.returnResource(jedis);
        return result;
    }

    public static String get(String key,String field){
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.select(1);
        String result = jedis.hget(key,field);
        JedisPoolUtil.returnResource(jedis);
        return result;
    }

    public static Map<String,String> getAll(String key){
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.select(1);
        Map<String,String> result = jedis.hgetAll(key);
        JedisPoolUtil.returnResource(jedis);
        return result;
    }

    public static Long delete(String key,String field){
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.select(1);
        Long result = jedis.del(key);
        JedisPoolUtil.returnResource(jedis);
        return result;
    }


    public static void main(String[] args){
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = jedisPool.getResource();
        Map map = new HashMap();
        map.put("name","liuce");
        map.put("age","15");
        map.put("all","500");
        jedis.hmset("test",map);
        System.out.println(jedis.hgetAll("test"));
    }
}
