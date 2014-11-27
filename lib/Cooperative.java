/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */

// This class implements the Cooperative metaclass in Chapter 7.

package putting.lib;

import java.util.*;
import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class Cooperative extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 
    static public ClassReference noPrevailingClassIndicator = new ClassReference(); 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "Cooperative class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement(Environment.Class);

            List aParentList = new List();
            aParentList.addElement(Environment.Class);

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "Cooperative";

            RDictionary anIVPairs = new RDictionary();
            anIVPairs.put( "terminalImplementations", new RDictionary() );
            anIVPairs.put( "cooperationChains", new RDictionary() );
            anIVPairs.put( "pendingRequests", new RDictionary() );
            anIVPairs.put( "satisfiedRequests", new RDictionary() );
            anIVPairs.put( "isAddCooperativeCalling", new Boolean( false ) );


            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVPairs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "initializeClass", new CooperativeCodePtr_initializeClass() );

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "addMethod", new CooperativeCodePtr_addMethod() );

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "overrideMethod", new CooperativeCodePtr_overrideMethod() );

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "resolveTerminal", new CooperativeCodePtr_resolveTerminal() );

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "readyClass", new CooperativeCodePtr_readyClass() );

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "putRDStub", new CooperativeCodePtr_putRDStub() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "addCooperativeMethod", new CooperativeCodePtr_addCooperativeMethod() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "getNextCooperative", new CooperativeCodePtr_getNextCooperative() );

            classObject.invoke( Environment.Class, "addMethod", 
                                classObject, 
                                "requestFirstCooperativeMethodCall", 
                                new CooperativeCodePtr_requestFirstCooperativeMethodCall() );

            classObject.invoke( Environment.Class, "addMethod", classObject, "satisfyRequests", new CooperativeCodePtr_satisfyRequests() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "Cooperative class object: construction completed" );
        }
        return classObject;
    }
}

class CooperativeCodePtr_initializeClass      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //  Boolean initializeClass ( List aParentList, RDictionary nameValuePairs )
        //
        Boolean result;
        boolean doDispatchOverride = true; 
        result = (Boolean)target.parentInvoke( Cooperative.classObject, Environment.Class, "initializeClass", param1, param2 );
        RDictionary termImpls = new RDictionary();

        if ( result.booleanValue() ) {

            List theMRO = (List)target.invoke( Environment.Class, "getMRO" );
            if ( theMRO == null ) {
                return new Boolean( false );
            }
            for ( Enumeration e = theMRO.elements(); e.hasMoreElements(); ) { 
                ObjectReference anAncestor = (ObjectReference)e.nextElement();
                if ( ((Boolean)Environment.getClass( anAncestor ).invoke( Environment.Class, "isDescendantOf", Cooperative.classObject )).booleanValue() ) { 
                    // It the ancestor is Cooperative, merge the terminal implementations
                    RDictionary inheritedTermImpls = (RDictionary)anAncestor.invoke( Environment.Object, 
                                                                                     "getIV", 
                                                                                     Cooperative.classObject, 
                                                                                     "terminalImplementations" );
                    termImpls = termImpls.recursive_merge( inheritedTermImpls );
                }
            }
            target.invoke( Environment.Object, "setIV", Cooperative.classObject, "terminalImplementations", termImpls );

            List parents = (List)target.invoke( Environment.Class, "getParents" );
            for ( int i = 0; i < parents.size(); i++ ) {
                ClassReference aParent = (ClassReference)parents.elementAt(i);
                if ( ((Boolean)Environment.getClass( aParent ).invoke( Environment.Class, "isDescendantOf", Cooperative.classObject )).booleanValue() ) {
                    doDispatchOverride = false;
                }
            }
            if ( doDispatchOverride ) {
                target.invoke( Environment.Class, "overrideMethod", Environment.Object, "dispatch", new CooperativeCodePtr_dispatch() );               
            }

        }
        return result;
    }
    public String toString() { return "CooperativeCodePtr_initializeClass";} 
}

