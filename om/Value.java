/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

/** This interface is for all classes that can be passed 
    as parameters to methods.
    */
public interface Value extends Cloneable {

    public Object clone();        

}

