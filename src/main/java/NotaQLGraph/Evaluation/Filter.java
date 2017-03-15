package NotaQLGraph.Evaluation;

import java.util.LinkedList;

/**
 * Created by yannick on 04.03.16.
 * Wird erzeugt zum Vorfiltern
 * wird verwendet um die Keys und die passenden Values abzulegen
 */
public class Filter {
    String key =null;
    LinkedList<Object> value = new LinkedList<Object>();
    boolean collectionIsEmpty = false; //wenn ich beim Import z.b. bei FOllowing eine leere Liste hab, dann will ich dem Filter sagen dass er nach überhaupt keinem Element suchen soll.

    public Filter (boolean f){
        collectionIsEmpty=f;
    }

    public Filter(LinkedList<Object> o, String k){
        this.key=k;
        value=o;
    }

    public Filter(String k, Object o){
        this.key=k;
        value = new LinkedList<Object>();
        value.add(o);
    }

    public String getKey(){
        return key;
    }

    public LinkedList<Object> getValue(){
        return value;
    }

    public int getSize(){
        return value.size();
    }

    public boolean isCollectionIsEmpty(){
        return collectionIsEmpty;
    }

}
