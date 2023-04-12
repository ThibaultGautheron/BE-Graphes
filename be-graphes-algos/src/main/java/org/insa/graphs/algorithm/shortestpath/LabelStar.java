package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{
    private double cout_destination;

    public LabelStar(Node sommet, boolean marque, double cout, Arc pere){
        super(sommet,marque,cout,pere);
    }

    public double getTotalCost(){
        return this.getCoutRealise() + this.cout_destination;
    }

    public void setCoutDestination(double d){
        this.cout_destination = d;
    }

    public double getCoutDestination(){
        return this.cout_destination;
    }
}
