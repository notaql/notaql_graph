package NotaQLGraph.model.importModel;

import java.util.LinkedList;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportEdgeConstructorLabel implements Import {
    String name;
    Import label;

    public String importGraphSide() {
        return  name+"<-" +label.importGraphSide()  ;
    }

    public String importOtherSide() {
        if (label instanceof ImportInputData){
            return label.importOtherSide();
        }
        else {
            return "";
        }
    }

    public ImportEdgeConstructorLabel(String d, Import l){this.name=d;this.label=l; }
}
