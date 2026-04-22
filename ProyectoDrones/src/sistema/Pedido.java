package sistema;

import modelo.Nodo;

public class Pedido {

    public Nodo destino;
    public boolean entregado;

    public Pedido(Nodo destino) {
        this.destino = destino;
        this.entregado = false;
    }
}