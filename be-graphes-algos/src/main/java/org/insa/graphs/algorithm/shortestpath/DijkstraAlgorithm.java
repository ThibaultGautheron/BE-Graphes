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

    protected Label getLabelNode(Node e){
        int Id=e.getId();
        return labels.get(Id);
    }

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        for (Node node: data.getGraph().getNodes()){
            labels.add(new Label(node, false, Double.POSITIVE_INFINITY, null));
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
        Label x;
        x=getLabelNode(data.getOrigin());
        while (x.getSommetCourant()!=data.getDestination()){
            x = tas.deleteMin();
            x.setMarque(true);
            for (Arc arc: x.getSommetCourant().getSuccessors()){
                if (data.isAllowed(arc)){
                    if(!getLabelNode(arc.getDestination()).getMarque()){
                        double w = data.getCost(arc);
                        if (getLabelNode(arc.getDestination()).getCoutRealise()>x.getCoutRealise()+w){
                            if (getLabelNode(arc.getDestination()).getVu()){
                                tas.remove(getLabelNode(arc.getDestination()));
                                getLabelNode(arc.getDestination()).setCoutRealise(x.getCoutRealise()+w);                    
                                tas.insert(getLabelNode(arc.getDestination()));
                                getLabelNode(arc.getDestination()).setPere(arc);
                            }
                            else{
                                getLabelNode(arc.getDestination()).setCoutRealise(x.getCoutRealise()+w);
                                tas.insert(getLabelNode(arc.getDestination()));
                                getLabelNode(arc.getDestination()).setVu(true);
                                notifyNodeReached(getLabelNode(arc.getDestination()).getSommetCourant());
                                getLabelNode(arc.getDestination()).setPere(arc);
                            }
                            predecessorArcs[arc.getDestination().getId()]=arc;
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
