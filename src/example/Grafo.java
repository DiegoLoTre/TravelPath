package example;
/*
 * Grafo.java
 *
 * Trabajo Práctico Nro. 2 
 * Algoritmos y Estruturas de Datos III
 * Autor: Cristhian Daniel Parra
 *
 * Fecha: 07 - 05 - 2005
 *
 * -- NOTA -- Sencillamente uso la implementación proveída por 
 *            el Profesor con algunas modificaciones
 *
 * -- Modificaciones al original
 *
 * - El original Implementaba una representacion sencilla de 
 *   grafos dirigidos que, esencialmente, usaba matriz de adyacencia.
 *   La implementación actual es de un Grafo dirigido con lista 
 *   de adyacencias
 *
 */
import java.util.*;

class Grafo {
    Vector lista_vert;
    int numAristas = 0;        // Cantidad de aristas del Grafo

    Grafo() {
        init();
    }

    /*
     * Reinicializa el grafo, perdiendose lo que pudiera contener.
     */
    void init() {
        lista_vert = new Vector();
    }

    int cantVertices() {
        return lista_vert.size();
    }

    /*
     * Version simplifada de unir(from, to, Arista a) para el caso en
     * que solo se tiene costo como atributo, que suele ser el caso.
     */
    public int unir (int from, int to, double costo) {
        unir (from, to, new Arista (from, to, costo));
        return ++numAristas;
    }

    /*
     * Anota que hay conexion entre los vertices identificados con "from" y "to",
     * conteniendo "a" los atributos de esta union (arista).
     */
    void unir(int from, int to, Arista a) {
        /*
         * Si los vertices referenciados no existen, se instancian con nombre
         * null.
         */
        if ( from >= cantVertices() ) {
            lista_vert.setSize (from+1);
        }

        if ( to >= cantVertices() ) {
            lista_vert.setSize (to+1);
        }

        if ( vertice (from) == null ) {
            lista_vert.set (from, new Vertice (null));
        }

        if ( vertice (to) == null ) {
            lista_vert.set (to, new Vertice (null));
        }

        vertice(from).unir (a);
    }

    /*
     * Retorna un objeto de tipo Vertice. Existe solo para evitar que
     * el acceso a lista_vert se llene de casts.
     */
    Vertice vertice(int k)
    {
        return (Vertice) lista_vert.elementAt (k);
    }

    /*
     * Retorna la arista (from, to), si existe.
     * Retorna null si
     * - no existe el vertice from o no existe el vertice to.
	 * - existen los dos vértices pero "to" no aparece entre los
	 *   adyacentes de "from".
     */
    Arista arista(int from, int to) {
        Arista a = null;
        Vertice v;
        int cantVert = cantVertices();

        if ( from < cantVert && to < cantVert ) {
            v = vertice (from);
            a = v.adyacente(to);
        }
        return a;
    }

    void agregarVert(String id, int pos) {
        if ( pos >= cantVertices() )
            lista_vert.setSize (pos+1);

        lista_vert.set (pos, new Vertice (id));
    }

    private void Dijkstra(int s) {
        Heap vertices;
        Vertice actual = vertice(s);
        Vertice adyacente;
        Arista a;
        ListIterator<Arista> AdyItr;

        int v = cantVertices();
        vertices = new Heap(v,0);
        actual.distMin = 0.0;

        // Cargamos los vértices en el Heap
        for (int i=0; i<v; i++) {
            Vertice vertex = vertice (i);
            vertices.insertar(vertex);
        }
        while (v > 0) {
            do {
                actual = (Vertice) vertices.extract_val_extremo();
            } while (actual !=null && actual.visitado);

            if (actual == null) {
                break;
            }
            actual.visitado = true;
            v--;

            AdyItr =  actual.lista_ady.listIterator(0);

            while ( AdyItr.hasNext() ) {
                a = AdyItr.next();
                adyacente = vertice (a.to);

                if ( !adyacente.visitado &&
                        adyacente.distMin > actual.distMin + a.costo) {

                    adyacente.distMin = actual.distMin + a.costo;
                    adyacente.ultimoCaminoMin = a.from;

                }
            }

            // Cargamos "de nuevo" los vértices en el Heap
            for (int i=0; i<v; i++) {
                Cmp vertexMin = vertice (i);
                vertices.insertar(vertexMin);
            }
        }
    }
}