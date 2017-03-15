package NotaQLGraph.Database;

/**
 * Created by yannick on 21.01.16.
 */
public class TinkerGraphDatabase extends Database {
    public TinkerGraphDatabase(String path) {
        super();
        this.path=path;
        this.graph = new com.tinkerpop.blueprints.impls.tg.TinkerGraph(path);
    }
    public TinkerGraphDatabase() {

        super();
        this.path="/tmp/"+String.valueOf((Math.random()*1000)); //in dem Fall nur interessant zum printen
        this.graph = new com.tinkerpop.blueprints.impls.tg.TinkerGraph();
    }

    @Override
    public void commit() {

    }
}
