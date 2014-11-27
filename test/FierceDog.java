/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;

class FierceDog extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "FierceDog class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement(Fierce.newClass());

            List aParentList = new List();
            aParentList.addElement(Dog.newClass());

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "FierceDog";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "FierceDog class object: construction completed" );
        }
        return classObject;
    }
}
