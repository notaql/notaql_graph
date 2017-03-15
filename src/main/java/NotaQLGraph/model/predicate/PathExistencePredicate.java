package NotaQLGraph.model.predicate;

import NotaQLGraph.Evaluation.Filter;
import NotaQLGraph.Evaluation.VertexToCheck;
import NotaQLGraph.model.vdata.AggregatedEdgeVData;
import NotaQLGraph.model.vdata.NoVData;
import NotaQLGraph.model.vdata.VData;
import com.tinkerpop.blueprints.Vertex;

import java.util.LinkedList;

/**
 * Created by yannick on 25.11.15.
 */
public class PathExistencePredicate implements Predicate {
    String path;
    Source source;
    VData vdata;

    public enum Source {
        NodeName, Edge
    }

    public PathExistencePredicate(String visit, Source name) {//KOnstruktor für Path
        this.path = visit;
        this.source=name;
    }

    public PathExistencePredicate(VData vdata, Source name) {//KOnstruktor für die Edges
        this.vdata=new AggregatedEdgeVData(vdata, AggregatedEdgeVData.Operation.PREDICATE);//noch aggregated mit rein machen, damit ich op = Predicate angeben kann

        this.source=name;
    }

    public boolean evaluate(VertexToCheck vtc) {
        Vertex v = vtc.getVertex();
        // System.out.println("evaluate"+vtc.getProperty());
        if (source == Source.NodeName) {
            if (v.getProperty(path) == null) {
                return false;
            } else {
                if (vtc.getProperty() == null) {//erweiterung für @ operator
                    return true; //so ist es ohne dem @ operator
                } else {
                    //System.out.println("wird gerade gemacht"+(vtc.getProperty().equals(path)));
                    return (vtc.getProperty().equals(path));
                }
            }
        }else if(source==Source.Edge){
            if (vdata.evaluate(vtc) instanceof NoVData){//Wenn ich NOVDATA zurück bekomme, ist es net erfüllt und somit falsch, ansonsten gibt es mind. 1 Kante, die das erfüllt
                return false;
            }else {
                return true;
            }


         }else {
            return false;
        }

    }

    public String[] getLabel() {
        return null;
    }

    public String getDirection() {
        return null;
    }

    public LinkedList<Filter> getAttribute(Vertex v) {
        return null;
    }
}
