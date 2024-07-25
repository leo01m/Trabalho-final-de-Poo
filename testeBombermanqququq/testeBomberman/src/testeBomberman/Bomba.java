package testeBomberman;

public class Bomba {
    private Posicao posicao;
    private int tempo; // Tempo restante para a bomba explodir
    private boolean explodida;

    // Construtor da Bomba
    public Bomba(int x, int y, int tempo) {
        this.posicao = new Posicao(x, y);
        this.tempo = tempo; // 50 ticks = 5 segundos
        this.explodida = false;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void tick() {
        if (tempo > 0) {
            tempo--; // Decrementa o tempo restante
        } else {
            explodida = true; // Marca a bomba como explodida
        }
    }

    public boolean estaExplodida() {
        return explodida;
    }
}
