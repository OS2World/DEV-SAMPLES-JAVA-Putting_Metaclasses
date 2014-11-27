/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class MetaSingleInstanced extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public CodePtr myMakeInstance = new MetaSingleInstancedCodePtr_makeInstance();
    static public CodePtr myDeleteInstance = new MetaSingleInstancedCodePtr_deleteInstance();

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "MetaSingleInstanced class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement( Cooperative.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "MetaSingleInstanced";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "initializeClass", new MetaSingleInstancedCodePtr_initializeClass() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "MetaSingleInstanced class object: construction completed" );
        }
        return classObject;
    }
}

class MetaSingleInstancedCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  Boolean initializeClass ( List aParentList, RDictionary nameValuePairs )
        //
        Boolean result;
        result = (Boolean)target.parentInvoke( MetaSingleInstanced.classObject, Environment.Class, "initializeClass", param1, param2 );

        if ( result.booleanValue() ) {
                result = (Boolean)target.invoke( Cooperative.classObject, 
                                                 "requestFirstCooperativeMethodCall", 
                                                 Environment.Class, 
                                                 "makeInstance",
                                                 MetaSingleInstanced.myMakeInstance
                                                 );
                if ( result.booleanValue() ) {
                    result = (Boolean)target.invoke( Cooperative.classObject, 
                                                     "requestFirstCooperativeMethodCall", 
                                                     Environment.Class, 
                                                     "deleteInstance",
                                                     MetaSingleInstanced.myDeleteInstance
                                                     );
                    if ( result.booleanValue() ) {
                        target.invoke( Cooperative.classObject, "satisfyRequests" );
                    }
                }
            }
        return result;
    }
    public String toString() { return "MetaSingleInstancedCodePtr_initializeClass";} 
}



class MetaSingleInstancedCodePtr_makeInstance             extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        // Object makeInstance ( ObjectReference obj )
        //
        long refCnt = ((Long)target.invoke( Environment.Object, "getIV", SingleInstanced.classObject, "referenceCount" )).longValue();
        target.invoke( Environment.Object, "setIV", SingleInstanced.classObject, "referenceCount", new Long( refCnt++ ) );
        ObjectReference si = (ObjectReference)target.invoke( Environment.Object, "getIV", SingleInstanced.classObject, "singleInstance" );
        if ( si == SingleInstanced.nullSingleInstance ) {
            ClassReference myClass = Environment.getClass( target );
            CodePtr nextMakeInstance = (CodePtr)myClass.invoke( Cooperative.classObject, 
                                                                "getNextCooperative", 
                                                                Environment.Class, 
                                                                "makeInstance", 
                                                                MetaSingleInstanced.myMakeInstance );
            si = (ObjectReference)nextMakeInstance.execute( target, param1 );
            target.invoke( Environment.Object, "setIV", SingleInstanced.classObject, "singleInstance", si );    
        }
        return si;
   }
    public String toString() { return "MetaSingleInstanced_makeInstance";} 
}



class MetaSingleInstancedCodePtr_deleteInstance         extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  void deleteInstance ( ObjectReference objToBeDeleted )
        //
        long refCnt = ((Long)target.invoke( Environment.Object, "getIV", SingleInstanced.classObject, "referenceCount" )).longValue();
        target.invoke( Environment.Object, "setIV", SingleInstanced.classObject, "referenceCount", new Long( refCnt-- ) );
        if ( refCnt == 0 ) {
            ClassReference myClass = Environment.getClass( target );
            CodePtr nextDeleteInstance = (CodePtr)myClass.invoke( Cooperative.classObject, 
                                                                  "getNextCooperative", 
                                                                  Environment.Class, 
                                                                  "deleteInstance", 
                                                                  MetaSingleInstanced.myDeleteInstance );
            nextDeleteInstance.execute( target, param1 );
            target.invoke( Environment.Object, "setIV", SingleInstanced.classObject, "singleInstance", SingleInstanced.nullSingleInstance );    
        }
	return null;
    }
    public String toString() { return "MetaSingleInstanced_deleteInstance";} 
}
