/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;

// MetaCooperativeX is the metaclass for CooperativeX in this test.
// In the test, MetaCooperativeX adds a coopertive method to aMethod,
// which is a method of X.
class MetaCooperativeX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static CodePtr cooperativeOverride1 = new coopOverride_XCodePtr_aMethod();
    static CodePtr cooperativeOverride2 = new coopOverride_XCodePtr_aMethod();
    static CodePtr cooperativeOverride3 = new coopOverride_XCodePtr_aMethod();

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "MetaCooperativeX class object: construction begins" );

        ClassReference CooperativeClass = Cooperative.newClass();

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(CooperativeClass);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            if (Environment.traceLevel > 0) {
                classObject.name = "MetaCooperativeX";
            }

            RDictionary anIVDefs = new RDictionary( );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "initializeClass", new MetaCooperativeXCodePtr_initializeClass() );

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "MetaCooperativeX class object: construction completed" );
        }
        return classObject;
    }
}

class MetaCooperativeXCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  Boolean initializeClass ( List aParentList, RDictionary nameValuePairs )
        //
        Boolean result;
        result = (Boolean)target.parentInvoke( Cooperative.classObject, Environment.Class, "initializeClass", param1, param2 );

        if ( result.booleanValue() ) {
            result = (Boolean)target.invoke( Cooperative.classObject, 
                                             "requestFirstCooperativeMethodCall", 
                                             X.classObject, 
                                             "aMethod", 
                                             MetaCooperativeX.cooperativeOverride3 );
            if ( result.booleanValue() ) {
                target.invoke( Cooperative.classObject, "satisfyRequests" );
                target.invoke( Cooperative.classObject, "addCooperativeMethod", X.classObject, "aMethod", MetaCooperativeX.cooperativeOverride1 );
                target.invoke( Cooperative.classObject, "addCooperativeMethod", X.classObject, "aMethod", MetaCooperativeX.cooperativeOverride2 );
                target.invoke( Cooperative.classObject, "addCooperativeMethod", CooperativeX.classObject, "aCoopMethod", MetaCooperativeX.cooperativeOverride2 );
            }
            else {
              System.out.println( "***** FAILURE ***** MetaCooperativeXCodePtr_initializeClass: requestFirstCooperativeMethodCall fails" );  
              throw new RuntimeException();
            }
        }
        return result;
    }
    public String toString() { return "CooperativeCodePtr_initializeClass";} 
}

class coopOverride_XCodePtr_aMethod     extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  Boolean aMethod()
        //
        Boolean returnValue;
        System.out.println( "coopOverride_XCodePtr_aMethod begins" );
        CodePtr nextAMethodImpl = (CodePtr)Environment.getClass(target).invoke( Cooperative.classObject, 
                                                                                "getNextCooperative", 
                                                                                MetaCooperativeX.cooperativeOverride1 );
        returnValue = (Boolean)nextAMethodImpl.execute( target );
        System.out.println( "coopOverride_XCodePtr_aMethod ends" );
        return returnValue;
    }
    public String toString() { return "coopOverride_X_aMethod";} 
}

class CooperativeX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "CooperativeX class object: construction begins" );

        ClassReference MetaCooperativeXClass = MetaCooperativeX.newClass();

        ClassReference XClass = X.newClass();

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement(MetaCooperativeXClass);

            List aParentList = new List();
            aParentList.addElement(XClass);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            if (Environment.traceLevel > 0) {
                classObject.name = "CooperativeX";
            }

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "addMethod", classObject, "aCoopMethod", new CooperativeXCodePtr_aCoopMethod() );

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "CooperativeX class object: construction completed" );
        }
        return classObject;
    }
}


class CooperativeXCodePtr_aCoopMethod     extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  Boolean aMethod()
        //
        return null;
    }
    public String toString() { return "CooperativeX_aCoopMethod";} 
}


class CoopTest
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
                CoopTest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                CoopTest.usage();
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
        System.out.println( "        -------> CoopTest passed" );
    }

    private static void runtest() {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 1;

        ClassReference CoopXClass = CooperativeX.newClass();

        CodePtr terminalAMethodImpl = (CodePtr)CoopXClass.invoke( Environment.Class, 
                                                                  "resolveTerminal", 
                                                                  X.classObject, 
                                                                  "aMethod" );

        if ( !terminalAMethodImpl.toString().equals( "X_aMethod" )) 
              throw new RuntimeException();

        // This test is a bit artificial in that one would not expect a metaclass to add
        // two cooperative implementations to the same method.  However, nothing prevents
        // this.  This test checks that the methods are added properly by checking that
        // getNextCooperative returns the expected method implementation.

        CodePtr nextAMethodImpl = (CodePtr)CoopXClass.invoke( Cooperative.classObject, 
                                                              "getNextCooperative", 
                                                              X.classObject, 
                                                              "aMethod", 
                                                              MetaCooperativeX.cooperativeOverride3 );

        if ( !(nextAMethodImpl == MetaCooperativeX.cooperativeOverride2) ) 
              throw new RuntimeException();

        nextAMethodImpl = (CodePtr)CoopXClass.invoke( Cooperative.classObject, 
                                                      "getNextCooperative", 
                                                      X.classObject, 
                                                      "aMethod", 
                                                      MetaCooperativeX.cooperativeOverride2 );

        if ( !(nextAMethodImpl == MetaCooperativeX.cooperativeOverride1) ) 
              throw new RuntimeException();

        nextAMethodImpl = (CodePtr)CoopXClass.invoke( Cooperative.classObject, 
                                                      "getNextCooperative", 
                                                      X.classObject, 
                                                      "aMethod", 
                                                      MetaCooperativeX.cooperativeOverride1 );

        if ( !nextAMethodImpl.toString().equals( "X_aMethod" ) ) 
              throw new RuntimeException();

        nextAMethodImpl = (CodePtr)CoopXClass.invoke( Cooperative.classObject, 
                                                      "getNextCooperative", 
                                                      CooperativeX.classObject, 
                                                      "aCoopMethod", 
                                                      MetaCooperativeX.cooperativeOverride2 );

        if ( !nextAMethodImpl.toString().equals( "CooperativeX_aCoopMethod" ) ) 
              throw new RuntimeException();
    }

    private static void usage()
    {
        System.out.println( "Usage: CoopTest" );
    }

}
