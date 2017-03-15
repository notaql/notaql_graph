package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

/**
 * Created by yannick on 20.01.16.
 * wird benutzt, wenn ich bei den Kanten ein Label erzeugen möchte
 */
public class LabelVData implements VData {
    VData vData;
    public LabelVData(VData visit) {
        this.vData=visit;
    }

    public VData evaluate(VertexToCheck vtc) {
        return vData.evaluate(vtc);
    }
}
