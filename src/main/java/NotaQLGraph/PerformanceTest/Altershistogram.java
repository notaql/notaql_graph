package NotaQLGraph.PerformanceTest;

import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 18.02.16.
 */
public class Altershistogram {
    static String notaql =
        "IN-ENGINE: neo4j(path <- '/tmp/pokec250000.db'),\n" +
        "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/alter.db'),\n" +
        "OUT._id <- IN.AGE,"+
        "OUT.anzahl <- COUNT();"+
        "";

    public static void main (String[] args) {
        long davor = System.currentTimeMillis();
        NotaQLGraph.notaqlGraph(notaql);
        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer/1000+"Sekunden");

    }
}
