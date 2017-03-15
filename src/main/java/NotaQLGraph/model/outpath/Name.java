package NotaQLGraph.model.outpath;

/**
 * Created by yannick on 02.12.15.
 */
public class Name implements  Outpath{
   private String name;
    public Name(String outpath){
        this.name=outpath;
    }

    public String getValue() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Name) {
            return getValue().equals(((NodeID) obj).getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
