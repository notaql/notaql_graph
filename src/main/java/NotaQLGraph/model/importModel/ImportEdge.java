package NotaQLGraph.model.importModel;

import java.util.LinkedList;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportEdge implements Import {
    Import predicate;
    Import edgeConstructor;
    public String importGraphSide() {

        return "OUT._e_?("+predicate.importGraphSide() + ") <-"+edgeConstructor.importGraphSide();
    }

    public String importOtherSide() {
        String left = predicate.importOtherSide();
        String right = edgeConstructor.importOtherSide();
        if (!(left.equals(""))&&!(right.equals(""))){
            return left +","+ right;
        }
        else return left+right;
    }

    public ImportEdge(Import p, Import e){
        this.predicate=p;
        this.edgeConstructor=e;
    }
}
