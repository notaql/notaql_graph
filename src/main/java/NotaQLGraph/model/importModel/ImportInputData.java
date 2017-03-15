package NotaQLGraph.model.importModel;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportInputData implements Import {
    String any;
    public String importGraphSide() {
        return "IN."+Util.reduceName(any);
    }

    public String importOtherSide() {
        return "OUT."+Util.reduceName(any)+ "<-"+"IN."+any;
    }

    public ImportInputData(String any){
        this.any=any;
    }


}
