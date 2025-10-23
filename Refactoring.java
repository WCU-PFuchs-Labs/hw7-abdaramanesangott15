import java.util.Random;

public class Refactoring {
    static int numIndepVars = 3;
    static int maxDepth = 5;
    static Random rand = new Random();

    public static void main(String[] args) {
        double[] data = new double[3];
        data[0] = 3.14;
        data[1] = 2.78;
        data[2] = 1.0;

        Binop[] ops = { new Plus(), new Minus(), new Mult(), new Divide() };
        NodeFactory n = new NodeFactory(ops, numIndepVars);

        Node root = n.getOperator(rand);
        root.addRandomKids(n, maxDepth, rand);

        String s = root.toString();
        System.out.println(s + " = " + String.format("%.2f", root.eval(data)));
    }
}

abstract class Op implements Cloneable {
    public abstract double eval(double[] data, Node left, Node right);
    public abstract String toString(Node left, Node right);
    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Op can't clone.");
        }
        return o;
    }
}

abstract class Binop extends Op { }

class Const extends Op {
    private double value;
    public Const(double value) { this.value = value; }
    public double eval(double[] data, Node left, Node right) { return value; }
    public String toString(Node left, Node right) { return String.format("%.2f", value); }
}

class Variable extends Op {
    private int index;
    public Variable(int index) { this.index = index; }
    public double eval(double[] data, Node left, Node right) { return data[index]; }
    public String toString(Node left, Node right) { return "X" + index; }
}

class Plus extends Binop {
    public double eval(double[] data, Node left, Node right) { return left.eval(data) + right.eval(data); }
    public String toString(Node left, Node right) { return "(" + left.toString() + " + " + right.toString() + ")"; }
}

class Minus extends Binop {
    public double eval(double[] data, Node left, Node right) { return left.eval(data) - right.eval(data); }
    public String toString(Node left, Node right) { return "(" + left.toString() + " - " + right.toString() + ")"; }
}

class Mult extends Binop {
    public double eval(double[] data, Node left, Node right) { return left.eval(data) * right.eval(data); }
    public String toString(Node left, Node right) { return "(" + left.toString() + " * " + right.toString() + ")"; }
}

class Divide extends Binop {
    // HW7: Handle near-zero denominator safely
    public double eval(double[] data, Node left, Node right) {
        double denom = right.eval(data);
        if (Math.abs(denom) < 1.0e-4) return 1.0;
        return left.eval(data) / denom;
    }
    public String toString(Node left, Node right) { return "(" + left.toString() + " / " + right.toString() + ")"; }
}

class Node implements Cloneable {
    protected Op operation;
    protected Node left;
    protected Node right;
    protected int depth = 0;

    public Node(Op operation) { this.operation = operation; }
    public double eval(double[] data) { return operation.eval(data, left, right); }
    public String toString() { return operation.toString(left, right); }

    public void addRandomKids(NodeFactory nf, int maxDepth, Random rand) {
        if (!(operation instanceof Binop)) return;
        if (this.depth >= maxDepth) {
            this.left = nf.getTerminal(rand);
            this.left.depth = this.depth + 1;
            this.right = nf.getTerminal(rand);
            this.right.depth = this.depth + 1;
            return;
        }
        int span = nf.getNumOps() + nf.getNumIndepVars();
        int r = rand.nextInt(span + 1);
        this.left = (r < nf.getNumOps()) ? nf.getOperator(rand) : nf.getTerminal(rand);
        this.left.depth = this.depth + 1;
        if (this.left.operation instanceof Binop) this.left.addRandomKids(nf, maxDepth, rand);
        r = rand.nextInt(span + 1);
        this.right = (r < nf.getNumOps()) ? nf.getOperator(rand) : nf.getTerminal(rand);
        this.right.depth = this.depth + 1;
        if (this.right.operation instanceof Binop) this.right.addRandomKids(nf, maxDepth, rand);
    }

    public Object clone() {
        Object o = null;
        try { o = super.clone(); } 
        catch (CloneNotSupportedException e) { System.out.println("Node can't clone."); }
        Node b = (Node) o;
        if (left != null)  b.left = (Node) left.clone();
        if (right != null) b.right = (Node) right.clone();
        if (operation != null) b.operation = (Op) operation.clone();
        return b;
    }

    // ===== HW7 ADDITIONS =====
    public void traverse(Collector c) {
        if (c == null) return;
        c.collect(this);
        if (left  != null) left.traverse(c);
        if (right != null) right.traverse(c);
    }

    public void swapLeft(Node trunk) {
        if (trunk == null) return;
        Node tmp = this.left;
        this.left = trunk.left;
        trunk.left = tmp;
    }

    public void swapRight(Node trunk) {
        if (trunk == null) return;
        Node tmp = this.right;
        this.right = trunk.right;
        trunk.right = tmp;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }
    // =========================
}

class NodeFactory {
    private final int numIndepVars;
    private final Binop[] currentOps;

    public NodeFactory(Binop[] ops, int numVars) {
        if (ops == null || ops.length == 0) throw new IllegalArgumentException("Need at least one Binop");
        if (numVars < 0) throw new IllegalArgumentException("numVars must be >= 0");
        this.currentOps = ops.clone();
        this.numIndepVars = numVars;
    }

    public Node getOperator(Random rand) {
        int idx = rand.nextInt(currentOps.length);
        Op opClone = (Op) currentOps[idx].clone();
        return new Node(opClone);
    }

    public int getNumOps() { return currentOps.length; }

    public Node getTerminal(Random rand) {
        int pick = rand.nextInt(numIndepVars + 1);
        if (pick < numIndepVars) return new Node(new Variable(pick));
        return new Node(new Const(rand.nextDouble()));
    }

    public int getNumIndepVars() { return numIndepVars; }
}
