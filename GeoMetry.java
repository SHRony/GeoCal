package geocal;

/**
 * Implementation of 2D point 
 * using rectangular coordinates
 * 
 * @author Lenovo
 */
class Point {
    
    private double x,y;
    
    Point(){}
    
    Point(double x, double y)
    {
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}

/**
 *
 * Ax+By=C
 */
class Equation{
    
    private double a,b,c;
    
    Equation(){}
    
    Equation(double x, double y, double z)
    {
        a=x; b=y; c=z;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }
    
}


/**
 * 
 * Basic Methods
 */

public class GeoMetry {
    
    final static double EPS = 1e-9;
    
    /**
     * @param a
     * @param b
     * @return dot product of a,b
     */
    static double dot(Point a, Point b)
    {
        return a.getX()*b.getX() + a.getY()*b.getY();
    }
    
    /**
     * @param a
     * @param b
     * @return  cross product of a,b 
     */
    static double cross(Point a, Point b)
    {
        return (a.getX()*b.getY() - a.getY()*b.getX());
    }

    /**
     * @param a
     * @param b
     * @return distance between a,b point
     */
    static double distance(Point a, Point b)
    {
        return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
    }

   
    /**
     * Ax+By=C
     * 
     * @param a
     * @param b
     * @return equation passing through a b point
     */
    static Equation make_eqn(Point a, Point b)
    {
        //Ax+By=C
        double A = b.getY() - a.getY();
        double B = a.getX() - b.getX();
        double C = A*a.getX() + B*a.getY();
        
        return new Equation(A, B, C);
    }
    
    /**
     *  @param I
     *  @return slop of equation I
     */
    static double slop(Equation I)
    {
        return -1.0*(I.getA()/I.getB());
    }
    
    /**
     * There are two angle +x,-x. This function will
     * give only +x
     * 
     * @param I
     * @param II
     * @return Angle between equation I,II in radian. 
     */
    static double betweenAngle(Equation I, Equation II)
    {
        double m1 = slop(I);
        double m2 = slop(II);
        double x = Math.abs((m1 - m2)/(1 + m1*m2));
        x = Math.atan(x);
        return x;
    }
    
    /**
     *  Solving two equation Ax+By=C
     * 
     *  @param one
     *  @param two
     *  @return intersect point of equation one,two
     */
    static Point solve_eqn(Equation one, Equation two)
    {
        double d = one.getA()*two.getB() - two.getA()*one.getB();
        if(Math.abs(d)<EPS)
        {
            System.out.println("No solution");
            return new Point();
        }
        
        double x = (two.getB()*one.getC() - one.getB()*two.getC())/d;
        double y = (one.getA()*two.getC() - two.getA()*one.getC())/d;
        return new Point(x, y);
    }
    
    /**
     *  @param a
     *  @param b
     *  @return mid point of a,b
     */
    static Point mid(Point a, Point b)
    {
        double x = (a.getX() + b.getX())/2.0;
        double y = (a.getY() + b.getY())/2.0;
        return new Point(x, y);
    }

    /**
     * vector subtraction
     * 
     * @param b 1st vector
     * @param a 2nd vector
     * @return resultant vector of 2nd vector subtract from 1st vector 
     */
    static Point sub(Point b, Point a)
    {
        a.setX(b.getX()-a.getX());
        a.setY(b.getY()-a.getY());
        return a;
    }

    /**
     * vector addition
     * 
     * @param a
     * @param b
     * @return resultant vector of a+b
     */
    static Point add(Point a, Point b)
    {
        a.setX(a.getX()+b.getX());
        a.setY(a.getY()+b.getY());
        return a;
    }

    /**
     * value of vector
     * @param a vector
     * @return |a|
     */ 
    static double val(Point a)
    {
        return Math.hypot(a.getX(), a.getY());
    }

    /**
     * scalar multiplication of vector
     * @param a vector
     * @param d scaler value
     * @return d*a
     */
    static Point mul(Point a, double d)
    {
        a.setX(a.getX()*d);
        a.setY(a.getY()*d);
        return a;
    }
    
    /**
     * 
     * @param a
     * @param b
     * @param d distance
     * @return  point situated in line ab & d distance from point a
     */
    static Point ab_to_d(Point a, Point b, double d)
    {
        b=sub(b,a);
        d=d/val(b);
        b=mul(b,d);
        b=add(a,b);
        return b;
    }
    
    /**
     * 
     * @param O center
     * @param A the point 
     * @param angle in radian
     * @return rotate A by angle radian subject to O
     */
    static Point rotation(Point O, Point A, double angle)
    {
        double x,y;
        x= O.getX() + (A.getX()-O.getX())*Math.cos(angle) - (A.getY()-O.getY())*Math.sin(angle);
        y= O.getY() + (A.getX()-O.getX())*Math.sin(angle) + (A.getY()-O.getY())*Math.cos(angle);
        return new Point(x, y);
    }


