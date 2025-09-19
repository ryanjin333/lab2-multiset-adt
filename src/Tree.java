import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Objects;

public class Tree {
    // === Private Attributes ===
    // The item stored at this tree's root, or null if the tree is empty.
    private Integer _root;
    // The list of all subtrees of this tree.
    private final List<Tree> _subtrees;

    private static final Random random = new Random();

    // === Representation Invariants ===
    // - If this._root is null then this._subtrees is an empty list.
    //   This setting of attributes represents an empty tree.
    //
    //   Note: this._subtrees may be empty when this._root is not null.
    //   This setting of attributes represents a tree consisting of just one node.

    // Constructors: allow root and subtrees to be null (like Python None)
    public Tree(Integer root, List<Tree> subtrees) {
        this._root = root;
        if (subtrees == null) {
            this._subtrees = new ArrayList<>();
        } else {
            this._subtrees = new ArrayList<>(subtrees);
        }
    }

    public Tree(Integer root) {
        this(root, null);
    }

    public Tree() {
        this(null, null);
    }

    public boolean is_empty() {
        return this._root == null;
    }

    public int __len__() {
        if (this.is_empty()) {
            return 0;
        } else {
            int size = 1; // count the root
            for (Tree subtree : this._subtrees) {
                size += subtree.__len__();
            }
            return size;
        }
    }

    public int count(int item) {
        if (this.is_empty()) {
            return 0;
        } else {
            int num = 0;
            if (Objects.equals(this._root, item)) {
                num += 1;
            }
            for (Tree subtree : this._subtrees) {
                num += subtree.count(item);
            }
            return num;
        }
    }

    public String __str__() {
        return this._str_indented(0);
    }

    private String _str_indented(int depth) {
        if (this.is_empty()) {
            return "";
        } else {
            StringBuilder s = new StringBuilder("  ".repeat(depth) + this._root + "\n");
            for (Tree subtree : this._subtrees) {
                s.append(subtree._str_indented(depth + 1));
            }
            return s.toString();
        }
    }

    public double average() {
        if (this.is_empty()) {
            return 0.0;
        } else {
            int[] result = this._average_helper();
            int total = result[0];
            int count = result[1];
            return (double) total / count;
        }
    }

    private int[] _average_helper() {
        if (this.is_empty()) {
            return new int[]{0, 0};
        } else {
            int total = this._root;
            int size = 1;
            for (Tree subtree : this._subtrees) {
                int[] sub = subtree._average_helper();
                total += sub[0];
                size += sub[1];
            }
            return new int[]{total, size};
        }
    }

    public boolean __eq__(Tree other) {
        if (other == null) return false;
        if (this.is_empty() && other.is_empty()) {
            return true;
        } else if (this.is_empty() || other.is_empty()) {
            return false;
        } else {
            if (!Objects.equals(this._root, other._root)) {
                return false;
            }
            if (this._subtrees.size() != other._subtrees.size()) {
                return false;
            }
            return this._subtrees.equals(other._subtrees);
        }
    }

    public boolean __contains__(int item) {
        if (this.is_empty()) {
            return false;
        }
        if (Objects.equals(this._root, item)) {
            return true;
        } else {
            for (Tree subtree : this._subtrees) {
                if (subtree.__contains__(item)) {
                    return true;
                }
            }
            return false;
        }
    }

    public List<Integer> leaves() {
        if (this.is_empty()) {
            return new ArrayList<>();
        } else if (this._subtrees.isEmpty()) {
            List<Integer> leaf = new ArrayList<>();
            leaf.add(this._root);
            return leaf;
        } else {
            List<Integer> leaves = new ArrayList<>();
            for (Tree subtree : this._subtrees) {
                leaves.addAll(subtree.leaves());
            }
            return leaves;
        }
    }

    // -------------------------------------------------------------------------
    // Mutating methods
    // -------------------------------------------------------------------------
    public boolean delete_item(int item) {
        if (this.is_empty()) {
            return false;
        } else if (Objects.equals(this._root, item)) {
            this._delete_root();
            return true;
        } else {
            for (Tree subtree : new ArrayList<>(this._subtrees)) {
                boolean deleted = subtree.delete_item(item);
                if (deleted && subtree.is_empty()) {
                    this._subtrees.remove(subtree);
                    return true;
                } else if (deleted) {
                    return true;
                }
            }
            return false;
        }
    }

    private void _delete_root() {
        if (this._subtrees.isEmpty()) {
            this._root = null;
        } else {
            Tree chosen_subtree = this._subtrees.removeLast();
            this._root = chosen_subtree._root;
            this._subtrees.addAll(chosen_subtree._subtrees);
        }
    }

    private int _extract_leaf() {
        if (this._subtrees.isEmpty()) {
            int old_root = this._root;
            this._root = null;
            return old_root;
        } else {
            int leaf = this._subtrees.getFirst()._extract_leaf();
            if (this._subtrees.getFirst().is_empty()) {
                this._subtrees.removeFirst();
            }
            return leaf;
        }
    }

    public void insert(int item) {
        if (this.is_empty()) {
            this._root = item;
        } else if (this._subtrees.isEmpty()) {
            this._subtrees.add(new Tree(item, null));
        } else {
            if (random.nextInt(3) + 1 == 3) {
                this._subtrees.add(new Tree(item, null));
            } else {
                int idx = random.nextInt(this._subtrees.size());
                this._subtrees.get(idx).insert(item);
            }
        }
    }

    public boolean insert_child(int item, int parent) {
        if (this.is_empty()) {
            return false;
        } else if (Objects.equals(this._root, parent)) {
            this._subtrees.add(new Tree(item, null));
            return true;
        } else {
            for (Tree subtree : this._subtrees) {
                if (subtree.insert_child(item, parent)) {
                    return true;
                }
            }
            return false;
        }
    }
}