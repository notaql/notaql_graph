package NotaQLGraph.PerformanceTest.ImportExport;

import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 22.02.16.
 */
public class GraphToJson {

    static String notaql =
            "IN-ENGINE: neo4j(path <- '/tmp/jsontograph.db'),\n" +
            "OUT-ENGINE: json(path <- '/tmp/test2.json'),\n" +
            "OUT._id <- IN._id,"+
            "OUT.user_id<- IN._id,"+
            "OUT.avatar <- IN.avatar,"+
            "OUT.following <- list(IN._e?(_outgoing)_._id),"+
            "OUT.follower <- list(IN._e?(_incoming)_._id)"+
            "";

    public static void main (String[] args) {
        long davor = System.currentTimeMillis();
        NotaQLGraph.notaqlGraph(notaql);
        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer+"Milli-Sekunden");

    }
}
