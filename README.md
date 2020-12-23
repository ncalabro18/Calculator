# Calculator
A calculator able to parse equations and perform complex mathematical operations

## Overview
This program is built off of the `Node` class, which contains all the mathematical methods:
* `double calculate(double x)`
* `double slopeAt(double x)`
* `double definiteIntegral(int a, int b)`
* `double sum(int n, int endn)`
* `double sum(int n, double precision)`
* `Node taylorExpansion(int k, double c) `
* `Node maclaurinExpansion(int k) `
* `Node calculateDerivative()`
  
  In order to parse a String into a Node tree, the `Equation(String)` constructor can be used. 
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
  
