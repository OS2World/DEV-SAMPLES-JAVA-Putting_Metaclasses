/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;



class XTest
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
              case 'm': // Use the complex MRO algorithm
                Environment.simpleMRO = false;
                break;
              case 't':
                trace = true;
                break;
               case '?':
                XTest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                XTest.usage();
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
        System.out.println( "        -------> XTest passed" );
    }

    private static void runtest() {
        Environment E = new Environment();

        if ( trace )
          E.traceLevel = 1;

        if ( Environment.getClass( Environment.Object ) != Environment.Class ) {
            System.out.println( "XTest failure: getClass not working" );
            throw new RuntimeException();
        }

        ClassReference XClass = X.newClass();

        ObjectReference iX = (ObjectReference)XClass.invoke( Environment.Class, "makeInstance", new X() );

        iX.invoke( XClass, "aMethod" );

        iX.invoke( Environment.Object, "free" );

        Boolean b = (Boolean)XClass.invoke( Environment.Class, "isDescendantOf", Environment.Class );
        if ( b.booleanValue() )
              throw new RuntimeException();

        b = (Boolean)XClass.invoke( Environment.Class, "isDescendantOf", Environment.Object );
        if ( !b.booleanValue() )
              throw new RuntimeException();

        b = (Boolean)XClass.invoke( Environment.Class, "isAncestorOf", Environment.Class );
        if ( b.booleanValue() )
              throw new RuntimeException();

        b = (Boolean)Environment.Object.invoke( Environment.Class, "isAncestorOf", XClass );
        if ( !b.booleanValue() )
              throw new RuntimeException();

        List l = (List)XClass.invoke( Environment.Class, "getMRO" );
        if ( l.size() != 2 )
              throw new RuntimeException();

        l = (List)XClass.invoke( Environment.Class, "getParents" );
        if ( l.size() != 1 )
              throw new RuntimeException();

        // System.out.println( XClass.toString() );


        b = (Boolean)XClass.invoke( Environment.Class, "isSubclassOf", Environment.Object );
        if ( !b.booleanValue() )
              throw new RuntimeException();

        b = (Boolean)XClass.invoke( Environment.Class, "definesMethod", XClass, "aMethod" );
        if ( !b.booleanValue() )
              throw new RuntimeException();

        b = (Boolean)XClass.invoke( Environment.Class, "supportsMethod", XClass, "aMethod" );
        if ( !b.booleanValue() )
              throw new RuntimeException();

        b = (Boolean)XClass.invoke( Environment.Class, "introducesMethod", XClass, "aMethod" );
        if ( !b.booleanValue() ){
            System.out.println( "XText failed: XClass->introducesMethod( XClass, aMethod )" );
            throw new RuntimeException();
        }

        b = (Boolean)XClass.invoke( Environment.Class, "introducesMethod", Environment.Object, "free" );
        if ( b.booleanValue() ){
            System.out.println( "XText failed: XClass->introducesMethod( Object, free )" );
            throw new RuntimeException();
        }

        b = (Boolean)XClass.invoke( Environment.Class, "supportsIV", XClass, "aVariable" );
        if ( !b.booleanValue() ){
            System.out.println( "XText failed: XClass->supportsIV( X, aVariable )" );
            throw new RuntimeException();
        }

        b = (Boolean)XClass.invoke( Environment.Class, "supportsIV", XClass, "notAVariable" );
        if ( b.booleanValue() ){
            System.out.println( "XText failed: XClass->supportsIV( X, notAVariable ) returned true" );
            throw new RuntimeException();
        }

        b = (Boolean)XClass.invoke( Environment.Class, "supportsIV", Environment.Class, "aVariable" );
        if ( b.booleanValue() ){
            System.out.println( "XText failed: XClass->supportsIV( Class, aVariable ) returned true" );
            throw new RuntimeException();
        }

        b = (Boolean)Environment.Class.invoke( Environment.Class, "supportsIV", Environment.Class, "mtab" );
        if ( !b.booleanValue() ){
            System.out.println( "XText failed: Class->supportsIV( Class, mtab )" );
            throw new RuntimeException();
        }

        b = (Boolean)Environment.Class.invoke( Environment.Class, "supportsIV", Environment.Class, "xxxx" );
        if ( b.booleanValue() ){
            System.out.println( "XText failed: Class->supportsIV( Class, xxxx ) returned true" );
            throw new RuntimeException();
        }

        if ( ((List)XClass.invoke( Environment.Class, "getSupportedMethods" )).size() != 8 )
              throw new RuntimeException();

        XClass.invoke( Environment.Object, "free" );
    }

    private static void usage()
    {
        System.out.println( "Usage: XTest" );
    }

}
