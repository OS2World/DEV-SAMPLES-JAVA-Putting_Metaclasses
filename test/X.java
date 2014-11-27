/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;



//=========================================================================
// Bindings
//=========================================================================
class X extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0)
              System.out.println( "X class object: construction begins" );

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(Environment.Object);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) Environment.Class.invoke( metaclass, "makeInstance", new ClassReference() );

            classObject.name = "X";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "aVariable", "" );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "addMethod", classObject, "aMethod", new XCodePtr_aMethod() );
            
            classObject.invoke( Environment.Class, "addMethod", classObject, "bMethod", new XCodePtr_bMethod() );

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Object, "initialize", new XCodePtr_initialize() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "X class object: construction completed" );
        }
        return classObject;
    }
}

class XCodePtr_initialize      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  void initialize()
        //
        target.parentInvoke( X.classObject, Environment.Object, "initialize" );
        return null;
    }
    public String toString() { return "X_initialize";} 
}

class XCodePtr_aMethod     extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  Boolean aMethod()
        //
        if ( !((String)target.invoke( Environment.Object, "getIV", X.classObject, "aVariable" )).equals( "" ) ) {
            System.out.println( "***** FAILURE ***** aMethod: aVariable not initialized");
            throw new RuntimeException();
        }

        target.invoke( Environment.Object, "setIV", X.classObject, "aVariable", "wxyz" );

        if ( !((String)target.invoke( Environment.Object, "getIV", X.classObject, "aVariable" )).equals( "wxyz" ) ) {
            System.out.println( "***** FAILURE ***** aMethod: aVariable not set properly");
            throw new RuntimeException();
        }
        System.out.println( "Hello World!" );
        return new Boolean(true);
    }
    public String toString() { return "X_aMethod";} 
}

class XCodePtr_bMethod     extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  Boolean bMethod()
        //

        if ( !((String)target.invoke( Environment.Object, "getIV", X.classObject, "aVariable" )).equals( "wxyz" ) ) {
            System.out.println( "***** FAILURE ***** bMethod: aVariable not set properly");
            throw new RuntimeException();
        }
        System.out.println( "Hello World!" );
        return new Boolean(true);
    }
    public String toString() { return "X_bMethod";} 
}

