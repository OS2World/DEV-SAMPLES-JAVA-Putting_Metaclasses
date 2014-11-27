/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;

/*
  This test is for the MRO calculation.  In runtest, a number of environments
  are created and in each environment a class hierarchy is created. The goal
  of each test is to create the class Z (or fail to create Z if there is a 
  serious order disagreement.  The method makeClassO makes a class named O 
  below Object; class O introduces the method foo. The method makeClass takes
  a parent list, a boolean, and a string as parameters.  The boolean tells makeClass 
  whether or not to override foo. The string is a print name for debugging purposes.
  */

class MROTest_foo    extends CodePtr { 
    public Object execute( ObjectReference target ) {
	return null;
    }
    public String toString() { return "MROTest_foo";} 
}



class MROTest
{
    static boolean         trace    = false;
    static ClassReference  O;

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
                MROTest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                MROTest.usage();
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
        System.out.println( "        -------> MROTest passed" );
    }

    private static void runtest() {

        Environment E;
        ClassReference V, W, X, Y, Z;

	//=============================================================
        // Test 1
        // 
        E = new Environment();
        if ( trace )
          E.traceLevel = 1;
	O = makeClassO();
	V = makeClass( new List(O), true, "V" );
	W = makeClass( new List(O), true, "W" );
	X = makeClass( new List(V,W), false, "X" );
	Y = makeClass( new List(W,V), false, "Y" );
	Z = makeClass( new List(X,Y), false, "Z" );
	if ( Z != null ) {
            System.out.println( "Test1 failed" );
	    throw new RuntimeException();
        }


	//=============================================================
        // Test 2
        // 
        E = new Environment();
        if ( trace )
          E.traceLevel = 1;
	O = makeClassO();
	V = makeClass( new List(O), false, "V" );
	W = makeClass( new List(O), false, "W" );
	X = makeClass( new List(V,W), false, "X" );
	Y = makeClass( new List(W,V), false, "Y" );
	Z = makeClass( new List(X,Y), false, "Z" );
	if ( Z != null ) {
            System.out.println( "Test2 failed" );
	    throw new RuntimeException();
        }


	//=============================================================
        // Test 3
        // 
        E = new Environment();
        if ( trace )
          E.traceLevel = 1;
        E.simpleMRO = false;
	O = makeClassO();
	V = makeClass( new List(O), true, "V" );
	W = makeClass( new List(O), true, "W" );
	X = makeClass( new List(V,W), false, "X" );
	Y = makeClass( new List(W,V), false, "Y" );
	Z = makeClass( new List(X,Y), false, "Z" );
	if ( Z != null ) {
            System.out.println( "Test3 failed" );
	    throw new RuntimeException();
        }


	//=============================================================
        // Test 4
        // 
        E = new Environment();
        if ( trace )
          E.traceLevel = 1;
        E.simpleMRO = false;
	O = makeClassO();
	V = makeClass( new List(O), false, "V" );
	W = makeClass( new List(O), false, "W" );
	X = makeClass( new List(V,W), false, "X" );
	Y = makeClass( new List(W,V), false, "Y" );
	Z = makeClass( new List(X,Y), false, "Z" );
	if ( Z == null ) {
            System.out.println( "Test4 failed" );
	    throw new RuntimeException();
        }
    }


    private static void usage()
    {
        System.out.println( "Usage: MROTest" );
    }


    static public ClassReference makeClass( List aParentList, boolean overrideFoo, String className ) {

	List aMetaclassList = new List(Environment.Class);

	ClassReference metaclass;
	metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

	ClassReference classObject;
	classObject = (ClassReference) Environment.Class.invoke( metaclass, "makeInstance", new ClassReference() );
        classObject.name = "nascentClass";
	RDictionary anIVDefs = new RDictionary();

	Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
	if ( !b.booleanValue() )
	    return null;

	if ( overrideFoo ) {
	    classObject.invoke( Environment.Class, "overrideMethod", O, "foo", new MROTest_foo() );
	}

	classObject.invoke( Environment.Class, "readyClass" );

	classObject.name = className;
        return classObject;
    }

    static public ClassReference makeClassO( ) {

	List aMetaclassList = new List(Environment.Class);
	List aParentList = new List(Environment.Object);

	ClassReference metaclass;
	metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

	ClassReference classObject;
	classObject = (ClassReference) Environment.Class.invoke( metaclass, "makeInstance", new ClassReference() );

	RDictionary anIVDefs = new RDictionary();

	Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
	if ( !b.booleanValue() )
	    return null;

	classObject.invoke( Environment.Class, "addMethod", classObject, "foo", new MROTest_foo() );

	classObject.invoke( Environment.Class, "readyClass" );

	classObject.name = "O";
        return classObject;
    }

}
