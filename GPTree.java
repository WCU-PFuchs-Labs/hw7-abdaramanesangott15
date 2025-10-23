import java.util.ArrayList;
import java.util.Random;

public class GPTree implements Collector {
    private Node root;
    private ArrayList<Node> crossNodes;

  
    public void collect(Node node) {
        if (node == null) return;
        if (!node.isLeaf()) crossNodes.add(node);
    }

    public void traverse() {
        crossNodes = new ArrayList<>();
        root.traverse(this);
    }

    public String getCrossNodes() {
        StringBuilder sb = new StringBuilder();
        int last = crossNodes.size() - 1;
        for (int i = 0; i < last; i++) {
            sb.append(crossNodes.get(i).toString()).append(";");
        }
        sb.append(crossNodes.get(last));
        return sb.toString();
    }

    public void crossover(GPTree other, Random rand) {
        this.traverse();
        other.traverse();
        int p1 = rand.nextInt(this.crossNodes.size());
        int p2 = rand.nextInt(other.crossNodes.size());
        boolean left = rand.nextBoolean();
        Node t1 = this.crossNodes.get(p1);
        Node t2 = other.crossNodes.get(p2);
        if (left) t1.swapLeft(t2);
        else      t1.swapRight(t2);
    }

    public GPTree() { root = null; }

    public GPTree(NodeFactory nf, int maxDepth, Random rand) {
        root = nf.getOperator(rand);
        root.addRandomKids(nf, maxDepth, rand);
    }

    public String toString() { return root.toString(); }

    public double eval(double[] data) { return root.eval(data); }
}
