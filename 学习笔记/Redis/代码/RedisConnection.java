package cn.redis;

import redis.clients.jedis.Jedis;

public class RedisConnection {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("192.168.66.132", 6379);
        System.out.println("Connection is ok: " + jedis.ping());
    }
}
