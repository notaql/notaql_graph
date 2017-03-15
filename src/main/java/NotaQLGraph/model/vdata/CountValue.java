package NotaQLGraph.model.vdata;

/**
 * Created by yannick on 07.01.16.
 *Für das Aggregieren bei AggregatedEdgeVDATa muss man wissen,
 * ob es sich um einen Counter handelt oder um tatsächliche Werte.
 * Counter kann man nur erhalten, wenn man über 2 Kanten springt
 */
public class CountValue extends NumberValue {
        public CountValue(Number value) {
        super(value);

    }
}
