package G12P3.ag.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.arbol.NodoAccion;
import G12P3.arbol.NodoAst;
import G12P3.evaluacion.TipoAccion;
import java.util.List;
import java.util.Random;

public class MutacionTerminal implements Mutacion {

    private static final TipoAccion[] ACCIONES = TipoAccion.values();
    private final Random rnd;

    public MutacionTerminal(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        List<NodoAst> terminales = cromosoma.arbol
            .obtenerTodosNodos()
            .stream()
            .filter(n -> n instanceof NodoAccion)
            .toList();

        if (terminales.isEmpty()) return;

        NodoAccion nodo = (NodoAccion) terminales.get(
            rnd.nextInt(terminales.size())
        );
        TipoAccion actual = nodo.getTipoAccion();

        TipoAccion nueva;

        // forzamos que el nuevo terminal sea distinto al actual
        do {
            nueva = ACCIONES[rnd.nextInt(ACCIONES.length)];
        } while (nueva == actual);

        nodo.setTipoAccion(nueva);
    }
}
