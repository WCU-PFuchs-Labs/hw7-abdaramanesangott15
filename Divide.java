public class Divide extends Binop {
    // HW7: handle near-zero divisor by returning 1.0
    public double eval(double[] data, Node left, Node right) {
        double denom = right.eval(data);
        if (Math.abs(denom) < 1.0e-4) return 1.0;
        return left.eval(data) / denom;
    }
    public String toString(Node left, Node right) { return "(" + left.toString() + " / " + right.toString() + ")"; }
}
