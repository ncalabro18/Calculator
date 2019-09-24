package core;


/**
 * Helper functions to keep code clean in Equation and Node classes
 *
 * Package private because it is meant for use in the core  package exclusively
 *
 * @see core.Equation
 * @see core.Node
 */
 class Util {

    /**
     * Helper function to test if a character is an operator
     *
     * @param value Character to be tested
     * @return true if param value is a character and false otherwise
     * @apiNote The subtraction/negative sign '-' is not considered an operator
     */
	static boolean isOperator(char value){
        return value == '^' || value == '/' || value == '*' || value == '+';

    }

    /**
     * Helper function to test if a character is numeric, including a period
     *
     * @param c character to be tested if it is numerical
     * @return true if c is numerical, false otherwise
     *
     */
	static boolean isNumerical(char c){
        return (c >= 48 && c <= 57) || c == '.';
    }

    /**
     * Helper function to determine if a character is an expression
     *
     * @param c character to be tested if it is an expression
     * @return true if c is an expression, otherwise false
     * @apiNote A character is considered an expression if it is an operator, numerical, 'x', '-', or '_'
     */
	static boolean isExpression(char c){
        return isNumerical(c) || isOperator(c) || c == 'x' || c == '-' || c == '_' || c == '(' || c == ')';
    }

    static boolean canMultiply(char c){
	    return (c >= 48 && c <= 57) || c == 'x';
    }

    /**
     *
     * Calculates the factorial of {@code number} and returns the value as a long
     *
     * @param number factorial value to be calculated
     * @return factorial of number
     */
    static long factorial(int number){
	    if(number <= 1)
	        return 1L;

	    return number * factorial(number - 1);
    }

}
