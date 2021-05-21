package com.wskey.game.entities;

public class BoundingBox
{
    protected Vector min;
    protected Vector max;

    public BoundingBox(Vector min, Vector max)
    {
        this.min = min;
        this.max = max;
    }

    public Vector getMin() {
        return min;
    }

    public void setMin(Vector min) {
        this.min = min;
    }

    public Vector getMax() {
        return max;
    }

    public void setMax(Vector max) {
        this.max = max;
    }

    public boolean collide(BoundingBox other)
    {
        return (
                (other.getMin().getX() >= min.getX() && other.getMax().getX() <= max.getX()) &&
                (other.getMin().getY() >= min.getY() && other.getMax().getY() <= max.getY())
        );
    }


}
