/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import java.util.*;
import putting.om.*;


public class ProxyFor extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static CodePtr myFree = new ProxyForCodePtr_free();
    static CodePtr myGetIV = new ProxyForCodePtr_getIV();
    static CodePtr mySetIV = new ProxyForCodePtr_setIV();

    static public ClassReference newClass() {

        if (Environment.traceLevel > 0)
          System.out.println( "ProxyFor class object: construction begins" );

        if ( classObject == null ) {

            List aMetaclassList = new List(Environment.Class);

            List aParentList = new List(Redispatched.newClass());

            ClassReference metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "ProxyFor";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "classOfTarget", new ClassReference() );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "initializeClass", new ProxyForCodePtr_initializeClass() );
 
            classObject.invoke( Environment.Class, "addMethod", classObject, "createProxyClass", new ProxyForCodePtr_createProxyClass() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "getTargetClass", new ProxyForCodePtr_getTargetClass() );

            classObject.invoke( Environment.Class, "readyClass" );


            if (Environment.traceLevel > 0)
              System.out.println( "ProxyFor class object: construction completed" );
        }
        return classObject;
    }
}


class ProxyForCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  void initializeClass()
        //
        Boolean result;
        result = (Boolean)target.parentInvoke( ProxyFor.classObject, Environment.Class, "initializeClass", param1, param2 );
        if ( result.booleanValue() ) {
            result = (Boolean)target.invoke( Cooperative.classObject, 
                                             "requestFirstCooperativeMethodCall", 
                                             Environment.Object, 
                                             "free",
                                             ProxyFor.myFree
                                             );
            if ( result.booleanValue() ) {
                result = (Boolean)target.invoke( Cooperative.classObject, 
                                                 "requestFirstCooperativeMethodCall", 
                                                 Environment.Object, 
                                                 "getIV",
                                                 ProxyFor.myGetIV
                                                 );
                if ( result.booleanValue() ) {
                    result = (Boolean)target.invoke( Cooperative.classObject, 
                                                     "requestFirstCooperativeMethodCall", 
                                                     Environment.Object, 
                                                     "setIV",
                                                     ProxyFor.mySetIV
                                                     );
                    if ( result.booleanValue() ) {
                        target.invoke( Cooperative.classObject, "satisfyRequests" );
                    }
                }
            }
        }
        return result;
    }
    public String toString() { return "ProxyForCodePtr_initializeClass";} 
}

class ProxyForCodePtr_createProxyClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  void createProxyClass( Object target, ClassReference targetClass )
        //
        ClassReference proxyClass;

        List aMetaclassList = new List(Environment.Class);
        List aParentList = new List(target);

        ClassReference metaclass;
        metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

        proxyClass = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

        proxyClass.name = "ProxyFor" + ((ClassReference)param1).name;

        RDictionary anIVDefs = new RDictionary();
        Boolean b = (Boolean)proxyClass.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
        if ( !b.booleanValue() )
          throw new RuntimeException();

        List methodList = (List)((ClassReference)param1).invoke( Environment.Class, "getSupportedMethods" );

	for ( Enumeration e = methodList.elements(); e.hasMoreElements(); ) {
	    Method m = (Method)e.nextElement();
            if ( !((Boolean)target.invoke( Environment.Class, "supportsMethod", m.ic, m.mn )).booleanValue() ) {
                proxyClass.invoke( Environment.Class, "addMethod", m.ic, m.mn, Environment.nullMethod );
            }
	}

        proxyClass.invoke( Environment.Class, "readyClass" );

        proxyClass.invoke( Environment.Object, "setIV", ProxyFor.classObject, "classOfTarget", param1 );

        return proxyClass;
    }
    public String toString() { return "ProxyForCodePtr_createProxyClass";} 
}


class ProxyForCodePtr_getTargetClass      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  void getTargetClass( Object target )
        //
        return target.invoke( Environment.Object, "getIV", ProxyFor.classObject, "classOfTarget" );
    }
    public String toString() { return "ProxyForCodePtr_getTargetClass";} 
}


class ProxyForCodePtr_free           extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  Object free()
        //
        ResultHolder returnValue = new ResultHolder();
        ObjectReference proxyTarget = (ObjectReference)target.invoke( ProxyForObject.classObject, "getTarget" );
        ClassReference myClass = Environment.getClass(proxyTarget);
        if ( proxyTarget != ProxyForObject.nullProxyTargetObject ) {
            CodePtr aCodePtr = (CodePtr)myClass.invoke( Environment.Class,
                                                        "resolveMethod",
                                                        Environment.Object,
                                                        "free" );
            Environment.apply( proxyTarget, aCodePtr, new List(), returnValue );
            if ( !((Boolean)returnValue.value).booleanValue() ) {
                return new Boolean( false );
            }
            proxyTarget.invoke( ProxyForObject.classObject, "setTarget", ProxyForObject.nullProxyTargetObject );
            CodePtr nextFree = (CodePtr)myClass.invoke( Cooperative.classObject, "getNextCooperativeMethod", Environment.Object, "free", ProxyFor.myFree );
            nextFree.execute( target );
        }
	return null;
    }
    public String toString() { return "ProxyForCodePtr_free";} 
}


class ProxyForCodePtr_getIV          extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2  ) {
        //
        //  Object getIV( ClassReference introducingClass, String ivName )
        //        

        ClassReference myClass = Environment.getClass(target);
        if ( ((Boolean)myClass.invoke( Environment.Class, "supportsIV", param1, param2 )).booleanValue() ) {
            CodePtr nextGetIV = (CodePtr)myClass.invoke( Cooperative.classObject, 
                                                         "getNextCooperative", 
                                                         Environment.Object, 
                                                         "getIV", 
                                                         ProxyFor.myGetIV );
            return nextGetIV.execute( target, param1, param2 );
        }
        else {
            ObjectReference proxyTarget = (ObjectReference)target.invoke( ProxyForObject.classObject, "getTarget" );
            return proxyTarget.invoke( Environment.Object, "getIV",  param1, param2 );
        }
    }
    public String toString() { return "ProxyForCodePtr_getIV";} 
}


class ProxyForCodePtr_setIV          extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) {
        //
        //  void setIV( ClassReference introducingClass, String ivName, Object value )
        //
        ClassReference myClass = Environment.getClass(target);
        if ( ((Boolean)myClass.invoke( Environment.Class, "supportsIV", param1, param2 )).booleanValue() ) {
            CodePtr nextGetIV = (CodePtr)myClass.invoke( Cooperative.classObject, 
                                                         "getNextCooperative", 
                                                         Environment.Object, 
                                                         "setIV", 
                                                         ProxyFor.mySetIV );
            nextGetIV.execute( target, param1, param2, param3 );
        }
        else {
            ObjectReference proxyTarget = (ObjectReference)target.invoke( ProxyForObject.classObject, "getTarget" );
            proxyTarget.invoke( Environment.Object, "setIV",  param1, param2, param3 );
        }
        return null;
    }
    public String toString() { return "ProxyForCodePtr_setIV";} 
}