class CooperativeCodePtr_addMethod       extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) {
        //
        //  void addMethod ( ClassReference introducingClass, String methodName, CodePtr methodImpl )
        //
        List coopChain = null;
        RDictionary cooperationChains =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "cooperationChains" );
        RDictionary aCoopSegment = (RDictionary)cooperationChains.get( param1 );
        if ( aCoopSegment != null ) {
            coopChain = (List)aCoopSegment.get( param2 );
        }
        if ( coopChain != null ) {
            RDictionary terminalImplementations =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "terminalImplementations" );
            RDictionary anInnerDict = new RDictionary( param2, param3 );
            RDictionary anOuterDict = new RDictionary( param1, anInnerDict );
            terminalImplementations = anOuterDict.recursive_merge( terminalImplementations );
            target.invoke( Environment.Object, "setIV", Cooperative.classObject, "terminalImplementations", terminalImplementations );
            coopChain.setElementAt( param3, coopChain.size()-1 ); // destructive modication of the value of instance variable
            param3 = coopChain.elementAt(0);
        }
        target.parentInvoke( Cooperative.classObject, Environment.Class, "addMethod", param1, param2, param3 );
	return null;
    }
    public String toString() { return "CooperativeCodePtr_addMethod";} 
}

class CooperativeCodePtr_overrideMethod  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) {
        //
        //  void overrideMethod ( ClassReference introducingClass, String methodName, CodePtr methodImpl )
        //
        Boolean isAddCooperativeCalling = (Boolean)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "isAddCooperativeCalling" );
        if ( !isAddCooperativeCalling.booleanValue() ) {
            List coopChain = null;
            RDictionary cooperationChains =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "cooperationChains" );
            RDictionary aCoopSegment = (RDictionary)cooperationChains.get( param1 );
            if ( aCoopSegment != null ) {
                coopChain = (List)aCoopSegment.get( param2 );
            }
            if ( coopChain != null ) {
                // If there is cooperation on this method, the terminal implementations and the cooperation chain must be changed.
                CodePtr mtabEntry = (CodePtr)target.invoke( Environment.Class, "resolveMethod", param1, param2 );
                target.parentInvoke( Cooperative.classObject, Environment.Class, "overrideMethod", param1, param2, mtabEntry );
                RDictionary terminalImplementations =  (RDictionary)target.invoke( Environment.Object, 
                                                                                   "getIV", 
                                                                                   Cooperative.classObject, 
                                                                                   "terminalImplementations" );
                RDictionary anInnerDict = new RDictionary( param2, param3 );
                RDictionary anOuterDict = new RDictionary( param1, anInnerDict );
                terminalImplementations = anOuterDict.recursive_merge( terminalImplementations );
                target.invoke( Environment.Object, "setIV", Cooperative.classObject, "terminalImplementations", terminalImplementations );
                coopChain.setElementAt( param3, coopChain.size()-1 ); // destructive modication of the value of instance variable
            }
            else {
                target.parentInvoke( Cooperative.classObject, Environment.Class, "overrideMethod", param1, param2, param3 );
            }
        }
        else {
            target.parentInvoke( Cooperative.classObject, Environment.Class, "overrideMethod", param1, param2, param3 );
        }
	return null;
    }
    public String toString() { return "CooperativeCodePtr_overrideMethod";} 
}

class CooperativeCodePtr_resolveTerminal  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        //
        //   CodePtr resolveTerminal( ClassReference introducingClass, String methodName )
        //
        RDictionary terminalImplementations =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "terminalImplementations" );
        if ( terminalImplementations != null ) {
            RDictionary aTableSegment = (RDictionary)terminalImplementations.get( param1 );
            if ( aTableSegment != null ) {
                CodePtr terminalImpl = (CodePtr)aTableSegment.get( param2 );
                if ( terminalImpl != null ) {
                    return terminalImpl;
                }
            }
        }
        return (CodePtr)target.invoke( Environment.Class, "resolveMethod", param1, param2 ); 
    }
    public String toString() { return "CooperativeCodePtr_resolveTerminal";} 
}

