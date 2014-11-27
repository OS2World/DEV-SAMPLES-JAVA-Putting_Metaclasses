/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
import putting.om.*;
import putting.lib.*;


class Barking extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static ClassReference classObject; 

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "Barking class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(BeforeAfter.newClass());

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "Barking";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", BeforeAfter.classObject, "beforeMethod", new BarkingCodePtr_beforeMethod() );

            classObject.invoke( Environment.Class, "overrideMethod", BeforeAfter.classObject, "afterMethod", new BarkingCodePtr_afterMethod() );

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "Barking class object: construction completed" );
        }
        return classObject;
    }
}



class BarkingCodePtr_beforeMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        //
        //  list beforeMethod( Object targetObject, Class introducingClass, String methodName, List ParameterList )
        //
        String traceString = "";
        if (Environment.traceLevel > 0)
          traceString = " (before <" + ((ClassReference)param2).name + "," + param3 + ">)";
        System.out.println( "WOOF" + traceString );
        return new Long(0);
    }
    public String toString() { return "BarkingCodePtr_beforeMethod";} 
}


class BarkingCodePtr_afterMethod      extends CodePtr { 
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
    public String toString() { return "BarkingCodePtr_afterMethod";} 
}


