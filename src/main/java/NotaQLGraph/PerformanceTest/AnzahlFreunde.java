package NotaQLGraph.PerformanceTest;

import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 18.02.16.
 */
public class AnzahlFreunde {
    static String notaql =
        "IN-ENGINE: neo4j(path <- '/tmp/pokec250000.db'),\n" +
        "OUT-ENGINE: neo4j(path <- '/tmp/freundesfreunde4.db'),\n" +
        "IN-FILTER: (AGE = '33'),"+
        "OUT._id <- IN._id,"+

        "OUT.freundesfreunde <- count(IN._e_[1,2]._id);"+
        "";

    public static void main (String[] args) {
        long davor = System.currentTimeMillis();
        NotaQLGraph.notaqlGraph(notaql);
        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer/1000+"Sekunden");

    }
}
