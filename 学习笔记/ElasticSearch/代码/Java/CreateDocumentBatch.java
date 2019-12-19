package Demo;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class CreateDocumentBatch {

    public static void main(String[] args) throws Exception {
        // Create Client
        Settings set = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(set).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
        );

        // Create Document
        for (int i = 2; i < 50; i++){
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("id", i)
                    .field("title", "hello es, the " + i + " day")
                    .field("content", "learning es " + i + " day")
                    .endObject();

            client.prepareIndex("blog2", "article").setSource(builder).get();
        }

        // Close Client
        client.close();
    }
}
