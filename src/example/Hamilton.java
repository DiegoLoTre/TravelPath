/*
 * Hamilton.java
 *
 * Trabajo Práctico Nro. 2
 * Algoritmos y Estruturas de Datos III
 * Autor: Cristhian Daniel Parra
 * C.I.: 2.045.856
 *
 * Fecha: 07 - 06 - 2005
 * Modificado: 12 - 06 - 2005
 *
 * Hamilton.java contiene los métodos que resuelven
 * el problema del ciclo simple de costo mínimo a partir
 * de un vertice dado. La Lista de Métodos es la siguiente:
 *   - VecinoMasProx ---> Algoritmo Voraz que resuelve
 *                       el problema aproximadamente.
 *
 *   - VecinoMasProxVAtras ---> Es el mismo algoritmo anterior
 *                        pero con la optimización de la vuelta atras
 *                        para que cuando desde un vertice ya no hay opciones
 *                        el programa no termine, sino que vuelva atrás
 *                        y explore otros caminos.
 *
 *    - VecinoMasProxFBruta ---> El mismo Algoritmo pero que explora todas las
 *                        posibilidades y elige la menor.
 */
package example;

class Hamilton {
    /*
     * El vector solucion contenerá la sucesión de Vertices a seguir
     * que forma el ciclo hamiltoniano de costo mínimo
     * solucion [i] es el i-ésima vertice en la sucesión.
     */
    private Grafo G;
    private Vertice [] solucion;        // Ciclo Solucion
    private int [] solucionFB;          // Ciclo Solucion usado en el algoritmo de FuerzaBruta
    private int [] solucionFBMin;       // Ciclo Solucion final para el algoritmo de FuerzaBruta
    private double costo;               // Costo del Ciclo Mínimo.
    private int cantVert;
    // vuelta Atras.

    Hamilton(Grafo G, int cantVertices) {
        this.G = G;
        this.cantVert = cantVertices;
        solucion = new Vertice [cantVertices+1];
        solucionFB = new int [cantVertices+1];
        solucionFBMin = new int [cantVertices+1];
        costo = 0.0;
    }

    /*
     * Este método resuelve el problema mediante un algoritmo
     * de Vuelta Atrás que analiza todas las posibilidades.
     * Por lo tanto es de Fuerza Bruta
     *
     * Para que el método funcione, el índice del vector de los
     * vértices deben empezar en 1, por ello le sumamos uno al
     * inicio
     */
    boolean HamiltonFuerzaBruta(int verticeInicio) {
        solucionFB[1] = verticeInicio+1;
        solucionFBMin[0] = verticeInicio;

        for (int i=2 ; i< solucionFB.length ; i++ ) {
            solucionFB[i] = 0;
        }

        solucionFBMin[cantVert] = verticeInicio;
        costo = Double.POSITIVE_INFINITY;
        FuerzaBruta(2);

        return costo < Double.POSITIVE_INFINITY;

    }

    private void FuerzaBruta (int k) {

        do {
            NuevoVertice(k);

            if (k==cantVert && solucionFB[k]!=0 ) {

                double costoActual = CalcularCosto();

                if (costoActual < costo) {
                    costo = costoActual;

                    for (int i = 2; i < solucionFB.length ; i++ ) {
                        solucionFBMin[i-1] = solucionFB[i]-1;
                    }
                }
            } else if (solucionFB[k]!=0){
                FuerzaBruta (k+1);
            }
        } while (solucionFB[k]!=0 );
    }

    private void NuevoVertice(int k) {
        boolean vuelta=false;

        do {
            solucionFB[k] = (solucionFB[k]+1) % (cantVert+1);

            if ( (solucionFB[k] !=0 && G.arista(solucionFB[k-1]-1,solucionFB[k]-1)!= null)) {
                int j = 0;

                while (j <= k-1 && solucionFB[j]!=solucionFB[k] ) {
                    j++;
                }

                if (j==k && solucionFB[cantVert] != 0) {
                    vuelta = (G.arista(solucionFB[cantVert]-1,solucionFB[1]-1)!=null);
                }

                if ( j==k && ((k<cantVert) ||
                        ((k==cantVert) && vuelta))) {
                    break;
                }
            }
        } while (solucionFB[k] !=0);
    }

    private double CalcularCosto() {
        double c = 0.0;
        int i;

        for (i = 1 ; i < solucionFB.length-1 ; i++ ) {
            c += G.arista(solucionFB[i]-1,solucionFB[i+1]-1).costo;
        }

        c +=  G.arista(solucionFB[i]-1,solucionFB[1]-1).costo;
        return c;
    }

    void imprimirFB() {

        System.out.print(""+G.vertice(solucionFBMin[0]).nombre());

        for( int i=1 ; i < solucion.length ; i++ ) {
            System.out.print(" -> "+G.vertice(solucionFBMin[i]).nombre());
        }
        System.out.println("\nCosto del Ciclo con costo Mínimo: " + costo);
    }
}