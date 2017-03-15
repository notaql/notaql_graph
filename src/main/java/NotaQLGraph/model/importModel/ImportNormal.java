package NotaQLGraph.model.importModel;

import java.util.LinkedList;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportNormal implements Import {
    String any;
    public String importGraphSide() {
        return "";
    }

    public String importOtherSide() {
        return "OUT."+any;
    }

    public ImportNormal(String any){
        this.any=any;
    }
}
