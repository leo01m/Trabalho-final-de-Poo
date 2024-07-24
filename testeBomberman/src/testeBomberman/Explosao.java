package testeBomberman;

import java.util.ArrayList;
import java.util.List;

public class Explosao {
    private List<Posicao> posicoesAfetadas;
    private int duracao; // Duração da explosão em ticks

    public Explosao(int x, int y, int alcance, Tabuleiro tabuleiro) {
        posicoesAfetadas = new ArrayList<>();
        duracao = 10; // Aumenta a duração da explosão para 10 ticks

        // Explosão horizontal para a esquerda
        for (int i = 0; i <= alcance; i++) {
            int novoX = x - i;
            if (novoX >= 0) {
                posicoesAfetadas.add(new Posicao(novoX, y));
                if (tabuleiro.temParede(novoX, y) || tabuleiro.temCaixa(novoX, y)) {
                    break;
                }
            } else {
                break;
            }
        }

        // Explosão horizontal para a direita
        for (int i = 1; i <= alcance; i++) {
            int novoX = x + i;
            if (novoX < tabuleiro.getLargura()) {
                posicoesAfetadas.add(new Posicao(novoX, y));
                if (tabuleiro.temParede(novoX, y) || tabuleiro.temCaixa(novoX, y)) {
                    break;
                }
            } else {
                break;
            }
        }

        // Explosão vertical para cima
        for (int i = 1; i <= alcance; i++) {
            int novoY = y - i;
            if (novoY >= 0) {
                posicoesAfetadas.add(new Posicao(x, novoY));
                if (tabuleiro.temParede(x, novoY) || tabuleiro.temCaixa(x, novoY)) {
                    break;
                }
            } else {
                break;
            }
        }

        // Explosão vertical para baixo
        for (int i = 1; i <= alcance; i++) {
            int novoY = y + i;
            if (novoY < tabuleiro.getAltura()) {
                posicoesAfetadas.add(new Posicao(x, novoY));
                if (tabuleiro.temParede(x, novoY) || tabuleiro.temCaixa(x, novoY)) {
                    break;
                }
            } else {
                break;
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
