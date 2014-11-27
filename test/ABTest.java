/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;


class AbstractX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "AbstractX class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement( Abstract.newClass() );

            List aParentList = new List();
            aParentList.addElement( X.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "AbstractX";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "addMethod", classObject, "doIt", Environment.nullMethod );

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "AbstractX class object: construction completed" );
        }
        return classObject;
    }
}

class ConcreteX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "ConcreteX class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement( Environment.Class );

            List aParentList = new List();
            aParentList.addElement( AbstractX.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "ConcreteX";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", AbstractX.classObject, "doIt", new CodePtr() );
            // For the sake of simplicity, we use a new CodePtr.  It only matters that it is not equal
            // to Environment.nullMethod.

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "ConcreteX class object: construction completed" );
        }
        return classObject;
    }
}




class ABTest
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
                ABTest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                ABTest.usage();
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
        System.out.println( "        -------> ABTest passed" );
    }

    private static void runtest() throws Exception {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 1;

        ClassReference AbstractXClass = AbstractX.newClass();
        ClassReference ConcreteXClass = ConcreteX.newClass();

        try {
            ObjectReference iX = (ObjectReference)AbstractXClass.invoke( Environment.Class, 
                                                                         "makeInstance", 
                                                                         new AbstractX() );
            throw new Exception();
        } catch ( RuntimeException e ) {
            System.out.println("Preceding message means the test has passed");
        }

        ObjectReference iConcreteX = (ObjectReference)ConcreteXClass.invoke( Environment.Class, 
                                                                             "makeInstance", 
                                                                             new ConcreteX() );

    }

    private static void usage()
    {
        System.out.println( "Usage: ABTest" );
    }

}
