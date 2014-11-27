/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

public class ObjectReference extends RDictionary {
    /* This class allows the proper typing of objects,
       that is, it distinguishes them from dictionaries.
       In addition, it runs the proper methods when an
       object is garbage collected. 
       */
    
    protected void finalize () {
        invoke( Environment.Object, "free" );
    }

    //
    // Below are methods that correspond to the macros generated in the bindings and the C capability
    // of invoking a function with a pointer.  When creating a binding, one subclasses ObjectReference, creates a newClass
    // static method (to create the class object with the MOP), and implements any defined methods as a subclass 
    // of CodePtr.
    //

    //========================= Zero parameters =========================================================================

    public Object invoke( ClassReference introducingClass, String methodName ){
	CodePtr implementation = Environment.resolve( Environment.getClass(this), introducingClass, methodName );
        traceCall( implementation, 0 );
	Object result = implementation.execute( this );
        traceReturn( implementation );
        return result;
    }
    
    public Object parentInvoke( ClassReference callingClass, ClassReference introducingClass, String methodName ){
	CodePtr implementation = Environment.parentResolve( this, callingClass, introducingClass, methodName );
	traceCall( implementation, 0 );
	Object result = implementation.execute( this );
        traceReturn( implementation );
        return result;
    }
    


    //========================= One parameter ===========================================================================

    public Object invoke( ClassReference introducingClass, String methodName, Object param1 ){
        CodePtr implementation = Environment.resolve( Environment.getClass(this), introducingClass, methodName );
	traceCall( implementation, 1 );
	Object result = implementation.execute( this, param1 );
        traceReturn( implementation );
        return result;
    }
    
    public Object parentInvoke( ClassReference callingClass, ClassReference introducingClass, String methodName, Object param1 ){
	CodePtr implementation = Environment.parentResolve( this, callingClass, introducingClass, methodName );
	traceCall( implementation,1  );
	Object result = implementation.execute( this, param1 );
        traceReturn( implementation );
        return result;
    }
    

    //========================= Two parameters ==========================================================================

    public Object invoke( ClassReference introducingClass, String methodName, Object param1, Object param2 ){
        CodePtr implementation = Environment.resolve( Environment.getClass(this), introducingClass, methodName );
	traceCall( implementation, 2 );
	Object result = implementation.execute( this, param1, param2 );
        traceReturn( implementation );
        return result;
    }
    
    public Object parentInvoke( ClassReference callingClass, ClassReference introducingClass, String methodName,
				  Object param1, Object param2 ){
	CodePtr implementation = Environment.parentResolve( this, callingClass, introducingClass, methodName );
	traceCall( implementation, 2 );
	Object result = implementation.execute( this, param1, param2 );
        traceReturn( implementation );
        return result;
    }
    

    //========================= Three parameters ==========================================================================

    public Object invoke( ClassReference introducingClass, String methodName,
			    Object param1, Object param2 , Object param3){
        CodePtr implementation = Environment.resolve( Environment.getClass(this), introducingClass, methodName );
	traceCall( implementation, 3 );
	Object result = implementation.execute( this, param1, param2, param3 );
        traceReturn( implementation );
        return result;
    }
    
    public Object parentInvoke( ClassReference callingClass, ClassReference introducingClass, String methodName, 
				  Object param1, Object param2, Object param3 ){
	CodePtr implementation = Environment.parentResolve( this, callingClass, introducingClass, methodName );
	traceCall( implementation, 3 );
	Object result = implementation.execute( this, param1, param2, param3 );
        traceReturn( implementation );
        return result;
    }
    

    //========================= Four parameters ===========================================================================

    public Object invoke( ClassReference introducingClass, String methodName, 
			    Object param1, Object param2, Object param3, Object param4 ){
        CodePtr implementation = Environment.resolve( Environment.getClass(this), introducingClass, methodName );
	traceCall( implementation, 4 );
	Object result = implementation.execute( this, param1, param2, param3, param4 );
        traceReturn( implementation );
        return result;
    }
    
    public Object parentInvoke( ClassReference callingClass, ClassReference introducingClass, String methodName, 
				  Object param1, Object param2, Object param3, Object param4 ){
	CodePtr implementation = Environment.parentResolve( this, callingClass, introducingClass, methodName );
	traceCall( implementation, 4 );
	Object result = implementation.execute( this, param1, param2, param3, param4 );
        traceReturn( implementation );
        return result;
    }
    

    //========================= Five parameters ===========================================================================

    public Object invoke( ClassReference introducingClass, String methodName, 
			    Object param1, Object param2, Object param3, Object param4, Object param5 ){
        CodePtr implementation = Environment.resolve( Environment.getClass(this), introducingClass, methodName );
	traceCall( implementation, 5);
	Object result = implementation.execute( this, param1, param2, param3, param4, param5 );
        traceReturn( implementation );
        return result;
    }
    
    public Object parentInvoke( ClassReference callingClass, ClassReference introducingClass, String methodName, 
				  Object param1, Object param2, Object param3, Object param4, Object param5 ){
	CodePtr implementation = Environment.parentResolve( this, callingClass, introducingClass, methodName );
	traceCall( implementation, 5 );
	Object result = implementation.execute( this, param1, param2, param3, param4, param5 );
        traceReturn( implementation );
        return result;
    }

    static public void traceCall( CodePtr implementation, long numParams ) {
	if ( Environment.traceLevel > 0 ) {
            System.out.println( Indentation.spaces + "Executing " + implementation + "()" );
            Indentation.increment();
        }
    }
    
    static public void traceReturn( CodePtr implementation ) {
	if ( Environment.traceLevel > 0 ) {
            Indentation.decrement();
            System.out.println( Indentation.spaces + "Returning " + implementation );
        }
    }
}
