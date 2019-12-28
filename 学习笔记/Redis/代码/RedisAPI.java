package cn.redis;

import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisAPI {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.66.132", 6379);

        // Key
        Set<String> keys = jedis.keys("*");
        for(Iterator key = keys.iterator(); key.hasNext();) {
            String currKey = (String) key.next();
            System.out.println(currKey);
        }
        System.out.println("---------------------------");

        // String
        jedis.set("test1", "t1");
        jedis.mset("test2", "t2", "test3", "t3");
        System.out.println(jedis.mget("test1", "test2", "test3"));
        System.out.println("---------------------------");

        // List
        List<String> list = jedis.lrange("list1", 0, -1);
        for(String val : list){
            System.out.println(val);
        }
        System.out.println("---------------------------");

        // Set
        jedis.sadd("set2", "s1");
        jedis.sadd("set2", "s2");
        jedis.sadd("set2", "s3");
        Set<String> set2 = jedis.smembers("set2");
        for(Iterator iterator = set2.iterator(); iterator.hasNext(); ){
            String val = (String) iterator.next();
            System.out.println(val);
        }
        System.out.println("---------------------------");

        // Hash
        jedis.hset("hash1", "name", "cn");
        System.out.println(jedis.hget("hash1", "name"));

        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "cn2");
        map.put("age", "21");

        jedis.hmset("hash2", map);
        List<String> result = jedis.hmget("hash2", "name", "age");
        for(String val: result){
            System.out.println(val);
        }
        System.out.println("---------------------------");

        // Zset
        jedis.zadd("zset2", 20, "z1");
        jedis.zadd("zset2", 30, "z2");
        jedis.zadd("zset2", 10, "z3");
        Set<String> zset2 = jedis.zrange("zset2", 0, -1);
        for (Iterator iterator = zset2.iterator(); iterator.hasNext(); ) {
            String val = (String) iterator.next();
            System.out.println(val);
        }

    }
}
