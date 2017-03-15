package NotaQLGraph.model.importModel;
import NotaQLGraph.model.importModel.Import;


/**
 * Created by yannick on 11.02.16.
 */
public class ImportEdgeConstructorAttribute implements Import {
    String name;
    Import attribute;

    public String importGraphSide() {
        return name+"<-"+attribute.importGraphSide();
    }

    public String importOtherSide() {
        return attribute.importOtherSide();
    }

    public ImportEdgeConstructorAttribute(String d, Import l){this.name=d;this.attribute=l;
    }
}
