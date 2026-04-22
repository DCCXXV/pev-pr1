package G12P3.ag.cruce;

import G12P3.ag.Cromosoma;
import G12P3.arbol.NodoAst;
import G12P3.arbol.NodoBloque;
import G12P3.arbol.NodoCondicional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class CruceSubArboles implements Cruce {

    private record Slot(Consumer<NodoAst> setter, NodoAst nodo) {}

    private final Random rand;

    public CruceSubArboles() {
        this.rand = new Random();
    }

    public CruceSubArboles(Random rand) {
        this.rand = rand;
    }

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        List<Slot> slotsP1 = recolectarCondicionales(padre1);
        List<Slot> slotsP2 = recolectarCondicionales(padre2);

        if (slotsP1.isEmpty() || slotsP2.isEmpty()) return;

        Slot s1 = slotsP1.get(rand.nextInt(slotsP1.size()));
        Slot s2 = slotsP2.get(rand.nextInt(slotsP2.size()));

        NodoAst tmp = s1.nodo();
        s1.setter().accept(s2.nodo());
        s2.setter().accept(tmp);
    }

    private List<Slot> recolectarCondicionales(Cromosoma cromosoma) {
        List<Slot> slots = new ArrayList<>();
        recolectarCondicionalesRec(cromosoma.arbol, nuevo -> cromosoma.arbol = nuevo, slots);
        return slots;
    }

    private void recolectarCondicionalesRec(NodoAst nodo, Consumer<NodoAst> setter, List<Slot> slots) {
        if (nodo.isConditional())
            slots.add(new Slot(setter, nodo));

        if (nodo instanceof NodoCondicional cond) {
            recolectarCondicionalesRec(cond.getHijoTrue(), cond::setHijoTrue, slots);
            if (cond.getHijoFalse() != null)
                recolectarCondicionalesRec(cond.getHijoFalse(), cond::setHijoFalse, slots);
        } else if (nodo instanceof NodoBloque bloque) {
            List<NodoAst> hijos = bloque.getHijos();
            for (int i = 0; i < hijos.size(); i++) {
                final int idx = i;
                recolectarCondicionalesRec(hijos.get(i), nuevo -> hijos.set(idx, nuevo), slots);
            }
        }
    }
}
