package simulacion;

import modelo.*;
import sistema.*;
import java.util.*;

public class Simulacion {

    public static void main(String[] args) {

        System.out.println("===== SIMULACIÓN DE DRONES =====");

        // Escenarios de prueba
        ejecutarSimulacion("ESCENARIO 1: Condiciones normales", 100, false);
        ejecutarSimulacion("ESCENARIO 2: Batería baja", 30, false);
        ejecutarSimulacion("ESCENARIO 3: Clima adverso", 100, true);
    }

    public static void ejecutarSimulacion(String titulo, double bateriaInicial, boolean climaInicial) {

        System.out.println("\n==============================");
        System.out.println(titulo);
        System.out.println("==============================");

        Grafo grafo = new Grafo();

        // Nodos
        Nodo CEDI = new Nodo("CEDI");
        Nodo N1 = new Nodo("N1");
        Nodo N2 = new Nodo("N2");
        Nodo N3 = new Nodo("N3");
        Nodo N4 = new Nodo("N4");
        Nodo N5 = new Nodo("N5");

        Nodo Nav1 = new Nodo("Nav1");
        Nodo Nav2 = new Nodo("Nav2");
        Nodo Nav3 = new Nodo("Nav3");
        Nodo Nav4 = new Nodo("Nav4");
        Nodo Nav5 = new Nodo("Nav5");
        Nodo Nav6 = new Nodo("Nav6");

        // Agregar nodos
        Nodo[] todos = {CEDI, N1, N2, N3, N4, N5, Nav1, Nav2, Nav3, Nav4, Nav5, Nav6};
        for (Nodo n : todos) grafo.agregarNodo(n);

        // Aristas 
        grafo.agregarArista(N1, Nav5, 1.4);
        grafo.agregarArista(N1, Nav1, 4.5);
        grafo.agregarArista(N1, CEDI, 2.3);
        grafo.agregarArista(N2, Nav1, 1.5);
        grafo.agregarArista(N2, Nav2, 1.6);
        grafo.agregarArista(Nav1, CEDI, 2.1);
        grafo.agregarArista(Nav1, Nav2, 1.7);
        grafo.agregarArista(Nav2, N3, 2.0);
        grafo.agregarArista(Nav2, Nav3, 2.7);
        grafo.agregarArista(N3, Nav3, 2.0);
        grafo.agregarArista(Nav3, N4, 2.0);
        grafo.agregarArista(Nav3, CEDI, 1.5);
        grafo.agregarArista(N4, Nav4, 4.0);
        grafo.agregarArista(Nav4, N5, 3.0);
        grafo.agregarArista(Nav4, Nav6, 1.3);
        grafo.agregarArista(Nav5, N5, 1.4);
        grafo.agregarArista(Nav5, Nav6, 4.8);
        grafo.agregarArista(Nav6, CEDI, 0.8);

        // Drones
        List<Dron> drones = new ArrayList<>();
        drones.add(new Dron(CEDI));
        drones.add(new Dron(CEDI));

        // Ajustar batería inicial
        for (Dron d : drones) {
            d.bateria = bateriaInicial;
        }

        List<Pedido> pedidos = new ArrayList<>();
        Random rand = new Random();

        Nodo[] destinos = {N1, N2, N3, N4, N5};

        boolean climaAdverso = climaInicial;
        boolean zonaRestringida = false;

        int entregas = 0;

        // Simulación
        for (int t = 0; t < 10; t++) {

            System.out.println("\nTiempo: " + t);

            // Generar pedidos
            if (t % 3 == 0) {
                Nodo destino = destinos[rand.nextInt(destinos.length)];
                pedidos.add(new Pedido(destino));
                System.out.println("Nuevo pedido " + destino.nombre);
            }

            // Cambios de clima
            if (t % 4 == 0) {
                climaAdverso = !climaAdverso;
                System.out.println("Clima: " + (climaAdverso ? "ADVERSO" : "NORMAL"));
            }

            // Zona restringida
            if (t % 5 == 0) {
                zonaRestringida = !zonaRestringida;
                System.out.println("Zona restringida activa: " + zonaRestringida);
            }

            for (Dron dron : drones) {

                System.out.println("Dron en " + dron.posicion.nombre + " | batería: " + dron.bateria);

                // Asignar pedido
                if (!dron.ocupado && !pedidos.isEmpty()) {
                    dron.pedido = pedidos.remove(0);
                    dron.ocupado = true;
                    System.out.println("Dron tomó pedido a " + dron.pedido.destino.nombre);
                }

                if (dron.ocupado) {

                    // Clima
                    if (climaAdverso) {
                        System.out.println("Dron detenido por clima");
                        continue;
                    }

                    // Batería
                    if (dron.bateria <= 20) {
                        System.out.println("Batería baja regresando a CEDI");
                        dron.posicion = CEDI;
                        dron.bateria = 100;
                        dron.ocupado = false;
                        continue;
                    }

                    List<Arista> vecinos = grafo.obtenerVecinos(dron.posicion);

                    if (vecinos != null && !vecinos.isEmpty()) {

                        Arista siguiente = vecinos.get(rand.nextInt(vecinos.size()));

                        // Zona restringida
                        if (zonaRestringida &&
                           (siguiente.destino.nombre.equals("Nav5") || siguiente.destino.nombre.equals("Nav6"))) {
                            System.out.println("Ruta bloqueada (zona restringida)");
                            continue;
                        }

                        dron.posicion = siguiente.destino;
                        dron.bateria -= siguiente.peso;

                        System.out.println("Dron se movió a " + dron.posicion.nombre);
                    }

                    // Entrega
                    if (dron.posicion == dron.pedido.destino) {
                        dron.pedido.entregado = true;
                        dron.ocupado = false;
                        entregas++;
                        System.out.println("ENTREGA COMPLETADA");
                    }
                }
            }
        }

        // RESULTADOS
        System.out.println("\n--- RESULTADOS ---");
        System.out.println("Total entregas: " + entregas);
        System.out.println("Pedidos restantes: " + pedidos.size());
    }
}