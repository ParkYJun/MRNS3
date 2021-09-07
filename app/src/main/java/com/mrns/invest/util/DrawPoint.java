package com.mrns.invest.util;

public class DrawPoint {
    private float fX;
    private float fY;

    public DrawPoint() {

    }

    public DrawPoint(float fX, float fY) {
        this.fX = fX;
        this.fY = fY;
    }

    public float getfX() {
        return fX;
    }

    public void setfX(float fX) {
        this.fX = fX;
    }

    public float getfY() {
        return fY;
    }

    public void setfY(float fY) {
        this.fY = fY;
    }

    public void setXY(float fX, float fY) {
        setfX(fX);
        setfY(fY);
    }
}
