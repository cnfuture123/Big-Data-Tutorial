package Demo;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class CreateMappings {

    // Example 2: Create Mappings
    public static void main(String[] args) throws Exception {
        // Create Client
        Settings set = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(set).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
        );

        // Create Mappings
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("article")
                .startObject("properties")
                .startObject("id")
                .field("type", "long")
                .field("store", "yes")
                .endObject()
                .startObject("type")
                .field("type", "string")
                .field("store", "yes")
                .field("analyzer", "ik_smart")
                .endObject()
                .startObject("content")
                .field("type", "string")
                .field("store", "yes")
                .field("analyzer", "ik_smart")
                .endObject()
                .endObject()
                .endObject()
                .endObject();

        // Create Mappings
        PutMappingRequest mapping = Requests.putMappingRequest("blog2")
                .type("article")
                .source(builder);

        client.admin().indices().putMapping(mapping).get();

        // Close Client
        client.close();
    }
}
