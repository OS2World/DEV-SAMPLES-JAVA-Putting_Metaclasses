/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class Final extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "Final class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement( Environment.Class );

            List aParentList = new List();
            aParentList.addElement( Environment.Class );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "Final";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "initializeClass", new FinalCodePtr_initializeClass() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "Final class object: construction completed" );
        }
        return classObject;
    }
}

class FinalCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  Boolean initializeClass ( List aParentList, RDictionary nameValuePairs )
        //
        Boolean result;
        result = (Boolean)target.parentInvoke( Final.classObject, Environment.Class, "initializeClass", param1, param2 );

        if ( result.booleanValue() ) {
            List parents = (List)target.invoke( Environment.Class, "getParents" );
            for ( int i = 0; i < parents.size(); i++ ) {
                ClassReference aParent = (ClassReference)parents.elementAt(i);
                if ( ((Boolean)Environment.getClass( aParent ).invoke( Environment.Class, "isDescendantOf", Final.classObject )).booleanValue() ) {
                    System.out.println( "<Final,initializeClass> failed: attempt subclass a Final parent" );
                    throw new RuntimeException();
                }
            }
	}
        return result;
    }
    public String toString() { return "FinalCodePtr_initializeClass";} 
}


