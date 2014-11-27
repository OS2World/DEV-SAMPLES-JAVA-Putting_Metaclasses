/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

public class CodePtr implements Value {
    public Object execute( ObjectReference target ) 
    { 	
        if ( Environment.traceLevel > 0 ) {
            System.out.println( "*****Failure***** no implementation for 0 parameter method" );
        }
        throw new OMImplementationNotFound(); 
    }
    public Object execute( ObjectReference target, Object param1 ) 
    {  	
        if ( Environment.traceLevel > 0 ) {
            System.out.println( "*****Failure***** no implementation for 1 parameter method" );
        }
        throw new OMImplementationNotFound(); 
    }
    public Object execute( ObjectReference target, Object param1, Object param2 ) 
    {  	
        if ( Environment.traceLevel > 0 ) {
            System.out.println( "*****Failure***** no implementation for 2 parameter method" );
        }
        throw new OMImplementationNotFound(); 
    }
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) 
    {  	
        if ( Environment.traceLevel > 0 ) {
            System.out.println( "*****Failure***** no implementation for 3 parameter method" );
        }
        throw new OMImplementationNotFound(); 
    }
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) 
    {  	
        if ( Environment.traceLevel > 0 ) {
            System.out.println( "*****Failure***** no implementation for 4 parameter method" );
        }
        throw new OMImplementationNotFound(); 
    }
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4, Object param5 ) 
    {  	
        if ( Environment.traceLevel > 0 ) {
            System.out.println( "*****Failure***** no implementation for 5 parameter method" );
        }
        throw new OMImplementationNotFound(); 
    }


    public Object clone()                   { return this; } // CodePtrs are never cloned.
    public String toString()                { return "CodePtr_nullMethod"; }
    public Object merge( Object r )           { throw new OMShouldNotOccur(); }
    public Object recursive_merge( Object r ) { throw new OMShouldNotOccur(); }
}
