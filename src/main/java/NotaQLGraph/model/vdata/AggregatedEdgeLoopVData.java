package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;
import NotaQLGraph.model.predicate.Predicate;

/**
 * Created by yannick on 13.01.16.
 */
public class AggregatedEdgeLoopVData implements VData {
    VData vdata;
    MultiEdgeVData lastEdge;
    MultiVertexVData lastVertex;

    public AggregatedEdgeLoopVData(Predicate edgeVdatapredicate, Predicate knotenPredicat, VData edgeVData, RangeVData range) {
        range.getStart();

        vdata = new MultiVertexVData(knotenPredicat, null, edgeVData, (0 >= range.getStart()) ? true : false);
        lastVertex = (MultiVertexVData) vdata;

        for (int i=1;i<=range.getEnde();i++){
                lastEdge=new MultiEdgeVData(edgeVdatapredicate);
                lastVertex.setEdge(lastEdge);
                lastVertex=new MultiVertexVData(knotenPredicat,null, edgeVData,(i >=range.getStart())?true:false);
                lastEdge.setVertex(lastVertex);

        }


    }

    public VData evaluate(VertexToCheck vtc) {
        return vdata.evaluate(vtc);
    }
}
