package core;

/**
 * Node Class
 * <p>
 *     Used to represent a binary syntax tree, where {@code left} and {@code right} are the next in the tree.
 * Holds 4 fields. Two other Node instances {@code left} and {@code right}, a String {@code value},
 * and a EdgeType {@code type}.
 * </p>
 */
public class Node {

    //_________Fields_________//
    Node left, right;
    String value;
    EdgeType type;

    //_________Constructors_________//

    Node(String value, EdgeType type) {
        this.value = value;
        this.type = type;
    }

    Node(String value) {
        this(value, EdgeType.DEFAULT);
    }

    Node() {
        this(null, EdgeType.DEFAULT);
    }


    /**
     * Constructor to easily copy a Node
     *
     * @param node Node to be copied
     */
    private Node(Node node) {
        this.type = node.type;
        this.value = node.value;
        if (node.left != null) {
            this.left = new Node(node.left);
            this.right = new Node(node.right);
        }


    }


    //_________public methods_________//

    /**
     * Calculates the value of the node, taking into account the EdgeType (sine, cosine, etc)
     *
     * @param x value of x to be calculated
     * @return value of node with branch type accounted for
     */
    public double calculate(double x) {
        if (type == EdgeType.DEFAULT)
            return rawValue(x);

        if (type == EdgeType.NATURAL_LOG)
            return Math.log(rawValue(x));

        if (type == EdgeType.LOG_BASE_TEN)
            return Math.log10(rawValue(x));

        if (type == EdgeType.SINE)
            return Math.sin(rawValue(x));

        if (type == EdgeType.COSINE)
            return Math.cos(rawValue(x));

        if (type == EdgeType.TANGENT)
            return Math.tan(rawValue(x));

        if (type == EdgeType.COSECANT)
            return 1.0d / Math.sin(rawValue(x));

        if (type == EdgeType.SECANT)
            return 1.0d / Math.cos(rawValue(x));

        if (type == EdgeType.COTANGENT)
            return 1.0d / Math.tan(rawValue(x));

        if (type == EdgeType.ARCSINE)
            return Math.asin(rawValue(x));

        if (type == EdgeType.ARCCOSINE)
            return Math.acos(rawValue(x));

        if (type == EdgeType.ARCTANGENT)
            return Math.atan(rawValue(x));

        if (type == EdgeType.ARCCOSECANT)
            return Math.asin(rawValue(1.0d / x));

        if (type == EdgeType.ARCSECANT)
            return Math.acos(rawValue(1.0d / x));

        if (type == EdgeType.ARCCOTANGENT)
            return Math.atan(rawValue(1.0d / x));

        if (type == EdgeType.ABSOLUTE_VALUE)
            return Math.abs(rawValue(x));

        return 0.0d;
    }


    /**
     * Calculates the instantaneous rate of change, or the derivative at a point
     *
     * @param x place at which the slope is calculated
     * @return approximated slope of the tangent line at x
     */
    public double slopeAt(double x) {
        final double num = .0000000001d;
        return ((calculate(x + num) - calculate(x)) / ((x + num) - (x)));
    }

    /**
     * Calculates the value of the definite integral of the equation from a to b
     *
     * @param a starting value
     * @param b ending value
     * @return double value representing the approximated integral from a to b
     */
    public double definiteIntegral(double a, double b) {
        //check if they have to be swapped
        boolean swapped = false;
        if (a > b) {
            double tempA = a;
            a = b;
            b = tempA;
            swapped = true;
        }
        //approximate value
        double summation = 0;
        double dx = (b - a) / 10000.0;
        for (double pos = a; pos < b; pos += dx) {
            summation += calculate(pos) * dx;
        }


        if (swapped)
            return -1.0d * summation;
        return summation;
    }


