package edu.mit.lab.entry;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: Kewill Lab Center</p>
 * <p>Description: edu.mit.lab.entry.Tree</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Kewill Co., Ltd.</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/15/2016
 */
public class Tree<T> {

    private TreeNode<T> root;

    @SuppressWarnings(value = {"unused"})
    public Tree(T seed) {
        this(new TreeNode<>(seed));
    }

    public Tree(TreeNode<T> root) {
        this.root = root;
    }

    /**
     * Walks the Tree in pre-order style. This is a recursive method, and is
     * called from the toList() method with the root element as the first
     * argument. It appends to the second argument, which is passed by reference
     * as it recurse down the tree.
     *
     * @param element the starting element.
     * @param list    the output of the walk.
     */
    private void walk(TreeNode<T> element, List<TreeNode<T>> list) {
        list.add(element);
        for (TreeNode<T> data : element.subtrees()) {
            walk(data, list);
        }
    }

    /**
     * Returns the Tree<T> as a List of TreeNode<T> objects. The elements of the
     * List are generated from a pre-order traversal of the tree.
     *
     * @return a List<TreeNode<T>>.
     */
    @SuppressWarnings(value = {"unused"})
    public List<TreeNode<T>> toList() {
        List<TreeNode<T>> list = new ArrayList<>();
        walk(root, list);
        return list;
    }

    public TreeNode<T> root() {
        return root;
    }
}
