package NotaQLGraph.PerformanceTest.ImportExport;

import NotaQLGraph.NotaQLGraph;

/**
 * Created by yannick on 22.02.16.
 */
public class JsonAnFFCSV {
    static String notaql =
            "IN-ENGINE: json(path <- '/home/yannick/graph/Quelldaten/personen_15000.json'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/tmp/temp2.db'),\n" +
                    "OUT._id <- IN._id,"+
                //    "OUT.$(IN.?(@.name()!='following'&& @.name()!='follower').name()) <- IN.@,"+
                    "OUT._e_?(_id=IN.following) <- EDGE (_outgoing, _l <- 'kennt');"+

                    "IN-ENGINE: neo4j(path <- '/tmp/temp2.db'),\n" +
                    "OUT-ENGINE: json(path <- '/tmp/anzahlFreundesFreunde.json'),\n" +
                    "OUT._id <- IN._id,"+
                    "OUT.freundesfreunde <- count(IN._e_[1,2]._id);"+
                    "";

    public static void main (String[] args) {
        long davor = System.currentTimeMillis();
        NotaQLGraph.notaqlGraph(notaql);
        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer+"Milli-Sekunden");

    }
}
