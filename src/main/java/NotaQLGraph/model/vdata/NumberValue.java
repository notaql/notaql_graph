package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

/**
 * Created by yannick on 26.11.15.
 */
public class NumberValue extends BaseAtomValue<Number> implements Comparable<NumberValue> {

    public NumberValue(Number value){
        super(value);
    };

    public VData evaluate(VertexToCheck vertex) {
        return this;
    }

    public int compareTo(NumberValue o) {
        return new Double(getValue().doubleValue()).compareTo(o.getValue().doubleValue());
    }
    public String toString(){
        return super.getValue().toString();
    }

    @Override
    public boolean equals( Object o ){
        return (o instanceof  NumberValue && ((NumberValue) o).getValue().equals(this.getValue()));
    }

    @Override
    public int hashCode(){
        return this.getValue().hashCode();
    }
}