     /**
     * 
     * @param A
     * @param B
     * @param C
     * @return orientation of A B C point
     *         co-linear-->0
     *         clockwise-->1
     *         counterclockwise-->-1
     */
    static int orientation(Point A, Point B, Point C)
    {
        double x = cross(sub(B,A),sub(C,B)); /// ABxBC
        if(Math.abs(x)<EPS)
            return 0;
        if(x>0)
            return -1;
        return 1;
    }

    
    /**
     * check whether q point on line segment pr ?? 
     * @param p
     * @param q
     * @param r
     * @return true/false
     */
    static boolean onSegment(Point p, Point q, Point r)
    {
        return ( q.getX()<=Math.max(p.getX(),r.getX()) && q.getX()>=Math.min(p.getX(),r.getX()) 
         && q.getY()<=Math.max(p.getY(),r.getY()) && q.getY()>=Math.min(p.getY(),r.getY()) );
    }


    
    /**
     * check whether line segment 'p1q1' and 'p2q2' intersect. 
     * @param p1
     * @param q1
     * @param p2
     * @param q2
     * @return true/false
     */
    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2)
    {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if(o1!=o2 && o3!=o4)
            return true;
        if(o1==0 && onSegment(p1, p2, q1))
            return true;
        if(o2==0 && onSegment(p1, q2, q1))
            return true;
        if(o3==0 && onSegment(p2, p1, q2))
            return true;
        if(o4==0 && onSegment(p2, q1, q2))
            return true;

        return false;
}

    /**
     * find intersecting point of two circle
     * 
     * @param a center of 1st circle
     * @param r0 radius of 1st circle
     * @param b center of 2nd circle
     * @param r1 radius of 2nd circle
     */
    static void circleIntersect(Point a, double r0, Point b, double r1)
    {
        double d,x,angle;
        Point one,two,k;
        d = distance(a, b);
        if(d>r0+r1)
        {
            System.out.println("Separated");
            return;
        }
        if(d<Math.abs(r0-r1))
        {
            System.out.println("One circle contained within the other");
            return;
        }
        x=(r0*r0 - r1*r1 + d*d)/(2*d);
        k=ab_to_d(a,b,r0);
        if(Math.abs(d-r0+r1)<EPS) ///d==|r0-r1|
        {
            System.out.println(k.getX()+" "+k.getY());
            return;
        }
        angle=Math.acos(x/r0);
        one=rotation(a,k,angle);
        two=rotation(a,k,-1*angle);
        System.out.println(one.getX()+" "+one.getY()+" , "+two.getX()+" "+two.getY());
    }
    
    /**
     * check whether 2 line are parallel or not
     * 
     * @param I
     * @param II
     * @return 
     */
    static boolean isParallel(Equation I, Equation II)
    {
        double x = slop(I);
        double y = slop(II);
        
        return (Math.abs(x-y)<EPS);
            
    }
    
     /**
     * check whether 2 line are perpendicular or not
     * 
     * @param I
     * @param II
     * @return 
     */
    static boolean isPerpendicular(Equation I, Equation II)
    {
        double x = slop(I);
        double y = slop(II);
        //x*y=-1
        return (Math.abs(x*y+1)<EPS);
            
    }
    
    /**
     * 
     * @param I given line
     * @param a passing point
     * @return parallel line of I passing through point a 
     */
    static Equation parallelEqn(Equation I, Point a)
    {
        double x = I.getA()*a.getX() + I.getB()*a.getY();
        I.setC(x);
        return I;
    }
    
    /**
     * 
     * @param I given line
     * @param a passing point
     * @return perpendicular line of I passing through point a 
     */
    static Equation perpendicularEqn(Equation I, Point a)
    {
        Equation II = new Equation();
        II.setA(I.getB());
        II.setB(I.getA());
        if(II.getA()<0)
            II.setA(-1.0*II.getA());
        else
            II.setB(-1.0*II.getB());
        double x = II.getA()*a.getX() + II.getB()*a.getY();
        II.setC(x);
        return II;
    }
    
     /**
     * Draw a circle passing through given 3 points
     * @param A
     * @param B
     * @param C
     * @return the circle passing through A,B,C point
     */
    public static Circle Circle3Point(Point A, Point B, Point C)
    {
        Circle c = new Circle();
        Equation AB = GeoMetry.make_eqn(A, B);
        Equation BC = GeoMetry.make_eqn(C, B);
        Equation I = GeoMetry.perpendicularEqn(AB, GeoMetry.mid(A, B));
        Equation II = GeoMetry.perpendicularEqn(BC, GeoMetry.mid(C, B));
        Point p = GeoMetry.solve_eqn(I, II);
        c.setCenterX(p.getX());
        c.setCenterY(p.getY());
        c.setRadius(GeoMetry.distance(p, A));
        return c;
    }
    
    
    
    public static void main(String[] args) {

        Point a = new Point(3,4);
        Point b = new Point(6,4);
        Point c = new Point(4,4);
        Equation I = make_eqn(a, b);
        Equation II = make_eqn(c, new Point(10, -10));
        Point d = solve_eqn(I, II);
        System.out.println(d.getX()+" "+d.getY());
        System.out.println(onSegment(a, c, b));
        System.out.println(GeoMetry.val(a));
    }
    
}
