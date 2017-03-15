package NotaQLGraph.model.importModel;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportInputDataDestination implements Import {
    String any;
    public String importGraphSide() {
        return any;
    }

    public String importOtherSide() {
        return "";
    }

    public ImportInputDataDestination(String any){
        this.any=any;
    }
}