    /**
     * Calculates the sum of the equation from n to endN
     *
     * @param n    Value to start the summation at
     * @param endN Value to end the summation at (inclusive)
     * @return Sum of the calculated values from n to endN
     */
    public double sum(int n, int endN) {
        double sum = 0.0d;
        for (; n <= endN; n++) {
            sum += calculate(n);
        }
        return sum;
    }

    /**
     * <p>
     * Finds the sum of the equation from n until the change is less than the precision
     * </p>
     *
     * <p>
     * @param n         Starting number to iterate from
     * @param precision The maximum change between the sum of n and the sum of n + 1 to return
     * @return sum from n until the change is less than precision
     * </p>
     */
    public double sum(int n, double precision) {
        double sumL = 0.0d - precision - 10;
        double sumC = 0.0d;
        for (; Math.abs(sumC - sumL) >= precision; n++) {
            sumL = sumC;
            sumC += calculate(n);
        }

        return sumC;
    }


    /**
     * <p>
     * Creates a tree representing the taylor expansion from 0 to {@code k}  at the point {@code c}
     * </p>
     * <P>
     * @param k The last term of the expansion to be added to the tree
     * @param c Evaluated point of derivative in each term
     * @return Node representing taylor expansion from 0 to k, inclusive, at point c
     * </P>
     */
    public Node taylorExpansion(int k, double c) {
        Node expansion = new Node("+");
        double tempC = c * -1.0;


        Node last = new Node(this);
        Node temp = expansion;
        for (int i = 0; i <= k; i++) {

            temp.left = new Node("/");
            temp.left.right = new Node(Double.toString(Util.factorial(i)));

            double dervC = last.calculate(c);
            temp.left.left = new Node("*");
            temp.left.left.left = new Node(Double.toString(dervC));
            temp.left.left.right = new Node("^");
            temp.left.left.right.right = new Node(Integer.toString(i));
            temp.left.left.right.left = new Node("+");
            temp.left.left.right.left.left = new Node("x");

            temp.left.left.right.left.right = new Node(Double.toString(tempC));

            last = last.calculateDerivative();
            temp = temp.right;
            temp = new Node("+");
        }

        return expansion;
    }

    /**
     * Creates and returns a Node representing a Maclaurin expansion from 0 to {@code k}
     *
     * @param k The last term of the expansion to be added to the tree
     * @return Node representing Maclaurin expansion from 0 to k, inclusive
     */
    public Node maclaurinExpansion(int k) {
        return taylorExpansion(k, 0.0);
    }


