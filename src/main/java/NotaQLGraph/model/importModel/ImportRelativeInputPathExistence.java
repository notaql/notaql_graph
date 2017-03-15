package NotaQLGraph.model.importModel;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportRelativeInputPathExistence implements Import {
    String any;
    public String importGraphSide() {
        return Util.reduceName(any);
    }

    public String importOtherSide() {
        return "";
                //"OUT."+Util.reduceName(any)+ "<-"+any; ==> normalerweise sollte inputpath immer auf Zielknoten sein

    }

    public ImportRelativeInputPathExistence(String any){
        this.any=any;
    }
}
