package NotaQLGraph.model.predicate;

import NotaQLGraph.Evaluation.Filter;
import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Vertex;

import java.util.LinkedList;

/**
 * Created by yannick on 25.11.15.
 */
public class NegatedPredicate implements Predicate {
    private Predicate predicate;
    public NegatedPredicate(Predicate visit) {
        this.predicate=visit;
    }

    public boolean evaluate(VertexToCheck v) {
        return !predicate.evaluate(v);

    }

    public String[] getLabel() {
        return null; //Wird schwierig wenn da steht "!kennt" und ich dann kennt zurück geben, deshalb kann ich eigetlich nix zurückgeben
    }

    public String getDirection() {
        return null;
    }
    public LinkedList<Filter> getAttribute(Vertex vertex) {
        return null;
    }
}
