package com.tinkerpop.gremlin.elastic;

import com.tinkerpop.gremlin.AbstractGraphProvider;
import com.tinkerpop.gremlin.elastic.structure.*;
import com.tinkerpop.gremlin.structure.Graph;
import org.apache.commons.configuration.Configuration;
import org.elasticsearch.node.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class ElasticGraphGraphProvider extends AbstractGraphProvider {

    private static final Set<Class> IMPLEMENTATION = new HashSet<Class>() {{
        add(ElasticEdge.class);
        add(ElasticElement.class);
        add(ElasticGraph.class);
        add(ElasticProperty.class);
        add(ElasticVertex.class);
        add(ElasticVertexProperty.class);
    }};

    Map<String, Node> nodes = new HashMap<>();

    @Override
    public Map<String, Object> getBaseConfiguration(final String graphName, final Class<?> test, final String testMethodName) {
        if(!nodes.containsKey(graphName))
            nodes.put(graphName, ElasticService.createNode("test", graphName, true, false));

        System.out.println("graphName: " + graphName);
        return new HashMap<String, Object>() {{
            put(Graph.GRAPH, ElasticGraph.class.getName());
            put("elasticsearch.cluster.name", "test");
            put("elasticsearch.index.name", graphName);
            put("elasticsearch.local", true);
            put("elasticsearch.refresh", true);
            put("elasticsearch.client", true);
        }};
    }

    @Override
    public void clear(final Graph g, final Configuration configuration) throws Exception {
        if (g != null) {
            if (g instanceof ElasticGraph) ((ElasticGraph) g).elasticService.clearAllData();
            g.close();
        }
    }

    @Override
    public Set<Class> getImplementations() {
        return IMPLEMENTATION;
    }
}
