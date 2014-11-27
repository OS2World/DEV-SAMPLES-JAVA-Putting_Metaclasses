/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;



//=========================================================================
// Bindings
//=========================================================================
class Dog extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0)
              System.out.println( "Dog class object: construction begins" );

            List aMetaclassList = new List(Environment.Class);

            List aParentList = new List(Environment.Object);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) Environment.Class.invoke( metaclass, "makeInstance", new ClassReference() );

            classObject.name = "Dog";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "addMethod", classObject, "bite", new DogCodePtr_bite() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "Dog class object: construction completed" );
        }
        return classObject;
    }
}

class DogCodePtr_bite     extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  Boolean aMethod()
        //
        System.out.println( "Hello World!" );
        return new Boolean(true);
    }
    public String toString() { return "Dog_bite";} 
}

