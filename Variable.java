public class Variable extends Op {
    private final int index;
    public Variable(int index) { this.index = index; }
    public double eval(double[] data, Node left, Node right) { return data[index]; }
    public String toString(Node left, Node right) { return "X" + index; }
}
