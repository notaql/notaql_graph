package NotaQLGraph.PerformanceTest;

import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 23.02.16.
 */
public class StichprobeNehmen {
    static String notaql =
            "IN-ENGINE: neo4j(path <- '/tmp/pokec250000.db'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/tmp/pokecStichprobeAge33Gender1.db'),\n" +
                    "IN-FILTER: (AGE = 33),"+
                    "OUT._id <- IN._id," +
                  "OUT.AGE <- IN.AGE,"+
                    "OUT.gender <- IN.gender,"+

                    "OUT.freunde <- count(IN._e?(_outgoing)_._id);"+

                    "";

    public static void main (String[] args) {
        long davor = System.currentTimeMillis();
        NotaQLGraph.notaqlGraph(notaql);
        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer/1000+"Sekunden");

    }
}
