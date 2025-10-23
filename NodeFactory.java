import java.util.Random;

public class NodeFactory {
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
