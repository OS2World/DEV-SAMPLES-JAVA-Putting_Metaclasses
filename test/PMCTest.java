/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;

/*
  We create a surrogate for BeforeAfter that is an instance of ParentMethodCallPreventionEnabled.
  One might think that this is unnecessary, but it is, because of the way parentResolve works.
  parentResolve invoke resolveTerminal on the first class in the MRO (of the class of the target)
  above the overridingClass that defines the method.  Thus, one needs to ensure that there is a
  definition of the method in the class where a metaclass overrides resolveTerminal.
*/

class BeforeAfterWithPMC extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "BeforeAfterWithPMC class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement(ParentMethodCallPreventionEnabled.newClass());

            List aParentList = new List();
            aParentList.addElement(BeforeAfter.newClass());

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "BeforeAfterWithPMC";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", BeforeAfter.classObject, "beforeMethod", new BeforeAfterWithPMCCodePtr_beforeMethod() );

            classObject.invoke( Environment.Class, "overrideMethod", BeforeAfter.classObject, "afterMethod", new BeforeAfterWithPMCCodePtr_afterMethod() );

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "BeforeAfterWithPMC class object: construction completed" );
        }
        return classObject;
    }
}


class BeforeAfterWithPMCCodePtr_beforeMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        //
        //  list beforeMethod( Object targetObject, Class introducingClass, String methodName, List ParameterList )
        //
        target.parentInvoke( BeforeAfterWithPMC.classObject, BeforeAfter.classObject, "beforeMethod", param1, param2, param3, param4 );;
        return new Long(0);
    }
    public String toString() { return "BeforeAfterWithPMCCodePtr_beforeMethod";} 
}


class BeforeAfterWithPMCCodePtr_afterMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4, Object param5 ) {
        //
        //  list afterMethod( Object targetObject, Class introducingClass, String methodName, List ParameterList, ResultHolder returnValue )
        //
        target.parentInvoke( BeforeAfterWithPMC.classObject, BeforeAfter.classObject, "afterMethod", param1, param2, param3, param4, param5 );;
        return null;
    }
    public String toString() { return "BeforeAfterWithPMCCodePtr_afterMethod";} 
}

class BarkingWithPMC extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "BarkingWithPMC class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(BeforeAfterWithPMC.newClass());

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "BarkingWithPMC";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", BeforeAfter.classObject, "beforeMethod", new BarkingWithPMCCodePtr_beforeMethod() );

            classObject.invoke( Environment.Class, "overrideMethod", BeforeAfter.classObject, "afterMethod", new BarkingWithPMCCodePtr_afterMethod() );

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "BarkingWithPMC class object: construction completed" );
        }
        return classObject;
    }
}



class BarkingWithPMCCodePtr_beforeMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        //
        //  list beforeMethod( Object targetObject, Class introducingClass, String methodName, List ParameterList )
        //
        String traceString = "";
        if (Environment.traceLevel > 0)
          traceString = " (before <" + ((ClassReference)param2).name + "," + param3 + ">)";
        if (Environment.traceLevel > 50)
          System.out.println( ((ClassReference)target).name 
                              + "->__parent(" 
                              + BarkingWithPMC.classObject.name 
                              + "," 
                              + BeforeAfter.classObject.name
                              + ",beforeMethod,...)" 
                              );
        target.parentInvoke( BarkingWithPMC.classObject, BeforeAfter.classObject, "beforeMethod", param1, param2, param3, param4 );;
       
        System.out.println( "WOOFWOOF" + traceString );
        return new Long(0);
    }
    public String toString() { return "BarkingWithPMCCodePtr_beforeMethod";} 
}


class BarkingWithPMCCodePtr_afterMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4, Object param5 ) {
        //
        //  list afterMethod( Object targetObject, Class introducingClass, String methodName, List ParameterList, ResultHolder returnValue )
        //
        String traceString = "";
        if (Environment.traceLevel > 0)
          traceString = " (after <" + ((ClassReference)param2).name + "," + param3 + ">)";
        System.out.println( "WOOF" + traceString );
        return null;
    }
    public String toString() { return "BarkingWithPMCCodePtr_afterMethod";} 
}


class BarkingWithPMCX extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "BarkingWithPMCX class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement(BarkingWithPMC.newClass());

            List aParentList = new List();
            aParentList.addElement(X.newClass());

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "BarkingWithPMCX";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "BarkingWithPMCX class object: construction completed" );
        }
        return classObject;
    }
}


class PMCTest
{
    static boolean trace    = false;

    public static void main(String args[]) throws Exception
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
                PMCTest.usage();
                System.exit(0);
                break;
             default:
                System.out.println( "unknown option: " + args[i].charAt(1) );
                PMCTest.usage();
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
        System.out.println( "        -------> PMCTest passed" );
    }

    private static void runtest() throws Exception {
        Environment E = new Environment();
        if ( trace )
          E.traceLevel = 51;

        ClassReference BarkingWithPMCXClass = BarkingWithPMCX.newClass();

        try {
            // makeInstance invokes initialize, which fails when the beforeMethod tries to do a parent method call
            ObjectReference iX = (ObjectReference)BarkingWithPMCXClass.invoke( Environment.Class, "makeInstance", new BarkingWithPMCX() );

            iX.invoke( X.classObject, "aMethod" );
            throw new Exception();
        } catch ( OMImplementationNotFound e) {
        }
  }

    private static void usage()
    {
        System.out.println( "Usage: PMCTest" );
    }

}
