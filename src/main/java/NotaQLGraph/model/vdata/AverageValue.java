package NotaQLGraph.model.vdata;

/**
 * Created by yannick on 07.01.16.
 */
public class AverageValue extends NumberValue {
    private int count;
    public AverageValue(Number value, int count) {
        super(value);
        this.count=count;
    }


    public int getCounter() {
        return count;
    }
}
