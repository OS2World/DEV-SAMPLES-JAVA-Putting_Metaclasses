/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class ParentMethodCallPreventionEnabled extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public CodePtr myResolveTerminal = new ParentMethodCallPreventionEnabledCodePtr_resolveTerminal();

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "ParentMethodCallPreventionEnabled class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement( Environment.Class );

            List aParentList = new List();
            aParentList.addElement( Cooperative.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "ParentMethodCallPreventionEnabled";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, 
                                "overrideMethod", 
                                Environment.Class, 
                                "resolveTerminal", 
                                new ParentMethodCallPreventionEnabledCodePtr_resolveTerminal() );

             classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "ParentMethodCallPreventionEnabled class object: construction completed" );
        }
        return classObject;
    }
}

class ParentMethodCallPreventionEnabledCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  Boolean initializeClass ( List aParentList, RDictionary nameValuePairs )
        //
        Boolean result;
        result = (Boolean)target.parentInvoke( ParentMethodCallPreventionEnabled.classObject, Environment.Class, "initializeClass", param1, param2 );

        if ( result.booleanValue() ) {
            result = (Boolean)target.invoke( Cooperative.classObject, 
                                             "requestFirstCooperativeMethodCall", 
                                             Environment.Class, 
                                             "resolveTerminal", 
                                             ParentMethodCallPreventionEnabled.myResolveTerminal );
            if ( result.booleanValue() ) {
                target.invoke( Cooperative.classObject, "satisfyRequests" );
            }
	}
        return result;
    }
    public String toString() { return "ParentMethodCallPreventionEnabledCodePtr_initializeClass";} 
}



class ParentMethodCallPreventionEnabledCodePtr_parentMethodCallDisabler      extends CodePtr { 
    public String toString() { return "ParentMethodCallPreventionEnabledCodePtr_parentMethodCallDisabler";} 
}

class ParentMethodCallPreventionEnabledCodePtr_resolveTerminal      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //   CodePtr resolveTerminal( ClassReference introducingClass, String methodName )
        //
        if ( param1 == BeforeAfter.classObject && ("beforeMethod".equals(param2) || "afterMethod".equals(param2))  ) {
            return new ParentMethodCallPreventionEnabledCodePtr_parentMethodCallDisabler();
        }
        return target.parentInvoke( ParentMethodCallPreventionEnabled.classObject,
                                    Environment.Class,
                                    "resolveTerminal",
                                    param1,
                                    param2 );
    }
    public String toString() { return "ParentMethodCallPreventionEnabledCodePtr_resolveTerminal";} 
}

