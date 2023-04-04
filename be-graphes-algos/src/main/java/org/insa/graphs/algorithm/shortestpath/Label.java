package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label {
    private Node sommet_courant;
    private boolean marque;
    private double cout_realise;
    private Arc pere;

    public Label(Node sommet, boolean marque, double cout, Arc pere){
        this.sommet_courant = sommet;
        this.marque = marque;
        this.cout_realise = cout;
        this.pere = pere;
    }

    public Node getSommetCourant(){
        return this.sommet_courant;
    }

    public boolean getMarque(){
        return this.marque;
    }

    public double getCoutRealise(){
        return this.cout_realise;
    }

    public Arc getPere(){
        return this.pere;
    }

    public double getCost(){
        return this.getCoutRealise();
    }

    public void setMarque(boolean a){
        this.marque = a;
    }

    public void setCoutRealise(double b){
        this.cout_realise = b;
    }

    public void setPere(Arc pere){
        this.pere = pere;
    }

}
