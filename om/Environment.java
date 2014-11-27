/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

public class Environment {

    static public CodePtr        nullMethod = new CodePtr();
    static public int            traceLevel = 0;
    static public boolean        simpleMRO  = true;

    static public ClassReference Object = new ClassReference();
    static public ClassReference Class = new ClassReference();

    static String  myclass    = "class";



    public Environment() {

        Object.name = "Object";
        Class.name = "Class";

        Object.put( myclass, Class );
        Object.put( Class, new ClassIVS() );

        Class.put( myclass, Class );
        Class.put( Class, new ClassIVS() );
        
        ClassIVS classIVS = (ClassIVS)Class.get( Class );

        // Class.Class.parents
        classIVS.parents.addElement(Object);

        // Class.Class.ivdefs
        classIVS.ivdefs = new RDictionary( Class, new ClassIVS() );

        // Class.Class.ivs
        classIVS.ivs = new RDictionary( Class, new ClassIVS() );

        RDictionary classMtabSegment = new RDictionary();
        classMtabSegment.put( "addMethod",            new CodePtr_addMethod() );
        classMtabSegment.put( "definesMethod",        new CodePtr_definesMethod() );
        classMtabSegment.put( "deleteInstance",       new CodePtr_deleteInstance() );
        classMtabSegment.put( "getMRO",               new CodePtr_getMRO() );
        classMtabSegment.put( "getParents",           new CodePtr_getParents() );
        classMtabSegment.put( "getSupportedMethods",  new CodePtr_getSupportedMethods() );
        classMtabSegment.put( "initializeClass",      new CodePtr_initializeClass() );
        classMtabSegment.put( "introducesMethod",     new CodePtr_introducesMethod() );
        classMtabSegment.put( "isAncestorOf",         new CodePtr_isAncestorOf() );
        classMtabSegment.put( "isDescendantOf",       new CodePtr_isDescendantOf() );
        classMtabSegment.put( "isSubclassOf",         new CodePtr_isSubclassOf() );
        classMtabSegment.put( "makeInstance",         new CodePtr_makeInstance() );
        classMtabSegment.put( "overrideMethod",       new CodePtr_overrideMethod() );
        classMtabSegment.put( "putRDStub",            new CodePtr_putRDStub() );
        classMtabSegment.put( "readyClass",           new CodePtr_readyClass() );
        classMtabSegment.put( "resolveMethod",        new CodePtr_resolveMethod() );
        classMtabSegment.put( "resolveTerminal",      new CodePtr_resolveTerminal() );
        classMtabSegment.put( "supportsIV",           new CodePtr_supportsIV() );
        classMtabSegment.put( "supportsMethod",       new CodePtr_supportsMethod() );

        RDictionary objectMtabSegment = new RDictionary();
        objectMtabSegment.put( "initialize",       new CodePtr_initialize() );
        objectMtabSegment.put( "destroy",          new CodePtr_destroy() );
        objectMtabSegment.put( "dispatch",         new CodePtr_dispatch() );
        objectMtabSegment.put( "free",             new CodePtr_free() );
        objectMtabSegment.put( "getIV",            new CodePtr_getIV() );
        objectMtabSegment.put( "setIV",            new CodePtr_setIV() );

        classIVS.mdefs = new RDictionary( Class, classMtabSegment );
        classIVS.mtab = new RDictionary( Class, (RDictionary)classMtabSegment.clone() );
        classIVS.mtab.put( Object, objectMtabSegment );

        ClassIVS objectIVS = (ClassIVS)Object.get( Class );
        objectIVS.mdefs = new RDictionary( Object, (RDictionary)objectMtabSegment.clone() );
        objectIVS.mtab = new RDictionary( Object, (RDictionary)objectMtabSegment.clone() );
    }


    static public ClassReference getClass( ObjectReference target ) {
        ClassReference targetClass = (ClassReference)target.get(myclass);
        if ( targetClass == null ) {
            //System.out.println( "***** FAILURE ***** getClass: target is ill formed object ---- no class");
            throw new RuntimeException(); // getClassFailure();
        }
        return targetClass;
    }

