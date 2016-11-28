package edu.mit.lab.unit;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Iterator;

/**
 * <p>Title: MIT Lab Project</p>
 * <p>Description: edu.mit.lab.unit.GraphExplore</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@mit.lab>
 * @version 1.0
 * @since 11/24/2016
 */
public class GraphExplore {

    public static void main(String args[]) {
        new GraphExplore();
    }

    private GraphExplore() {
        Graph graph = new SingleGraph("tutorial 1");

        String styleSheet = "node {" +
            "	fill-color: black;" +
            "}" +
            "node.marked {" +
            "	fill-color: red;" +
            "}";
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.display();

        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.addEdge("AD", "A", "D");
        graph.addEdge("DE", "D", "E");
        graph.addEdge("DF", "D", "F");
        graph.addEdge("EF", "E", "F");

        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }

        explore(graph.getNode("A"));
    }

    private void explore(Node source) {
        Iterator<? extends Node> k = source.getBreadthFirstIterator();

        while (k.hasNext()) {
            Node next = k.next();
            next.setAttribute("ui.class", "marked");
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
