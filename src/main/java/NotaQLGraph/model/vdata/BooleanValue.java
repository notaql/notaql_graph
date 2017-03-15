package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

/**
 * Created by yannick on 26.11.15.
 */
public class BooleanValue extends BaseAtomValue<Boolean> {
    public BooleanValue(Boolean value) {
        super(value);
    }
    public VData evaluate(VertexToCheck vertex) {
        return this;
    }


    @Override
    public String toString(){
        return super.toString();
    }


    @Override
    public boolean equals( Object o ){
           return (o instanceof  BooleanValue && ((BooleanValue) o).getValue().equals(this.getValue()));
    }

    @Override
    public int hashCode(){
        return this.getValue().hashCode();
    }
}
