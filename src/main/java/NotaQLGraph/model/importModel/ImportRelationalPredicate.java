package NotaQLGraph.model.importModel;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportRelationalPredicate implements Import {
    String operation;
    Import right;
    Import left;
    public String importGraphSide() {
        return left.importGraphSide()+operation+right.importGraphSide();
    }

    public String importOtherSide() {
        return left.importOtherSide()+right.importOtherSide();
    }

    public ImportRelationalPredicate(Import l, String d, Import r){this.operation=d;this.left=l;this.right=r;
    }
}
