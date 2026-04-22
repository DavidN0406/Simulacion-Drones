package modelo;

import java.util.*;

public class Grafo {

    public Map<Nodo, List<Arista>> adyacencias = new HashMap<>();

    public void agregarNodo(Nodo n) {
        adyacencias.putIfAbsent(n, new ArrayList<>());
    }

    public void agregarArista(Nodo origen, Nodo destino, double peso) {
        adyacencias.get(origen).add(new Arista(destino, peso));
        adyacencias.get(destino).add(new Arista(origen, peso));
    }

    public List<Arista> obtenerVecinos(Nodo n) {
        return adyacencias.get(n);
    }
}