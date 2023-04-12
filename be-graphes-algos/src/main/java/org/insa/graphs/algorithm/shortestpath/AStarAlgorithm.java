package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.utils.BinaryHeap;

public class AStarAlgorithm extends DijkstraAlgorithm {

    protected final ArrayList<LabelStar> labels = new ArrayList<LabelStar>();

    private LabelStar getLabelStarNode(Node e){
        int Id=e.getId();
        return labels.get(Id);
    }

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        for (Node node: data.getGraph().getNodes()){
            labels.add(new LabelStar(node, false, Double.POSITIVE_INFINITY, null));
        }
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

        int OriginId = data.getOrigin().getId();

        getLabelNode(graph.getNodes().get(OriginId)).setCoutRealise(0);

        tas.insert(getLabelNode(graph.getNodes().get(OriginId)));
        LabelStar x;
        x=getLabelStarNode(data.getOrigin());
        while (x.getSommetCourant()!=data.getDestination()){
            x = getLabelStarNode(tas.deleteMin().getSommetCourant());
            x.setMarque(true);
            for (Arc arc: x.getSommetCourant().getSuccessors()){
                if(!getLabelStarNode(arc.getDestination()).getMarque()){
                    double w = data.getCost(arc);
                    if (getLabelStarNode(arc.getDestination()).getTotalCost()>x.getTotalCost()+w){
                        if (getLabelStarNode(arc.getDestination()).getVu()){
                            tas.remove(getLabelNode(arc.getDestination()));
                            getLabelStarNode(arc.getDestination()).setCoutDestination(x.getTotalCost()+w);                    
                            tas.insert(getLabelNode(arc.getDestination()));
                            getLabelStarNode(arc.getDestination()).setPere(arc);
                        }
                        else{
                            getLabelStarNode(arc.getDestination()).setCoutDestination(x.getCoutRealise()+w);
                            tas.insert(getLabelNode(arc.getDestination()));
                            getLabelStarNode(arc.getDestination()).setVu(true);
                            notifyNodeReached(getLabelStarNode(arc.getDestination()).getSommetCourant());
                            getLabelStarNode(arc.getDestination()).setPere(arc);
                        }
                        predecessorArcs[arc.getDestination().getId()]=arc;
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
