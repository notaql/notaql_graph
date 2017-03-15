package NotaQLGraph.model.importModel;

import java.util.LinkedList;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportEdgeConstructor implements Import {
    String direction;
    Import label;
    LinkedList<Import> list;
    public String importGraphSide() {
        String result = label.importGraphSide();
        for (int i = 0;i<list.size();i++){
           result = result +","+ list.get(i).importGraphSide();
        }
        return "EDGE("+ direction +", " + result+")";
             //   'EDGE(' direction ',' edgeConstructorLabel (',' edgeConstructorAttribute)* ')'  ;
    }

    public String importOtherSide() {
        String result ="";
        for (int i=0;i<list.size();i++){
            result = result +","+ list.get(i).importOtherSide();

        }

        return label.importOtherSide()+result;
    }

    public ImportEdgeConstructor(String d, Import l, LinkedList<Import> list){this.direction=d;this.label=l;this.list=list;
    }
}
