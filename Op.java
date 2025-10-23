public abstract class Op implements Cloneable {
    public abstract double eval(double[] data, Node left, Node right);
    public abstract String toString(Node left, Node right);


    public Object clone() {
        try { return super.clone(); }
        catch (CloneNotSupportedException e) {
            throw new AssertionError("Op can't clone.");
        }
    }
}