    /**
     * <P>
     * Calculates the derivative and returns a {@code Node} as a representation
     * Since Node is a superclass of Equation and all mathematical methods are in Node,
     * the returned derivative can execute all the methods
     * </P>
     * @return derivative of the the Node
     */
    public Node calculateDerivative() {
        Node n;

        if (type == EdgeType.COSINE || type == EdgeType.SINE) {

            n = new Node("*");
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = temp.calculateDerivative();
            temp.type = ((type == EdgeType.SINE) ? EdgeType.COSINE : EdgeType.SINE);
            n.right = temp;

            if (type == EdgeType.COSINE) {
                Node a = new Node("*");
                a.left = new Node("-1");
                a.right = n;
                return a;
            }
            return n;
        }

        if (type == EdgeType.TANGENT) {
            n = new Node("*");
            n.right = new Node("^");
            n.right.right = new Node("2");
            n.right.left = new Node(this);
            n.right.left.type = EdgeType.SECANT;
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = temp.calculateDerivative();
            return n;
        }

        if (type == EdgeType.COSECANT) {
            n = new Node("*");
            n.right = new Node("*");
            n.right.left = new Node("*");
            n.right.left.left = new Node("-1");
            n.right.left.right = new Node(this);
            n.right.left.right.type = EdgeType.COSECANT;
            n.right.right = new Node(this);
            n.right.right.type = EdgeType.COTANGENT;

            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = temp.calculateDerivative();

            return n;
        }

        if (type == EdgeType.SECANT) {
            n = new Node("*");
            n.right = new Node("*");
            n.right.left = new Node(this);
            n.right.left.type = EdgeType.SECANT;
            n.right.right = new Node(this);
            n.right.right.type = EdgeType.TANGENT;

            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = temp.calculateDerivative();
            return n;
        }
        if (type == EdgeType.COTANGENT) {
            n = new Node("*");
            n.right = new Node("*");
            n.right.left = new Node("-1");
            n.right.right = new Node("^");
            n.right.right.left = new Node(this);
            n.right.right.left.type = EdgeType.COSECANT;
            n.right.right.right = new Node("2");

            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = temp.calculateDerivative();
            return n;
        }

        if (type == EdgeType.NATURAL_LOG) {
            n = new Node("/");
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = left.calculateDerivative();
            n.right = temp;
            return n;
        }

        if (type == EdgeType.LOG_BASE_TEN) {
            n = new Node("/");
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = left.calculateDerivative();
            n.right = new Node("*");
            n.right.left = new Node(Double.toString(Math.log(10)));
            n.right.right = temp;
            return n;
        }

        if (type == EdgeType.ARCCOSINE) {
            n = new Node("/");
            n.left = new Node("*");
            n.left.left = new Node("-1");
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left.right = temp.calculateDerivative();

            n.right = new Node("^");
            n.right.right = new Node(".5");
            n.right.left = new Node("+");
            n.right.left.left = new Node("1");
            n.right.left.right = new Node("*");
            n.right.left.right.left = new Node("-1");
            n.right.left.right.right = new Node("^");
            n.right.left.right.right.left = temp;
            n.right.left.right.right.right = new Node("2");

            return n;
        }

        if (type == EdgeType.ARCSINE) {
            n = new Node("/");
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = temp.calculateDerivative();

            n.right = new Node("^");
            n.right.right = new Node(".5");
            n.right.left = new Node("+");
            n.right.left.left = new Node("1");
            n.right.left.right = new Node("*");
            n.right.left.right.left = new Node("-1");
            n.right.left.right.right = new Node("^");
            n.right.left.right.right.left = temp;
            n.right.left.right.right.right = new Node("2");

            return n;
        }

        if (type == EdgeType.ARCTANGENT) {
            n = new Node("/");
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = temp.calculateDerivative();
            n.right = new Node("+");
            n.right.left = new Node("1");
            n.right.right = new Node("^");
            n.right.right.left = temp;
            n.right.right.right = new Node("2");

            return n;
        }

        if (type == EdgeType.ARCSECANT) {
            n = new Node("/");
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left = temp.calculateDerivative();
            n.right = new Node("*");

            n.right.left = temp;
            n.right.left.type = EdgeType.ABSOLUTE_VALUE;
            temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.right.right = new Node("^");
            n.right.right.right = new Node(".5");
            n.right.right.left = new Node("+");
            n.right.right.left.left = new Node("^");
            n.right.right.left.left.left = temp;
            n.right.right.left.left.right = new Node("2");
            n.right.right.left.right = new Node("-1");

            return n;
        }


        if (type == EdgeType.ARCCOSECANT) {
            n = new Node("/");
            n.left = new Node("*");
            n.left.left = new Node("-1");
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left.right = temp.calculateDerivative();

            n.right = new Node("*");
            n.right.left = temp;
            n.right.left.type = EdgeType.ABSOLUTE_VALUE;
            temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.right.right = new Node("^");
            n.right.right.right = new Node(".5");
            n.right.right.left = new Node("+");
            n.right.right.left.right = new Node("-1");
            n.right.right.left.left = new Node("^");
            n.right.right.left.left.right = new Node("2");
            n.right.right.left.left.left = temp;
            return n;
        }

        if (type == EdgeType.ARCCOTANGENT) {
            n = new Node("/");
            n.left = new Node("*");
            n.left.left = new Node("-1");
            Node temp = new Node(this);
            temp.type = EdgeType.DEFAULT;
            n.left.right = temp.calculateDerivative();

            n.right = new Node("+");
            n.right.right = new Node("1");
            n.right.left = new Node("^");
            n.right.left.left = temp;
            n.right.left.right = new Node("2");
            return n;

        }

        //way to find derivative of an absolute value
        //square the value, making it positive, then take
        // the square root returning it to its original value
        if (type == EdgeType.ABSOLUTE_VALUE) {
            n = new Node("^");
            n.right = new Node(".5");
            n.left = new Node("^");
            n.left.right = new Node("2");
            n.left.left = new Node(this);
            n.left.left.type = EdgeType.DEFAULT;

            return n.calculateDerivative();
        }

        //Breaks derivative into two simpler parts and add them together
        if (value.equals("+")) {
            n = new Node("+");
            n.left = left.calculateDerivative();
            n.right = right.calculateDerivative();
            return n;
        }


        if (value.equals("*")) {
            if (type == EdgeType.DEFAULT) {
                //tests if the value is simply (constant)*x
                if (Util.isNumerical(left.value.charAt(0))) {
                    if (right.value.contains("x") && right.type == EdgeType.DEFAULT) {
                        return new Node(left);
                    }
                }
            }
            //same as above fragment except its reversed: x*(constant)
            if (Util.isNumerical(right.value.charAt(0))) {
                if (left.value.contains("x") && left.type == EdgeType.DEFAULT) {
                    return new Node(right);
                }
            }

            //product rule
            //left*d(right) + right*d(left)
            n = new Node("+");
            n.left = new Node("*");
            n.left.left = left;
            n.left.right = right.calculateDerivative();

            n.right = new Node("*");
            n.right.left = right;
            n.right.right = left.calculateDerivative();
            return n;
        }

        if (value.equals("^")) {

            if (Util.isNumerical(right.value.charAt(0))) {
                n = new Node("*");
                n.right = new Node("^");
                n.right.left = new Node(left);
                n.right.right = new Node(Double.toString(Double.parseDouble(right.value) - 1));
                n.left = new Node("*");
                n.left.right = left.calculateDerivative();
                n.left.left = new Node(right);
                return n;
            }
        }

        // quotient rule
        // ( low*d(high) - high*d(low) ) / (low^2)
        // low = right, high = left
        // ( right * d(left)   -   left * d(right) ) / right^2
        if (value.equals("/")) {
            n = new Node("/");
            n.right = new Node("^");
            n.right.left = new Node(right);
            n.right.right = new Node("2");


            n.left = new Node("+");
            n.left.left = new Node("*");
            n.left.left.left = new Node(right);
            n.left.left.right = left.calculateDerivative();

            n.left.right = new Node("*");
            n.left.right.left = new Node("-1");
            n.left.right.right = new Node("*");
            n.left.right.right.left = new Node(left);
            n.left.right.right.right = right.calculateDerivative();

            return n;
        }

        //base cases
        if (value.equals("x")) {
            return new Node("1");
        }
        if (Util.isNumerical(value.charAt(0))) {
            return new Node("0");
        }

        return null;
    }

