package edu.mit.lab.entry;

import edu.mit.lab.action.Traverse;
import edu.mit.lab.exception.NodeException;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>Title: Kewill Lab Center</p>
 * <p>Description: edu.mit.lab.entry.TreeNode</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Kewill Co., Ltd.</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/15/2016
 */
public class TreeNode<T> implements Cloneable, Serializable {

    /**
     * Identifier generator, used to get a unique id for each created tree node
     */
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    /**
     * A unique identifier, used to distinguish or compare the tree nodes
     */
    private final long id = ID_GENERATOR.getAndIncrement();

    private TreeNode<T> parent;
    private List<TreeNode<T>> children;
    private T data;

    /**
     * Creates an instance of this class
     *
     * @param data data to store in the current tree node
     */
    public TreeNode(T data) {
        this.data = data;
    }

    /**
     * Returns the collection of the child nodes of the current node
     * with all of its proper descendants, if any
     * <p>
     * Returns {@link Collections#emptySet()} if the current node is leaf
     *
     * @return collection of the child nodes of the current node with
     * all of its proper descendants, if any;
     * {@link Collections#emptySet()} if the current node is leaf
     */
    public Collection<? extends TreeNode<T>> subtrees() {
        return children;
    }

    /**
     * Adds the subtree with all of its descendants to the current tree node
     * <p>
     * {@code null} subtree cannot be added, in this case return result will
     * be {@code false}
     * <p>
     * Checks whether this tree node was changed as a result of the call
     *
     * @param data subtree to add to the current tree node
     * @return {@code true} if this tree node was changed as a
     * result of the call; {@code false} otherwise
     */
    public boolean add(T data) {
        return add(new TreeNode<>(data));
    }

    /**
     * Adds the subtree with all of its descendants to the current tree node
     * <p>
     * {@code null} subtree cannot be added, in this case return result will
     * be {@code false}
     * <p>
     * Checks whether this tree node was changed as a result of the call
     *
     * @param subtree subtree to add to the current tree node
     * @return {@code true} if this tree node was changed as a
     * result of the call; {@code false} otherwise
     */
    public boolean add(TreeNode<T> subtree) {
        if (subtree != null) {
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(subtree);
            subtree.parent = this;
            return true;
        }
        return false;
    }

    /**
     * Returns the parent node of the current node
     * <p>
     * Returns {@code null} if the current node is root
     *
     * @return parent node of the current node; {@code null}
     * if the current node is root
     */
    public TreeNode<T> parent() {
        return parent;
    }

    /**
     * Returns the data object stored in the current tree node
     *
     * @return data object stored in the current tree node
     */
    public T data() {
        return data;
    }

    /**
     * Searches the tree node within the tree, which has the specified data,
     * starting from the current tree node and returns the first occurrence of it
     *
     * @param data data to find the tree node with
     * @return first occurrence of the searched tree node with data specified
     */
    @SuppressWarnings("unchecked")
    public TreeNode<T> find(final T data) {
        if (isLeaf()) {
            return (data() == null ? data == null : data().equals(data)) ? this : null;
        }
        final TreeNode<T>[] searchedNode = (TreeNode<T>[]) Array.newInstance(getClass(), 1);
        traversePreOrder(new Traverse<TreeNode<T>>() {
            @Override
            public void perform(TreeNode<T> node) {
                if ((
                    node.data() == null ?
                        data == null : node.data().equals(data))) {
                    searchedNode[0] = node;
                }
            }

            @Override
            public boolean isCompleted() {
                return searchedNode[0] != null;
            }
        });
        return searchedNode[0];
    }

