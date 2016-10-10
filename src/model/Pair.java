package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jsybran on 9/29/16.
 */
public class Pair<T> {
    private Optional<T> left, right;
    public Pair(){
        this(null,null);
    }
    public Pair(T left){
        this(left,null);
    }
    public Pair(T left, T right){
        if(left != null)
            this.left = Optional.of(left);
        else
            this.left = Optional.empty();
        if(right != null)
            this.right = Optional.of(right);
        else
            this.right = Optional.empty();
    }
    public List<T> toList(){
        List<T> ret = new ArrayList<T>();
        if(left.isPresent())ret.add(left.get());
        if(right.isPresent())ret.add(right.get());
        return ret;
    }
    public Optional<T> getOther(T test) {
        if(left.isPresent() && test ==  left.get())
            return right;
        if(right.isPresent() && test == right.get())
            return left;
        return Optional.empty();
    }
    public Optional<T> getLeft(){return left;}
    public Optional<T> getRight(){return right;}

    public boolean equals(Pair<T> other){
        return (left.equals(other.left) && right.equals(other.right)) ||
                (left.equals(other.right) && right.equals(other.left));
    }
}
