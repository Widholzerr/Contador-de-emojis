package model;

public class registroCagada {
    private final String data;
    private final int pedro;
    private final int gabriela;
    private final String obsPedro;
    private final String obsGabi;

    public registroCagada(String data, int pedro, int gabriela, String obsPedro, String obsGabi) {
        this.data = data;
        this.pedro = pedro;
        this.gabriela = gabriela;
        this.obsPedro = obsPedro == null? "" : obsPedro.trim();
        this.obsGabi = obsGabi == null? "" : obsGabi.trim();
    }

    public String getData() {
        return data;
    }
    public int getPedro(){
        return pedro;
    }
    public int getGabriela(){
        return gabriela;
    }

    public String getObsPedro(){
        return obsPedro;
    }

    public String getObsGabi(){
        return obsGabi;
    }

    public String toString() {
        return String.format(
                "%s - Pedro: %d 💩 (%s) | Gabi: %d 💩 (%s)",
                data, pedro, obsPedro, gabriela, obsGabi
        );
    }

}
