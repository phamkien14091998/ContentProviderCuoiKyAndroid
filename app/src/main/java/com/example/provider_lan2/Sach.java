package com.example.provider_lan2;

public class Sach {
    private int msSach;
    private String tenSach;
    private String tacGia;

    public Sach(int msSach, String tenSach, String tacGia) {
        this.msSach = msSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
    }
    public Sach() {

    }

    public int getMsSach() {
        return msSach;
    }

    public void setMsSach(int msSach) {
        this.msSach = msSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    @Override
    public String toString() {
        return
                "msSach=  " + msSach +
                "   tenSach=  " + tenSach +
                "   tacGia=   " + tacGia ;
    }
}
