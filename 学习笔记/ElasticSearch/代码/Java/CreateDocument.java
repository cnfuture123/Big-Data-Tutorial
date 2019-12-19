package Demo;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class CreateDocument {

    public static void main(String[] args) throws Exception {
        // Create Client
        Settings set = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(set).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
        );

        // Create Document
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .field("id", 1)
                    .field("title", "hello es")
                    .field("content", "hello es. keep in touch")
                .endObject();

        client.prepareIndex("blog2", "article", "1").setSource(builder).get();

        // Close Client
        client.close();
    }
}
