package edu.mit.lab.utils;

import edu.mit.lab.constant.Scheme;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 * <p>Title: MIT Lab Project</p>
 * <p>Description: edu.mit.lab.utils.CssUtility</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@mit.lab>
 * @version 1.0
 * @since 11/18/2016
 */
class CssUtility {

    static void handleStyle(Node target, Node source) {
        if (!target.hasAttribute(Scheme.DATA_PARENT)) {
            target.addAttribute(Scheme.UI_CLASS, Scheme.ROOT);
        }
        if (!source.hasAttribute(Scheme.DATA_CHILD)) {
            source.addAttribute(Scheme.UI_CLASS, Scheme.LEAF);
        }
    }

    static void handleStyle(Edge edge) {
        if (!edge.getTargetNode().hasAttribute(Scheme.DATA_PARENT)) {
            edge.addAttribute(Scheme.UI_CLASS, Scheme.ROOT);
        } else {
            if (!edge.getSourceNode().hasAttribute(Scheme.DATA_CHILD)) {
                edge.addAttribute(Scheme.UI_CLASS, Scheme.LEAF);
            }
        }
    }
}