class CooperativeCodePtr_addCooperativeMethod      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) { 
        //
        //  void addCooperativeMethod( ClassReference introducingClass, String methodName, CodePtr methodImpl )
        //
        RDictionary terminalImplementations =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "terminalImplementations" );
        RDictionary cooperationChains =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "cooperationChains" );

        boolean addMethodAlreadyDone = true;
        CodePtr mtabEntry;
	try {
            mtabEntry = (CodePtr)target.invoke( Environment.Class, "resolveMethod", param1, param2 );
	}  catch ( OMResolutionFailure e ) {
            mtabEntry = new CodePtr();
            addMethodAlreadyDone = false;
            // This means that the addMethod has not yet been done.
            // Set mtabEntry to a useless code pointer and put it in the cooperation chain 
            // (addMethod fills it in later).
	}

        List coopChain = null;
        RDictionary aCoopSegment = (RDictionary)cooperationChains.get( param1 );
        if ( aCoopSegment != null ) {
            coopChain = (List)aCoopSegment.get( param2 );
        }
        if ( coopChain == null ) {
            CodePtr terminalImpl = null;
            RDictionary aTableSegment = (RDictionary)terminalImplementations.get( param1 );
            if ( aTableSegment != null ) {
                terminalImpl = (CodePtr)aTableSegment.get( param2 );
            }
            if ( terminalImpl == null ) {
                RDictionary anInnerDict = new RDictionary( param2, mtabEntry );
                RDictionary anOuterDict = new RDictionary( param1, anInnerDict );
                terminalImplementations = terminalImplementations.recursive_merge( anOuterDict );
                target.invoke( Environment.Object, "setIV", Cooperative.classObject, "terminalImplementations", terminalImplementations );
            }
            aTableSegment = (RDictionary)terminalImplementations.get( param1 );
            terminalImpl = (CodePtr)aTableSegment.get( param2 );

            RDictionary anInnerDict = new RDictionary( param2, new List(terminalImpl) );
            RDictionary anOuterDict = new RDictionary( param1, anInnerDict );
            cooperationChains = cooperationChains.recursive_merge( anOuterDict );
            target.invoke( Environment.Object, "setIV", Cooperative.classObject, "cooperationChains", cooperationChains );
        }

        aCoopSegment = (RDictionary)cooperationChains.get( param1 );
        List chain = (List)aCoopSegment.get( param2 );
        chain = (new List( param3 )).merge( chain );
 
        RDictionary anInnerDict = new RDictionary( param2, chain );
        RDictionary anOuterDict = new RDictionary( param1, anInnerDict );
        cooperationChains = anOuterDict.recursive_merge( cooperationChains );
        target.invoke( Environment.Object, "setIV", Cooperative.classObject, "cooperationChains", cooperationChains );

        if ( addMethodAlreadyDone ) {
            target.invoke( Environment.Object, "setIV", Cooperative.classObject, "isAddCooperativeCalling", new Boolean( true ) );
            target.invoke( Environment.Class, "overrideMethod", param1, param2, param3 );
            target.invoke( Environment.Object, "setIV", Cooperative.classObject, "isAddCooperativeCalling", new Boolean( false ) );
        }

        return null; 
    }
    public String toString()                        { return "CooperativeCodePtr_addCooperativeMethod";} 
}

