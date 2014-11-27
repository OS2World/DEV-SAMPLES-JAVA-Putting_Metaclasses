/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;


class EMX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "EMX class object: construction begins" );

        ClassReference EMClass = ExtentManaged.newClass();

        ClassReference XClass = X.newClass();

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement(EMClass);

            List aParentList = new List();
            aParentList.addElement(XClass);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            if (Environment.traceLevel > 0) {
                classObject.name = "EMX";
            }

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "EMX class object: construction completed" );
        }
        return classObject;
    }
}


class EMTest
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
                EMTest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                EMTest.usage();
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
        System.out.println( "        -------> EMTest passed" );
    }

    private static void runtest() {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 1;

        ClassReference EMXClass = EMX.newClass();
        ObjectReference iEMX = (ObjectReference)EMXClass.invoke( Environment.Class, "makeInstance", new ObjectReference() );

        List extent = (List)EMXClass.invoke( ExtentManaged.classObject, "getExtent" );

       if ( !extent.contains( iEMX ) ) {
              throw new RuntimeException();
        }

        iEMX.invoke( Environment.Object, "free" );
       
        if ( extent.contains( iEMX ) ) {
              throw new RuntimeException();
        }
  }

    private static void usage()
    {
        System.out.println( "Usage: EMTest" );
    }

}
