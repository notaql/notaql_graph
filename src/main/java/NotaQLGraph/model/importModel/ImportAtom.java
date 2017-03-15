package NotaQLGraph.model.importModel;

import java.util.LinkedList;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportAtom implements Import {
   String atom;
    public String importGraphSide() {
        return atom;
    }

    public String importOtherSide() {
        return "";
    }

    public ImportAtom(String a){
        this.atom=a;
    }
}
