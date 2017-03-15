package NotaQLGraph.model.vdata;

/**
 * Created by yannick on 26.11.15.
 */
public abstract class BaseAtomValue<T> implements AtomValue<T>, VData{
    private T value;

    public BaseAtomValue(T value){
        this.value=value;
    }


    public T getValue() {
        return this.value;
    }






}

