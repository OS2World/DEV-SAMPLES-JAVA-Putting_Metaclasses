/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;


public class RDictionary extends Properties implements Value {

    public RDictionary( ) { }
    public RDictionary( Object key, Object value ) { this.put( key, value ); }

    public Object clone() {
	RDictionary myclone = new RDictionary();
	for ( Enumeration e = keys(); e.hasMoreElements(); ) {
	    Object key = e.nextElement();
	    Object value = get( key );
            if ( value.getClass().getName().equals("java.lang.String") )
              myclone.put( key, new String((String)value) );
            else if ( value.getClass().getName().equals("java.lang.Boolean") )
              myclone.put( key, new Boolean( ((Boolean)value).booleanValue() ) );
            else if ( value.getClass().getName().equals("java.lang.Long") )
              myclone.put( key, new Long( ((Long)value).longValue() ) );
            else
              myclone.put( key, ((Value)value).clone() );
	}
	return myclone;
    }

    public String toString() {
        Indentation.increment();
        String result = "\n" + Indentation.spaces + "{\n";
        String className;
        String keyName;
        for ( Enumeration e = keys(); e.hasMoreElements(); ) {
	    Object key = e.nextElement();
	    Object value = get( key );
            if ( key == Environment.Class )  keyName = "Class";
            else if ( key == Environment.Object ) keyName = "Object";
            else if ( key.getClass().getName().equals("java.lang.String") ) keyName = (String)key;
            else {
                if ( ((ClassReference)key).name == null )
                  keyName = "a-" + key.getClass().getName();
                else
                  keyName = ((ClassReference)key).name;
              }
            
            if ( key == Environment.myclass ) {
                if ( value == Environment.Class )  className = "Class";
                else if ( value == Environment.Object ) className = "Object";
                else {
                    if ( ((ClassReference)value).name == null ) {
                        className = "anUnnamedClass";}
                    else {
                        className = ((ClassReference)value).name;
                    }
                }
                result = result + Indentation.spaces + "  class=" + className  + "\n";
            }
            else
	        result = result + Indentation.spaces + "  " + keyName + "=" + value  + "\n";
	}
        result = result + Indentation.spaces + "}";
        Indentation.decrement();
        return result;
    }

    public Object merge( Object r ) {
        RDictionary result = (RDictionary) clone();
	for ( Enumeration e = ((RDictionary)r).keys(); e.hasMoreElements(); ) {
	    Object key = e.nextElement();
	    Object value = ((RDictionary)r).get( key );
            if ( !result.containsKey(key) ) {
                if ( value.getClass().getName().equals("java.lang.String") )
                  result.put( key, new String((String)value) );
                else
                  result.put( key, ((Value)value).clone() );
                }          
	}
        return result;
    }

    public RDictionary recursive_merge( RDictionary r ) {
        RDictionary result = (RDictionary) clone();
	for ( Enumeration e = ((RDictionary)r).keys(); e.hasMoreElements(); ) {
	    Object key = e.nextElement();
	    Object value = ((RDictionary)r).get( key );
	    if ( value.getClass().getName().equals("putting.om.RDictionary") ) {
                if ( result.containsKey(key) ) {
		    result.put( key, ((RDictionary)get(key)).recursive_merge( (RDictionary)value ) );
		} else {
                    if ( value.getClass().getName().equals("java.lang.String") )
                      result.put( key, new String((String)value) );
                    else
                      result.put( key, ((Value)value).clone() );}
            } else {
                if ( !result.containsKey(key) ) {
                    if ( value.getClass().getName().equals("java.lang.String") )
                      result.put( key, new String((String)value) );
                    else
                      result.put( key, ((Value)value).clone() );}
            }  
	}
        return result;
    }

    /** commonMethods assumes that the both the target and the parameter r are
      * method tables. commonMethods returns the List of Methods that are common
      * to both method tables.
      */
    public List commonMethods( RDictionary r ) {
        List result = new List(); 
	for ( Enumeration e = keys(); e.hasMoreElements(); ) {
	    ClassReference thisClassRef = (ClassReference)e.nextElement();
	    if ( r.containsKey(thisClassRef) ) {
		RDictionary thisSegment = (RDictionary)get( thisClassRef );
		RDictionary rSegment = (RDictionary)r.get( thisClassRef );
		for ( Enumeration emn = thisSegment.keys(); emn.hasMoreElements(); ) {
		    String thisMethodName = (String)emn.nextElement();
		    if ( rSegment.containsKey(thisMethodName) ) {
			result.addElement( new Method( thisClassRef, thisMethodName ) );
		    }
		}
	    }
	}
        return result;
    }

}
