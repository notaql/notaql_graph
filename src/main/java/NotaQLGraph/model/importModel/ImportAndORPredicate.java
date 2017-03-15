package NotaQLGraph.model.importModel;

import java.util.LinkedList;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportAndORPredicate implements Import {
    String operation;
    Import right;
    Import left;
    public String importGraphSide() {
        return left.importGraphSide()+operation+right.importGraphSide();
    }

    public String importOtherSide() {
        String l = left.importOtherSide();
        String r = right.importOtherSide();
        if (!(l.equals(""))&&!(r.equals(""))){
            return l+", "+r;
        }
        else {
            return l+r;
        }
    }

    public ImportAndORPredicate( Import l,String d, Import r){this.operation=d;this.left=l;this.right=r;
    }
}
