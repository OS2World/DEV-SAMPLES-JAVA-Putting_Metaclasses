/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;



class FB2 extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "FB2 class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement( Environment.Class );

            List aParentList = new List();
            aParentList.addElement( FierceDog.newClass() );
            aParentList.addElement( BarkingDog.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "FB2";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "FB2 class object: construction completed" );
        }
        return classObject;
    }
}


class BATest2
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
                BATest2.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                BATest2.usage();
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
        System.out.println( "        -------> BATest2 passed" );
    }

    private static void runtest() {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 1;

        ClassReference FB2Class = FB2.newClass();

        ObjectReference fido = (ObjectReference)FB2Class.invoke( Environment.Class, "makeInstance", new FB2() );

        System.out.println( " " );

        fido.invoke( Dog.classObject, "bite" );

  }

    private static void usage()
    {
        System.out.println( "Usage: BATest2" );
    }

}
