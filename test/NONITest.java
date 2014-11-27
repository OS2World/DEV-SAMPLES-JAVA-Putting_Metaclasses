/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;

class NoninstantiableObject extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "NoninstantiableObject class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement( Noninstantiable.newClass() );

            List aParentList = new List();
            aParentList.addElement( Environment.Object );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "NoninstantiableObject";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

           classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "NoninstantiableObject class object: construction completed" );
        }
        return classObject;
    }
}


class NoninstantiableX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "NoninstantiableX class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement( Environment.Class );

            List aParentList = new List();
            aParentList.addElement( X.newClass() );
            aParentList.addElement( NoninstantiableObject.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "NoninstantiableX";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "NoninstantiableX class object: construction completed" );
        }
        return classObject;
    }
}





class NONITest
{
    static boolean trace    = false;

    public static void main(String args[]) throws Exception
    {
        boolean autotest = false;
        int     i        = 0;

        // Command line flags
        while ( i<args.length && args[i].charAt(0) == '-' ) {
            switch ( args[i].charAt(1) ) {
              case 'a':
                autotest = true;
                break;
             case 't':
                trace = true;
                break;
               case '?':
                NONITest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                NONITest.usage();
                System.exit(1);
            }
            i++;
        }

        // Other command line parameters
        for (; i<args.length; i++) {
        }

        // ---------- Paremeter processing completed ----------------------

        if ( autotest ) {
            try {
                runtest();
            } catch ( Exception e ) {
                System.exit(2);
            }
        }
        else {
            runtest();
        }
        System.out.println( "        -------> NONITest passed" );
    }

    private static void runtest() throws Exception {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 1;

        ClassReference NoninstantiableObjectClass = NoninstantiableObject.newClass();
        ClassReference NoninstantiableXClass = NoninstantiableX.newClass();

        try {
            ObjectReference iNoninstantiableObject = (ObjectReference)NoninstantiableObjectClass.invoke( Environment.Class, 
                                                                                                         "makeInstance", 
                                                                                                         new NoninstantiableObject() );
            throw new Exception();
        } catch ( RuntimeException e ) {
        }

        ObjectReference iX = (ObjectReference)NoninstantiableXClass.invoke( Environment.Class, 
                                                                            "makeInstance", 
                                                                            new NoninstantiableX() );

    }

    private static void usage()
    {
        System.out.println( "Usage: NONITest" );
    }

}
