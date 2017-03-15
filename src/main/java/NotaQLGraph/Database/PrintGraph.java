package NotaQLGraph.Database;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;
import com.tinkerpop.blueprints.util.wrappers.id.IdGraph;

import java.util.List;

/**
 * Created by yannick on 22.02.16.
 */
public class PrintGraph {
    public static void main (String args[]) {
      //  String path = "/media/yannick/BE2EE9682EE91A63/Master/pokec.db";
        String path = "/media/yannick/BE2EE9682EE91A63/Master/pokec250000.db";
        Database graph = new Neo4JDatabase(path);

          graph.printGraph();
        int i=0;
        for (Vertex v : graph.getDatabase().getVertices()){
            i++;
        }
        System.out.println(i++);
        graph.getDatabase().shutdown();
    }
}
