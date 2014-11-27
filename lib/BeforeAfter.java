/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class BeforeAfter extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public CodePtr myDispatch = new BeforeAfterCodePtr_dispatch();

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "BeforeAfter class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement( MetaBeforeAfter.newClass() );

            List aParentList = new List();
            aParentList.addElement( Redispatched.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "BeforeAfter";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "dispatchList", new List() );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "initializeClass", new BeforeAfterCodePtr_initializeClass() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "beforeMethod", new BeforeAfterCodePtr_beforeMethod() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "afterMethod", new BeforeAfterCodePtr_afterMethod() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "getDispatcher", new BeforeAfterCodePtr_getDispatcher() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "inDispatchList", new BeforeAfterCodePtr_inDispatchList() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "addToDispatchList", new BeforeAfterCodePtr_addToDispatchList() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "removeFromDispatchList", new BeforeAfterCodePtr_removeFromDispatchList() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "BeforeAfter class object: construction completed" );
        }
        return classObject;
    }
}

class BeforeAfterCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  void initializeClass()
        //
        Boolean result;
        result = (Boolean)target.parentInvoke( BeforeAfter.classObject, Environment.Class, "initializeClass", param1, param2 );
        if ( result.booleanValue() ) {
            result = (Boolean)target.invoke( Cooperative.classObject, 
                                             "requestFirstCooperativeMethodCall", 
                                             Environment.Object, 
                                             "dispatch",
                                             BeforeAfter.myDispatch
                                             );
            if ( result.booleanValue() ) {
                target.invoke( Cooperative.classObject, "satisfyRequests" );
            }
            else {
                result = new Boolean(false);
            }
        }
        return result;
    }
    public String toString() { return "BeforeAfterCodePtr_initializeClass";} 
}



class BeforeAfterCodePtr_beforeMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        //
        //  list beforeMethod( Object targetObject, Class introducingClass, String methodName, List ParameterList )
        //
        return new Long(0);
    }
    public String toString() { return "BeforeAfterCodePtr_beforeMethod";} 
}


class BeforeAfterCodePtr_afterMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4, Object param5 ) {
        //
        //  list afterMethod( Object targetObject, Class introducingClass, String methodName, List ParameterList, ResultHolder returnValue )
        //
        return null;
    }
    public String toString() { return "BeforeAfterCodePtr_afterMethod";} 
}


class BeforeAfterCodePtr_getDispatcher      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  CodePtr getDispatcher()
        //
        return BeforeAfter.myDispatch;
    }
    public String toString() { return "BeforeAfterCodePtr_getDispatcher";} 
}


class BeforeAfterCodePtr_inDispatchList      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  Boolean inDispatchList( Object target )
        //
        List dispatchList = (List)target.invoke( Environment.Object, "getIV", BeforeAfter.classObject, "dispatchList" );
        return new Boolean( dispatchList.contains(param1) );
    }
    public String toString() { return "BeforeAfterCodePtr_inDispatchList";} 
}


class BeforeAfterCodePtr_addToDispatchList      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  void addToDispatchList( Object target )
        //
        List dispatchList = (List)target.invoke( Environment.Object, "getIV", BeforeAfter.classObject, "dispatchList" );
        dispatchList.addElement( param1 );
        target.invoke( Environment.Object, "setIV", BeforeAfter.classObject, "dispatchList", dispatchList );
        return null;
    }
    public String toString() { return "BeforeAfterCodePtr_addToDispatchList";} 
}


class BeforeAfterCodePtr_removeFromDispatchList      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  void removeFromDispatchList( Object target )
        //
        List dispatchList = (List)target.invoke( Environment.Object, "getIV", BeforeAfter.classObject, "dispatchList" );
        dispatchList.removeElement( param1 );
        target.invoke( Environment.Object, "setIV", BeforeAfter.classObject, "dispatchList", dispatchList );
        return null;
    }
    public String toString() { return "BeforeAfterCodePtr_removeFromDispatchList";} 
}


class BeforeAfterCodePtr_dispatch      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        //
        //  Object dispatch ( ClassReference introducingClass, String methodName, List args, Object result )
        //

        int i = 0;
        int j = 0;
        long beforeReturnCode = 0;

        ClassReference myClass = Environment.getClass(target);
        ClassReference myMetaclass = Environment.getClass(myClass);

        CodePtr nextDispatch = (CodePtr)myClass.invoke( Cooperative.classObject, 
                                                        "getNextCooperative", 
                                                        Environment.Object, 
                                                        "dispatch", 
                                                        BeforeAfter.myDispatch );
         

        List beforeTable = (List) myMetaclass.invoke( MetaBeforeAfter.classObject, "getBeforeTable" );
        List afterTable = (List) myMetaclass.invoke( MetaBeforeAfter.classObject, "getAfterTable" );


        //-------------------------- Section 0 -- protect against loop --------------------------------

        boolean inList;
        if ( ((Boolean)myClass.invoke( BeforeAfter.classObject, "inDispatchList", param1 )).booleanValue() ) {
            return nextDispatch.execute( target, param1, param2, param3, param4 );
        }
        myClass.invoke( BeforeAfter.classObject, "addToDispatchList", param1 );


        //-------------------------- Section 1 -- invoke before methods -------------------------------

        for (; i < beforeTable.size(); i++ ) {
            CodePtr beforeMethod = (CodePtr)beforeTable.elementAt(i);
            beforeReturnCode = ( (Long)beforeMethod.execute( myClass, target, param1, param2, param3 ) ).longValue();
            if ( beforeReturnCode == 1 )
              break;
            else if  ( beforeReturnCode == 2 ) {
                i++;
                break;
            }
        }


        //-------------------------- Section 2 -- invoke next implementation on implementation chain --

        if ( beforeReturnCode == 0 )
          nextDispatch.execute( target, param1, param2, param3, param4 );


        //-------------------------- Section 3 -- invoke after methods --------------------------------

        for ( j=i-1; j>=0; j-- ) {
            CodePtr afterMethod = (CodePtr)afterTable.elementAt(j);
            afterMethod.execute( myClass, target, param1, param2, param3, param4 );
        }


        //-------------------------- Section 4 -- protect against loop --------------------------------
        myClass.invoke( BeforeAfter.classObject, "removeFromDispatchList", param1 );
	return null;
    }
    public String toString() { return "BeforeAfter_dispatch";} 
}

