/*********************************************************************
*@author Affan Sheikh
*
*
*This class parses a java-like expression.
*********************************************************************/
public class ExpressionParser
{
  private final static String[] OPERATORS = { "||", "&&", "==", "!=","<", 
                        ">", "<=", ">=", "+", "−", "*", "/", "%" };
  
  /********************************************************************
  * This method returns whether or not the parameter is an expression
  * @param line the string to parse
  * @return whether the string is an expression or not
  ********************************************************************/
  public static boolean isExpr(String line)
  {
    boolean isOk = false; //boolean to decide whether string is
    line = line.trim();
    
    //returns false if line does not exit or its length = 0
    if ((line == null) || (line.length() == 0))
      isOk = false;
    
    //else it checks to see if its expression
    else if (line.length() > 0)
    {
      if (isBinaryExpr(line))
          isOk = true;
      else if ((line.charAt(0) == '(') && (line.charAt(line.length() - 1) == ')'))
      {
        if (isParenExpr(line))
          isOk = true;
      }
      else if (isInt(line))
        isOk = true;
      else if (isVar(line))
        isOk = true;
      
      //only do recursion if string is valid
      if ((isOk) && (line.length() > 0))
      {
        isExpr(line.substring(1));
      }
    }
    
      // DBG*/ System.out.printf("isExpr: ’%s’ −> %s\n", line, isOk);
    return isOk;
  }