    static public ClassReference getClass( ClassReference target ) {
        ClassReference targetClass = (ClassReference)target.get(myclass);
        if ( targetClass == null ) {
            System.out.println( "***** FAILURE ***** getClass: target " + target.name + " is ill formed object -- no class");
            throw new RuntimeException(); // getClassFailure();
        }
        return targetClass;
    }


    static public CodePtr resolve( ClassReference targetClass, ClassReference introducingClass, String methodName ){
        CodePtr implementation;
        RDictionary mtab = getClassData(targetClass).mtab;
        if ( mtab != null ) {
            RDictionary icSegment = (RDictionary)mtab.get(introducingClass);
            if ( icSegment == null ) {
                if (traceLevel > 0)
                  System.out.println( "***** FAILURE ***** resolveMethod: could not find mtab segment for <" + introducingClass.name + "," + methodName
                                      + ">\n                                   where class of target is: " + targetClass.name );
                throw new  OMResolutionFailure();
            }
            implementation = (CodePtr)icSegment.get(methodName);
            if ( implementation == null ) {
                if (traceLevel > 0)
                  System.out.println( "***** FAILURE ***** resolveMethod: could not find implementation for <" + introducingClass.name + "," + methodName
                                      + ">\n                                   where class of target is: " + targetClass.name );
                throw new  OMResolutionFailure();
            }
        }
        else {
            if (traceLevel > 0)
              System.out.println( "***** FAILURE ***** resolveMethod: could not find mtab for <" + introducingClass.name + "," + methodName
                                      + ">\n                                   where class of target is: " + targetClass.name );
            throw new  OMResolutionFailure();
        }
        return implementation;
    }


    static public CodePtr parentResolve( ObjectReference target, 
                                         ClassReference overridingClass, 
                                         ClassReference introducingClass, 
                                         String methodName ){
        ClassReference targetClass = Environment.getClass( target );
        boolean foundOverridingClass = false;
        for ( Enumeration e = MRO(targetClass).elements(); e.hasMoreElements(); ) { 
            ClassReference anAncestor = (ClassReference)e.nextElement();
            if (!foundOverridingClass) {
              if ( anAncestor == overridingClass )
                foundOverridingClass = true; }
            else {
                if ( Environment.defines( anAncestor, introducingClass, methodName ) )
                  return (CodePtr)anAncestor.invoke( Environment.Class, "resolveTerminal", introducingClass, methodName );
           }
        }
        System.out.println( "***** FAILURE ***** ParentResolve: failed to find implementation of " + methodName );
        throw new OMRuntimeException(); //ParentResolveFailure();
    }

    static public ClassReference solveMetaclassConstraints( List aMetaclassList, List aParentList ) {
        String traceString = "";
        ClassReference solution;
        List aMetaclassConstraintList = new List();
        
        // Find the unique members of aMetaclassList
        for ( int i = 0; i < aMetaclassList.size(); i++ ) {
            if (traceLevel > 50)
              traceString = traceString + ((ClassReference)aMetaclassList.elementAt(i)).name + " ";
            if ( isNotAncestorOfAnyOf( (ClassReference)aMetaclassList.elementAt(i), aMetaclassConstraintList ) )
              aMetaclassConstraintList.addElement( aMetaclassList.elementAt(i) );
        }

       // Append any metaclass of a parent that is not already in the list aClassList
        for ( int i = 0; i < aParentList.size(); i++ ) {
            ClassReference aParentMetaclass = (ClassReference)((ClassReference)aParentList.elementAt(i)).get(myclass);
            if (traceLevel > 50)
              traceString = traceString + aParentMetaclass.name + " ";
            if ( isNotAncestorOfAnyOf( aParentMetaclass, aMetaclassConstraintList ) )
              aMetaclassConstraintList.addElement( aParentMetaclass );
        }

        if ( aMetaclassConstraintList.size() == 1 )
          solution = (ClassReference)aMetaclassConstraintList.elementAt(0);
        else {
            // Construct a derived metaclass from all of the constraints
            ClassReference metaclassForSolution = solveMetaclassConstraints( new List(), aMetaclassConstraintList );
            solution = (ClassReference)metaclassForSolution.invoke( Environment.Class, "makeInstance", new ClassReference() );
            solution.invoke( Environment.Class, "initializeClass", aMetaclassConstraintList, new List() );
            solution.name = "DerivedMetaclass(";
            for ( Enumeration e = aMetaclassConstraintList.elements(); e.hasMoreElements(); ) {
                solution.name = solution.name + " " + ((ClassReference)e.nextElement()).name;
            }
            solution.name = solution.name + " )";
            solution.invoke( Environment.Class, "readyClass" );
        }
        if (traceLevel > 50)
	  System.out.println( "solveMetaclassConstraints of < " + traceString + "> returns " + solution.name );
        return solution;
    }

