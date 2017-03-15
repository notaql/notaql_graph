package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

/**
 * Created by yannick on 15.12.15.
 */
public class AggregatedVData implements VData {
    VData vdata;
    Operation op;
    public AggregatedVData(VData vdata, Operation op) {
        this.vdata=vdata;
        this.op=op;
    }

    public enum Operation {
        SUM, AVG, MIN, MAX, COUNT,
    }
    public VData evaluate(VertexToCheck vtc) {
        return vdata.evaluate(vtc);
    }

    public Operation getOperation(){
        return op;
    }

    public VData getVdata() {
        return vdata;
    }
}
