/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

class RedispatchStub extends CodePtr {
    ObjectReference introducingClass;
    String methodName;

    public RedispatchStub( ObjectReference ic, String mn ) {
        // A redispatch stub is constructed for each method.
        introducingClass = ic;
        methodName = mn;
    }

    public Object execute( ObjectReference target ) {
	List args = new List(); 
        ResultHolder result = new ResultHolder();
	CodePtr implementation = Environment.resolve( Environment.getClass(target), Environment.Object, "dispatch" );
	implementation.execute( target, introducingClass, methodName, args, result );
        return result.value;
      }

    public Object execute( ObjectReference target, Object param1 ){
        List args = new List( param1 );
        ResultHolder result = new ResultHolder();
	CodePtr implementation = Environment.resolve( Environment.getClass(target), Environment.Object, "dispatch" );
	implementation.execute( target, introducingClass, methodName, args, result );
        return result.value;
      }
 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        List args = new List( param1 ); 
        args.addElement( param2 );
        ResultHolder result = new ResultHolder();
	CodePtr implementation = Environment.resolve( Environment.getClass(target), Environment.Object, "dispatch" );
	implementation.execute( target, introducingClass, methodName, args, result );
        return result.value;
      }
 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) {
        List args = new List( param1 ); 
	args.addElement( param2 );
        args.addElement( param3 );
        ResultHolder result = new ResultHolder();
	CodePtr implementation = Environment.resolve( Environment.getClass(target), Environment.Object, "dispatch" );
	implementation.execute( target, introducingClass, methodName, args, result );
        return result.value;
      }
 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
        List args = new List( param1 ); 
        args.addElement( param2 );
        args.addElement( param3 );
        args.addElement( param4 );
        ResultHolder result = new ResultHolder();
	CodePtr implementation = Environment.resolve( Environment.getClass(target), Environment.Object, "dispatch" );
	implementation.execute( target, introducingClass, methodName, args, result );
        return result.value;
      }
 
    public String toString() { return "RedispatchStub(" + ((ClassReference)introducingClass).name + "," + methodName + ")" ;} 
}
