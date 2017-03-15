package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

/**
 * Created by yannick on 20.01.16.
 */
public class InputDataEdgeAtVData implements VData {
    VData vData;
    public InputDataEdgeAtVData(VData visit) {
        this.vData=visit;
    }

    public VData evaluate(VertexToCheck vtc) {
        return vData.evaluate(vtc);
    }
}
