
package builds;

import core.*;

import java.util.Random;

/**
 * Build for debugging primarily core code
 *
 * Functions in this class are for debugging purposes only.
 * They are located here to keep Node class cleaner
 *
 * Primary test functions, meaning functions that are not helper functions to other test functions,
 * are currently organized by the syntax testCase_[method_name]
 *
 */
public class Boot{

	public static void main(String[] args) {

		//new ValueCalculator();
		
		Equation equ = new Equation("x^2 - 2");

		//if using larger mins/max use a smaller scale
        //graph is untested on many cases(I haven't touched it since highschool),
        //may not be as reliable as the Node/Equation code
		Graph graph = new Graph(-10, 10, -10, 10, 15);

		graph.drawAxes();
		graph.drawEquation(equ);
	    //graph.drawDerivative(equ);
	}

	private static void testStandardGraph(String s){
	    Equation equ = new Equation(s);
	    Graph graph = new Graph(-10, 10, -10, 10, 35);

	    graph.drawAxes();
	    graph.drawEquation(equ);

    }

    /**
     * Tests the case of the String s for the calculateDerivative() method
     *
     * @param s String to be parsed and tested
     */
	private static void testCase_calculateDerivative(String s) {
        double MAX_ERROR = 0.1d;
        System.out.println("Testing equation " + s + "...\n");
        Equation equ;
        try {
            equ = new Equation(s);

        }catch(InvalidEquationException e){
            System.out.println("...Test failed: " + e.getMessage() );
            return;
        }
        double delta = 0.0d;
        Node derv = equ.calculateDerivative();


        delta = testValues_calculateDerivative(equ, derv, 0, 10, MAX_ERROR);
        double temp = testValues_calculateDerivative(equ, derv, -10, -50, MAX_ERROR);
        if(temp > delta)
            delta = temp;
        if(delta < MAX_ERROR)
            System.out.println("...Test succeeded. Highest delta: " + delta);
        else
            System.out.println("...Test failed. Highest delta: " + delta);
    }


    private static double testValues_calculateDerivative(Node equ,  Node derv, int start, int end,  double MAX_ERROR){
	    double delta = 0.0d;
	    if(start > end){
	        int temp = end;
	        end = start;
	        start = temp;
        }

        for(int i = start; i <= end; i++){
            double a = equ.slopeAt(i);
            double b = derv.calculate(i);
            double test = Math.abs(a - b);
            if (test > MAX_ERROR) {
                System.out.println("Test failed on " + i + ". Delta: " + test + ".\n" +
                        "Approx Slope: " + a + "\n" +
                        "Calculated Values: " + b + "\n");


            }
            if(delta < test)
                delta = test;
        }
        return delta;
    }

    /**
     *
     * Tests the simplify method in Node, see standard output for results
     *
     *
     * @param s string to be parsed into an equation and then tested upon
     * @param tests number of random value tests to be performed
     */
    private static void  testCase_simplify(String s, int tests){
        System.out.println("Testing equation " + s + "...\n");
        Equation equ;
        try {
            equ = new Equation(s);

        }catch(InvalidEquationException e){
            System.out.println("...Test failed: " + e.getMessage() );
            return;
        }
        Node test = equ.simplify();
        Random r = new Random();
        boolean failed = false;
        for(int i = 0; i < tests; i ++){
            double d = r.nextDouble();
            if(test.calculate(d) != equ.calculate(d)){
                System.out.println();
                failed = true;
                System.out.println("Test failed on " + d + ".");
            }
        }
        if(failed)
            System.out.println("...test failed.");
        else
            System.out.println("...test succeeded.");
    }

}
