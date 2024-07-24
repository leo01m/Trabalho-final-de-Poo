package testeBomberman;

public class Jogador {
    private Posicao posicao;
    private int velocidade;

    public Jogador(int inicioX, int inicioY, int velocidade) {
        this.posicao = new Posicao(inicioX, inicioY);
        this.velocidade = velocidade;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void moverParaCima(Tabuleiro tabuleiro) {
        if (tabuleiro.isPosicaoValida(posicao.getX(), posicao.getY() - velocidade)) {
            posicao.mover(0, -velocidade);

        }
    }

    public void moverParaBaixo(Tabuleiro tabuleiro) {
        if (tabuleiro.isPosicaoValida(posicao.getX(), posicao.getY() + velocidade)) {
            posicao.mover(0, velocidade);
        }
    }

    public void moverParaEsquerda(Tabuleiro tabuleiro) {
        if (tabuleiro.isPosicaoValida(posicao.getX() - velocidade, posicao.getY())) {
            posicao.mover(-velocidade, 0);
        }
    }

    public void moverParaDireita(Tabuleiro tabuleiro) {
        if (tabuleiro.isPosicaoValida(posicao.getX() + velocidade, posicao.getY())) {
            posicao.mover(velocidade, 0);
        }
    }

    public void colocarBomba(Tabuleiro tabuleiro) {
        Bomba bomba = new Bomba(posicao.getX(), posicao.getY(), 15);
        tabuleiro.colocarBomba(bomba);
    }
}
