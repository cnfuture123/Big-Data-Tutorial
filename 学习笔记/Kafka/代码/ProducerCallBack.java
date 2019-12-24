package cn.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerCallBack {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.66.132:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        // 1.Create producer object
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        // 2.Call send()
        for (int i=0; i<100; i++){
            Thread.sleep(10);
            producer.send(new ProducerRecord<String, String>("first", i + "", "message: " + i),
                    new Callback() {
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            if(e==null){
                                System.out.println("success");
                            }else{
                                e.printStackTrace();
                            }
                        }
                    });
        }

        // 3.Close producer
        producer.close();

    }
}