     static public void apply( ObjectReference target, CodePtr methodImpl, List aParameterList, ResultHolder resultValueAddr ) {
         Object param1;
         Object param2;
         Object param3;
         Object param4;
         switch ( aParameterList.size() ) {
           case 0:
             resultValueAddr.value = methodImpl.execute( target );
             break;
           case 1:
             param1 = aParameterList.elementAt( 0 );
             resultValueAddr.value = methodImpl.execute( target, param1 );
             break;
           case 2:
             param1 = aParameterList.elementAt( 0 );
             param2 = aParameterList.elementAt( 1 );
             resultValueAddr.value = methodImpl.execute( target, param1, param2 );
             break;
           case 3:
             param1 = aParameterList.elementAt( 0 );
             param2 = aParameterList.elementAt( 1 );
             param3 = aParameterList.elementAt( 2 );
             resultValueAddr.value = methodImpl.execute( target, param1, param2, param3 );
             break;
           case 4:
             param1 = aParameterList.elementAt( 0 );
             param2 = aParameterList.elementAt( 1 );
             param3 = aParameterList.elementAt( 2 );
             param4 = aParameterList.elementAt( 3 );
             resultValueAddr.value = methodImpl.execute( target, param1, param2, param3, param4 );
             break;
           default:
         }
     }

   //
    //==============================================================================================
    //==============================================================================================

    static protected ClassIVS getClassData( ClassReference targetClass ) {
        ClassIVS classIVS = ((ClassIVS)targetClass.get(Class));
        if ( classIVS == null ) {
            System.out.println( "***** FAILURE ***** getClassData: targetClass is not a proper class");
            throw new RuntimeException(); // getClassDataFailure();
        }
        return classIVS;
    }


    static protected boolean isNotAncestorOfAnyOf( ClassReference aClass, List aClassList ) {
        for ( int i = 0; i < aClassList.size(); i++ ) {
            if ( hasAncestor( (ClassReference)aClassList.elementAt(i), aClass ) ) {
	      return false;
            }
        }
        return true;
    }

    static protected boolean hasAncestor( ClassReference aClass, ClassReference aPossibleAncestor ) {
        //System.out.println("xxxx");
        boolean b = MRO(aClass).contains( aPossibleAncestor );
        //System.out.println("yyyy");
        return b;
    }

    static protected boolean defines( ClassReference targetClass, ClassReference introducingClass, String methodName ) {
        boolean result = false;
        RDictionary mdefs = getClassData(targetClass).mdefs;
        RDictionary segment = (RDictionary)mdefs.get( introducingClass );
        if ( segment != null )
          if ( segment.get( methodName ) != null )
            result = true;
        return result;
    }


    //==============================================================================================
    // Below are the methods for computing the MRO.
    // The code has two modes of operation depending on the value of the public static variable
    // simpleMRO.  If simpleMRO is true, the code works as described in Chapter 4 and returns null
    // for any order disagreement.  If simpleMRO is false, the code works as describe in the 
    // "Advanced Linearization" chapter and returns null only if there is a serious order disagreement.

    static protected List MRO( ClassReference targetClass ){
        traceCall( "MRO(" + targetClass.name + ")", 100 );

        List resultMRO = new List();
        resultMRO.addElement( targetClass );
        for ( Enumeration e = getClassData(targetClass).parents.elements(); e.hasMoreElements(); ) {
            if ( Environment.simpleMRO ) {
                resultMRO = MROMerge( resultMRO, MRO( (ClassReference)e.nextElement() ) );
            }
            else {
                List rightMRO = MRO( (ClassReference)e.nextElement() );
                if ( noSeriousOrderDisagreements( resultMRO, rightMRO ) ) {
                    resultMRO = MROMerge( resultMRO, rightMRO );
                }
                else {
                    traceReturn( "MRO returns", 100 );
                    return null;
                }
            }
        }

        traceReturn( "MRO returns", 100 );
        return resultMRO;
    }


