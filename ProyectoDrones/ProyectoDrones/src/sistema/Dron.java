package sistema;

import modelo.Nodo;

public class Dron {

    public Nodo posicion;
    public double bateria;
    public boolean ocupado;
    public Pedido pedido;

    public Dron(Nodo posicion) {
        this.posicion = posicion;
        this.bateria = 100;
        this.ocupado = false;
    }
}