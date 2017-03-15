package NotaQLGraph.PerformanceTest.Repeat;

import NotaQLGraph.Database.Database;
import NotaQLGraph.Database.Neo4JDatabase;
import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 23.02.16.
 */
public class Distanz {
    static String notaql =
            "IN-ENGINE: neo4j(path <- '/tmp/dinput.db'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/tmp/dinput.db'),\n" +
                    "REPEAT: -1,"+
                    "IN-FILTER: dist," +
                    "OUT._id <- IN._e_?(!(dist))._id," +
                    "OUT.dist <- MIN(IN.dist+1);" +

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
