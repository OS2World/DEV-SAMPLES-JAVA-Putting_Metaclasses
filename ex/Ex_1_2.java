/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;

/*
       Exercise 1.2
 */

class Ex_1_2
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
                Ex_1_2.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                Ex_1_2.usage();
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
        System.out.println( "        -------> Ex_1_2 passed" );
    }

    private static void runtest() {
        Environment E = new Environment();

        if ( trace )
          E.traceLevel = 1;

        RDictionary editX = new RDictionary();
        editX.put( "Cut", "xCutFunction" );
        editX.put( "Copy", "xCopyFunction" );
        editX.put( "Paste", "xPasteFunction" );

        RDictionary fileX = new RDictionary();
        fileX.put( "New", "xNewFunction" );
        fileX.put( "Open", "xOpenFunction" );
        fileX.put( "Save", "xSaveFuction" );

        RDictionary X = new RDictionary();
        X.put( "Edit", editX );
        X.put( "File", fileX );


        RDictionary editY = new RDictionary();
        editY.put( "Cut", "yCutFunction" );
        editY.put( "Copy", "yCopyFunction" );
        editY.put( "Paste", "yPasteFunction" );

        RDictionary fileY = new RDictionary();
        fileY.put( "New", "yNewFunction" );
        fileY.put( "Open", "yOpenFunction" );
        fileY.put( "Save", "ySaveFunction" );
        fileY.put( "SaveAs", "ySaveAsFunction" );

        RDictionary Y = new RDictionary();
        Y.put( "Edit", editY );
        Y.put( "File", fileY );


        RDictionary XmergeY = (RDictionary) X.merge(Y);

        System.out.println( "XmergeY =" );
        System.out.println( XmergeY );

        RDictionary YmergeX = (RDictionary) Y.merge(X);

        System.out.println( "\n\nYmergeX =" );
        System.out.println( YmergeX );

        RDictionary Xrecursive_mergeY = (RDictionary) X.recursive_merge(Y);

        System.out.println( "Xrecursive_mergeY =" );
        System.out.println( Xrecursive_mergeY );

        RDictionary Yrecursive_mergeX = (RDictionary) Y.recursive_merge(X);

        System.out.println( "\n\nYrecursive_mergeX =" );
        System.out.println( Yrecursive_mergeX );

     }

    private static void usage()
    {
        System.out.println( "Usage: Ex_1_2" );
    }

}
