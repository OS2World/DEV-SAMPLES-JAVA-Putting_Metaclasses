/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import java.util.*;
import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class Abstract extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "Abstract class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement( Cooperative.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "Abstract";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "isAbstract", new Boolean(false) );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "makeInstance", new AbstractCodePtr_makeInstance() );

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "readyClass", new AbstractCodePtr_readyClass() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "Abstract class object: construction completed" );
        }
        return classObject;
    }
}

class AbstractCodePtr_makeInstance      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        // Object makeInstance ( ObjectReference obj )
        //
        ObjectReference result;
        Boolean isAbstract = (Boolean)target.invoke( Environment.Object, "getIV", Abstract.classObject, "isAbstract" );
        if ( isAbstract.booleanValue() ) {
            System.out.println( "<Abstract,makeInstance> failed: attempt make to create an instance of an abstract class" );
            throw new RuntimeException();
        }
        result = (ObjectReference)target.parentInvoke( Abstract.classObject, Environment.Class, "makeInstance", param1 );
       return result;
    }
    public String toString() { return "AbstractCodePtr_makeInstance";} 
}

class AbstractCodePtr_readyClass      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  void readyClass()
        //
        ObjectReference obj;
        target.parentInvoke( Abstract.classObject, Environment.Class, "readyClass" );
        List methodList = (List)((ClassReference)target).invoke( Environment.Class, "getSupportedMethods" );

	for ( Enumeration e = methodList.elements(); e.hasMoreElements(); ) {
	    Method m = (Method)e.nextElement();
            CodePtr methodImpl = (CodePtr)target.invoke( Environment.Class, "resolveTerminal", m.ic, m.mn );
            if ( methodImpl == Environment.nullMethod ) {
                target.invoke( Environment.Object, "setIV", Abstract.classObject, "isAbstract", new Boolean(true) );
            }
        }
        return null;
    }
    public String toString() { return "AbstractCodePtr_readyClass";} 
}



