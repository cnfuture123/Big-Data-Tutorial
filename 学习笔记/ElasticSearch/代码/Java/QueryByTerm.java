package Demo;

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

public class QueryByTerm {

    public static void main(String[] args) throws Exception {
        // Create Client
        Settings set = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(set).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)
        );

        // Set Search Criteria
        SearchResponse response = client.prepareSearch("blog2")
                .setTypes("article")
                .setQuery(QueryBuilders.termQuery("content", "hello"))
                .get();

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
