package NotaQLGraph.model.predicate;

import NotaQLGraph.Evaluation.Filter;
import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Vertex;

import java.util.LinkedList;

/**
 * Created by yannick on 25.11.15.
 */
public interface Predicate {
    public boolean evaluate(VertexToCheck vtc);
    public String[] getLabel();
    public String getDirection();
    public LinkedList<Filter> getAttribute(Vertex v); //Gibt eine Liste mit {gibt ein Key zurück, sowie eine Liste von Values}
}
