package core;

import java.util.ArrayList;

/**
 * Takes in a String and parses it into a binary syntax tree.
 *      First it formats the equation, replacing pi/e with their values and
 *      checking and fixing other bugs that may occur do to inexplicit input.
 *
 *      It then parses parenthesis in a recursive manner,
 *      as in it creates a new equation for the content of the parenthesis
 *
 *      Then it checks for functions (sine, ln, etc.). It sets the branch type to
 *      its respective function in that particular node
 *
 *      It parses all the operators in order of operation
 *
 *      The result is a binary syntax tree which can be used to calculate values
 *
 * Equation itself does not contain any mathematical public methods
 *
 * @see core.Node
 *
 */
public class Equation extends Node {

    //String of entered equation
    private String equation;
    //ArrayList of Nodes used to store parsed fragments of the final Equation
    private ArrayList<Node> nodes = new ArrayList<>();

    /**
     * Constructor to create a new equation. Equation is parsed on constructor call
     * @param equation String to be parsed into an equation
     * @throws InvalidEquationException If
     */
    public Equation(String equation) throws InvalidEquationException{
        this.equation = equation;
        parseEquation();
        //test case, throws and exception if needed
        calculate(0.0d);

    }

    /**
     * Parses the equation into nodes, called by constructor
     */
    private void parseEquation() {
        String originalString = equation;
        formatEquation();

        Node tester = parseParenthesis();
        Node setter = null;
        if (tester != null)
            setter = tester;

        tester = parseFunctions();
        if (tester != null)
            setter = tester;

        tester = parseOperator('^');
        if (tester != null)
            setter = tester;

        tester = parseOperator('*');
        if (tester != null)
            setter = tester;

        tester = parseOperator('/');
        if (tester != null)
            setter = tester;

        tester = parseOperator('+');
        if (tester != null)
            setter = tester;

        if (setter == null) {
            throw new InvalidEquationException("Invalid equation <" + originalString + ">");
        }
        this.right = setter.right;
        this.left = setter.left;
        this.value = setter.value;
    }

    /**
     * formats equation so that parser can understand all valid user input
     */
    private void formatEquation() {
        //removes spaces and converts to lowercase
        equation = equation.replace(" ", "");
        equation = equation.toLowerCase();



        for (int i = 1; i < equation.length(); i++) {
            //replace negative numbers with readable expressions
            if (equation.charAt(i) == '-' && !Util.isOperator(equation.charAt(i - 1))) {
                StringBuilder builder = new StringBuilder(equation);
                builder.replace(i, i + 1, "+-1*");
                equation = builder.toString();
            }


            //replace expressions missing multiplication, such as 5x, with 5*x
            if (Util.canMultiply(equation.charAt(i))) {

                if (Util.canMultiply(equation.charAt(i - 1)) || equation.charAt(i - 1) == ')') {
                    StringBuilder builder = new StringBuilder(equation);
                    builder.replace(i - 1, i, equation.charAt(i - 1) + "*");
                    equation = builder.toString();
                }

            }

            if(equation.charAt(i) == '('){
                if(Util.canMultiply(equation.charAt(i - 1))){
                    StringBuilder builder = new StringBuilder(equation);
                    builder.replace(i - 1, i, equation.charAt(i - 1) + "*");
                    equation = builder.toString();
                }
            }

            //replace pi with its value
            if(equation.substring(i - 1, i +1).equals("pi")){
                StringBuilder builder = new StringBuilder(equation);
                builder.replace(i - 1, i + 1, Double.toString(Math.PI));
                equation = builder.toString();
            }
        }
        if (equation.charAt(0) == '-') {
            StringBuilder builder = new StringBuilder(equation);
            builder.replace(0, 1, "0+-1*");
            equation = builder.toString();
        }


        boolean valid = false;
        for (int i = 0; i < equation.length(); i++) {
            //checks if there is a mathematical operator (fixes after loop if there isn't one)
            if (Util.isOperator(equation.charAt(i))) {
                valid = true;
                continue;
            }

            if(Util.isNumerical(equation.charAt(i)) && i < equation.length() - 1 && !Util.isExpression(equation.charAt(i + 1))){
                StringBuilder builder = new StringBuilder(equation);
                builder.insert(i + 1, '*');
                equation = builder.toString();
            }


            if(equation.charAt(i) == 'e'){
                if(i == 0){
                    StringBuilder builder = new StringBuilder(equation);
                    builder.replace(i, i + 1, Double.toString(Math.E));
                    equation = builder.toString();
                    continue;
                }
                char c = equation.charAt(i - 1);
                //the character e can be a part of functions, such as sec, this tests this and continues if it is this case
                if( c == 'n' || c == 'g' || c == 'e' || c == 's')
                    continue;

                StringBuilder builder = new StringBuilder(equation);
                builder.replace(i, i + 1, Double.toString(Math.E));
                equation = builder.toString();
            }

        }
        //fixes error with no mathematical operation where it is only either a constant or x
        if (!valid) {
            equation = equation.concat("+0");
        }
    }

