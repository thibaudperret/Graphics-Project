package tree;

/**
 * The class Nil represent a null object.
 * NOTE: We implement such a class after having few courses around functional programming in Scala where we used
 * a lot the object Nil.
 */
public class Nil extends Tree {

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public boolean isNil() {
        return true;
    }
    
    @Override
    public String toString() {
        return "---";
    }
}