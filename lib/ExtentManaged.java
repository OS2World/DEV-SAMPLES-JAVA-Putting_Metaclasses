/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class ExtentManaged extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "ExtentManaged class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(Environment.Class);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "ExtentManaged";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "extent", new List() );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "makeInstance", new ExtentManagedCodePtr_makeInstance() );

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "deleteInstance", new ExtentManagedCodePtr_deleteInstance() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "getExtent", new ExtentManagedCodePtr_getExtent() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "ExtentManaged class object: construction completed" );
        }
        return classObject;
    }
}

class ExtentManagedCodePtr_makeInstance      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  void makeInstance()
        //
        ObjectReference obj;
        obj = (ObjectReference)target.parentInvoke( ExtentManaged.classObject, Environment.Class, "makeInstance", param1 );
        ((List)target.invoke( Environment.Object, "getIV", ExtentManaged.classObject, "extent" )).addElement( obj );
        return obj;
    }
    public String toString() { return "ExtentManagedCodePtr_makeInstance";} 
}

class ExtentManagedCodePtr_deleteInstance      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  void deleteInstance( ObjectReference objToBeDeleted )
        //
        ((List)target.invoke( Environment.Object, "getIV", ExtentManaged.classObject, "extent" )).removeElement( param1 );
        target.parentInvoke( ExtentManaged.classObject, Environment.Class, "deleteInstance", param1 );
        return null;
    }
    public String toString() { return "ExtentManagedCodePtr_deleteInstance";} 
}

class ExtentManagedCodePtr_getExtent      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  list getExtent()
        //
        return target.invoke( Environment.Object, "getIV", ExtentManaged.classObject, "extent" );
    }
    public String toString() { return "ExtentManagedCodePtr_getExtent";} 
}

