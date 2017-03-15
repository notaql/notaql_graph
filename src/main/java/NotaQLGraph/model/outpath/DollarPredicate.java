package NotaQLGraph.model.outpath;

import NotaQLGraph.Evaluation.Filter;
import NotaQLGraph.Evaluation.VertexToCheck;
import NotaQLGraph.model.predicate.Predicate;
import com.tinkerpop.blueprints.Vertex;

import java.util.LinkedList;

/**
 * Created by yannick on 10.12.15.
 */
public class DollarPredicate implements Outpath, Predicate{
    private Predicate predicate;

    public DollarPredicate(Predicate predicate){
        this.predicate = predicate;
    }

    public String getValue() {
        return null;
    }

    public boolean evaluate(VertexToCheck v) {
      // System.out.println(predicate.getClass());
       // System.out.println("evaluate dollarpredicate"+predicate.evaluate(v));
        return predicate.evaluate(v);
    }

    public String[] getLabel() {
        return null;
    }

    public String getDirection() {
        return null;
    }

    public LinkedList<Filter> getAttribute(Vertex v){return null;}
}
