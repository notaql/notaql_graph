package NotaQLGraph.PerformanceTest;

import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 18.02.16.
 */
public class Pathlength {
    static String notaql =
        "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
        "OUT-ENGINE: neo4j(path <- '/tmp/distanz2.db'),\n" +
        "OUT._id <- IN._id,"+
        "OUT.name <- IN.name,"+
        "OUT.distanz <- pathlength(IN._e_?(name='chantal')[0,4]._id);"+
        "";

    public static void main (String[] args) {
        long davor = System.currentTimeMillis();
        NotaQLGraph.notaqlGraph(notaql);
        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer/1000+"Sekunden");

    }
}
