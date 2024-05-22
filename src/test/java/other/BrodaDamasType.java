/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package other;
import com.calclogic.lambdacalculo.Term;
import com.calclogic.lambdacalculo.TypedA;
import com.calclogic.parse.CombUtilities;
import com.calclogic.parse.TermUtilities;
/**
 *
 * @author feder
 */
public class BrodaDamasType {
    
    public static void main(String[] args) {
        Term t = CombUtilities.getTerm("\\Phi_{}", null, null);
/*        System.out.println("\\Phi_{}: "+t.type().toString().equals("-> x_{1} x_{1}"));
        t = CombUtilities.getTerm("\\Phi_{b}", null, null);
        System.out.println("\\Phi_{b}: "+t.type().toString().equals("-> (-> x_{2} x_{1}) (-> x_{2} x_{1})"));
        t = CombUtilities.getTerm("\\Phi_{c}", null, null);
        System.out.println("\\Phi_{c}: "+t.type().toString().equals("-> (-> x_{2} (-> x_{2} x_{1})) x_{1}")); 
        t = CombUtilities.getTerm("\\Phi_{bb}", null, null);
        System.out.println("\\Phi_{bb}: "+t.type().toString().equals("-> (-> (-> x_{3} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{2})"));
        t = CombUtilities.getTerm("\\Phi_{bc}", null, null);
        System.out.println("\\Phi_{bc}: "+t.type().toString().equals("-> (-> (-> x_{3} (-> x_{2} x_{1})) x_{1}) (-> x_{3} x_{2})")); 
        t = CombUtilities.getTerm("\\Phi_{cb}", null, null);
        System.out.println("\\Phi_{cb}: "+t.type().toString().equals("-> (-> (-> x_{3} x_{1}) (-> (-> x_{3} x_{2}) x_{1})) x_{2}"));
        t = CombUtilities.getTerm("\\Phi_{cc}", null, null);
        System.out.println("\\Phi_{cc}: "+t.type().toString().equals("-> (-> (-> x_{3} (-> (-> x_{3} x_{2}) x_{1})) x_{1}) x_{2}")); 
        t = CombUtilities.getTerm("\\Phi_{(b,b)}", null, null);
        System.out.println("\\Phi_{(b,b)}: "+t.type().toString().equals("-> (-> (-> x_{3} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{3} x_{2}) x_{1})")); 
        t = CombUtilities.getTerm("\\Phi_{(b,c)}", null, null);
        System.out.println("\\Phi_{(b,c)}: "+t.type().toString().equals("-> (-> (-> x_{3} (-> x_{2} x_{1})) x_{1}) (-> (-> x_{3} x_{2}) (-> x_{2} x_{1}))"));
        t = CombUtilities.getTerm("\\Phi_{(cb,b)}", null, null);
        System.out.println("\\Phi_{(cb,b)}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> x_{2} x_{1})) (-> (-> (-> x_{4} x_{2}) x_{3}) x_{1})) x_{3}")); 
        t = CombUtilities.getTerm("\\Phi_{(c,b)}", null, null);
        System.out.println("\\Phi_{(c,b)}: "+t.type().toString().equals("-> (-> (-> x_{3} (-> (-> x_{3} x_{2}) x_{2})) (-> x_{2} (-> (-> x_{3} x_{2}) x_{2}))) x_{2}"));
        t = CombUtilities.getTerm("\\Phi_{(bc,cc)}", null, null);
        System.out.println("\\Phi_{(bc,cc)}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} (-> (-> x_{3} x_{2}) x_{1})) x_{1}) x_{2}) x_{1}) (-> (-> x_{5} x_{3}) (-> x_{3} x_{2}))"));
        t = CombUtilities.getTerm("\\Phi_{(bcc,cc)}", null, null);
        System.out.println("\\Phi_{(bcc,cc)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} (-> (-> x_{3} x_{2}) x_{1})) x_{1}) x_{2}) x_{1}) x_{2}) (-> (-> x_{6} x_{3}) x_{3})"));
        t = CombUtilities.getTerm("\\Phi_{bbb}", null, null);
        System.out.println("\\Phi_{bbb}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{2})) (-> x_{4} x_{3})"));
        t = CombUtilities.getTerm("\\Phi_{cbbb}", null, null);
        System.out.println("\\Phi_{cbbb}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{2})) (-> (-> x_{5} x_{4}) x_{3})) x_{4}"));
        t = CombUtilities.getTerm("\\Phi_{bcbb}", null, null);
        System.out.println("\\Phi_{bcbb}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{4} x_{3}) x_{2})) x_{3}) (-> x_{5} x_{4})"));
        t = CombUtilities.getTerm("\\Phi_{(b,)}", null, null);
        System.out.println("\\Phi_{(b,)}: "+t.type().toString().equals("-> (-> x_{2} x_{1}) (-> (-> x_{2} x_{1}) x_{1})"));
        t = CombUtilities.getTerm("\\Phi_{(bbb,)}", null, null);
        System.out.println("\\Phi_{(bbb,)}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{2})) (-> (-> x_{4} x_{1}) x_{3})"));
        t = CombUtilities.getTerm("\\Phi_{bcb}", null, null);
        System.out.println("\\Phi_{bcb}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> (-> x_{3} x_{2}) x_{1})) x_{2}) (-> x_{4} x_{3})"));
        t = CombUtilities.getTerm("\\Phi_{ccbb}", null, null);
        System.out.println("\\Phi_{ccbb}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> x_{2} x_{1})) (-> (-> (-> x_{5} x_{4}) x_{3}) x_{2})) x_{3}) x_{4}"));
        t = CombUtilities.getTerm("\\Phi_{cbb}", null, null);
        System.out.println("\\Phi_{cbb}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{4} x_{3}) x_{2})) x_{3}"));
        t = CombUtilities.getTerm("\\Phi_{ccb}", null, null);
        System.out.println("\\Phi_{ccb}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> (-> (-> x_{4} x_{3}) x_{2}) x_{1})) x_{2}) x_{3}"));
        t = CombUtilities.getTerm("\\Phi_{(bb,)}", null, null);
        System.out.println("\\Phi_{(bb,)}: "+t.type().toString().equals("-> (-> (-> x_{3} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{3} x_{1}) x_{2})"));
        t = CombUtilities.getTerm("\\Phi_{(cb,)}", null, null);
        System.out.println("\\Phi_{(cb,)}: "+t.type().toString().equals("-> (-> (-> x_{3} x_{1}) (-> (-> (-> x_{3} x_{1}) x_{2}) x_{1})) x_{2}"));
        t = CombUtilities.getTerm("\\Phi_{(bc,)}", null, null);
        System.out.println("\\Phi_{(bc,)}: "+t.type().toString().equals("-> (-> (-> x_{3} (-> x_{2} x_{1})) x_{1}) (-> (-> x_{3} (-> x_{2} x_{1})) x_{2})"));
        t = CombUtilities.getTerm("\\Phi_{b(bb,)}", null, null);
        System.out.println("\\Phi_{b(bb,)}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{3} x_{1}) x_{2})) (-> x_{4} x_{3})"));
        t = CombUtilities.getTerm("\\Phi_{c(bb,)}", null, null);
        System.out.println("\\Phi_{c(bb,)}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> x_{2} x_{1})) (-> (-> (-> x_{4} x_{3}) x_{1}) x_{2})) x_{3}"));
        t = CombUtilities.getTerm("\\Phi_{c(bbb,)}", null, null);
        System.out.println("\\Phi_{c(bbb,)}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{2})) (-> (-> (-> x_{5} x_{4}) x_{1}) x_{3})) x_{4}"));
        t = CombUtilities.getTerm("\\Phi_{cc(bb,)}", null, null);
        System.out.println("\\Phi_{cc(bb,)}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> x_{2} x_{1})) (-> (-> (-> (-> x_{5} x_{4}) x_{3}) x_{1}) x_{2})) x_{3}) x_{4}"));
        t = CombUtilities.getTerm("\\Phi_{c(ccb,)}", null, null);
        System.out.println("\\Phi_{c(ccb,)}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> (-> (-> (-> (-> x_{5} x_{4}) x_{1}) x_{3}) x_{2}) x_{1})) x_{2}) x_{3}) x_{4}"));
        t = CombUtilities.getTerm("\\Phi_{cc(bbb,)}", null, null);
        System.out.println("\\Phi_{cc(bbb,)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{2})) (-> (-> (-> (-> x_{6} x_{5}) x_{4}) x_{1}) x_{3})) x_{4}) x_{5}"));
        t = CombUtilities.getTerm("\\Phi_{(bcbb,cb)}", null, null);
        System.out.println("\\Phi_{(bcbb,cb)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> x_{7} x_{1}) (-> (-> x_{3} x_{2}) x_{1})) x_{2}) (-> x_{4} x_{1})) (-> (-> x_{6} x_{5}) x_{4})) x_{5}) (-> (-> x_{7} x_{3}) x_{6})"));
        t = CombUtilities.getTerm("\\Phi_{b(b,b)}", null, null);
        System.out.println("\\Phi_{b(b,b)}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{3} x_{2}) x_{1})) (-> x_{4} x_{3})"));
        t = CombUtilities.getTerm("\\Phi_{cbc(cb,)}", null, null);
        System.out.println("\\Phi_{cbc(cb,)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> (-> (-> (-> x_{4} x_{3}) x_{1}) x_{2}) x_{1})) x_{2}) x_{3}) (-> (-> x_{6} x_{5}) x_{4})) x_{5}"));
        t = CombUtilities.getTerm("\\Phi_{(bcb,cbb)}", null, null);
        System.out.println("\\Phi_{(bcb,cbb)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> x_{7} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{4} x_{3}) x_{2})) x_{3}) (-> (-> x_{6} x_{5}) x_{1})) x_{5}) (-> (-> x_{7} x_{4}) x_{6})"));
        t = CombUtilities.getTerm("\\Phi_{(b(b,b),b)}", null, null);
        System.out.println("\\Phi_{(b(b,b),b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> (-> x_{4} x_{3}) x_{1})) (-> (-> x_{5} x_{2}) x_{4})"));
        t = CombUtilities.getTerm("\\Phi_{(c(b,b),b)}", null, null);
        System.out.println("\\Phi_{(c(b,b),b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> (-> (-> (-> x_{5} x_{2}) x_{4}) x_{3}) x_{1})) x_{4}"));
        t = CombUtilities.getTerm("\\Phi_{b((b,b),b)}", null, null);
        System.out.println("\\Phi_{b((b,b),b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> (-> (-> x_{4} x_{2}) x_{3}) x_{1})) (-> x_{5} x_{4})"));
        t = CombUtilities.getTerm("\\Phi_{c((b,b),b)}", null, null);
        System.out.println("\\Phi_{c((b,b),b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> x_{5} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> (-> (-> (-> x_{5} x_{4}) x_{2}) x_{3}) x_{1})) x_{4}"));
        t = CombUtilities.getTerm("\\Phi_{((b,b),b)}", null, null);
        System.out.println("\\Phi_{((b,b),b)}: "+t.type().toString().equals("-> (-> (-> (-> x_{4} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> (-> (-> x_{4} x_{2}) x_{3}) x_{1})"));
        t = CombUtilities.getTerm("\\Phi_{c(c(b,bb),)}", null, null);
        System.out.println("\\Phi_{c(c(b,bb),)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{2})) (-> (-> (-> (-> (-> x_{6} x_{5}) x_{1}) x_{4}) x_{3}) x_{1})) x_{4}) x_{5}"));
        t = CombUtilities.getTerm("\\Phi_{ccbbcb}", null, null);
        System.out.println("\\Phi_{ccbbcb}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> x_{7} x_{1}) (-> (-> x_{3} x_{2}) x_{1})) x_{2}) (-> x_{4} x_{3})) (-> (-> (-> x_{7} x_{6}) x_{5}) x_{4})) x_{5}) x_{6}"));
        t = CombUtilities.getTerm("\\Phi_{c((ccb,b),)}", null, null);
        System.out.println("\\Phi_{c((ccb,b),)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> x_{2} x_{1})) (-> (-> (-> (-> (-> (-> x_{6} x_{5}) x_{1}) x_{2}) x_{4}) x_{3}) x_{1})) x_{3}) x_{4}) x_{5}"));
        t = CombUtilities.getTerm("\\Phi_{ccbbbcb}", null, null);
        System.out.println("\\Phi_{ccbbbcb}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> x_{8} x_{1}) (-> (-> x_{3} x_{2}) x_{1})) x_{2}) (-> x_{4} x_{3})) (-> x_{5} x_{4})) (-> (-> (-> x_{8} x_{7}) x_{6}) x_{5})) x_{6}) x_{7}"));
        t = CombUtilities.getTerm("\\Phi_{cc(cc(cb,),)}", null, null);
        System.out.println("\\Phi_{cc(cc(cb,),)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> x_{7} x_{1}) (-> (-> (-> (-> (-> (-> (-> (-> x_{7} x_{6}) x_{5}) x_{1}) x_{4}) x_{3}) x_{1}) x_{2}) x_{1})) x_{2}) x_{3}) x_{4}) x_{5}) x_{6}"));
        t = CombUtilities.getTerm("\\Phi_{(ccccbbcb,b)}", null, null);
        System.out.println("\\Phi_{(ccccbbcb,b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> (-> (-> x_{10} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{4} x_{3}) x_{1})) x_{3}) (-> x_{5} x_{4})) (-> (-> (-> (-> (-> (-> x_{10} x_{2}) x_{9}) x_{8}) x_{7}) x_{6}) x_{5})) x_{6}) x_{7}) x_{8}) x_{9}"));
        t = CombUtilities.getTerm("\\Phi_{cc(ccc(cb,),)}", null, null);
        System.out.println("\\Phi_{cc(ccc(cb,),)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> x_{8} x_{1}) (-> (-> (-> (-> (-> (-> (-> (-> (-> x_{8} x_{7}) x_{6}) x_{1}) x_{5}) x_{4}) x_{3}) x_{1}) x_{2}) x_{1})) x_{2}) x_{3}) x_{4}) x_{5}) x_{6}) x_{7}"));
        t = CombUtilities.getTerm("\\Phi_{b(bcb,cbb)}", null, null);
        System.out.println("\\Phi_{b(bcb,cbb)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> x_{8} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{4} x_{3}) x_{2})) x_{3}) (-> (-> x_{6} x_{5}) x_{1})) x_{5}) (-> (-> x_{7} x_{4}) x_{6})) (-> x_{8} x_{7})"));
        t = CombUtilities.getTerm("\\Phi_{c(bbb,b)}", null, null);
        System.out.println("\\Phi_{c(bbb,b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> x_{4} x_{3})) (-> (-> (-> x_{6} x_{5}) x_{2}) x_{4})) x_{5}"));
        t = CombUtilities.getTerm("\\Phi_{(c(cbb,),b)}", null, null);
        System.out.println("\\Phi_{(c(cbb,),b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> (-> (-> (-> (-> x_{6} x_{2}) x_{5}) x_{1}) x_{4}) x_{3})) x_{4}) x_{5}"));
        t = CombUtilities.getTerm("\\Phi_{(bb,(bcb,b))}", null, null);
        System.out.println("\\Phi_{(bb,(bcb,b))}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> x_{7} x_{1}) (-> x_{2} x_{1})) (-> (-> x_{4} x_{3}) x_{1})) x_{3}) (-> (-> x_{5} x_{2}) x_{4})) (-> x_{6} x_{1})) (-> (-> x_{7} x_{5}) x_{6})"));
        t = CombUtilities.getTerm("\\Phi_{b(bb,cb)}", null, null);
        System.out.println("\\Phi_{b(bb,cb)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> (-> x_{3} x_{2}) x_{1})) x_{2}) (-> x_{4} x_{1})) (-> (-> x_{5} x_{3}) x_{4})) (-> x_{6} x_{5})"));
        t = CombUtilities.getTerm("\\Phi_{bcc(ccb,b)}", null, null);
        System.out.println("\\Phi_{bcc(ccb,b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> x_{8} x_{1}) (-> x_{2} x_{1})) (-> (-> (-> (-> (-> (-> x_{7} x_{6}) x_{5}) x_{2}) x_{4}) x_{3}) x_{1})) x_{3}) x_{4}) x_{5}) x_{6}) (-> x_{8} x_{7})"));
        t = CombUtilities.getTerm("\\Phi_{bcc(ccb,b)}", null, null);
        System.out.println("\\Phi_{bcc(ccb,b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> x_{8} x_{1}) (-> x_{2} x_{1})) (-> (-> (-> (-> (-> (-> x_{7} x_{6}) x_{5}) x_{2}) x_{4}) x_{3}) x_{1})) x_{3}) x_{4}) x_{5}) x_{6}) (-> x_{8} x_{7})"));
        t = CombUtilities.getTerm("\\Phi_{(b(bb,b),b)}", null, null);
        System.out.println("\\Phi_{(b(bb,b),b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> x_{4} x_{1})) (-> (-> x_{5} x_{3}) x_{4})) (-> (-> x_{6} x_{2}) x_{5})"));
        t = CombUtilities.getTerm("\\Phi_{(bb(ccbb,b),cb)}", null, null);
        System.out.println("\\Phi_{(bb(ccbb,b),cb)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> (-> (-> x_{10} x_{1}) (-> (-> x_{3} x_{2}) x_{1})) x_{2}) (-> x_{4} x_{1})) (-> x_{5} x_{1})) (-> (-> (-> (-> x_{8} x_{4}) x_{7}) x_{6}) x_{5})) x_{6}) x_{7}) (-> x_{9} x_{8})) (-> (-> x_{10} x_{3}) x_{9})"));
        t = CombUtilities.getTerm("\\Phi_{(bb(bb,),b)}", null, null);
        System.out.println("\\Phi_{(bb(bb,),b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> (-> x_{4} x_{1}) x_{3})) (-> x_{5} x_{4})) (-> (-> x_{6} x_{2}) x_{5})"));
        t = CombUtilities.getTerm("\\Phi_{(bb,(bb,b))}", null, null);
        System.out.println("\\Phi_{(bb,(bb,b))}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> x_{6} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> (-> x_{4} x_{2}) x_{3})) (-> x_{5} x_{1})) (-> (-> x_{6} x_{4}) x_{5})"));
        t = CombUtilities.getTerm("\\Phi_{cccc((ccbbbb,bbb),b)}", null, null);
        System.out.println("\\Phi_{cccc((ccbbbb,bbb),b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> x_{15} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> x_{4} x_{3})) (-> x_{5} x_{4})) (-> x_{6} x_{1})) (-> x_{7} x_{6})) (-> x_{8} x_{7})) (-> (-> (-> (-> (-> (-> (-> (-> (-> x_{15} x_{14}) x_{13}) x_{12}) x_{11}) x_{2}) x_{5}) x_{10}) x_{9}) x_{8})) x_{9}) x_{10}) x_{11}) x_{12}) x_{13}) x_{14}"));
        t = CombUtilities.getTerm("\\Phi_{cc(cc(bbbb,b),b)}", null, null);
        System.out.println("\\Phi_{cc(cc(bbbb,b),b)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> x_{11} x_{1}) (-> x_{2} x_{1})) (-> x_{3} x_{1})) (-> x_{4} x_{1})) (-> x_{5} x_{4})) (-> x_{6} x_{5})) (-> (-> (-> (-> (-> (-> (-> x_{11} x_{10}) x_{9}) x_{2}) x_{8}) x_{7}) x_{3}) x_{6})) x_{7}) x_{8}) x_{9}) x_{10}"));
        t = CombUtilities.getTerm("\\Phi_{cccc(cccccb,)}", null, null);
        System.out.println("\\Phi_{cccc(cccccb,)}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> x_{11} x_{1}) (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> x_{11} x_{10}) x_{9}) x_{8}) x_{7}) x_{1}) x_{6}) x_{5}) x_{4}) x_{3}) x_{2}) x_{1})) x_{2}) x_{3}) x_{4}) x_{5}) x_{6}) x_{7}) x_{8}) x_{9}) x_{10}"));
        t = CombUtilities.getTerm("\\Phi_{bc(cccccb(cb,b),(cb,b))}", null, null);
        System.out.println("\\Phi_{bc(cccccb(cb,b),(cb,b))}: "+t.type().toString().equals("-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> (-> x_{15} x_{1}) (-> x_{2} x_{1})) (-> (-> (-> x_{4} x_{2}) x_{3}) x_{1})) x_{3}) (-> x_{5} x_{1})) (-> (-> (-> x_{7} x_{5}) x_{6}) x_{1})) x_{6}) (-> (-> (-> (-> (-> (-> (-> (-> x_{14} x_{13}) x_{4}) x_{12}) x_{11}) x_{10}) x_{9}) x_{8}) x_{7})) x_{8}) x_{9}) x_{10}) x_{11}) x_{12}) x_{13}) (-> x_{15} x_{14})"));*/
        //t = CombUtilities.getTerm("\\Phi_{(,b)}", null, null);
        //System.out.println("\\Phi_{(,b)}: "+t.type().toString());
        /*t = CombUtilities.getTerm("\\Phi_{bbb} \\Phi_{b}", null, null);
        System.out.println(t.type());*/
        t = CombUtilities.getTerm("\\Phi_{cbb} (\\Phi_{K} c_{9}) c_{12} \\Phi_{b}", null, null);
    }
}
