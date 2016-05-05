package elasticHandler;

        import model.Inzerat;
        import org.elasticsearch.client.Client;
        import org.elasticsearch.client.transport.TransportClient;
        import org.elasticsearch.common.transport.InetSocketTransportAddress;
        import org.elasticsearch.index.query.QueryBuilder;
        import org.elasticsearch.index.query.QueryBuilders;
        import org.elasticsearch.search.SearchHit;
        import org.elasticsearch.search.SearchHits;

        import java.io.IOException;
        import java.net.InetAddress;
        import java.net.UnknownHostException;
        import java.util.ArrayList;
        import java.util.List;

        import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;


public class ElasticSearch {

    Client client;
    String host = "localhost";
    Integer port = 9300;
    String index = "inzeraty";
    String type = "inzerat";

    public List<Inzerat> advancedSearch(String categ, String city, Integer price_min, Integer price_max, Integer distance) throws IOException {
        List<String> mesta = processCities(
                client.prepareSearch("mesta").setTypes("mesto")
                        .setQuery(QueryBuilders.matchQuery("from", city))
                        .setPostFilter(QueryBuilders.rangeQuery("vzdialenost").from(0).to(distance))
                .execute().actionGet().getHits());

        List<Inzerat> inzeraty = new ArrayList<>();
        for(String mesto: mesta) {
            inzeraty.addAll(processHits(
                    client.prepareSearch(index).setTypes(type)
                            .setQuery(QueryBuilders.matchQuery(categ, "typ"))
                            .setQuery(QueryBuilders.multiMatchQuery(mesto, "subjects"))
                            .setPostFilter(QueryBuilders.rangeQuery("cena").from(price_min).to(price_max))
                            .execute().actionGet().getHits()));
        }
        return inzeraty;
    }

    public List<Inzerat> advancedSearch(String phrase, String categ, String city, Integer price_min, Integer price_max, Integer distance) throws IOException {
        QueryBuilder query = multiMatchQuery(phrase,"nazov","info","typ", "keywords","subjects");

        List<String> mesta = processCities(
                client.prepareSearch("mesta").setTypes("mesto")
                        .setQuery(QueryBuilders.matchQuery("from", city))
                        .setPostFilter(QueryBuilders.rangeQuery("vzdialenost").from(0).to(distance))
                        .execute().actionGet().getHits());

        List<Inzerat> inzeraty = new ArrayList<>();
        for(String mesto: mesta) {
            inzeraty.addAll(processHits(
                    client.prepareSearch(index).setTypes(type)
                            .setQuery(query)
                            .setQuery(QueryBuilders.matchQuery(categ, "typ"))
                            .setQuery(QueryBuilders.multiMatchQuery(mesto, "subjects"))
                            .setPostFilter(QueryBuilders.rangeQuery("cena").from(price_min).to(price_max))
                            .execute().actionGet().getHits()));
        }
        return inzeraty;
    }

    public List<Inzerat> simpleSearch(String phrase) throws IOException {
        QueryBuilder query = multiMatchQuery(phrase,"nazov","info","typ", "keywords","subjects");
        return processHits(client.prepareSearch(index).setTypes(type).setQuery(query).execute().actionGet().getHits());
    }

    public List<Inzerat> simpleSearch() throws IOException {
        return processHits(client.prepareSearch(index).setTypes(type).execute().actionGet().getHits());
    }

    protected List<Inzerat> processHits(SearchHits hits){
        List<Inzerat> inzeraty = new ArrayList<Inzerat>();

        for (SearchHit hit : hits)
            inzeraty.add(processRowProfile(hit));

        return inzeraty;
    }

    protected List<String> processCities(SearchHits hits){
        List<String> mesta = new ArrayList<String>();

        for (SearchHit hit : hits)
            mesta.add(hit.getSource().get("from").toString());

        return mesta;
    }


    protected Inzerat processRowProfile(SearchHit rs){
        String mesto = rs.getSource().get("subjects").toString();
        mesto = mesto.substring(mesto.indexOf("v") + 2, mesto.length());
        mesto = mesto.substring(0, mesto.indexOf('}'));

        return new Inzerat(rs.getSource().get("nazov").toString(),mesto,Integer.parseInt(rs.getSource().get("id").toString()),
                Integer.parseInt(rs.getSource().get("cena").toString()),rs.getSource().get("info").toString());
    }

    public ElasticSearch() throws UnknownHostException {
        client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
    }
}