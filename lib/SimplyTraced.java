/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */


package putting.lib;

import java.util.*;
import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class SimplyTraced extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 
    static public CodePtr myDispatch = new SimplyTracedCodePtr_dispatch();
    

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "SimplyTraced class object: construction begins" );
            }

            ClassReference RedispatchedClass = Redispatched.newClass();

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(RedispatchedClass);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "SimplyTraced";

            RDictionary anIVPairs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVPairs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "initializeClass", new SimplyTracedCodePtr_initializeClass() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "SimplyTraced class object: construction completed" );
        }
        return classObject;
    }
}

class SimplyTracedCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  Boolean initializeClass ( List aParentList, RDictionary nameValuePairs )
        //
        Boolean result;
        result = (Boolean)target.parentInvoke( SimplyTraced.classObject, Environment.Class, "initializeClass", param1, param2 );

        if ( result.booleanValue() ) {
            target.invoke( Cooperative.classObject, "addCooperativeMethod", Environment.Object, "dispatch", SimplyTraced.myDispatch );
       }
        return result;
    }
    public String toString() { return "SimplyTracedCodePtr_initializeClass";} 
}



class SimplyTracedCodePtr_dispatch      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        //
        //  Object dispatch ( ClassReference introducingClass, String methodName, List args, Object result )
        //
        System.out.println( "***** SIMPLY TRACED ***** <" + ((ClassReference)param1).name + "," + param2 + "> invoked" );
        ClassReference myClass = Environment.getClass(target);
        CodePtr nextDispatch = (CodePtr)myClass.invoke( Cooperative.classObject, 
                                                        "getNextCooperative", 
                                                        Environment.Object, 
                                                        "dispatch", 
                                                        SimplyTraced.myDispatch );
        nextDispatch.execute( target, param1, param2, param3, param4 );
        System.out.println( "***** SIMPLY TRACED ***** <" + ((ClassReference)param1).name + "," + param2 + "> returned" );
	return null;
    }
    public String toString() { return "SimplyTraced_dispatch";} 
}

