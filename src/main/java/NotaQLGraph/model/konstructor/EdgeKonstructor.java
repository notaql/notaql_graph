package NotaQLGraph.model.konstructor;

import NotaQLGraph.Evaluation.VertexToCheck;
import NotaQLGraph.model.vdata.NoVData;
import NotaQLGraph.model.vdata.VData;
import com.tinkerpop.blueprints.Direction;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;

/**
 * Created by yannick on 19.01.16.
 */
public class EdgeKonstructor implements VData {
    Direction direction;
    VData label;
    LinkedList<VData> attributes;
    public EdgeKonstructor(String direction, VData label, LinkedList<VData> attributes) {
        if (direction.equals("_outgoing")){
            this.direction= Direction.OUT;
        }else {
            this.direction=Direction.IN;
        }
        this.label=label;
        this.attributes=attributes;
    }

    public VData evaluate(VertexToCheck vtc) {
        return null;
    }

    public Direction getDirection() {
        return direction;
    }

    public VData getLabel() {
        return label;
    }

    public LinkedList<VData> getAttributes() {
        return attributes;
    }
}
