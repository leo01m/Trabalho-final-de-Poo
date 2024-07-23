package testeBomberman;

import java.util.ArrayList;
import java.util.List;

public class Explosao {
    private List<Posicao> posicoesAfetadas;
    private int duracao; // Duração da explosão em ticks

    public Explosao(int x, int y, int alcance) {
        posicoesAfetadas = new ArrayList<>();
        duracao = 10; // Aumenta a duração da explosão para 10 ticks

        for (int i = -alcance; i <= alcance; i++) {
            if (x + i >= 0) {
                posicoesAfetadas.add(new Posicao(x + i, y));
            }
            if (y + i >= 0) {
                posicoesAfetadas.add(new Posicao(x, y + i));
            }
        }
    }

    public void tick() {
        if (duracao > 0) {
            duracao--;
        }
    }

    public boolean estaExpirada() {
        return duracao <= 0;
    }

    public List<Posicao> getPosicoesAfetadas() {
        return posicoesAfetadas;
    }
}
