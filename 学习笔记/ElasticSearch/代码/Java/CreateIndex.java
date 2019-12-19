package Demo;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class CreateIndex {

    // Example 1: Create Index only
    public static void main(String[] args) throws Exception {
        // Create Client
        Settings set = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(set).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
        );

        // Create Index
        client.admin().indices().prepareCreate("blog2").get();

        // Close Client
        client.close();
    }
}
