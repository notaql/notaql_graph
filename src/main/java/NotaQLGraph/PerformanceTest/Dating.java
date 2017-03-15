package NotaQLGraph.PerformanceTest;

import NotaQLGraph.Database.Database;
import NotaQLGraph.Database.Neo4JDatabase;
import NotaQLGraph.NotaQLGraph;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;

import java.util.LinkedList;

/**
 * Created by yannick on 23.02.16.
 */
public class Dating {


    static String notaql =
        /*    "IN-ENGINE: neo4j(path <- '/tmp/pokec250000.db'),\n" +
                    "OUT-ENGINE: json(path <- '/tmp/pokec250000.json'),\n" +
                    "IN-FILTER: (AGE='33'&&gender=1),"+
                    "OUT._id <- IN._id,"+
                    //        "OUT.e <- list (IN._e?(_outgoing)_._e?(_outgoing)_?(gender=0&&AGE>=33 && AGE <=33+4)._id);"+
                    "OUT.anzahl <- count(IN._e?(_outgoing)_._e?(_outgoing)_?(gender=0&&AGE=33)._id);"+
                    "";

*/

    "IN-ENGINE: neo4j(path <- '/tmp/pokec250000.db'),\n" +
        "OUT-ENGINE: neo4j(path <- '/tmp/pokec250000.db'),\n" +
        "IN-FILTER: (AGE='33'&&gender=1),"+
        "OUT._id <- IN._id,"+
        //        "OUT.e <- list (IN._e?(_outgoing)_._e?(_outgoing)_?(gender=0&&AGE>=33 && AGE <=33+4)._id);"+
        "OUT._e_?(_id=IN._e?(_outgoing)_._e?(_outgoing)_?(gender=0&&AGE>=33&&AGE<=37)._id) <- EDGE (_outgoing, _l <- 'Datingpartner', datum <- '29.2.2016');"+


        "IN-ENGINE: neo4j(path <- '/tmp/pokec250000.db'),\n" +
            "OUT-ENGINE: neo4j(path <- '/tmp/pokec250000.db'),\n" +
            "IN-FILTER: (AGE='33'&&gender=0),"+
            "OUT._id <- IN._id,"+
            //        "OUT.e <- list (IN._e?(_outgoing)_._e?(_outgoing)_?(gender=0&&AGE>=33 && AGE <=33+4)._id);"+
            "OUT._e_?(_id=IN._e?(_outgoing)_._e?(_outgoing)_?(gender=1&&AGE<=33&&AGE>29)._id) <- EDGE (_outgoing, _l <- 'Datingpartner', datum <- '29.2.2016');"+
            "";
/*
    "IN-ENGINE: neo4j(path <- '/tmp/pokec250000.db'),\n" +
            "OUT-ENGINE: json(path <- '/tmp/pokec250000.json'),\n" +
            "IN-FILTER: (AGE='33'&&gender=1),"+
            "OUT._id <- IN._id,"+
            //        "OUT.e <- list (IN._e?(_outgoing)_._e?(_outgoing)_?(gender=0&&AGE>=33 && AGE <=33+4)._id);"+
            "OUT.liste <- list(IN._e?(_outgoing)_._e?(_outgoing)_?(gender=0&&AGE=33)._id);"+
            "";
*/
    public static void main (String[] args) {

/*


        Database g = new Neo4JDatabase("/tmp/test.db");
        Graph graph1 = g.getDatabase();
        Vertex a = graph1.addVertex(1);
        Vertex b = graph1.addVertex(2);
        Vertex c = graph1.addVertex(3);
        Vertex d = graph1.addVertex(4);
        Vertex e = graph1.addVertex(5);
        a.setProperty("gender", 1);
        b.setProperty("gender", 0);
        c.setProperty("gender", 1);
        d.setProperty("gender", 1);
        e.setProperty("gender", 0);
        a.setProperty("AGE", 33);
        b.setProperty("AGE", 33);
        c.setProperty("AGE", 33);
        d.setProperty("AGE", 3);
        e.setProperty("AGE", 33);


        graph1.addEdge(null, a, b, "kennt");
        graph1.addEdge(null, b, c, "kennt");
        graph1.addEdge(null, b, d, "kennt");
        graph1.addEdge(null, b, e, "kennt");
        graph1.addEdge(null, c, e, "kennt");
        g.printGraph();

        graph1.shutdown();
*/


        long davor = System.currentTimeMillis();
        NotaQLGraph.notaqlGraph(notaql);
        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer/1000+"Sekunden");
/*
        g = new Neo4JDatabase("/tmp/test.db");
        g.printGraph();
        g.getDatabase().shutdown();
*/
    }
}
