/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */

// This class implements the Cooperative metaclass in Chapter 7.

package putting.lib;

import java.util.*;
import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class Redispatched extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "Redispatched class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement( Cooperative.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "Redispatched";

            RDictionary anIVPairs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVPairs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "readyClass", new RedispatchedCodePtr_readyClass() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "Redispatched class object: construction completed" );
        }
        return classObject;
    }
}

class RedispatchedCodePtr_readyClass      extends CodePtr { 
    public Object execute( ObjectReference target ) { 
        //
        //  void readyClass()
        //
        List mlist = (List)target.invoke( Environment.Class, "getSupportedMethods" );
	for ( Enumeration e = mlist.elements(); e.hasMoreElements(); ) {
            Method m = (Method)e.nextElement();
            if ( m.ic != Environment.Object || !m.mn.equals( "dispatch" ) ) {
                target.invoke( Environment.Class, "putRDStub", m.ic, m.mn );
            }
        }
        target.parentInvoke( Redispatched.classObject, Environment.Class, "readyClass" );
        return null; 
    }
    public String toString()                        { return "RedispatchedCodePtr_readyClass";} 
}


