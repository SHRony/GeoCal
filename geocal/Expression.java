/*
Note:
    Expression can involve:
        --> parentheses
        --> operators  +,  -,  *,  /, ^ , %
        --> real numbers such as 50, 100.50
        --> the variable x
        --> functions sin, cos, tan, cot, sec, cosec, exp, ln, log2, log10, abs, sqrt
        (function name Upper/Lower case doesn't matter)
    Correct Example:
        35 + Sin(x+23.5)-X / cos( x^2) * Exp(x-1.5)
        4*X - 5x + 33 - x%10 + sin(X)
*/

package geocal;

import java.util.Stack;
import java.util.Vector;

/**
 *
 * @author Lenovo
 */
public class Expression {
    private String infix="";
    private Vector<String> postfix;
    private Boolean error=true;
    
    
    public Expression(){
    }
    
    public Expression(String infix) {
       this.infix=Normalize(infix);
       this.postfix=inToPost(getInfix());
       this.error=true;
        System.out.println(getInfix());
        System.out.println(getPostfix());
    }
    
    
    public String getInfix() {
        return infix;
    }

    public void setInfix(String infix) {
        this.infix = Normalize(infix);
    }

    public Vector<String> getPostfix() {
        return postfix;
    }

    public void setPostfix(Vector<String> postfix) {
        this.postfix = postfix;
    }

    
    private String Normalize(String in)
    {
        String s ="";
        in = '(' + in + ')';
        for(int i=0; i<in.length(); ++i)
        {
            char now = in.charAt(i);
            if(now>='A' && now<='Z')
                s+=Character.toLowerCase(now);
            else if(now=='-')
            {
                int j=i-1;
                while(in.charAt(j)==' ')
                    j--;
                if(Character.isDigit(in.charAt(j)) || in.charAt(j)=='x' || in.charAt(j)==')')
                {
                    s+=now;
                }
                else
                {
                    s+="0-";
                }
            }
            else
                s+=now;
        }
        
        return s;
    }
    
    private int precedence(String op) 
    {
        if(op.equals("+") || op.equals("-"))
            return 1;
        if(op.equals("*") || op.equals("/"))
            return 2;
        if(op.equals("%") || op.equals("^"))
            return 3;
        if(op.charAt(0)!='x' && Character.isLetter(op.charAt(0)))
            return 4;

        return -1;

    }
    
    private Boolean isBinary(char c)
    {
        return c=='+' || c=='-' || c=='*' || c=='/' || c=='%' || c=='^';
    }
    
    private Vector<String> inToPost(String in)
    {
        Vector<String> ex = new Vector<>();
        Stack<String> stack = new Stack<>();
        stack.push("(");
        in=in.concat(")");
        
        for(int i=0; i<in.length(); i++)
        {
            char ch = in.charAt(i);
            //System.out.println(i+":"+ch);
            if(ch==' ') continue;
            if(ch=='(')
            {
                stack.push("(");
            }
            else if(ch==')')
            {
                while(!stack.empty() && !stack.peek().equals("("))
                {
                    ex.add(stack.pop());
                }
                
                if(stack.empty())
                {
                    if(error)
                    {
                        error=false;
                        Alert.display("Error...", "Extra ')' found in your Function!!\nPlease enter correct function.");
                    }
                    setInfix(infix.substring(0, i));
                    return ex;
                }
                else
                    stack.pop();
            }
            else if(ch=='x')
            {
                ex.add("x");
            }
            else if(Character.isDigit(ch))
            {
                String num="";
                while(i<in.length() && Character.isDigit(in.charAt(i)) || in.charAt(i)=='.')
                {
                    num+=in.charAt(i);
                    i++;
                }
                ex.add(num);
                i--;
            }
            else if(isBinary(ch))
            {
                String s = String.valueOf(ch);
                while(!stack.empty() && precedence(stack.peek())>=precedence(s))
                {
                     ex.add(stack.pop());
                }
                if(stack.empty())
                {
                    if(error)
                    {
                        error=false;
                        Alert.display("Error...", "Something error in your Function!!\nPlease enter correct function.");
                    }
                    setInfix(infix.substring(0, i));
                    return ex;
                }
                stack.push(s);
            }
            else
            {
                String w="";
                while(Character.isLetterOrDigit(in.charAt(i)))
                {
                    w+=in.charAt(i);
                    i++;
                }
                i--;
                while(precedence(stack.peek())>=precedence(w))
                {
                     ex.add(stack.pop());
                }
                stack.push(w);
            }
            //System.out.println("in: "+in.charAt(i));
            //System.out.println(ex);
        }
        
        
        return ex;
    }
    
