package NotaQLGraph.model.importModel;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportRelativePathExistencePredicate implements Import {
    Import predicate;

    public String importGraphSide() {
        return predicate.importOtherSide();
    }

    public String importOtherSide() {return predicate.importOtherSide();
    }

    public ImportRelativePathExistencePredicate(Import p){
        this.predicate=p;

    }
}
