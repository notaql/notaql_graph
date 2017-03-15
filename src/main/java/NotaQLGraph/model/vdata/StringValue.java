package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

/**
 * Created by yannick on 26.11.15.
 */
public class StringValue extends BaseAtomValue<String>{
    public StringValue(String value){
        super(value);
    }

    public VData evaluate(VertexToCheck vtc) {
        return this;
    }



    public int compareTo(StringValue o) {
        return new String(this.getValue()).compareTo(o.getValue()) ;
    }

    @Override
    public String toString(){
        return super.getValue().toString();
    }

   @Override
   public boolean equals( Object o ){
             return (o instanceof  StringValue && ((StringValue) o).getValue().equals(this.getValue()));
    }

    @Override
    public int hashCode(){
        return this.getValue().hashCode();
    }



}
