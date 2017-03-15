package NotaQLGraph.model.outpath;

/**
 * Created by yannick on 02.12.15.
 */
public class NodeID implements Outpath {
    private String outpath;

    public NodeID(String outpath){
        this.outpath=outpath;
    }

    public String getValue() {
        return outpath;
    }


    public boolean equals(Object obj) {
       if (obj instanceof NodeID) {
            return getValue().equals(((NodeID) obj).getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
