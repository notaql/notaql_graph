package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Edge;

/**
 * Created by yannick on 11.01.16.
 */
public class InputDataVDataEdge implements VData {
    public enum Mode {
       Name, EdgeID, Label
    }

    private Mode mode;
    private String path;

    public InputDataVDataEdge(String path, Mode mode){
        this.mode = mode;
        this.path= path;
    }

    public VData evaluate(VertexToCheck vtc) {
        Edge edge = vtc.getEdge();
        if(mode==Mode.EdgeID){
            return new StringValue(edge.getId().toString());
        }
        else if(mode==Mode.Label) {
            return new StringValue(edge.getLabel().toString());
        }
        else {
            if (edge.getProperty(path)!=null) {
                String inhalt = edge.getProperty(path).toString();

                if (inhalt.equalsIgnoreCase("true")){
                    return new BooleanValue(true);
                }
                if (inhalt.equalsIgnoreCase("false")){
                    return new BooleanValue(false);
                }
                try {
                    return new NumberValue(Double.parseDouble(inhalt));
                }
                catch (Exception e) {
                   return new StringValue(inhalt);
                }


            }
          //OLD  return new StringValue("null"); //damit geht dann auch der == null vergleich
            return new NoVData();
        }
    }



}
