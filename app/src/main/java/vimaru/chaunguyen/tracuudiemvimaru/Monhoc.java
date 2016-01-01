package vimaru.chaunguyen.tracuudiemvimaru;

/**
 * Created by Chau on 12/15/2015.
 */
public class Monhoc implements Item {
    private String maMH;
    private String tenMH;
    private String diemchu;
    private String tcht;
    private String diemX;
    private String diemY;
    private String diemZ;
    private String diemso;

    public Monhoc(String maMH, String tenMH, String tcht, String diemX, String diemY, String diemZ, String diemso, String diemchu) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.diemchu = diemchu;
        this.tcht = tcht;
        this.diemX = diemX;
        this.diemY = diemY;
        this.diemZ = diemZ;
        this.diemso = diemso;
    }

    public String getDiemso() {
        return diemso;
    }

    public void setDiemso(String diemso) {
        this.diemso = diemso;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getName() {
        return tenMH;
    }

    public void setName(String tenMH) {
        this.tenMH = tenMH;
    }

    public String getDiemchu() {
        return diemchu;
    }

    public void setDiemchu(String diemchu) {
        this.diemchu = diemchu;
    }

    public String getTCHT() {
        return tcht;
    }

    public void setTCHT(String tcht) {
        this.tcht = tcht;
    }

    public String getDiemX() {
        return diemX;
    }

    public void setDiemX(String diemX) {
        this.diemX = diemX;
    }

    public String getDiemY() {
        return diemY;
    }

    public void setDiemY(String diemY) {
        this.diemY = diemY;
    }

    public String getDiemZ() {
        return diemZ;
    }

    public void setDiemZ(String diemZ) {
        this.diemZ = diemZ;
    }
}
