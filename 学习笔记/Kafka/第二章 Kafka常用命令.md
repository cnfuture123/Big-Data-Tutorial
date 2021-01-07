# Kafka常用命令

  - 启动集群：bin/kafka-server-start.sh -daemon config/server.properties
  - 关闭集群： bin/kafka-server-stop.sh stop
  - 查看当前服务器中的所有topic：bin/kafka-topics.sh --zookeeper localhost:2181 --list
  - 创建topic: bin/kafka-topics.sh --zookeeper localhost:2181 --create --replication-factor 3 --partitions 1 --topic firstTopic
    - --topic: 定义topic名。
    - --replication-factor：定义副本数
    - --partitions：定义分区数
  - 查看某个Topic的详情: bin/kafka-topics.sh --zookeeper localhost:2181 --describe --topic firstTopic
  - 删除topic：bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic firstTopic
  - 发送消息: bin/kafka-console-producer.sh --broker-list localhost:9092 --topic firstTopic
  - 消费消息: bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic firstTopic
    - --from-beginning：会把主题中以往所有的数据都读取出来。
  - 修改分区数: bin/kafka-topics.sh --zookeeper localhost:2181 --alter --topic firstTopic --partitions 5
  - 查看主题配置: bin/kafka-configs.sh --zookeeper localhost:2181 --entity-type topics --entity-name firstTopic --descirbe
  - 修改主题数据有效期: bin/kafka-configs.sh --zookeeper localhost:2181 --entity-type topics --entity-name firstTopic --alter --add-config retention.ms=3000
  - 查看消费者组的消费情况: bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --describe --group topicGroup --command-config ./config/consumer.properties
  - 查看主题权限控制列表: bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --list --topic firstTopic
  - 给用户添加Producer/Consumer权限: bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --add --allow-principal User:<用户名> --producer/consumer --topic firstTopic
  - 删除用户Producer/Consumer权限: bin/kafka-acls.sh --authorizer-properties zookeeper.connect=localhost:2181 --remove --allow-principal User:<用户名> --producer/consumer --topic firstTopic
  
   
