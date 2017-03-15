package NotaQLGraph.Database;


import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;

import com.tinkerpop.blueprints.util.wrappers.id.IdGraph;


/**
 * Created by yannick on 21.01.16.
 */
public class Neo4JDatabase extends Database {
    public Neo4JDatabase(String path) {
        super();
        this.path=path;
        graph = new IdGraph(new Neo4j2Graph(path));
        ((IdGraph)graph).setVertexIdFactory(new MyIdFactory());
    }

    @Override
    public void commit() {
        ((IdGraph) graph).commit();
    }
}
