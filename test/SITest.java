/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;


class SingleInstancedX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "SingleInstancedX class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement( SingleInstanced.newClass() );

            List aParentList = new List();
            aParentList.addElement( X.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "SingleInstancedX";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "SingleInstancedX class object: construction completed" );
        }
        return classObject;
    }
}




class SITest
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
                SITest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                SITest.usage();
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
        System.out.println( "        -------> SITest passed" );
    }

    private static void runtest() {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 1;

        ClassReference SingleInstancedXClass = SingleInstancedX.newClass();

        ObjectReference iX = (ObjectReference)SingleInstancedXClass.invoke( Environment.Class, 
                                                                             "makeInstance", 
                                                                             new SingleInstancedX() );

        ObjectReference iY = (ObjectReference)SingleInstancedXClass.invoke( Environment.Class, 
                                                                             "makeInstance", 
                                                                             new SingleInstancedX() );

        if ( iX != iY ){
            System.out.println( "SITest failed: the object references refer to different objects" );
            throw new RuntimeException();
        }

        iX.invoke( X.classObject, "aMethod" );

        iX.invoke( Environment.Object, "free" );

        iY.invoke( X.classObject, "bMethod" );

    }

    private static void usage()
    {
        System.out.println( "Usage: SITest" );
    }

}
