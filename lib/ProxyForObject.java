/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

public class ProxyForObject extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ObjectReference nullProxyTargetObject = new ObjectReference();

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "ProxyForObject class object: construction begins" );

        if ( classObject == null ) {

            ClassReference ProxyForClassObject = ProxyFor.newClass();

            List aMetaclassList = new List();
            aMetaclassList.addElement(ProxyForClassObject);

            List aParentList = new List();
            aParentList.addElement(Environment.Object);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "ProxyForObject";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "target", nullProxyTargetObject );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Object, "dispatch", new ProxyForObjectCodePtr_dispatch() );
 
            classObject.invoke( Environment.Class, "addMethod", classObject, "getTarget", new ProxyForObjectCodePtr_getTarget() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "setTarget", new ProxyForObjectCodePtr_setTarget() );

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "ProxyForObject class object: construction completed" );
        }
        return classObject;
    }
}

class ProxyForObjectCodePtr_getTarget      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  void getTarget( Object target )
        //
        return target.invoke( Environment.Object, "getIV", ProxyForObject.classObject, "target" );
    }
    public String toString() { return "ProxyForObjectCodePtr_getTarget";} 
}


class ProxyForObjectCodePtr_setTarget      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  void setTarget( Object target, Object targetObject )
        //
        target.invoke( Environment.Object, "setIV", ProxyForObject.classObject, "target", param1 );
        return null;
    }
    public String toString() { return "ProxyForObjectCodePtr_setTarget";} 
}

class ProxyForObjectCodePtr_dispatch       extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        //
        //  Object dispatch ( ClassReference introducingClass, String methodName, List args, Object result )
        //
        CodePtr aCodePtr;
        ClassReference myClass = Environment.getClass( target );
        if ( ((Boolean)myClass.invoke( Environment.Class, "isDescendantOf", param1 )).booleanValue() ) {
            aCodePtr = (CodePtr)myClass.invoke( Environment.Class, "resolveTerminal", param1, param2 );
            if (Environment.traceLevel > 0) {
                System.out.println( Indentation.spaces + "ProxyForObjectCodePtr_dispatch invokes local method <" + ((ClassReference)param1).name + "," + param2 + ">" );
            }
            Environment.apply( target, aCodePtr, (List)param3, (ResultHolder)param4 );
        }
        else {
            ObjectReference proxyTarget;
            proxyTarget = (ObjectReference)target.invoke( ProxyForObject.classObject, "getTarget" );
            ClassReference proxyTargetClass;
            proxyTargetClass = (ClassReference)myClass.invoke( ProxyFor.classObject, "getTargetClass" );
            aCodePtr = (CodePtr)proxyTargetClass.invoke( Environment.Class, "resolveMethod", param1, param2 );
            if (Environment.traceLevel > 0) {
                System.out.println( Indentation.spaces + "ProxyForObjectCodePtr_dispatch forwards method <" + ((ClassReference)param1).name + "," + param2 + ">" );
            }
            Environment.apply( proxyTarget, aCodePtr, (List)param3, (ResultHolder)param4 );
        }
	return null;
    }
    public String toString() { return "ProxyForObject_dispatch";} 
}

