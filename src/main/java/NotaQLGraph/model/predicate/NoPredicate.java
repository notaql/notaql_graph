package NotaQLGraph.model.predicate;

import NotaQLGraph.Evaluation.Filter;
import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Vertex;

import java.util.LinkedList;

/**
 * Created by yannick on 08.01.16.
 * ==> Kein Predikat vorhanden, deshalb wird true zurückgegeben (Filter erfüllt)
 */
public class NoPredicate implements Predicate {

    public boolean evaluate(VertexToCheck v) {
        return true;
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
