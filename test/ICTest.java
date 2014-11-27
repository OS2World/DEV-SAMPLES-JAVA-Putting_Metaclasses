/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;


class InvariantCheckedX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "InvariantCheckedX class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement( Environment.Class );

            List aParentList = new List();
            aParentList.addElement( InvariantCheckedObject.newClass() );
            aParentList.addElement( X.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "InvariantCheckedX";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, 
                                "overrideMethod", 
                                InvariantCheckedObject.classObject, 
                                "computeInvariant",
                                new InvariantCheckedXCodePtr_computeInvariant() );

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "InvariantCheckedX class object: construction completed" );
        }
        return classObject;
    }
}


class InvariantCheckedXCodePtr_computeInvariant      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  boolean computeInvariant()
        //
        System.out.println("Checking invariant for InvariantCheckedX");
        return (Boolean)target.parentInvoke( InvariantCheckedX.classObject,
                                             InvariantCheckedObject.classObject, 
                                             "computeInvariant" );
    }
    public String toString() { return "InvariantCheckedXCodePtr_computeInvariant";} 
}


class ICTest
{
    static boolean trace    = false;

    public static void main(String args[])
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
                ICTest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                ICTest.usage();
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
        System.out.println( "        -------> ICTest passed" );
    }

    private static void runtest() {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 1;

        ClassReference InvariantCheckedXClass = InvariantCheckedX.newClass();

        ObjectReference iX = (ObjectReference)InvariantCheckedXClass.invoke( Environment.Class, 
                                                                             "makeInstance", 
                                                                             new InvariantCheckedX() );

        iX.invoke( X.classObject, "aMethod" );

    }

    private static void usage()
    {
        System.out.println( "Usage: ICTest" );
    }

}
