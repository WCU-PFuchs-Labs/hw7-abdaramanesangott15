import java.util.Random;

public class Node implements Cloneable {
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
        try {
            Node b = (Node) super.clone();
            if (left != null)  b.left = (Node) left.clone();
            if (right != null) b.right = (Node) right.clone();
            if (operation != null) b.operation = (Op) operation.clone();
            return b;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Node can't clone.");
        }
    }

    // ===== HW7 additions =====
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
}
