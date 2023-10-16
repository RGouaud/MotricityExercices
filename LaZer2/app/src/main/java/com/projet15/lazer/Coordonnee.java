package com.projet15.lazer;

public class Coordonnee {
    private int x;
    private int y;

    public Coordonnee(int CoordX, int CoordY){
        this.x = CoordX;
        this.y = CoordY;
    }

    public Coordonnee(int CoordonneeUneDimension, int LargeurImage, int HauteurImage){
        this.x = (CoordonneeUneDimension+1)%LargeurImage;
        this.y = (int)((CoordonneeUneDimension+1)/LargeurImage);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
}


