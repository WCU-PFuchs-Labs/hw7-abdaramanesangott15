public class Const extends Op {
    private final double value;
    public Const(double value) { this.value = value; }
    public double eval(double[] data, Node left, Node right) { return value; }
    public String toString(Node left, Node right) { return String.format("%.2f", value); }
}
