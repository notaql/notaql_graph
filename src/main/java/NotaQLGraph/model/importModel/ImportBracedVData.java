package NotaQLGraph.model.importModel;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportBracedVData implements Import {
    Import predicate;

    public String importGraphSide() {
        return "("+predicate.importGraphSide()+")";
    }

    public String importOtherSide() {
        return predicate.importOtherSide();
    }


    public ImportBracedVData(Import p){
        this.predicate=p;

    }
}
