package testeBomberman;

public class Inimigo {
    private Posicao posicao;
    private int velocidade;
    private String direcao;
    private boolean vivo;
    private double cooldown;

    public Inimigo(int inicioX, int inicioY, int velocidade) {
        this.posicao = new Posicao(inicioX, inicioY);
        this.velocidade = velocidade;
        this.direcao = "baixo"; // Direção inicial
        this.vivo = true;
        this.cooldown = 0;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void morrer() {
        this.vivo = false;
    }

    public void mover(Tabuleiro tabuleiro) {
        int x = posicao.getX();
        int y = posicao.getY();

        // Movimenta de acordo com a direção atual
        if (this.cooldown > 0) {
            cooldown--;
        } else {
            switch (direcao) {
                case "cima":
                    if (tabuleiro.isPosicaoValida(x, y - velocidade)) {
                        posicao.mover(0, -velocidade);
                        this.cooldown=3;
                    } else {
                        mudarDirecao();
                    }
                    break;
                case "baixo":
                    if (tabuleiro.isPosicaoValida(x, y + velocidade)) {
                        posicao.mover(0, velocidade);
                        this.cooldown=3;
                    } else {
                        mudarDirecao();
                    }
                    break;
                case "esquerda":
                    if (tabuleiro.isPosicaoValida(x - velocidade, y)) {
                        posicao.mover(-velocidade, 0);
                        this.cooldown=3;
                    } else {
                        mudarDirecao();
                    }
                    break;
                case "direita":
                    if (tabuleiro.isPosicaoValida(x + velocidade, y)) {
                        posicao.mover(velocidade, 0);
                        this.cooldown=3;
                    } else {
                        mudarDirecao();
                    }
                    break;
            }
        }

    }

    private void mudarDirecao() {
        String[] direcoes = { "cima", "baixo", "esquerda", "direita" };
        direcao = direcoes[(int) (Math.random() * 4)];
    }
}
