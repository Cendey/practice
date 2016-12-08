package edu.mit.lab.utils;

import edu.mit.lab.constant.Scheme;
import edu.mit.lab.entry.Tree;
import edu.mit.lab.entry.TreeNode;
import edu.mit.lab.infts.IRelevance;
import edu.mit.lab.meta.Keys;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.graph.DepthFirstIterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.utils.Toolkit</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 11/23/2016
 */
public class Toolkit {

    private static final String EXCLUDED_PATTERN =
        "^(?:FM|[CV](?=TRF)|VC|CC|SY|IFM|MSG|WORK(?=(FLOW|_EFFORT))|STRUCTURE|WF)\\w+|\\w+(?:ACCESS|SET(?:UP|TING)|TEMPLATE)$";
    public static final Pattern INSTANCE = Pattern.compile(EXCLUDED_PATTERN);

    private static <T> void processNode(Graph graph, TreeNode<T> node) {
        String nodeId = Scheme.NODE_PREFIX + String.valueOf(node.data());
        graph.addNode(nodeId);
        org.graphstream.graph.Node currentNode = graph.getNode(nodeId);
        currentNode.setAttribute(Scheme.UI_LABEL, node.data());
        if (!node.isRoot()) {
            String edgeId = Scheme.EDGE_PREFIX + String.valueOf(node.data()) + String.valueOf(node.parent().data());
            graph.addEdge(edgeId, nodeId, Scheme.NODE_PREFIX + String.valueOf(node.parent().data()), true);
        }
    }

    @SuppressWarnings(value = {"unused"})
    private static <T> void walk(Graph graph, TreeNode<T> node) {
        processNode(graph, node);
        if (!node.isLeaf()) {
            node.subtrees().forEach(subtree -> walk(graph, subtree));
        }
    }

    @SuppressWarnings(value = "unused")
    public static List<Tree<String>> buildTableTree(List<Keys> lstFKRef) {
        //Remember processed tables position which corresponding to position in the list of nodes
        Map<String, TreeNode<String>> index = new WeakHashMap<>();
        List<TreeNode<String>> nodes = new LinkedList<>();
        if (!CollectionUtils.isEmpty(lstFKRef)) {
            lstFKRef.forEach(item -> {
                TreeNode<String> ownNode = MapUtils.getObject(index, item.getPkTableName());
                TreeNode<String> refNode = MapUtils.getObject(index, item.getFkTableName());
                if (ownNode != null && refNode != null) {
                    if (!ownNode.equals(refNode)) {
                        TreeNode<String> root = ownNode.root();
                        TreeNode<String> target = root.find(item.getPkTableName());
                        target.add(refNode);
                        refNode.preOrdered().forEach(node -> index.put(node.data(), root));
                        nodes.remove(refNode);
                    }
                } else if (ownNode != null) {
                    TreeNode<String> root = ownNode.root();
                    TreeNode<String> target = root.find(item.getPkTableName());
                    target.add(item.getFkTableName());
                    index.put(item.getFkTableName(), root);
                } else {
                    TreeNode<String> elem = new TreeNode<>(item.getPkTableName());
                    if (refNode != null) {
                        TreeNode<String> root = refNode.root();
                        elem.add(root);
                        index.put(item.getPkTableName(), elem);
                        root.preOrdered().forEach(node -> index.put(node.data(), elem));
                        nodes.remove(root);
                        nodes.add(elem);
                    } else {
                        elem.add(item.getFkTableName());
                        nodes.add(elem);
                        index.put(item.getPkTableName(), elem);
                        index.put(item.getFkTableName(), elem);
                    }
                }
            });
        }
        List<Tree<String>> trees = new ArrayList<>(nodes.size());
        if (!CollectionUtils.isEmpty(nodes)) {
            nodes.forEach(node -> {
                Tree<String> tree = new Tree<>(node);
                trees.add(tree);
            });
        }
        return trees;
    }

    /**
     * <summary>This is the method which adjust the sizes of the nodes.</summary>
     *
     * @param graph   <p>Refer the graph</p>
     * @param minSize <p>Minimum size of noe</p>
     * @param maxSize <p>Maximum size of node</p>
     */
    public static void nodeSize(Graph graph, int minSize, int maxSize) {
        int smaller = -1;
        int greater = -1;
        for (Node n : graph.getEachNode()) {
            if (n.getDegree() > greater || smaller == -1)
                greater = n.getDegree();
            if (n.getDegree() < smaller || greater == -1)
                smaller = n.getDegree();
        }
        for (Node n : graph.getEachNode()) {
            double scale = (double) (n.getDegree() - smaller) / (double) (greater - smaller);
            if (null != n.getAttribute("ui.style")) {
                n.setAttribute(
                    "ui.style",
                    n.getAttribute("ui.style") + " size:" + Math.round((scale * maxSize) + minSize) + "px;");
            } else {
                n.addAttribute("ui.style", " size:" + Math.round((scale * maxSize) + minSize) + "px;");
            }
        }
    }

