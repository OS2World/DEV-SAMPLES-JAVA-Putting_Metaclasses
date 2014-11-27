/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;

/*
       Exercise 1.1
 */

class Ex_1_1
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
                Ex_1_1.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                Ex_1_1.usage();
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
        System.out.println( "        -------> Ex_1_1 passed" );
    }

    private static void runtest() {
        Environment E = new Environment();

        if ( trace )
          E.traceLevel = 1;

        RDictionary x = new RDictionary();
        x.put( "name", "Abraham Lincoln" );
        x.put( "home", "Springfield" );

        RDictionary y = new RDictionary();
        y.put( "position", "President" );
        y.put( "home", "Washington, D.C." );

        RDictionary x_merge_y = (RDictionary) x.merge(y);

        System.out.println( "x_merge_y =" );
        System.out.println( x_merge_y );

        RDictionary y_merge_x = (RDictionary) y.merge(x);

        System.out.println( "\n\ny_merge_x =" );
        System.out.println( y_merge_x );

    }

    private static void usage()
    {
        System.out.println( "Usage: Ex_1_1" );
    }

}
