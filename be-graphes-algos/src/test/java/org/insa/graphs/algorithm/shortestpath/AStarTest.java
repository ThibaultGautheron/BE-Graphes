package org.insa.graphs.algorithm.shortestpath;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;

public class AStarTest extends DijkstraAlgorithmTest {
	
	@BeforeClass
    public static void initAll() throws Exception{
		
		//get graph of map carré
		String mapName = "/home/tgauther/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr";
		GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		graph = reader.read();
		//get graph of map insa
		mapName = "/home/tgauther/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
		reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		graph_insa = reader.read();
		
		//init OneNodePath
		Node node = graph.getNodes().get(0);
		oneNodePath = new Path(graph, node);
		
		ShortestPathData data = new ShortestPathData(graph, node, node, ArcInspectorFactory.getAllFilters().get(0));
		AStarAlgorithm Astar = new AStarAlgorithm(data);
		oneNodeSolution = Astar.doRun();
		
		//Init simple path
		cost = 0;
		for (int i = 5; i < 9; i++) {
			for (Arc a : graph.getNodes().get(i).getSuccessors()) {
				if (a.getDestination() == graph.getNodes().get(i+1)) {
					cost += a.getLength();
					break;
				}
			}
		}
		
		data = new ShortestPathData(graph, graph.getNodes().get(5), graph.getNodes().get(9), ArcInspectorFactory.getAllFilters().get(0));
		Astar = new AStarAlgorithm(data);
		simplePathSolution = Astar.doRun();
		
		//Init medium path
		data = new ShortestPathData(graph_insa, graph_insa.getNodes().get(805), graph_insa.getNodes().get(70), ArcInspectorFactory.getAllFilters().get(0));
		Astar = new AStarAlgorithm(data);
		mediumPathSolution = Astar.doRun();
		
		BellmanFordAlgorithm BF = new BellmanFordAlgorithm(data);
		BFmediumSolution = BF.doRun();
		
		//Init Infeasible path
		data = new ShortestPathData(graph_insa, graph_insa.getNodes().get(75), graph_insa.getNodes().get(1255), ArcInspectorFactory.getAllFilters().get(0));
		Astar = new AStarAlgorithm(data);
		InfeasiblePathSolution = Astar.doRun();
	}
}