    public static SortedSet<String> resolveDisconnectedGraph(Graph graph) {
        SortedSet<String> graphIds = new TreeSet<>(Comparator.naturalOrder());
        ConnectedComponents components = new ConnectedComponents(graph);
        components.setCountAttribute(Scheme.COMPONENT);
        components.compute();
        for (ConnectedComponents.ConnectedComponent component : components) {
            StringBuilder ids = new StringBuilder();
            for (Iterator<Edge> iterator = component.getEdgeIterator(); iterator.hasNext(); ) {
                Edge edge = iterator.next();
                CssUtility.handleStyle(edge);
                Node target = edge.getTargetNode();
                Node source = edge.getSourceNode();
                CssUtility.handleStyle(target, source);
                if (!target.hasAttribute(Scheme.DATA_PARENT) && ids.indexOf(target.getId()) == -1) {
                    ids.append("|").append(target.getId());
                }
            }
            graphIds.add(StringUtils.removeFirst(ids.toString(), "[|]"));
        }
        return graphIds;
    }

    public static int height(Node node) {
        int height = 0;
        if (node.getDegree() == 0) {
            return 0;
        }

        Iterator<Node> iterator = node.getDepthFirstIterator(false);
        while (iterator.hasNext()) {
            iterator.next();
        }
        if (DepthFirstIterator.class.isAssignableFrom(iterator.getClass())) {
            height = DepthFirstIterator.class.cast(iterator).getDepthMax();
        }
        return height;
    }

    public static void addNodeInfo(IRelevance<String, List<String>> item, Node target, Node source) {
        if (!target.hasAttribute(Scheme.UI_LABEL)) {
            target.addAttribute(Scheme.UI_LABEL, item.to());
        }
        if (!target.hasAttribute(Scheme.DATA_CHILD)) {
            target.addAttribute(Scheme.DATA_CHILD, new ArrayList<String>());
        }
        target.<List<String>>getAttribute(Scheme.DATA_CHILD).add(source.getId());

        if (!source.hasAttribute(Scheme.UI_LABEL)) {
            source.addAttribute(Scheme.UI_LABEL, item.from());
        }
        if (!source.hasAttribute(Scheme.DATA_PARENT)) {
            source.addAttribute(Scheme.DATA_PARENT, new ArrayList<String>());
        }
        source.<List<String>>getAttribute(Scheme.DATA_PARENT).add(target.getId());
    }

    public static void addEdgeInfo(IRelevance<String, List<String>> item, Edge edge) {
        edge.addAttribute(Scheme.UI_LABEL, item.get(Scheme.FK_COLUMN_NAME).toString().replaceAll(",", " | "));
    }

    private static void perform(Node root, Collection<String> tableIds, StringBuilder script) {
        String tableName = StringUtils.removeFirst(root.getId(), Scheme.NODE_PREFIX);
        boolean exclusive = INSTANCE.matcher(tableName).matches();
        String format = (exclusive ? "--" : "") + "delete from  %s;\r\n";
        script.append(String.format(format, StringUtils.lowerCase(tableName)));
        root.addAttribute(Scheme.ACTION_MARKED, Scheme.IDENTIFIED);
        tableIds.remove(tableName);
    }

    @SuppressWarnings(value = {"unused"})
    private static void preTraverse(Graph graph, Node root, Collection<String> tableIds, StringBuilder script) {
        perform(root, tableIds, script);

        List<String> children = root.getAttribute(Scheme.DATA_CHILD);
        if (!CollectionUtils.isEmpty(children)) {
            for (String id : children) {
                Node node = graph.getNode(id);
                if (!Scheme.STATUS.contains(node.<String>getAttribute(Scheme.ACTION_MARKED))) {
                    node.addAttribute(Scheme.ACTION_MARKED, Scheme.MARKED);
                    preTraverse(graph, node, tableIds, script);
                }
            }
        }
    }

    public static void postTraverse(Graph graph, Node root, Collection<String> tableIds, StringBuilder script) {
        List<String> children = root.getAttribute(Scheme.DATA_CHILD);
        if (!CollectionUtils.isEmpty(children)) {
            for (String id : children) {
                Node node = graph.getNode(id);
                if (!Scheme.STATUS.contains(node.<String>getAttribute(Scheme.ACTION_MARKED))) {
                    node.addAttribute(Scheme.ACTION_MARKED, Scheme.MARKED);
                    postTraverse(graph, node, tableIds, script);
                }
            }
        }

        perform(root, tableIds, script);
    }
}
