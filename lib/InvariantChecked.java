/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class InvariantChecked extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "InvariantChecked class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement( BeforeAfter.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "InvariantChecked";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "initializeClass", new InvariantCheckedCodePtr_initializeClass() );

            classObject.invoke( Environment.Class, "overrideMethod", BeforeAfter.classObject, "beforeMethod", new InvariantCheckedCodePtr_beforeMethod() );

            classObject.invoke( Environment.Class, "overrideMethod", BeforeAfter.classObject, "afterMethod", new InvariantCheckedCodePtr_afterMethod() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "InvariantChecked class object: construction completed" );
        }
        return classObject;
    }
}

class InvariantCheckedCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  Boolean initializeClass ( List aParentList, RDictionary nameValuePairs )
        //
        Boolean result;
        result = (Boolean)target.parentInvoke( InvariantChecked.classObject, Environment.Class, "initializeClass", param1, param2 );

        if ( result.booleanValue() ) {
	  result = (Boolean)target.invoke( Environment.Class, "isDescendantOf", InvariantCheckedObject.classObject );
	}
        return result;
    }
    public String toString() { return "InvariantCheckedCodePtr_initializeClass";} 
}

class InvariantCheckedCodePtr_beforeMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        //
        //  long beforeMethod( Object targetObject, Class introducingClass, String methodName, List ParameterList )
        //
        if ( !((Boolean)((ObjectReference)param1).invoke( InvariantCheckedObject.classObject, "computeInvariant" )).booleanValue() ) {
	  System.exit(1);
        }
        return new Long(0);
    }
    public String toString() { return "InvariantCheckedCodePtr_beforeMethod";} 
}


class InvariantCheckedCodePtr_afterMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4, Object param5 ) {
        //
        //  void afterMethod( Object targetObject, Class introducingClass, String methodName, List ParameterList, ResultHolder returnValue )
        //
        if ( !((Boolean)((ObjectReference)param1).invoke( InvariantCheckedObject.classObject, "computeInvariant" )).booleanValue() ) {
	  System.exit(1);
        }
        return null;
    }
    public String toString() { return "InvariantCheckedCodePtr_afterMethod";} 
}


