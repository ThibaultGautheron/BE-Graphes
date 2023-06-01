package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;


public class AStarTest extends DijkstraAlgorithmTest {
	
	@BeforeClass
    public static void initAll() throws Exception{
		
		
		String mapName = "/home/tgauther/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr";
		GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		graph = reader.read();
		
		mapName = "/home/tgauther/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
		reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		graph_insa = reader.read();
		
		//Initialisation oneNodePath
		Node node = graph.getNodes().get(0);
		oneNodePath = new Path(graph, node);
		
		ShortestPathData data = new ShortestPathData(graph, node, node, ArcInspectorFactory.getAllFilters().get(0));
		AStarAlgorithm Astar = new AStarAlgorithm(data);
		oneNodeSolution = Astar.doRun();
		
		//Initialisation shortPath
		
		data = new ShortestPathData(graph, graph.getNodes().get(16), graph.getNodes().get(23), ArcInspectorFactory.getAllFilters().get(0));
		Astar = new AStarAlgorithm(data);
		shortPathSolution = Astar.doRun();
		BellmanFordAlgorithm BF = new BellmanFordAlgorithm(data);
		BFshortSolution = BF.doRun();
		
		//Initialisation mediumPath
		data = new ShortestPathData(graph_insa, graph_insa.getNodes().get(215), graph_insa.getNodes().get(915), ArcInspectorFactory.getAllFilters().get(0));
		Astar = new AStarAlgorithm(data);
		mediumPathSolution = Astar.doRun();
		
		BF = new BellmanFordAlgorithm(data);
		BFmediumSolution = BF.doRun();
		
		//Initialisation InfeasiblePath
		data = new ShortestPathData(graph_insa, graph_insa.getNodes().get(215), graph_insa.getNodes().get(1172), ArcInspectorFactory.getAllFilters().get(0));
		Astar = new AStarAlgorithm(data);
		InfeasiblePathSolution = Astar.doRun();
	}

	@Test
	public void TestOneNodePath() {
		assertTrue(Math.abs(oneNodePath.getLength() - oneNodeSolution.getPath().getLength() )< 0.01);
		assertTrue(oneNodeSolution.getPath().isValid());
	}

	@Test
	public void TestShortPath() {
		assertTrue(Math.abs(BFshortSolution.getPath().getLength() - shortPathSolution.getPath().getLength()) < 0.01);
		assertTrue(shortPathSolution.getPath().isValid());
	}
	
	@Test
	public void TestMediumPath() {
		assertTrue(Math.abs(mediumPathSolution.getPath().getLength() - BFmediumSolution.getPath().getLength()) < 0.01);
		assertTrue(mediumPathSolution.getPath().isValid());
	}
	
	@Test
	public void TestInfeasiblePath() {
		assertEquals(InfeasiblePathSolution.getStatus(), AbstractSolution.Status.INFEASIBLE);
	}
	
}
