/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

public class List extends Vector implements Value {

    public List( ) { }
    public List( Object value ) { this.addElement( value ); }
    public List( Object value1, Object value2 ) { this.addElement( value1 ); 
                                                  this.addElement( value2 ); }
    public List( Object value1, Object value2, Object value3 ) 
    { this.addElement( value1 ); 
      this.addElement( value2 ); 
      this.addElement( value3 ); }

    public List merge( List r ) {
        List result = (List) clone();
	for ( Enumeration e = r.elements(); e.hasMoreElements(); ) {
	    Value elt = (Value)e.nextElement();
	    if ( !result.contains(elt) ) {
                result.addElement( elt );
                }          
	}
        return result;
    }

    public List reverse( ){
	List result = new List();
	for ( Enumeration e = elements(); e.hasMoreElements(); ) {
	    result.insertElementAt( (Value)e.nextElement(), 0 );
	}
        return result;
    }

    public List sublist( int i , int j ) {
        if ( i < 0 || i > j || j > size() )
          throw new OMRuntimeException();
        List newList = new List();
        for (; i<j; i++)
          newList.addElement( elementAt( i ) );
        return newList;
    }


}

