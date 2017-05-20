package se.graphics.proj;

public class Nil extends Tree {

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public boolean isNil() {
        return true;
    }

}
