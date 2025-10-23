public class Mult extends Binop {
    public double eval(double[] data, Node left, Node right) { return left.eval(data) * right.eval(data); }
    public String toString(Node left, Node right) { return "(" + left.toString() + " * " + right.toString() + ")"; }
}
