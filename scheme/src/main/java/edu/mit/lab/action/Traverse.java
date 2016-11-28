package edu.mit.lab.action;

import edu.mit.lab.entry.TreeNode;

/**
 * <p>Title: MIT Lab Project</p>
 * <p>Description: edu.mit.lab.action.Traverse</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Kewill Co., Ltd.</p>
 *
 * @author <chao.deng@mit.lab>
 * @version 1.0
 * @since 11/16/2016
 */
public interface Traverse<T extends TreeNode> {

    /**
     * Is called on each node, while traversing the tree
     *
     * @param node reference to the current node during tree traversal
     */
    void perform(T node);

    /**
     * Checks whether the traversal is completed and no more required
     *
     * @return {@code true} if traversal is completed and no more required,
     * {@code false} otherwise
     */
    default boolean isCompleted(){
        return false;
    }
}
