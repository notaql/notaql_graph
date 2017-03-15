package NotaQLGraph.model.predicate;

import NotaQLGraph.Evaluation.Filter;
import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.lang.ArrayUtils;

import java.util.LinkedList;

/**
 * Created by yannick on 25.11.15.
 */
public class LogicalOperationPredicate implements Predicate {
   private Predicate left;
    private Predicate right;
    private Operator operator;

    public LogicalOperationPredicate(Predicate visit, Predicate visit1, Operator operator) {
        this.left=visit;
        this.right= visit1;
        this.operator=operator;
    }

    public boolean evaluate(VertexToCheck v) {
        switch (operator) {
            case AND:
                return left.evaluate(v) && right.evaluate(v);
            case OR:
                return left.evaluate(v) || right.evaluate(v);
            default:
              throw new RuntimeException("logical operation predicate, default fall");

        }


    }

    public String[] getLabel() {
       return (String[])ArrayUtils.addAll(left.getLabel(), right.getLabel());
    }

    public String getDirection() {
        String a = left.getDirection();
        String b = right.getDirection();
        if (operator==Operator.AND){
            return (a==null)?b:a;
        }
        return null; //Problem (bei or wird es schwer
    }

    public LinkedList<Filter> getAttribute(Vertex v) {
        LinkedList<Filter> a = left.getAttribute(v);
        LinkedList<Filter> b = right.getAttribute(v);
        if (operator==Operator.AND){
            if (a==null){
                return b;
            }
            else if (b==null) {
                return a;
            }
            else {
                LinkedList<Filter> result = new LinkedList<Filter>();
                result.addAll(a);
                result.addAll(b);
                return result;
            }
        }
        return null;
    }

    public enum Operator {
        AND, OR
    }
}
