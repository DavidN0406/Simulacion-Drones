package modelo;

public class Arista {
    public Nodo destino;
    public double peso;

    public Arista(Nodo destino, double peso) {
        this.destino = destino;
        this.peso = peso;
    }
}