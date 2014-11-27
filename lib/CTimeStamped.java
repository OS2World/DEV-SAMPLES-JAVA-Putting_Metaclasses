/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */

// This class implements the first of the two CTimeStamped metaclasses
// in Chapter 6.

package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class CTimeStamped extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 
    static public ClassReference noPrevailingClassIndicator = new ClassReference(); 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "CTimeStamped class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(Environment.Class);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );
            
            classObject.name = "CTimeStamped";

            if (Environment.traceLevel > 0) {
                noPrevailingClassIndicator.name = "noPrevailingClassIndicator";
           }

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "prevailingCTSClass", noPrevailingClassIndicator );


            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "makeInstance", new CTimeStampedCodePtr_makeInstance() );

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "initializeClass", new CTimeStampedCodePtr_initializeClass() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "getPrevailingCTSClass", new CTimeStampedCodePtr_getPrevailingCTSClass() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "CTimeStamped class object: construction completed" );
        }
        return classObject;
    }
}

class CTimeStampedCodePtr_makeInstance      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  void makeInstance()
        //
        ObjectReference obj;
        obj = (ObjectReference)target.parentInvoke( CTimeStamped.classObject, Environment.Class, "makeInstance", param1 );
        ClassReference prevailingCTSClass = (ClassReference)target.invoke( Environment.Object, "getIV", CTimeStamped.classObject, "prevailingCTSClass" );
        obj.invoke( Environment.Object, "setIV", prevailingCTSClass, "creationTime", new Long(System.currentTimeMillis()) );
        return obj;
    }
    public String toString() { return "CTimeStampedCodePtr_makeInstance";} 
}

class CTimeStampedCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  Boolean initializeClass ( List aParentList, RDictionary nameValuePairs )
        //
        Boolean result;
        boolean ctsDescendant = false;
        List parents = (List)target.invoke( Environment.Class, "getParents" );
        for ( int i = 0; i < parents.size(); i++ ) {
            ClassReference aParent = (ClassReference)parents.elementAt(i);
            if ( ((Boolean)Environment.getClass( aParent ).invoke( Environment.Class, "isDescendantOf", CTimeStamped.classObject )).booleanValue() ) {
                ctsDescendant = true;
                ClassReference inheritedPrevailingClass = (ClassReference)aParent.invoke( Environment.Object, "getIV", CTimeStamped.classObject, "prevailingCTSClass" );
                target.invoke( Environment.Object, "setIV", CTimeStamped.classObject, "prevailingCTSClass", inheritedPrevailingClass );
                break;
            }
        }
        if ( !ctsDescendant ) {
            ((RDictionary)param2).put( "creationTime", "" );
            target.invoke( Environment.Object, "setIV", CTimeStamped.classObject, "prevailingCTSClass", target );
        }

        result = (Boolean)target.parentInvoke( CTimeStamped.classObject, Environment.Class, "initializeClass", param1, param2 );

        if ( !ctsDescendant ) {
            target.invoke( Environment.Class, "addMethod", target, "getCreationTime", new CTimeStampedCodePtr_getCreationTime() );
        }

        return result;
    }
    public String toString() { return "CTimeStampedCodePtr_initializeClass";} 
}

class CTimeStampedCodePtr_getCreationTime      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  String getCreationTime()
        //
        ClassReference prevailingCTSClass = (ClassReference)(Environment.getClass(target)).invoke( CTimeStamped.classObject, "getPrevailingCTSClass" );
        return target.invoke( Environment.Object, "getIV", prevailingCTSClass, "creationTime" );
    }
    public String toString() { return "CTimeStampedCodePtr_getCreationTime";} 
}

class CTimeStampedCodePtr_getPrevailingCTSClass      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  ClassReference getPrevailingCTSClass()
        //
        return target.invoke( Environment.Object, "getIV", CTimeStamped.classObject, "prevailingCTSClass" );
    }
    public String toString() { return "CTimeStampedCodePtr_getPrevailingCTSClass";} 
}


