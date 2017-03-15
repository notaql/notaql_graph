package NotaQLGraph.model.importModel;

import java.util.LinkedList;

/**
 * Created by yannick on 11.02.16.
 */
public class ImportTransformation implements Import {
    LinkedList<Import> list;
    public String importGraphSide() {
        String result="";
       for (int i=0;i<list.size();i++) {
           String neu = list.get(i).importGraphSide();
            result=result+neu;
            if (!(neu.equals(""))&&i<list.size()-1){
                result = result +",";
            }
        }
        return result;
    }

    public String importOtherSide() {
        String result="";
        for (int i=0;i<list.size();i++) {
            String neu = list.get(i).importOtherSide();
            result=result+ neu;
            if (!(neu.equals(""))&&i<list.size()-1){
                result = result +",";
            }
        }
        return result;
    };


    public ImportTransformation (LinkedList<Import> list){
        this.list=list;
    }
}
