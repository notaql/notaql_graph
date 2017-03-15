package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

/**
 * Created by yannick on 14.12.15.
 * Klasse die benutzt wird um Beispielsweise bei Count eine leeres Element darzustellen
 */
public class NoVData implements VData {
    public VData evaluate(VertexToCheck vertex) {
        return this;
    }


    public String toString() {
        return "NoVData";
    }
}
