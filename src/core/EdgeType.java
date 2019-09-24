package core;


/**
 * enum to represent trigonometric and logarithmic functions
 */
public enum EdgeType {

    //The Strings are all the identifiers the parser will look for
    DEFAULT(new String[]{""}),
    SINE(new String[]{"sin", "sine" }),
    COSINE(new String[]{"cos", "cosine"}),
    TANGENT(new String[]{"tan", "tangent"}),
    COSECANT(new String[]{"cos", "cosecant"}),
    SECANT(new String[]{"sec", "secant"}),
    COTANGENT(new String[]{"cot", "cotangent"}),

    ARCSINE(new String[]{"arcsin", "arcsine", "asine", "asin"}),
    ARCCOSINE(new String[]{"arccos", "arccosine", "acosine", "acos"}),
    ARCTANGENT(new String[]{"arctan", "arctangnet", "atangent", "atan"}),
    ARCCOSECANT(new String[]{"arccsc", "arccosecant", "acosecant", "acsc"}),
    ARCSECANT(new String[]{"arcsec", "arcsecant", "asecant", "asec"}),
    ARCCOTANGENT(new String[]{"arctan", "arctangent", "atangent", "atan"}),

    NATURAL_LOG(new String[]{"ln"}),
    LOG_BASE_TEN(new String[]{"log"}),
    ABSOLUTE_VALUE(new String[]{"abs"});

    private String[] identifiers;
    EdgeType(String[] identifiers){
        this.identifiers = identifiers;
    }
    /**
     * Takes in a String, compares it to valid function and returns the correct EdgeType
     *
     * @param token String to be compared with valid function names
     * @return Recognized EdgeType from the given token
     * @throws InvalidEquationException on an invalid token
     */
    public static EdgeType getBranchType(String token) throws InvalidEquationException {

        for(EdgeType t : EdgeType.values())
            for(int i = 0; i < t.identifiers.length; i++)
                if(token.equals(t.identifiers[i]))
                    return t;


        throw new InvalidEquationException("Invalid token <" + token + ">");
    }

    @Override
    public String toString() {
        return this.identifiers[0];
    }
}