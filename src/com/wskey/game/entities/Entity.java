package com.wskey.game.entities;

public abstract class Entity
{

    protected float height = 0f;
    protected float width = 0f;
    protected Vector position = new Vector(0, 0);

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public Vector getPosition() { return position; }

    public void setPosition(Vector position) { this.position = position; }

}