class CooperativeCodePtr_getNextCooperative      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) { 
        //
        //  CodePtr getNextCooperative( ClassReference introducingClass, String methodName, CodePtr currentImpl )
        //
        RDictionary cooperationChains =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "cooperationChains" );
        RDictionary aCoopSegment = (RDictionary)cooperationChains.get( param1 );
        if ( aCoopSegment != null ) {
            List coopChain = (List)aCoopSegment.get( param2 );
            if ( coopChain != null ) {
                for ( Enumeration e = coopChain.elements(); e.hasMoreElements(); ) {
                    CodePtr aCodePtr = (CodePtr)e.nextElement();
                    if ( aCodePtr == (CodePtr)param3 ) {
                        if ( e.hasMoreElements() ) {
                            return (CodePtr)e.nextElement();
                        }
                        else {
                            System.out.println( "***** FAILURE ***** getNextCooperative: found current method at end of chain");
                            throw new RuntimeException(); 
                       }
                    }          
                }
                System.out.println( "***** FAILURE ***** getNextCooperative(" 
                                    + ((ClassReference)param1).name 
                                    + "," 
                                    + param2 
                                    + "," 
                                    + param3 
                                    + "): failed to find current method in chain");
                throw new RuntimeException(); 
            }
            else {
                System.out.println( "***** FAILURE ***** getNextCooperative: failed to find cooperation chain");
                throw new RuntimeException(); 
            }
        }
        else {
            System.out.println( "***** FAILURE ***** getNextCooperative: failed to find class segment");
            throw new RuntimeException();
        }
    }
    public String toString()                        { return "CooperativeCodePtr_getNextCooperative";} 
}

class CooperativeCodePtr_requestFirstCooperativeMethodCall      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) { 
        //
        //  void requestFirstCooperativeMethodCall( ClassReference introducingClass, String methodName, CodePtr methodImpl )
        //
        RDictionary satisfiedRequests =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "satisfiedRequests" );
        if ( satisfiedRequests != null ) {
            RDictionary aSegment = (RDictionary)satisfiedRequests.get( param1 );
            if ( aSegment != null ) {
                if ( aSegment.get( param2 ) != null ) {
                    target.invoke( Environment.Object, "setIV", Cooperative.classObject, "pendingRequests", new RDictionary() );
                    return new Boolean( false );
                }
            }
        }
        RDictionary pendingRequests = (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "pendingRequests" );
        if ( pendingRequests != null ) {
            RDictionary aSegment = (RDictionary)pendingRequests.get( param1 );
            if ( aSegment != null ) {
                if ( aSegment.get( param2 ) != null ) {
                    target.invoke( Environment.Object, "setIV", Cooperative.classObject, "pendingRequests", new RDictionary() );
                    return new Boolean( false );
                }
            }
        }
        RDictionary anInnerDict = new RDictionary( param2, param3 );
        RDictionary anOuterDict = new RDictionary( param1, anInnerDict );
        pendingRequests = anOuterDict.recursive_merge( pendingRequests );
        target.invoke( Environment.Object, "setIV", Cooperative.classObject, "pendingRequests", pendingRequests );
        return new Boolean( true ); 
    }
    public String toString()                        { return "CooperativeCodePtr_requestFirstCooperativeMethodCall";} 
}

class CooperativeCodePtr_satisfyRequests      extends CodePtr { 
    public Object execute( ObjectReference target ) { 
        //
        //  void satisfyRequests()
        //
        RDictionary satisfiedRequests = (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "satisfiedRequests" );
        RDictionary pendingRequests = (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "pendingRequests" );
        satisfiedRequests = satisfiedRequests.recursive_merge( pendingRequests );
        target.invoke( Environment.Object, "setIV", Cooperative.classObject, "satisfiedRequests", satisfiedRequests );
        return null; 
    }
    public String toString()                        { return "CooperativeCodePtr_satisfyRequests";} 
}

