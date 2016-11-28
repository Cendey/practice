package edu.mit.lab.unit;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * <p>Title: MIT Lab Project</p>
 * <p>Description: edu.mit.lab.unit.Tutorial</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@mit.lab>
 * @version 1.0
 * @since 11/17/2016
 */
public class Tutorial {
    public static void main(String args[]) {
        Graph graph = new SingleGraph("Tutorial 1");

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        graph.display();
    }
}
