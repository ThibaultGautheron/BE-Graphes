package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.utils.BinaryHeap;



public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    protected final ArrayList<Label> labels = new ArrayList<Label>();

    private Label getLabelNode(Node e){
        int Id=e.getId();
        return labels.get(Id);
    }

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();

        notifyOriginProcessed(data.getOrigin());
        Arc[] predecessorArcs = new Arc[nbNodes];

        BinaryHeap<Label> tas = new BinaryHeap<Label>();

        for (Node node: graph.getNodes()){
            getLabelNode(node).setMarque(false);
            getLabelNode(node).setCoutRealise(Double.POSITIVE_INFINITY);
            getLabelNode(node).setPere(null);
        }

        int OriginId = data.getOrigin().getId();

        getLabelNode(graph.getNodes().get(OriginId)).setCoutRealise(0);

        tas.insert(getLabelNode(graph.getNodes().get(OriginId)));

        boolean found = false;

        while (!found){
            Label x = tas.deleteMin();
            x.setMarque(true);
            for (Arc arc: x.getSommetCourant().getSuccessors()){
                if(!getLabelNode(arc.getDestination()).getMarque()){
                    double w = data.getCost(arc);
                    if (getLabelNode(arc.getDestination()).getCoutRealise()>x.getCoutRealise()+w){
                        getLabelNode(arc.getDestination()).setCoutRealise(x.getCoutRealise()+w);
                        if (getLabelNode(arc.getDestination()).getVu()){
                            tas.remove(getLabelNode(arc.getDestination()));
                            tas.insert(getLabelNode(arc.getDestination()));
                        }
                        else{
                            tas.insert(getLabelNode(arc.getDestination()));
                            getLabelNode(arc.getDestination()).setVu(true);
                        }
                    }
                }
            }
        }

        if (tas.isEmpty()) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            notifyDestinationReached(data.getDestination());

            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            Collections.reverse(arcs);

            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
        return solution;
    }
    
}
