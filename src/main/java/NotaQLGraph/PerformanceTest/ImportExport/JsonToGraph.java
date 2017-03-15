package NotaQLGraph.PerformanceTest.ImportExport;

import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 22.02.16.
 */
public class JsonToGraph {

    static String notaql =
            "IN-ENGINE: json(path <- '/home/yannick/graph/Quelldaten/personen_15000.json'),\n" +
            "OUT-ENGINE: neo4j(path <- '/tmp/jsontograph.db'),\n" +
            //"OUT._id <- IN._id,"+
            "OUT.$(IN.?(@.name()!='following'&& @.name()!='follower').name()) <- IN.@,"+
            "OUT._e_?(_id=IN.following) <- EDGE (_outgoing, _l <- 'kennt');"+
            "";

    static String notaql2 =
            "IN-ENGINE: json(path <- '/tmp/personen_5.json'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/tmp/jsontograph.db'),\n" +
                    //"OUT._id <- IN._id,"+
                    "OUT.$(IN.?(@.name()!='following'&& @.name()!='follower').name()) <- IN.@,"+
                    "OUT._e_?(_id=IN.following) <- EDGE (_outgoing, _l <- 'kennt');"+
                    "";

    public static void main (String[] args) {
        long davor = System.currentTimeMillis();
        NotaQLGraph.notaqlGraph(notaql2);
        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer+"Milli-Sekunden");

    }
}
