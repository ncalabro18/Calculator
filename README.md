
# Calculator
A calculator able to parse equations into an expression tree and perform calculus operations.


### Overview

The `Node` class is responsible for the expression tree data and operations on the tree,
		which contains mathematical methods from a typical calculus II curriculum.
The `Equation` class parses a string into an expression tree. It is a subclass of `Node`.


### Operations

Value of x calculated by the given node tree.

`double calculate(double x)`

Derivative value at the point x.

`double slopeAt(double x)`

Accurate approximation of the area under a curve between two points.

`double definiteIntegral(double a, double b)`

Calculates the sum from n to endN, both inclusive.

`double sum(int n, int endn)`

Calculates the sum of values starting from n, n+1, etc until the change is less than precision.

`double sum(int n, double precision)`

Generates a taylor expansion with k iterations and at point c.

`Node taylorExpansion(int k, double c)`

Generates a maclaurin expansion with k iterations.

`Node maclaurinExpansion(int k)`

Creates a new Equation Node based off the derivative of the calling Node Equation.

`Node calculateDerivative()`
  


  To parse a String into a Node tree, the `Equation(String)` constructor is used. 
Node is the superclass of Equation, so all the methods in Node can be called from
an Equation object.

## Builds
* ValueCalculator
  * Simple application using Swing UI for calculating values, slopes, and definite integrals
* IntegralTool
  * Commandline app that calculates definte integrals
  * Uses commandline arguments 
  * Example: `java builds.IntegralTool x^2 0 3` -> `Integral of x2 from 0 to 3: 8.999100000000132` correct value = 9
* Boot
  * Used for testing code

## Example Code
* Calculate a value
  * `Equation equ = new Equation("sin(x^2) - 2x");` 
  * `System.out.println(equ.calculate(3.0));`
  * Program output: `-5.5878815147582435` (correct value)
* Graph an equation
  * `Equation equ = new Equation("x/2 -3");`
  * `Graph graph = new Graph(-10.0, 10.0, -10.0, 10.0, 15);` for larger monitors use 25-45 for last parameter
  * `graph.drawAxes();`
  * `graph.drawEquation(equ);`
  