    /**
     * Searches the tree nodes within the tree, which have the specified data,
     * starting from the current tree node and returns the collection of the found
     * tree nodes
     *
     * @param data data to find the tree nodes with
     * @return collection of the searched tree nodes with data specified
     */
    @SuppressWarnings(value = {"unused"})
    public Collection<? extends TreeNode<T>> findAll(final T data) {
        if (isLeaf()) {
            return (data() == null ? data == null : data().equals(data)) ?
                Collections.singleton(this) : Collections.emptySet();
        }
        final Collection<TreeNode<T>> searchedNodes = new HashSet<>();
        traversePreOrder(node -> {
            if ((
                node.data() == null ?
                    data == null : node.data().equals(data))) {
                searchedNodes.add(node);
            }
        });
        return searchedNodes;
    }

    /**
     * Returns the root node of the current node
     * <p>
     * Returns itself if the current node is root
     *
     * @return root node of the current node; itself,
     * if the current node is root
     */
    public TreeNode<T> root() {
        if (isRoot()) {
            return this;
        }

        TreeNode<T> node = this;
        do {
            node = node.parent();
        } while (!node.isRoot());

        return node;
    }

    /**
     * Checks whether the current tree node is the root of the tree
     *
     * @return {@code true} if the current tree node is root of the tree;
     * {@code false} otherwise
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Traverses the tree in a pre ordered manner starting from the
     * current tree node and performs the traversal action on each
     * traversed tree node
     *
     * @param action action, which is to be performed on each tree
     *               node, while traversing the tree
     */
    private void traversePreOrder(Traverse<TreeNode<T>> action) {
        if (!action.isCompleted()) {
            action.perform(this);
            if (!isLeaf()) {
                for (TreeNode<T> subtree : subtrees()) {
                    subtree.traversePreOrder(action);
                }
            }
        }
    }

    /**
     * Traverses the tree in a post ordered manner starting from the
     * current tree node and performs the traversal action on each
     * traversed tree node
     *
     * @param action action, which is to be performed on each tree
     *               node, while traversing the tree
     */
    private void traversePostOrder(Traverse<TreeNode<T>> action) {
        if (!action.isCompleted()) {
            if (!isLeaf()) {
                for (TreeNode<T> subtree : subtrees()) {
                    subtree.traversePostOrder(action);
                }
            }
            action.perform(this);
        }
    }


    /**
     * Populates the input collection with the tree nodes, while traversing the tree
     *
     * @param collection input collection to populate
     * @param <T>        type of the tree node
     * @return traversal action, which populates the input collection with the tree nodes
     */
    private static <T> Traverse<TreeNode<T>> populateAction(final Collection<TreeNode<T>> collection) {
        return collection::add;
    }

    /**
     * Returns the pre ordered collection of nodes of the current tree
     * starting from the current tree node
     *
     * @return pre ordered collection of nodes of the current tree starting
     * from the current tree node
     */
    @SuppressWarnings(value = {"unused"})
    public Collection<TreeNode<T>> preOrdered() {
        if (isLeaf()) {
            return Collections.singleton(this);
        }
        final Collection<TreeNode<T>> mPreOrdered = new ArrayList<>();
        Traverse<TreeNode<T>> action = populateAction(mPreOrdered);
        traversePreOrder(action);
        return mPreOrdered;
    }

    /**
     * Returns the post ordered collection of nodes of the current tree
     * starting from the current tree node
     *
     * @return post ordered collection of nodes of the current tree starting
     * from the current tree node
     */
    @SuppressWarnings(value = {"unused"})
    public Collection<TreeNode<T>> postOrdered() {
        if (isLeaf()) {
            return Collections.singleton(this);
        }
        final Collection<TreeNode<T>> mPostOrdered = new ArrayList<>();
        Traverse<TreeNode<T>> action = populateAction(mPostOrdered);
        traversePostOrder(action);
        return mPostOrdered;
    }

    /**
     * Checks whether the current tree node is a leaf, e.g. does not have any
     * subtrees
     *
     * @return {@code true} if the current tree node is a leaf, e.g. does not
     * have any subtrees; {@code false} otherwise
     */
    public boolean isLeaf() {
        return CollectionUtils.isEmpty(subtrees());
    }

