package NotaQLGraph.Database;

/**
 * Klasse die erzeugt wurde um die geöffneten Datenbankverbindungen während einer Transaktion zu verwalten
 * Created by yannick on 03.02.16.
 */
public class OpenConnections {
    Database in = null;
    Database out = null;
    boolean inMem = false;      //zum schauen ob in memory datenbank => kein shutdown
    boolean outMem = false;     //zum schauen ob in memory datenbank => kein shutdown
    Database inmemoryDB = null;


    public void setIn(Database in, boolean inmemory) {
        if (inmemory==false) {
            this.in = in;
            inMem=false;
        }
        else{
            if (inmemoryDB==null){
                inmemoryDB = new TinkerGraphDatabase();
            }
            this.in=inmemoryDB;
            inMem=true;
        }
    }

    public void setOut(Database out, boolean inmemory) {
        if (inmemory==false) {
            this.out = out;
            outMem=false;
        }
        else{
            if (inmemoryDB==null){
                inmemoryDB = new TinkerGraphDatabase();
            }
            this.out=inmemoryDB;
            outMem=true;
        }
    }

    public Database getIn() {
        return in;
    }

    public Database getOut() {
        return out;
    }

    public void shutdown(){
        if (!inMem) {
            in.getDatabase().shutdown();
        }
        if (!(in.equals(out))) {
            if (!outMem) {
                out.getDatabase().shutdown();
            }
        }
    }

    public void shutdownInMemory(){
        shutdown();
        inmemoryDB.getDatabase().shutdown();
    }

    public void printOut(){
        out.printGraph();
    }
    public void printIn(){
        in.printGraph();
    }

    public void refresh() {
        in = null;
        inMem=false;
        outMem=false;
        out= null;
    }
}
