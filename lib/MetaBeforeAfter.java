/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class MetaBeforeAfter extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public long  globalSearchCount = 0; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "MetaBeforeAfter class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(Environment.Class);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "MetaBeforeAfter";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "beforeTable", new RDictionary() );
            anIVDefs.put( "afterTable", new RDictionary() );
            anIVDefs.put( "searchCount", new Long(0) );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "readyClass", new MetaBeforeAfterCodePtr_readyClass() );
            classObject.invoke( Environment.Class, "addMethod", classObject, "compileBA", new MetaBeforeAfterCodePtr_compileBA() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "getBeforeTable", new MetaBeforeAfterCodePtr_getBeforeTable() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "getAfterTable", new MetaBeforeAfterCodePtr_getAfterTable() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "MetaBeforeAfter class object: construction completed" );
        }
        return classObject;
    }
}

class MetaBeforeAfterCodePtr_readyClass      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  void readyClass()
        //
        ObjectReference obj;

        List beforeTable = new List();
        MetaBeforeAfter.globalSearchCount++;
        beforeTable = (List)target.invoke( MetaBeforeAfter.classObject, "compileBA", "beforeMethod", beforeTable ); 
        target.invoke( Environment.Object, "setIV", MetaBeforeAfter.classObject, "beforeTable", beforeTable ); 

        List afterTable = new List();
        MetaBeforeAfter.globalSearchCount++;
        afterTable = (List)target.invoke( MetaBeforeAfter.classObject, "compileBA", "afterMethod", afterTable ); 
        target.invoke( Environment.Object, "setIV", MetaBeforeAfter.classObject, "afterTable", afterTable ); 

        target.parentInvoke( MetaBeforeAfter.classObject, Environment.Class, "readyClass" );
        return null;
    }
    public String toString() { return "MetaBeforeAfterCodePtr_readyClass";} 
}



class MetaBeforeAfterCodePtr_compileBA      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  list compileBA( string baMethodName, List aList )
        //
        Long searchCount =  (Long)target.invoke( Environment.Object, "getIV", MetaBeforeAfter.classObject, "searchCount" ); 
        if ( searchCount.longValue() >= MetaBeforeAfter.globalSearchCount )
          return param2;
        searchCount = new Long( MetaBeforeAfter.globalSearchCount );
        target.invoke( Environment.Object, "setIV", MetaBeforeAfter.classObject, "searchCount", searchCount );
        boolean definesBefore = ((Boolean)target.invoke( Environment.Class, "definesMethod", BeforeAfter.classObject, "beforeMethod" )).booleanValue();
        boolean definesAfter = ((Boolean)target.invoke( Environment.Class, "definesMethod", BeforeAfter.classObject, "afterMethod" )).booleanValue();
        if ( definesBefore || definesAfter ) {
            CodePtr methodImpl = (CodePtr)target.invoke( Environment.Class, "resolveMethod", BeforeAfter.classObject, param1 );
            return ((List)param2).merge( new List(methodImpl) );
        }
        else {
            List parents = (List)target.invoke( Environment.Class, "getParents" );
            for ( int i = 0; i < parents.size(); i++ ) {
                ClassReference aParent = (ClassReference)parents.elementAt(i);
                boolean respondsToCompileBA = ((Boolean)(Environment.getClass(aParent)).invoke( Environment.Class, 
                                                                                                "supportsMethod", 
                                                                                                MetaBeforeAfter.classObject, 
                                                                                                "compileBA" )).booleanValue();
                if ( respondsToCompileBA ) {
                    param2 = aParent.invoke( MetaBeforeAfter.classObject, "compileBA", param1, param2 );
                }
            }
        }
        return param2;
    }
    public String toString() { return "MetaBeforeAfterCodePtr_compileBA";} 
}

class MetaBeforeAfterCodePtr_getBeforeTable      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  list getBeforeTable()
        //
        return target.invoke( Environment.Object, "getIV", MetaBeforeAfter.classObject, "beforeTable" ); 
    }
    public String toString() { return "MetaBeforeAfterCodePtr_getBeforeTable";} 
}

class MetaBeforeAfterCodePtr_getAfterTable      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  list getAfterTable()
        //
        return target.invoke( Environment.Object, "getIV", MetaBeforeAfter.classObject, "afterTable" ); 
    }
    public String toString() { return "MetaBeforeAfterCodePtr_getAfterTable";} 
}

