package NotaQLGraph.Database;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

/**
 * Created by yannick on 21.01.16.
 */
public class TitanGraphDatabase extends Database {
    public TitanGraphDatabase(String path) {
        //TODO DAS GEHT NOCH NICHT; ERST MAL ANDERE SACHEN MACHEN....
        super();
        this.path=path;
        Configuration conf = new BaseConfiguration();

        conf.setProperty("storage.directory","/tmp/titan2");
       /* TitanGraph g =  TitanFactory.open(conf);
        this.graph=g;
      //  graph = new IdGraph(g);
       // ((IdGraph)graph).setVertexIdFactory(new MyIdFactory());
*/
    }

    @Override
    public void commit() {

    }
}
