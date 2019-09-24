package core;

/**
 * InvalidEquationException is used to indicate an invalid equation on parsing.
 * Can be used in a catch statement so a program could identify when a user entered an
 * invalid equation. This will be thrown when a String is parsed. In other word, it
 * will be thrown when Equation Constructor is called on an invalid equation.
 * It could also be thrown if the resulting binary tree was changed to an invalid
 * tree then a value is calculated.
 * @see core.Equation
 * @see core.Node
 */
public class InvalidEquationException extends RuntimeException{
    public InvalidEquationException(String errorMessage){
        super(errorMessage);
    }
}