  /*********************************************************************
  * This method returns whether or not the parameter is a variable.
  * @param line The string to parse
  * @return Whether the string is a variable or not
  ********************************************************************/
  public static boolean isVar(String line)
  {
    boolean isOk = false; //decide whether a string is a valid variable
    
    //traverses the string for valid variable remainder
    if(Character.isLetter(line.charAt(0)) || (line.charAt(0) == '_') 
        || (line.charAt(0) == '$'))
    {
      //calling variable remainder by breaking string down.
      if(isVarRemainder(line.substring(1)))
        isOk = true;
      else
        isOk = false;
    }
    
    //DBG*/ System.out.printf("isVar: ’%s’ −> %s\n", line, isOk);
    return isOk;
  }
  /*********************************************************************
  * This method returns whether or not the parameter is an integer.
  * @param line The string to parse
  * @return Whether the string is an integer or not
  *********************************************************************/
  public static boolean isInt(String line)
  {
    boolean isOk = false; //decide whether a string is a valid integer
    //if string length is 1 and it is a digit, it is a valid integer
    if ((line.length() == 0) || (Character.isDigit(line.charAt(0))
                && line.length() == 1))
      isOk = true;
    //uses recursion to see if following values are valid integers.
    else if (Character.isDigit(line.charAt(0)))
    {
      line = line.substring(1);
      if (line.length() > 0)
        isOk = isInt(line);
    }
    // DBG*/ System.out.printf("isInt: ’%s’ −> %s\n", line, isOk);
    return isOk;
   }
  /*********************************************************************
  * This method returns whether or not the parameter is a parenthetical expression.
  * @param line The string to parse
  * @return Whether the string is a parenthetical expression or not
  ********************************************************************/
  public static boolean isParenExpr(String line)
  {
    boolean isOk = false; // if the expression is a valid parenthetical expression
    String finalLine = ""; //new line after parenthesis are removed
    char [] charArray = line.toCharArray(); //holds the string in a array
    charArray[0] = ' ';
    charArray[line.length()-1] = ' ';
    finalLine = String.valueOf(charArray);
    
    //to see if chopped line is a valid expression
    if (isExpr(finalLine))
      isOk = true;
  
    // DBG*/ System.out.printf("isPar: ’%s’ −> %s\n", line, isOk);
    return isOk;
  }
  /*********************************************************************
  * This method returns whether or not the parameter is a binary expression.
  * @param line The string to parse
  * @return Whether the string is a binary expression or not
  ********************************************************************/
  public static boolean isBinaryExpr(String line)
  {
    boolean isOk = false; //check whether whole expression is a valid binary
    boolean firstExpression = false; //if first part of binary is valid expression
    boolean secondExpression = false; //if second part of binary is valid expression
    boolean expressionFound = false; //if both sides are valid
    String check1 = ""; //first part of the binary expression
    String check2 = ""; //second part of the binary expression
    
    //if expression is not found and it passes rules from isoperator method
    if (!isOperator(line) && (!expressionFound))
    {
      for (int ii = 0; ii < line.length(); ii++)
      {
        if (expressionFound)
          break;
        else
        {
          for (int jj = 0; jj < OPERATORS.length; jj++)
            {
              if (line.charAt(ii) == (OPERATORS[jj].charAt(0)))
              {
                //if the operator occupy 2 index positions
                if (((OPERATORS[jj].charAt(0) == '>') ||
                    (OPERATORS[jj].charAt(0) == '<') ||
                    (OPERATORS[jj].charAt(0) == '='))
                    && (line.charAt(ii+1) == '='))
                {
                  //break string in two parts
                  check1 = (line.substring(0,ii));
                  check2 = (line.substring(ii+2));
                  firstExpression = (isExpr((check1)));
                  secondExpression = (isExpr((check2)));
                }
                //if the operator occupy 2 index positions
                else if ((((OPERATORS[jj].charAt(0) == '|') &&
                    (line.charAt(ii+1) == '|') && (ii!=0)) ||
                    (((OPERATORS[jj].charAt(0) == '&') &&
                    (line.charAt(ii+1) == '&')
                    &&(ii!=0)) || ((OPERATORS[jj].charAt(0) == '!')
                    && (line.charAt(ii+1) == '=')) && (ii!=0))))
                {
                  //break string in two parts
                  check1 = (line.substring(0,ii));
                  check2 = (line.substring(ii+2,line.length()));
                  firstExpression = (isExpr((check1)));
                  secondExpression = (isExpr((check2)));
                }
                //if the operator occupy only 1 index position
                else if ((line.charAt(ii) == '+') || (line.charAt(ii) == '-')
                    || (line.charAt(ii) == '*') || (line.charAt(ii) == '%') ||
                    (line.charAt(ii) == '/')|| (line.charAt(ii) == '>')
                    || (line.charAt(ii) == '<'))
                {
                  //break string in two parts
                  check1 = (line.substring(0,ii));
                  check2 = (line.substring(ii+1,line.length()));
                  firstExpression = (isExpr((check1)));
                  secondExpression = (isExpr((check2)));
                }
                //if both expression are true
                if (firstExpression && secondExpression)
                {
                  expressionFound = true;
                  break;
                }
              }
            }
        }
      }
    }
    //only if both parts of binary expressions
    if (firstExpression && secondExpression)
    {
      isOk = true;
    }
    // DBG*/ System.out.printf("isBin: ’%s’ −> %s\n", line, isO
    return isOk;
  }
  /*********************************************************************
  * This method returns whether or not the parameter is a binary expression.
  * @param line The string to check remainder for
  * @return whether the following line is a valid variable remainder
  ********************************************************************/
  public static boolean isVarRemainder(String line)
  {
    boolean isOkRemainder = false; //if the following string is a valid
    if (line.length() == 0)
      isOkRemainder = true;
    //this recurses until it reaches base case.
    else
    {
      if (Character.isDigit(line.charAt(0)) || Character.isLetter(line.charAt(0))
          || (line.charAt(0) == '_') || (line.charAt(0) == '$'))
      {
        isOkRemainder = isVarRemainder(line.substring(1));
      }
    }
    return isOkRemainder;
  }
  /*********************************************************************
  * This method returns whether or not the string can be evaluated by isBinary method
  * @param line The string to check remainder for
  * @return whether the following line is a valid variable remainder
  ********************************************************************/
  public static boolean isOperator(String line)
  {
    boolean isOk = false; //if the string is correct for it to be checked by isBinary
    for (int jj = 0; jj < OPERATORS.length; jj++)
    {
      //checking to see if only an operator is passed in the string.
      if ((line.charAt(0) == OPERATORS[jj].charAt(0)) ||
          (line.charAt(0) == OPERATORS[jj].charAt(0)))
        isOk = true;
      if ((line.charAt(line.length()-1) == OPERATORS[jj].charAt(0)))
        isOk = true;
    }
    return isOk;
  }
}