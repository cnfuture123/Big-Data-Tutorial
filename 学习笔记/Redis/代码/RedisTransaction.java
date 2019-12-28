package cn.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisTransaction {

    public static void main(String[] args) throws Exception {
        boolean result = transact();
        System.out.println("Transaction result: " + result);
    }

    public static boolean transact() throws Exception {
        Jedis jedis = new Jedis("192.168.66.132", 6379);

        int balance;
        int debitAmount;
        int txnAmount = 10;

        jedis.watch("balance");
        balance = Integer.parseInt(jedis.get("balance"));
        debitAmount = Integer.parseInt(jedis.get("debitAmount"));

        if(balance < txnAmount){
            jedis.unwatch();
            return false;
        } else {
            System.out.println("Transaction start...");

            Transaction transaction = jedis.multi();
            transaction.decrBy("balance", txnAmount);
            transaction.incrBy("debitAmount", txnAmount);
            transaction.exec();

            System.out.println("Balance: " + balance);
            System.out.println("Debit Amount: " + debitAmount);
            return true;
        }
    }
}
