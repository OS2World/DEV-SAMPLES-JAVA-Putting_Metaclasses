/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

class ClassIVS  implements Value { // This in not a public class.
    /* 
       Because the structure of a class is known and the MOP encapsulates
       that structure, we introduce a special kind of object (instead
       of an RDictionary) to represent the content of a class.
       Although this lacks generality (that is, it makes a class
       object special), it has the advantage of making the code 
       easier to read. 
       */
    List          parents = new List();
    RDictionary   ivdefs  = new RDictionary();
    RDictionary   mdefs   = new RDictionary();
    RDictionary   ivs     = new RDictionary();
    RDictionary   mtab    = new RDictionary();

    public Object clone() {
	ClassIVS myclone = new ClassIVS();
	myclone.parents  = (List)parents.clone();
	myclone.ivdefs   = (RDictionary)ivdefs.clone();
	myclone.mdefs    = (RDictionary)mdefs.clone();
	myclone.ivs      = (RDictionary)ivs.clone();
	myclone.mtab     = (RDictionary)mtab.clone();
	return myclone;
    }

     public String toString() {
        String result = "\n";
        Indentation.increment();
        result += Indentation.spaces + "{\n";
        //result += Indentation.spaces + "  parents=" + parents + "\n";
        //result += Indentation.spaces + "  ivdefs=" + ivdefs + "\n";
        result += Indentation.spaces + "  mdefs=" + mdefs + "\n";
        //result += Indentation.spaces + "  ivs=" + ivs + "\n";
        result += Indentation.spaces + "  mtab=" + mtab+ "\n" ;
        result += Indentation.spaces + "}";
        Indentation.decrement();
        return result;
    }

   public Object merge( Object r ) {
        ClassIVS result = (ClassIVS)clone();
	result.parents =  (List) parents .merge( ((ClassIVS)r).parents );
	result.ivdefs =   (RDictionary) ivdefs  .merge( ((ClassIVS)r).ivdefs );
	result.mdefs =    (RDictionary) mdefs   .merge( ((ClassIVS)r).mdefs );
	result.ivs =      (RDictionary) ivs     .merge( ((ClassIVS)r).ivs );
	result.mtab =     (RDictionary) mtab    .merge( ((ClassIVS)r).mtab );
        return result;
    }

    public Object recursive_merge( Object r ) {
        ClassIVS result = (ClassIVS)clone();
        result.parents = (List) parents .merge( ((ClassIVS)r).parents );
	result.ivdefs =  (RDictionary) ivdefs  .recursive_merge( ((ClassIVS)r).ivdefs );
	result.mdefs =   (RDictionary) mdefs   .recursive_merge( ((ClassIVS)r).mdefs );
	result.ivs =     (RDictionary) ivs     .recursive_merge( ((ClassIVS)r).ivs );
	result.mtab =    (RDictionary) mtab    .recursive_merge( ((ClassIVS)r).mtab );
        return result;
    }
}
