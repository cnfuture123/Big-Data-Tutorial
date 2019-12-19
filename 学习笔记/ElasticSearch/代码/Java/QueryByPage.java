package Demo;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.Iterator;

public class QueryByPage {

    public static void main(String[] args) throws Exception {
        // Create Client
        Settings set = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(set).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
        );

        // Search All Data, return 10 records by default
        SearchRequestBuilder builder = client.prepareSearch("blog2")
            .setTypes("article")
            .setQuery(QueryBuilders.matchAllQuery());

        // 5 records for one page
        builder.setFrom(0).setSize(5);

        // Get Result
        SearchResponse response = builder.get();

        // Get Result
        SearchHits hits = response.getHits();
        System.out.println("Total records: " + hits.totalHits);

        Iterator<SearchHit> hitIterator = hits.iterator();
        while(hitIterator.hasNext()){
            SearchHit hit = hitIterator.next();
            System.out.println(hit.getSourceAsString());
        }

        // Close Client
        client.close();
    }
}
