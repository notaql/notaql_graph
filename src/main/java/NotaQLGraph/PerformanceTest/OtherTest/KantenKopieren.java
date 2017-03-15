package NotaQLGraph.PerformanceTest.OtherTest;

import NotaQLGraph.Database.Database;
import NotaQLGraph.Database.Neo4JDatabase;
import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 23.02.16.
 */
public class KantenKopieren {
    static String notaql =
            "IN-ENGINE: neo4j(path <- '/tmp/dinput.db'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/tmp/output5.db'),\n" +
                    "REPEAT: 10,"+
                    "OUT._id <- IN._id," +
                    "OUT.$(IN._k?(true=true)) <- IN._v," +
                    "OUT._e_?(name && _id=IN._e?(_incoming)_._id) <- EDGE(_outgoing, _l <- IN._e[@]._l, seit <- IN._e[@].seit);"+

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
