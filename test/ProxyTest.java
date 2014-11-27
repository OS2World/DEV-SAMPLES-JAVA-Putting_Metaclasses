/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;



class ProxyTest
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
                ProxyTest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                ProxyTest.usage();
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
        System.out.println( "        -------> ProxyTest passed" );
    }

    private static void runtest() {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 51;

        ClassReference ProxyForObjectClass = ProxyForObject.newClass();

        ClassReference XClass = X.newClass();

        ObjectReference iX = (ObjectReference)XClass.invoke( Environment.Class, "makeInstance", new X() );

        ClassReference ProxyForXClass = (ClassReference)ProxyForObject.classObject.invoke( ProxyFor.classObject, "createProxyClass", XClass );

        ObjectReference proxyForIX = (ObjectReference)ProxyForXClass.invoke( Environment.Class, "makeInstance", new ObjectReference() );

        proxyForIX.invoke( ProxyForObject.classObject, "setTarget", iX );

        proxyForIX.invoke( XClass, "aMethod" );

        iX.invoke( Environment.Object, "free" );

        XClass.invoke( Environment.Object, "free" );
    }

    private static void usage()
    {
        System.out.println( "Usage: ProxyTest" );
    }

}
