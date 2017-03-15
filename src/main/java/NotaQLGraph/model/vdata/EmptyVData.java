package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

/**
 * Created by yannick on 14.12.15.
 * Klasse die benutzt wird um Beispielsweise bei Count eine leeres Element darzustellen. Also nicht, dass der Wert Null/nicht gesetzt ist, sondern das da überhaupt kein Wert in der eckigen KLamer war und man deshalb gar nicht weiß was man abfragen soll
 */
public class EmptyVData implements VData {
    public VData evaluate(VertexToCheck vertex) {
        return this;
    }


    public String toString() {
        return "EMPTYVData";
    }
}