    static protected List MROMerge( List leftMRO, List rightMRO ) {
        traceCall( "MROMerge(" + leftMRO.toString() + "," + rightMRO.toString() + ")", 100 );

        if ( leftMRO == null || rightMRO == null ) {
            traceReturn( "MROMerge returns", 100 );
            return null;
        }
        int r = 0;
        int i = 0;
        int j = 0;
        for ( i = 0; i < leftMRO.size(); i++ )
            for ( j = r; j < rightMRO.size(); j++ )
                if ( leftMRO.elementAt(i) == rightMRO.elementAt(j) ) { // Found an insertion point
                    if ( Environment.simpleMRO ) {
                        for (; r < j; r++ ) {
                            if ( leftMRO.contains( rightMRO.elementAt(r) ) ) {
                                if ( traceLevel > 0 )
                                    System.out.println( Indentation.spaces + "simpleMRO: found order disagreement" );
                                traceReturn( "MROMerge returns", 100 );
                                return null;
                            }
                        }
                    }
                    // Note that sublist(m,n) does not include the n'th element.
                    traceReturn( "MROMerge returns", 100 );
                    return MROMerge( leftMRO.sublist(0,i).merge( rightMRO.sublist(0,j) ).merge( leftMRO.sublist(i,leftMRO.size()) ), 
                                     rightMRO.sublist(j+1,rightMRO.size())
                                     );                              
                }
        for (; r < rightMRO.size(); r++ )
              leftMRO.addElement( rightMRO.elementAt(r) );

        traceReturn( "MROMerge returns", 100 );
        return leftMRO;
    }

    static protected boolean noSeriousOrderDisagreements( List leftMRO, List rightMRO ) {
        traceCall( "noSeriousOrderDisagreements(" + leftMRO.toString() + "," + rightMRO.toString() + ")", 100 );

        ClassReference x, y;
        int ri, rj;
        for ( int i = 0; i < leftMRO.size()-1; i++ ) {
            x = (ClassReference)leftMRO.elementAt(i);
            if ( (ri = rightMRO.indexOf( x ) ) >= 0 ) {
                for ( int j = i+1; j < leftMRO.size(); j++ ) {
                    y = (ClassReference)leftMRO.elementAt(j);
                    rj = rightMRO.lastIndexOf( y, ri );
                    if ( rj >= 0 ) {
                        // Found an order disagreement
                        if ( isDisagreementSerious( x, y ) ) {
                            traceReturn( "noSeriousOrderDisagreements returns false", 100 );
                            return false;
                        }
                    }
                }
            }
        }

        traceReturn( "noSeriousOrderDisagreements returns true", 100 );
        return true;
    }
 
    static protected boolean isDisagreementSerious( ClassReference x, ClassReference y ) {
        traceCall(  "isDisagreementSerious(" + x.toString() + "," + y.toString() + ")", 100 );

        RDictionary xmdefs = getClassData(x).mdefs;
        RDictionary ymdefs = getClassData(y).mdefs;
        List commonMethods = xmdefs.commonMethods( ymdefs );
        boolean result = false;
        for ( Enumeration e = commonMethods.elements(); e.hasMoreElements(); ) {
            Method m = (Method)e.nextElement();
            if ( isCooperative( m.ic, m.mn ) ) {
                if ( !isHighlyCooperative( m.ic, m.mn ) ) {
                    return true;
                 }
            }
        }
        traceReturn( "isDisagreementSerious returns " + result, 100 );
        return result;
    }

    static protected boolean isCooperative( ClassReference x, String methodName ) {
        if ( x == Object && methodName.equals("dispatch") ) {
            return false;
        }
        else {
            return true;
        }
    }

    static protected boolean isHighlyCooperative( ClassReference x, String methodName ) {
        if ( x == Object && methodName.equals("initialize") ) {
            return true;
        }
        else {
            return false;
        }
    }

    static private void traceCall( String message, int level ) {
        if ( traceLevel > level ) {
            Indentation.increment();
            System.out.println( Indentation.spaces + message );
            }
    }

        static private void traceReturn( String message, int level ) {
            if ( traceLevel > level ) {
                System.out.println( Indentation.spaces + message );
                Indentation.decrement();
            }
    }
}