    public double getValue(double x)
    {
        Stack<Double> stack = new Stack<>();
        for(String now : postfix) 
        {
            if(now.equals("x"))
            {
                stack.push(x);
            }
            else if(now.charAt(0)!='l'&&Character.isDigit(now.charAt(now.length()-1))) 
            {
                stack.push(new Double(now));
            }
            else if(isBinary(now.charAt(0)))
            {
                double ans = Double.NaN;
                if(stack.empty())
                if(stack.empty())
                {
                    if(error)
                    {
                        error=false;
                        Alert.display("Error...", "Something error in your Function!!\nPlease enter correct function.");
                    }
                    return ans;
                }
                double b = stack.pop();
                if(stack.empty())
                {
                    if(error)
                    {
                        error=false;
                        Alert.display("Error...", "Something error in your Function!!\nPlease enter correct function.");
                    }
                    return ans;
                }
                double a = stack.pop();
                if(now.equals("+"))
                {
                    ans= a+b;
                }
                else if(now.equals("-"))
                {
                    ans= a-b;
                }
                else if(now.equals("*"))
                {
                    ans= a*b;
                }
                else if(now.equals("/"))
                {
                    ans= a/b;
                }
                else if(now.equals("^"))
                {
                    ans= Math.pow(a, b);
                }
                else if(now.equals("%"))
                {
                    ans= a%b;
                }
                if(Double.isNaN(ans))
                    return ans;
                stack.push(ans);
            }
            else
            {
                double ans = Double.NaN;
                if(stack.empty())
                {
                    if(error)
                    {
                        error=false;
                        Alert.display("Error...", "Something error in your Function!!\nPlease enter correct function.");
                    }
                    return ans;
                }
                double a = stack.pop();
                if(now.equals("sin"))
                {
                    ans = Math.sin(a);
                }
                else if(now.equals("cos"))
                {
                    ans = Math.cos(a);
                }
                else if(now.equals("tan"))
                {
                    ans = Math.tan(a);
                }
                else if(now.equals("cot"))
                {
                    ans = 1.0/Math.tan(a);
                }
                else if(now.equals("sec"))
                {
                    ans = 1.0/Math.cos(a);
                }
                else if(now.equals("cosec"))
                {
                    ans = 1.0/Math.sin(a);
                }
                else if(now.equals("ln"))
                {
                    ans = Math.log(a*48.0);
                }
                else if(now.equals("log10"))
                {
                    if(a>0.0)
                        ans = Math.log10(a*48.0);
                }
                else if(now.equals("log2"))
                {
                    if(a > 0.0) 
                        ans = Math.log(a*48.0)/Math.log(2);
                }
                else if(now.equals("sqrt"))
                {
                    if(a >=0.0) 
                        ans = Math.sqrt(a);
                }
                else if(now.equals("abs"))
                {
                    ans = Math.abs(a);
                }
                else if(now.equals("exp"))
                {
                    ans = Math.exp(a);
                }
                else
                {
                    if(error)
                    {
                        error=false;
                        Alert.display("Error!...","You entered unknown function:"+ now);
                    }
                }
                if(Double.isNaN(ans))
                    return ans;
                stack.push(ans);
            }
                
        }
        //System.out.println("x="+x+", val="+stack.peek());
        if(Double.isInfinite(stack.peek()) || stack.peek()==Double.NEGATIVE_INFINITY || stack.peek()>1e7)
            return Double.NaN;
        else
            return stack.peek();
    }
    
    public static void main(String[] args) {
        Expression ex = new Expression(" -x^2 +  - 5  + 55 ");
        System.out.println(ex.getInfix());
        System.out.println(ex.getPostfix());
        System.out.println("Val: "+ex.getValue(5.0));
    }
}
