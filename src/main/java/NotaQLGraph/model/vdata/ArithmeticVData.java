package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;

/**
 * Created by yannick on 26.11.15.
 */
public class ArithmeticVData implements VData {
    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    private  VData left;
    private  VData right;
    private final Operation operator;

    public ArithmeticVData(VData left, VData right, Operation operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public NumberValue evaluate(VertexToCheck vtc) {

        //zuerst Teilbäume berechnen, danach mich selbst
        //if (left instanceof ArithmeticVData){
            VData le=left.evaluate(vtc);
        //}
        //if (right instanceof ArithmeticVData){
            VData ri=right.evaluate(vtc);
        //}
        return calculate(le,ri);
    }

    public NumberValue calculate(VData leftValue, VData rightValue) {
        if(!(leftValue instanceof NumberValue && rightValue instanceof NumberValue )) {

           throw new RuntimeException("Added values must evaluate to a NumberValue. This was not the case."+leftValue.getClass()+"*"+rightValue.getClass());
        }

        return calculate(operator, (NumberValue)leftValue, (NumberValue)rightValue);
    }

    public static NumberValue calculate(Operation operator, NumberValue leftValue, NumberValue rightValue) {
        final double leftDouble = leftValue.getValue().doubleValue();
        final double rightDouble = rightValue.getValue().doubleValue();

        final double sum;
        switch (operator) {
            case ADD:
                sum = leftDouble + rightDouble;
                break;
            case SUBTRACT:
                sum = leftDouble - rightDouble;
                break;
            case MULTIPLY:
                sum = leftDouble * rightDouble;
                break;
            case DIVIDE:
                sum = leftDouble / rightDouble;
                break;
            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }

       // System.out.println("Summe:"+sum);
        return new NumberValue(sum);
    }


}