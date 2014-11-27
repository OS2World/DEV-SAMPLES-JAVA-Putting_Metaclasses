/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

public class Indentation {
    static public String spaces = "";
    static public void increment()  { spaces += "  "; }
    static public void decrement()  { spaces = spaces.substring(2); }
}
