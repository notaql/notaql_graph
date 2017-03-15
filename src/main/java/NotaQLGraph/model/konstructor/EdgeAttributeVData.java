package NotaQLGraph.model.konstructor;

import NotaQLGraph.Evaluation.VertexToCheck;
import NotaQLGraph.model.vdata.VData;

import javax.xml.bind.ValidationEvent;

/**
 * Created by yannick on 20.01.16.
 */
public class EdgeAttributeVData implements VData {
    String key;
    VData value;
    public EdgeAttributeVData(String text, VData visit) {
        this.key=text;
        this.value=visit;
    }

    public VData evaluate(VertexToCheck vtc) {
        return value.evaluate(vtc);
    }

    public String getKey(){
        return key;
    }
}
