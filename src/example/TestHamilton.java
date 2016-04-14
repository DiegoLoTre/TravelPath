package example;

/*
 * TestHamilton.java
 *
 * Trabajo Práctico Nro. 2
 * Algoritmos y Estruturas de Datos III
 * Autor: Cristhian Daniel Parra
 * C.I.: 2.045.856
 *
 * Fecha: 12 - 06 - 2005
 * Pequeño Programa que lee un grafo desde la línea de comandos
 * y prueba con él los algoritmos de las clases MST.java y AllMst.java
 *
 */

import java.util.Objects;

public class TestHamilton {

    public static void main (String args[])
    {
        Grafo G = new GrafoNoDirigido();
        Hamilton Ham;

        try {
            GrafoIO.leer (G, System.in);
            GrafoIO.imprimir(G);
            Ham = new Hamilton (G,G.cantVertices());

            if (Ham.HamiltonFuerzaBruta(0)) {
                System.out.println("\nSolucion del Ciclo Mínimo para el Grafo introducido...");
                Ham.imprimirFB();
            } else {
                System.out.println("\nNo se encontró ningún ciclo Hamiltoniano...");
            }


        } catch (Exception e) {
            System.out.println ("\n\nError: " + e.getMessage());
            e.printStackTrace(System.out);
        }
        System.out.println();
    }
}