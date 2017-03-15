package NotaQLGraph.model.predicate;

import NotaQLGraph.Evaluation.Filter;
import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import java.util.LinkedList;

/**
 * Created by yannick on 10.01.16.
 */
public class PathExistencePredicateEdge implements Predicate {
    String path;
    Mode mode;

    public enum Mode {
        EdgeName, EdgeLabel, Direction
    }

    public PathExistencePredicateEdge(String path, Mode m) {
        this.path = path;
        this.mode = m;
    }

    public boolean evaluate(VertexToCheck vtc) {
        Edge e = vtc.getEdge();
       if (mode ==Mode.EdgeName) {
            if (e.getProperty(path) == null) {
                return false;
            } else {
                if (vtc.getProperty() == null) {//erweiterung für @ operator
                    return true; //so ist es ohne dem @ operator
                } else {
                    return (vtc.getProperty().equals(path));
                }
            }
        } else if (mode==Mode.EdgeLabel){

           return (path.substring(1,path.length()-1).equals(e.getLabel()));
        } else if (mode == Mode.Direction){
           if (path.equals("_outgoing")){
               if (e.getVertex(Direction.OUT).equals(vtc.getVertex())){
                   return true ;
               }
               else {return false;}
           }
           else {//_ingoing
               if (e.getVertex(Direction.IN).equals(vtc.getVertex())){
                   return true ;
               }
               else {return false;}
           }

       }

        return false; //Solte eigentlich nie vorkommen
    }

    public String[] getLabel() {
        if (mode==Mode.EdgeLabel){
            String[]a={path.substring(1,path.length()-1)};
            return a;
        }
        return null;
    }

    public String getDirection() {
        if (mode==Mode.Direction){
            return path;
        }
        return null;
    }

    public LinkedList<Filter> getAttribute(Vertex v) {
        return null;
    }
}