    /**
     * Returns the collection of nodes, which connect the current node
     * with its descendants
     *
     * @param descendant the bottom child node for which the path is calculated
     * @return collection of nodes, which connect the current node with its descendants
     * @throws NodeException exception that may be thrown in case if the
     *                       current node does not have such descendant or if the
     *                       specified tree node is root
     */
    @SuppressWarnings(value = {"unused"})
    public Collection<? extends TreeNode<T>> path(TreeNode<T> descendant) {
        if (descendant == null
            || isLeaf()
            || this.equals(descendant)) {
            return Collections.singletonList(this);
        }
        String errorMessage = "Unable to build the path between tree nodes. ";
        if (descendant.isRoot()) {
            String message = String.format(errorMessage + "Current node %1$s is root", descendant);
            throw new NodeException(message);
        }
        List<TreeNode<T>> path = new LinkedList<>();
        TreeNode<T> node = descendant;
        path.add(node);
        do {
            node = node.parent();
            path.add(0, node);
            if (this.equals(node)) {
                return path;
            }
        } while (!node.isRoot());
        String message = String.format(errorMessage +
            "The specified tree node %1$s is not the descendant of tree node %2$s", descendant, this);
        throw new NodeException(message);
    }

    /**
     * Checks whether the current tree node with all of its descendants
     * (entire tree) contains the specified node
     *
     * @param node node whose presence within the current tree node with
     *             all of its descendants (entire tree) is to be checked
     * @return {@code true} if the current node with all of its descendants
     * (entire tree) contains the specified node; {@code false}
     * otherwise
     */
    private boolean contains(TreeNode<T> node) {
        if (node == null
            || isLeaf()
            || node.isRoot()) {
            return false;
        }
        for (TreeNode<T> subtree : subtrees()) {
            if (subtree.equals(node)
                || subtree.contains(node)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the common ancestor of the current node and the node specified
     *
     * @param node node, which the common ancestor is determined for,
     *             along with the current node
     * @return common ancestor of the current node and the node specified
     * @throws NodeException exception that may be thrown in case if the
     *                       specified tree node is null or the specified tree node
     *                       does not belong to the current tree or if any of the tree
     *                       nodes either the current one or the specified one is root
     */
    @SuppressWarnings(value = {"unused"})
    public TreeNode<T> commonAncestor(TreeNode<T> node) {
        String errorMessage = "Unable to find the common ancestor between tree nodes. ";
        if (node == null) {
            String message = errorMessage + "The specified tree node is null";
            throw new NodeException(message);
        }
        if (!this.root().contains(node)) {
            String message = String.format(errorMessage +
                "The specified tree node %1$s was not found in the current tree node %2$s", node, this);
            throw new NodeException(message);
        }
        if (this.isRoot()
            || node.isRoot()) {
            String message = String.format(errorMessage + "The tree node %1$s is root", this.isRoot() ? this : node);
            throw new NodeException(message);
        }
        if (this.equals(node)
            || node.isSiblingOf(this)) {
            return parent();
        }
        int thisNodeLevel = this.level();
        int thatNodeLevel = node.level();
        return thisNodeLevel > thatNodeLevel ? node.parent() : this.parent();
    }

    /**
     * Checks whether the current tree node is a sibling of the specified node,
     * e.g. whether the current tree node and the specified one both have the
     * same parent
     *
     * @param node node, which sibling with the current tree node is to be checked
     * @return {@code true} if the current tree node is a sibling of the specified
     * node, e.g. whether the current tree node and the specified one both
     * have the same parent; {@code false} otherwise
     */
    private boolean isSiblingOf(TreeNode<T> node) {
        return node != null
            && !isRoot()
            && !node.isRoot()
            && this.parent().equals(node.parent());
    }

    /**
     * Checks whether the current tree node is the ancestor of the node specified
     *
     * @param node node, which is checked to be the descendant of the current tree
     *             node
     * @return {@code true} if the current tree node is the ancestor of the node
     * specified; {@code false} otherwise
     */
    @SuppressWarnings(value = {"unused"})
    public boolean isAncestorOf(TreeNode<T> node) {
        if (node == null
            || isLeaf()
            || node.isRoot()
            || this.equals(node)) {
            return false;
        }
        TreeNode<T> mNode = node;
        do {
            mNode = mNode.parent();
            if (this.equals(mNode)) {
                return true;
            }
        } while (!mNode.isRoot());
        return false;
    }

    /**
     * Checks whether the current tree node is the descendant of the node specified
     *
     * @param node node, which is checked to be the ancestor of the current tree
     *             node
     * @return {@code true} if the current tree node is the ancestor of the node
     * specified; {@code false} otherwise
     */
    @SuppressWarnings(value = "unused")
    public boolean isDescendantOf(TreeNode<T> node) {
        if (node == null
            || this.isRoot()
            || node.isLeaf()
            || this.equals(node)) {
            return false;
        }
        TreeNode<T> mNode = this;
        do {
            mNode = mNode.parent();
            if (node.equals(mNode)) {
                return true;
            }
        } while (!mNode.isRoot());
        return false;
    }

    /**
     * Returns the number of nodes in the entire tree, including the current tree node
     *
     * @return number of nodes in the entire tree, including the current tree node
     */
    @SuppressWarnings(value = {"unused"})
    public long size() {
        if (isLeaf()) {
            return 1;
        }
        final long[] count = {0};
        Traverse<TreeNode<T>> action = node -> count[0]++;
        traversePreOrder(action);
        return count[0];
    }

    /**
     * Returns the height of the current tree node, e.g. the number of edges
     * on the longest downward path between that node and a leaf
     *
     * @return height of the current tree node, e.g. the number of edges
     * on the longest downward path between that node and a leaf
     */
    @SuppressWarnings(value = {"unused"})
    private int height() {
        if (isLeaf()) {
            return 0;
        }
        int height = 0;
        for (TreeNode<T> subtree : subtrees()) {
            height = Math.max(height, subtree.height());
        }
        return height + 1;
    }

    /**
     * Returns the depth (level) of the current tree node within the entire tree,
     * e.g. the number of edges between the root tree node and the current one
     *
     * @return depth (level) of the current tree node within the entire tree,
     * e.g. the number of edges between the root tree node and the current
     * one
     */
    private int level() {
        if (isRoot()) {
            return 0;
        }
        int level = 0;
        TreeNode<T> node = this;
        do {
            node = node.parent();
            level++;
        } while (!node.isRoot());
        return level;
    }

    /**
     * Creates and returns a copy of this object
     *
     * @return a clone of this instance
     */
    @SuppressWarnings("unchecked")
    @Override
    public TreeNode<T> clone() {
        try {
            return (TreeNode<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            String message = "Unable to clone the current tree node";
            throw new NodeException(message, e);
        }
    }

    /**
     * Returns the string representation of this object
     *
     * @return string representation of this object
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\n");
        final int topNodeLevel = level();
        Traverse<TreeNode<T>> action = node -> {
            int nodeLevel = node.level() - topNodeLevel;
            for (int i = 0; i < nodeLevel; i++) {
                builder.append("|  ");
            }
            builder
                .append("+- ")
                .append(node.data())
                .append("\n");
        };
        traversePreOrder(action);
        return builder.toString();
    }

    /**
     * Indicates whether some object equals to this one
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null
            || getClass() != obj.getClass()) {
            return false;
        }
        TreeNode<T> that = (TreeNode<T>) obj;
        return this.id == that.id;
    }

    /**
     * Returns the hash code value of this object
     *
     * @return hash code value of this object
     */
    @Override
    public int hashCode() {
        return (int) (this.id ^ (this.id >>> 32));
    }
}
