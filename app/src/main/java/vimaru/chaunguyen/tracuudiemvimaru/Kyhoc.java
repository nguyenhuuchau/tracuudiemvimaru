package vimaru.chaunguyen.tracuudiemvimaru;

/**
 * Created by Chau on 12/15/2015.
 */
public class Kyhoc implements Item {
    private String kyhoc;
    public Kyhoc(String kyhoc) {
        this.kyhoc = kyhoc;
    }

    public String getName() {
        return kyhoc;
    }

    public void setName(String kyhoc) {
        this.kyhoc = kyhoc;
    }

}
