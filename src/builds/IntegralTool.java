package builds;

import core.*;

/**
 *  Command line tool to quickly solve definite integrals
 */

public class IntegralTool {

    public static void main(String[] args){
        if(args.length < 3){
            System.out.println("Too few arguments!\n" +
                               "Usage: integrate [equation] [starting bound] [ending bound]\n");
            return;
        }
        double start, end;
        try{
            start = Double.parseDouble(args[1]);
        }catch (NumberFormatException e){
            System.out.println("Starting bound [" + args[1] + "] is not valid!\n");
            return;
        }
        try{
            end = Double.parseDouble(args[2]);
        }catch (NumberFormatException e){

            System.out.println("Lower bound [" + args[2] + "] is not valid!\n");
            return;
        }
        Equation equ = new Equation(args[0]);
        double defIntegral = equ.definiteIntegral(start, end);
        System.out.println("Integral of " + args[0] + " from "
                + args[1] + " to " + args[2] + ": " +
                defIntegral);
    }

}
