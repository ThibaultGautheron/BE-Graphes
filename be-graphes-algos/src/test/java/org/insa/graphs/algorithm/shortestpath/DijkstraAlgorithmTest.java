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


public class DijkstraAlgorithmTest {
	
	protected static Graph graph, graph_insa, graph_France;
	
	protected static Path oneNodePath;
	
	protected static ShortestPathSolution oneNodeSolution, shortPathSolution, mediumPathSolution,BFshortSolution, BFmediumSolution, InfeasiblePathSolution;
	
	protected static double cost;
	
	@BeforeClass
    public static void initAll() throws Exception{
		
		//get graph of carré
		String mapName = "/home/tgauther/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr";
		GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		graph = reader.read();
		//get graph of insa
		mapName = "/home/tgauther/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
		reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		graph_insa = reader.read();
	
		//Initialisation oneNodePath
		Node node = graph.getNodes().get(0);
		oneNodePath = new Path(graph, node);
		
		ShortestPathData data = new ShortestPathData(graph, node, node, ArcInspectorFactory.getAllFilters().get(0));
		DijkstraAlgorithm Dijkstra = new AStarAlgorithm(data);
		oneNodeSolution = Dijkstra.doRun();

		//Initialisation shortPath
		
		data = new ShortestPathData(graph, graph.getNodes().get(16), graph.getNodes().get(23), ArcInspectorFactory.getAllFilters().get(0));
		Dijkstra = new DijkstraAlgorithm(data);
		shortPathSolution = Dijkstra.doRun();
		BellmanFordAlgorithm BF = new BellmanFordAlgorithm(data);
		BFshortSolution = BF.doRun();
		
		//Initialisation mediumPath
		data = new ShortestPathData(graph_insa, graph_insa.getNodes().get(215), graph_insa.getNodes().get(915), ArcInspectorFactory.getAllFilters().get(0));
		Dijkstra = new DijkstraAlgorithm(data);
		mediumPathSolution = Dijkstra.doRun();
		
		BF = new BellmanFordAlgorithm(data);
		BFmediumSolution = BF.doRun();
		
		//Initialisation InfeasiblePath
		data = new ShortestPathData(graph_insa, graph_insa.getNodes().get(215), graph_insa.getNodes().get(1172), ArcInspectorFactory.getAllFilters().get(0));
		Dijkstra = new DijkstraAlgorithm(data);
		InfeasiblePathSolution = Dijkstra.doRun();
		
		
		
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