    /**
     * Simplifies the node by checking for unnecessary constant operations
     * and other factors that make the tree needlessly complex
     */
    public Node simplify() {
        Node n;

        if(left == null){
            return new Node(this);
        }

        if( Util.isNumerical(left.value.charAt(0)) &&
                (this.type == EdgeType.DEFAULT ||
                        left.type == EdgeType.DEFAULT)){

            EdgeType targetType = this.type == EdgeType.DEFAULT ? left.type : this.type;
            double leftValue = Double.parseDouble(left.value);

            n = simplifyCommutative(left, right, leftValue, targetType);

            if(n != null){
                return n;
            }

            if(value.equals("/")){
                if(leftValue == 0.0){
                    n = new Node("0", targetType);
                    return n;
                }
            }

            if(value.equals("^")){
                if(leftValue == 1.0){
                    n = new Node("1", targetType);
                    return n;
                }
            }
        }

        if( Util.isNumerical(right.value.charAt(0)) &&
                (this.type == EdgeType.DEFAULT || right.type == EdgeType.DEFAULT)){

            EdgeType targetType = this.type == EdgeType.DEFAULT ? right.type : this.type;
            double rightValue = Double.parseDouble(right.value);

            n = simplifyCommutative(right, left, rightValue, targetType);
            if(n != null){
                return n;
            }

            if(value.equals("/")){
                if(rightValue == 1.0){
                    n = new Node(left);
                    n.type = targetType;
                    return n.simplify();
                }
            }

            if(value.equals("^")){
                if(rightValue == 0.0){
                    n = new Node("1", targetType);
                    return n;
                }

                if(rightValue == 1.0){
                    n = new Node(left);
                    n.type = targetType;
                    return n.simplify();
                }
            }

        }

        if(Util.isNumerical(left.value.charAt(0)) && Util.isNumerical(right.value.charAt(0))
            && left.type == EdgeType.DEFAULT && right.type == EdgeType.DEFAULT) {
            double newVal = Double.parseDouble(left.value) + Double.parseDouble(right.value);
            n = new Node(Double.toString(newVal), this.type);
            return n;
        }

        n = new Node(this);
        return n;
    }

