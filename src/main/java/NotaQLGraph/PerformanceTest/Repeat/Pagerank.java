package NotaQLGraph.PerformanceTest.Repeat;

import NotaQLGraph.Database.Database;
import NotaQLGraph.Database.Neo4JDatabase;
import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 23.02.16.
 */
public class Pagerank {
    static String notaql =
            "IN-ENGINE: neo4j(path <- '/tmp/dinput.db'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/tmp/dinput.db'),\n" +
                    "REPEAT: Pagerank(0.0005%),"+
                    "IN-FILTER: Label='Seite'," +
                    "OUT._id <- IN._e?(_outgoing)_._id," +
                    "OUT.anzahl <- count(IN._e?(_outgoing)._id)," +
                    "OUT.Pagerank <- SUM(IN.Pagerank/count(IN._e?(_outgoing)._id));" +
                    "";

    public static void main (String[] args) {
        Database sourcebase = new Neo4JDatabase("/tmp/dinput.db");
        sourcebase.initialisieren();
        sourcebase.printGraph();
        sourcebase.getDatabase().shutdown();
        long davor = System.currentTimeMillis();
        NotaQLGraph.notaqlGraph(notaql);
        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer/1000+"Sekunden");

    }
}
