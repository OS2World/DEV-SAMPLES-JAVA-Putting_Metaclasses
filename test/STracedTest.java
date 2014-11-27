/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;

class SimplyTracedX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "SimplyTracedX class object: construction begins" );

        ClassReference XClass = X.newClass();
        ClassReference SimplyTracedClass = SimplyTraced.newClass();

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement(SimplyTracedClass);

            List aParentList = new List();
            aParentList.addElement(XClass);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            if (Environment.traceLevel > 0) {
                classObject.name = "SimplyTracedX";
            }

            RDictionary anIVDefs = new RDictionary( );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "SimplyTracedX class object: construction completed" );
        }
        return classObject;
    }
}


class STracedTest
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
                STracedTest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                STracedTest.usage();
                System.exit(1);
            }
            i++;
        }

        // Other command line parameters
        for (; i<args.length; i++) {
        }

        // ---------- Parameter processing completed ----------------------

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
        System.out.println( "        -------> STracedTest passed" );
    }

    private static void runtest() {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 1;

        ClassReference XClass = X.newClass();
        ClassReference SimplyTracedXClass = SimplyTracedX.newClass();
        ObjectReference iX = (ObjectReference)SimplyTracedXClass.invoke( Environment.Class, "makeInstance", new SimplyTracedX() );

        iX.invoke( XClass, "aMethod" );

        iX.invoke( Environment.Object, "free" );
    }

    private static void usage()
    {
        System.out.println( "Usage: STracedTest" );
    }

}