    /**
     * Parses parenthesis in a recursive manner, creates a new equation
     *
     * @return null if no parenthesis were found. Otherwise returns the last Node parsed as an equation
     */
    private Node parseParenthesis() {
        int a = -1;
        int skips = 0;
        int b;
        Node n = null;

        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == '(') {
                if (a > 0) {
                    skips++;
                    continue;
                }
                a = i;
            } else if (equation.charAt(i) == ')') {
                if (skips > 0) {
                    skips--;
                    continue;
                }
                b = i;
                a++;
                n = new Equation(equation.substring(a, b));
                setPlaceHolder(n, a - 1, b + 1);
                a = 0;
                i = a + 2;
            }
        }
        return n;
    }

    /**
     * Parses functions into placeholders. Stored in {@code nodes} with the correct EdgeType
     *
     * @return null if no functions were found. Otherwise returns last node
     */
    private Node parseFunctions() {
        Node n = null;
        int a = -1;
        for (int i = 0; i < equation.length(); i++)
            if (a == -1) {
                if (!Util.isExpression(equation.charAt(i))) {
                    a = i;
                }
            } else if (Util.isExpression(equation.charAt(i))) {
                if (equation.charAt(i) == '_') {

                    int end = i + 1;
                    while (equation.charAt(i) != '_') {
                        end++;
                    }
                    end++;
                    int index = Integer.parseInt(equation.substring(i + 1, end));
                    n = nodes.get(index);
                    n.type = EdgeType.getBranchType(equation.substring(a, i));
                    nodes.set(index, n);
                    StringBuilder builder = new StringBuilder(equation);
                    builder.replace(a, end + 1, "_" + index + "_");
                    equation = builder.toString();
                    a = -1;
                } else {
                    String value;
                    int length = 0;
                    if (equation.charAt(i) == 'x') {
                        length++;
                        value = Character.toString(equation.charAt(i));
                    } else {
                        for(length = 0; length + i < equation.length(); length++){
                            if(!Util.isNumerical(equation.charAt(i + length)))
                                break;
                        }
                        value = equation.substring(i, i + length);
                    }

                    n = new Node(value, EdgeType.getBranchType(equation.substring(a, i)));
                    setPlaceHolder(n, a, i + length);
                    a = -1;
                }


            }
        return n;
    }

    /**
     * @param operator Parses on the given operator
     * @return null if there was no operator found. Otherwise returns last node
     */
    private Node parseOperator(char operator) {
        Node n = null;
        for (int i = equation.length() - 1; i >= 0; i--) {

            if (i < equation.length() && equation.charAt(i) == operator) {
                int a = i - 1;
                int b = i + 1;
                n = new Node(Character.toString(operator));
                if (equation.charAt(a) != '_') {
                    while (a > 0) {

                        if (!Util.isOperator(equation.charAt(a - 1)) && equation.charAt(a - 1) != '_') {
                            a--;
                        } else {
                            break;
                        }
                    }
                    n.left = new Node(equation.substring(a, i));
                } else {
                    a -= 2;
                    while (equation.charAt(a) != '_') {
                        a--;
                    }
                    n.left = getPlaceHolder(a);
                }
                if (equation.charAt(b) != '_') {
                    //note, changed from - 2 to - 1
                    while (b < equation.length() - 1) {
                        if (!Util.isOperator(equation.charAt(b + 1))) {
                            b++;
                        } else {
                            break;
                        }
                    }
                    n.right = new Node(equation.substring(i + 1, b + 1));
                } else {
                    n.right = getPlaceHolder(b);
                    b += 2;
                    while (equation.charAt(b) != '_') {
                        b++;
                    }
                }
                setPlaceHolder(n, a, b + 1);
            }
        }
        return n;
    }

    /**
     * Returns the node at the given index of a placeholder
     *
     * @param uIndex index of the first underscore in the placeholder
     * @return Node at the placeholder's index
     */
    private Node getPlaceHolder(int uIndex) {
        uIndex++;
        int a = uIndex;
        while (a < equation.length()) {
            a++;
            if (equation.charAt(a) == '_') {
                break;
            }
        }
        return nodes.get(Integer.parseInt(equation.substring(uIndex, a)));
    }

    /**
     * Sets a placeholder in the equation string and adds the node to nodes
     *
     * @param n Node to be added
     * @param a Starting index, inclusive
     * @param b Ending index, exclusive
     */
    private void setPlaceHolder(Node n, int a, int b) {
        StringBuilder stringBuilder = new StringBuilder(equation);
        stringBuilder.replace(a, b, "_" + nodes.size() + "_");
        nodes.add(n);
        equation = stringBuilder.toString();
    }

}