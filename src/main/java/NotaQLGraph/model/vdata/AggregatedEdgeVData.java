package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

import java.util.HashSet;

/**
 * Created by yannick on 06.01.16.
 */
public class AggregatedEdgeVData implements VData {
        VData vdata;
        Operation op;
        private HashSet<String> hashset = new HashSet<String>();

        public AggregatedEdgeVData(VData vdata, Operation op) {
            this.vdata=vdata;
            this.op=op;
        }

        public enum Operation {
            SUM, AVG, MIN, MAX, COUNT,NONE,PREDICATE, LISTE, PATHLENGTH

            // pathlengt um diestnaz zwischen zwei punkten zu berechnen, hier zählt das knotenprädikat nur für endknoten
            // /PRedicat ist dafür da, um zu schauen das es bei Pathexsitenzce etwas gibt, also existiert, das heißt eins wird zurückgegeben, egal welches: dass es dann nur eins überprüft,
            // wenn es stimmt gib was zurück und somit filter erfüllt

            //LISTE ist nur zum Export gedacht, sodass in JSON ein Array zurückgegebwn werden kann (z.b. mit allen Kanten die die bedingung erfüllen)

            //NONE: Wenn ich einen Baum hab und nur die Mutter zurückgeben soll => wenn ich dennoch liste habe, soll eine Fehlermeldung geworfen werden
            //FILTER WURDE NOCH NACHTRÄGLICH hinzugefügt, sodass ich beim vorzeitigen Filtern das auf Filter setzen kann (in evaluate) und dann nicht die Operation übernheme
        }
    public VData evaluate(VertexToCheck vtc) {
        vtc.setAggregationoperation(op);
        vtc.setNewHashset();
        return vdata.evaluate(vtc);
    }

    public Operation getOperation(){
        return op;
    }

    public VData getVdata() {
        return vdata;
    }
}


