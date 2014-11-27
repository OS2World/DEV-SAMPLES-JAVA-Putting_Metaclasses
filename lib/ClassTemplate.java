/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class ClassTemplate extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "ClassTemplate class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(Environment.Class);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "ClassTemplate";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "extent", new List() );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "makeInstance", new ClassTemplateCodePtr_makeInstance() );
            classObject.invoke( Environment.Class, "addMethod", classObject, "getExtent", new ClassTemplateCodePtr_sampleMethod() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "ClassTemplate class object: construction completed" );
        }
        return classObject;
    }
}

//
//  SAMPLE OF AN OVERRRIDE
//
class ClassTemplateCodePtr_makeInstance      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  void makeInstance()
        //
        ObjectReference obj;
        obj = (ObjectReference)target.parentInvoke( ClassTemplate.classObject, Environment.Class, "makeInstance", param1 );
        return obj;
    }
    public String toString() { return "ClassTemplateCodePtr_makeInstance";} 
}



class ClassTemplateCodePtr_sampleMethod      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  list getExtent()
        //
        return null;
    }
    public String toString() { return "ClassTemplateCodePtr_sampleMethod";} 
}

