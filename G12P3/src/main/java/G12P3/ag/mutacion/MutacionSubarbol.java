package G12P3.ag.mutacion;

import G12P3.ag.Cromosoma;
import G12P3.arbol.GeneradorArbol;
import G12P3.arbol.NodoAst;
import G12P3.arbol.NodoBloque;
import G12P3.arbol.NodoCondicional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class MutacionSubarbol implements Mutacion {

    private record Hueco(Consumer<NodoAst> setter, int prof) {}

    private final Random rnd;
    private final int profMax;

    public MutacionSubarbol(Random rnd, int profMax) {
        this.rnd = rnd;
        this.profMax = profMax;
    }

    @Override
    public void mutar(Cromosoma cromosoma) {
        List<Hueco> huecos = new ArrayList<>();

        // hueco raiz: profundidad 0
        huecos.add(new Hueco(nuevo -> cromosoma.arbol = nuevo, 0));

        recolectarHuecos(cromosoma.arbol, huecos);

        Hueco hueco = huecos.get(rnd.nextInt(huecos.size()));
        NodoAst nuevoSubarbol = GeneradorArbol.crearGrow(
            hueco.prof(),
            profMax,
            rnd
        );
        hueco.setter().accept(nuevoSubarbol);
    }

    private void recolectarHuecos(NodoAst nodo, List<Hueco> huecos) {
        if (nodo instanceof NodoCondicional cond) {
            huecos.add(
                new Hueco(
                    cond::setHijoTrue,
                    cond.getHijoTrue().getProfundidad()
                )
            );
            recolectarHuecos(cond.getHijoTrue(), huecos);
            if (cond.getHijoFalse() != null) {
                huecos.add(
                    new Hueco(
                        cond::setHijoFalse,
                        cond.getHijoFalse().getProfundidad()
                    )
                );
                recolectarHuecos(cond.getHijoFalse(), huecos);
            }
        } else if (nodo instanceof NodoBloque bloque) {
            List<NodoAst> hijos = bloque.getHijos();
            for (int i = 0; i < hijos.size(); i++) {
                final int idx = i;
                huecos.add(
                    new Hueco(
                        nuevo -> hijos.set(idx, nuevo),
                        hijos.get(i).getProfundidad()
                    )
                );
                recolectarHuecos(hijos.get(i), huecos);
            }
        }
    }
}