    /**
     * Overrides the toString method so that it returns a string representing the node in
     * a typical normal form
     *
     * May contain, and will likely contain, excess parenthesis
     *
     * @return String representing one way to represent the Node in normal mathematical form
     */
    @Override
    public String toString(){

        if(this.left == null){
            return  type.toString() + value;
        }

        return type.toString()+ "(" + left.toString() + value + right.toString() + ")";
    }



    //_________private methods_________//

    /**
     *
     * Helper method to decrease clutter in simplify method
     * With the given parameters, it checks for possible extra operations and fixes them for
     *
     * @param target Target Node to check
     * @param other Other node in the tree
     * @param parsedValue parsed value of target
     * @param targetType the new targets type
     * @return simplified Node, or null if no simplification occurred
     */
    private Node simplifyCommutative(Node target, Node other, double parsedValue, EdgeType targetType){
        Node n;

        if(value.equals("+")){
            if(parsedValue == 0.0){

                n = new Node(other);
                n.type = targetType;
                return n.simplify();
            }
        }

        if(value.equals("*")){
            if(parsedValue == 0.0){
                n = new Node("0", targetType);
                return n;
            }

            if(parsedValue == 1.0){
                n = new Node(other);
                n.type = targetType;
                return n.simplify();
            }
        }

        return null;
    }

    /**
     * Returns raw value of the node, meaning it does not account for EdgeType
     *
     * @param x value of x to be calculated
     * @return value without regard to current node's branch type
     */
    private double rawValue(double x) {
        if (value.equals("*"))
            return left.calculate(x) * right.calculate(x);
        if (value.equals("/"))
            return left.calculate(x) / right.calculate(x);
        if (value.equals("^"))
            return Math.pow(left.calculate(x), right.calculate(x));
        if (value.equals("+"))
            return left.calculate(x) + right.calculate(x);

        return toDouble(x);
    }


    /**
     * Parses the value, returns the value of x if it is x, or -x if it is -x.
     * Otherwise parses the value as a double
     *
     * @param x value of x to be calculated
     * @return numerical value of the String field value
     * @throws InvalidEquationException Throws when attempting to parse an invalid value
     */
    private double toDouble(double x) throws InvalidEquationException {
        if (value.equalsIgnoreCase("x"))
            return x;
        if (value.equalsIgnoreCase("-x"))
            return -1 * x;

        double d;
        try {
            d = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new InvalidEquationException("Failed to calculate value <" + value + ">");
        }
        return d;
    }

}