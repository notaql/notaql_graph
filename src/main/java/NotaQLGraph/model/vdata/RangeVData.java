package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;
import NotaQLGraph.NotaQLGraph;


/**
 * Created by yannick on 13.01.16.
 */
public class RangeVData implements VData {
    private int start;
    private int ende;

    public RangeVData(String a){
        int aa = Integer.parseInt(a);
        start = aa;
        ende = aa;
    }

    public RangeVData(String a, String b){
        int aa = Integer.parseInt(a);
        int bb = -1;
        if (b.equals("Infinity")){
            bb= NotaQLGraph.maxEdgeDepth;
        }else{
            bb = Integer.parseInt(b);
        }

        start = aa;
        ende = bb;


    }
    public VData evaluate(VertexToCheck vtc) {
        return new NoVData();
    }

    public int getStart() {
        return start;
    }

    public int getEnde() {
        return ende;
    }
}
