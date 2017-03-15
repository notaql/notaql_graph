package NotaQLGraph.model.importModel;

/**
 * Created by yannick on 12.02.16.
 */
public class Util {

    public static String reduceName(String name){
        String wert = name;
        wert=wert.replace("[","ob");
        wert=wert.replace("]","cb");
        wert=wert.replace("*","star");
        wert=wert.replace("@","at");
        wert=wert.replace("(","o");
        wert=wert.replace(")","c");
        wert=wert.replace("?","q");



        return wert+"_tmp_edge"; //TODO: hier muss sowas wie $ @ etc rausgefiltert werden
    }
}