class CooperativeCodePtr_readyClass      extends CodePtr { 
    public Object execute( ObjectReference target ) { 
        //
        //  void readyClass()
        //
        RDictionary pendingRequests = (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "pendingRequests" );
        for ( Enumeration e1 = pendingRequests.keys(); e1.hasMoreElements(); ) {
	    ClassReference introducingClass = (ClassReference)e1.nextElement();
	    RDictionary aSegment = (RDictionary)pendingRequests.get( introducingClass );
            for ( Enumeration e2 = aSegment.keys(); e2.hasMoreElements(); ) {
                String aMethodName = (String)e2.nextElement();
                CodePtr aCooperativeMethodImpl = (CodePtr)aSegment.get( aMethodName );
                target.invoke( Cooperative.classObject, "addCooperativeMethod", introducingClass, aMethodName, aCooperativeMethodImpl );
            }
        }
        target.parentInvoke( Cooperative.classObject, Environment.Class, "readyClass" );
        target.invoke( Environment.Object, "setIV", Cooperative.classObject, "pendingRequests", new RDictionary() );
        return null; 
    }
    public String toString()                        { return "CooperativeCodePtr_readyClass";} 
}

class CooperativeCodePtr_putRDStub      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) { 
        //
        //   void putRDStub( ClassReference introducingClass, String methodName )
        //
        RDictionary terminalImplementations =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "terminalImplementations" );
        RDictionary cooperationChains =  (RDictionary)target.invoke( Environment.Object, "getIV", Cooperative.classObject, "cooperationChains" );

        CodePtr mtabEntry;
	try {
            mtabEntry = (CodePtr)target.invoke( Environment.Class, "resolveMethod", param1, param2 );
	}  catch ( OMResolutionFailure e ) {
            mtabEntry = new CodePtr();
            // This means that the addMethod has not yet been done.
            // Set mtabEntry to a useless code pointer and put it in the cooperation chain 
            // (addMethod fills it in later).
	}

        List coopChain = null;
        RDictionary aCoopSegment = (RDictionary)cooperationChains.get( param1 );
        if ( aCoopSegment != null ) {
            coopChain = (List)aCoopSegment.get( param2 );
        }
        if ( coopChain == null ) {
            CodePtr terminalImpl = null;
            RDictionary aTableSegment = (RDictionary)terminalImplementations.get( param1 );
            if ( aTableSegment != null ) {
                terminalImpl = (CodePtr)aTableSegment.get( param2 );
            }
            if ( terminalImpl == null ) {
                RDictionary anInnerDict = new RDictionary( param2, mtabEntry );
                RDictionary anOuterDict = new RDictionary( param1, anInnerDict );
                terminalImplementations = terminalImplementations.recursive_merge( anOuterDict );
                target.invoke( Environment.Object, "setIV", Cooperative.classObject, "terminalImplementations", terminalImplementations );
            }
            aTableSegment = (RDictionary)terminalImplementations.get( param1 );
            terminalImpl = (CodePtr)aTableSegment.get( param2 );

            RDictionary anInnerDict = new RDictionary( param2, new List(terminalImpl) );
            RDictionary anOuterDict = new RDictionary( param1, anInnerDict );
            cooperationChains = cooperationChains.recursive_merge( anOuterDict );
            target.invoke( Environment.Object, "setIV", Cooperative.classObject, "cooperationChains", cooperationChains );
        }
        target.parentInvoke( Cooperative.classObject, Environment.Class, "putRDStub", param1, param2 );
        return null; 
    }
    public String toString()                        { return "CooperativeCodePtr_putRDStub";} 
}

class CooperativeCodePtr_dispatch      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        //
        //  Object dispatch ( ClassReference introducingClass, String methodName, List args, Object result )
        //
        RDictionary terminalImplementations =  (RDictionary)Environment.getClass(target).invoke( Environment.Object, 
                                                                                                 "getIV", 
                                                                                                 Cooperative.classObject, 
                                                                                                 "terminalImplementations" );
        RDictionary aTableSegment = (RDictionary)terminalImplementations.get( param1 );
        CodePtr terminalImpl = (CodePtr)aTableSegment.get( param2 );
        Environment.apply( target, terminalImpl, (List)param3, (ResultHolder)param4 );
	return null;
    }
    public String toString() { return "Cooperative_dispatch";} 
}

