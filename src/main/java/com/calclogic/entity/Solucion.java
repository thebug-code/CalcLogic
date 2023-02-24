package com.calclogic.entity;
// Generated Mar 20, 2017 12:50:11 PM by Hibernate Tools 3.2.1.GA

import com.calclogic.lambdacalculo.App;
import com.calclogic.lambdacalculo.Const;
import com.calclogic.lambdacalculo.Term;
import com.calclogic.lambdacalculo.TypedApp;
import com.calclogic.lambdacalculo.TypeVerificationException;
import com.calclogic.lambdacalculo.TypedM;
import com.calclogic.proof.CrudOperations;
import com.calclogic.proof.ProofBoolean;
import com.calclogic.parse.CombUtilities;
import com.calclogic.proof.GenericProofMethod;
import com.calclogic.service.SimboloManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * Solucion generated by hbm2java
 *
 * This table has the solutions (or attempted solutions)
 * to the demonstrations of theorems.
 */
public class Solucion implements java.io.Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "solucion_id_seq")
    @SequenceGenerator(name = "solucion_id_seq", sequenceName = "solucion_id_seq")
    private int id;

    private Term typedTerm;
    private Resuelve resuelve;
    private boolean resuelto;
    private String demostracion;
    private String metodo;
    private CrudOperations proofCrudOperations;


    public Solucion() {
    }

    public void setResuelto(boolean resuelto) {
        this.resuelto = resuelto;
    }

    public boolean isResuelto() {
        return resuelto;
    }
    
    public void setTypedTerm(Term typedTerm)
    {
        this.typedTerm = typedTerm;
        this.demostracion = (typedTerm!=null?typedTerm.toString():"");
    }
    
    public void setTypedTermNotStrProof(Term typedTerm)
    {
        this.typedTerm = typedTerm;
    }
    
    public Term getTypedTerm()
    {
        return typedTerm;
    }
    
    public String getDemostracion() {
    	return demostracion;
    }
    
    public String getMetodo() {
	return metodo;
    }

    public void setMetodo(String metodo) {
	this.metodo = metodo;
    }
    
    public Solucion(Term typeTerm) {
        this.typedTerm = typeTerm;
    }
    
    public Solucion(Resuelve resuelve, boolean resuelto, Term typeTerm, String metodo,
                    CrudOperations proofCrudOperations) {
        this.resuelve = resuelve;
        this.resuelto = resuelto;
        this.typedTerm = typeTerm;
        this.demostracion = (typeTerm != null?typeTerm.toString():"");
        this.metodo = metodo;
        this.proofCrudOperations = proofCrudOperations;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResuelve(Resuelve resuelve) {
        this.resuelve = resuelve;
    }
    
    public void setDemostracion(String demostracion) {
    	this.demostracion = demostracion;
    }
    
    public void setProofCrudOperations(CrudOperations proofCrudOperations) {
        this.proofCrudOperations = proofCrudOperations;
    }

    public int getId() {
        return id;
    }

    public Resuelve getResuelve() {
        return resuelve;
    }
    
    public boolean getResuelto() {
        return resuelto;
    }
    
    private Term branchedOneLineSubProof(Term formula,Term father, String user) {
        Map<String,String> values1 = new HashMap<>();
        values1.put("ST1",new App(new App(new Const(1,"c_{1}"),formula),formula).toString());
        values1.put("ST2", formula.toString());
        StrSubstitutor sub1 = new StrSubstitutor(values1, "%(",")");
        //String metaTheoT= "S (I^{[x_{113} := %(ST1)]} A^{c_{1} x_{113} (c_{1} x_{113} c_{8})}) (L^{\\lambda x_{122}.%(ST2)} A^{c_{1} x_{113} x_{113}})";
        String metaTheoT= "I^{[x_{113}:= %(ST2)]} A^{= (\\Phi_{(b,)} c_{1}) (\\Phi_{K} c_{8})} (L^{\\lambda x_{122}. c_{1} x_{122} (%(ST2))} (L^{\\lambda x_{122}.%(ST2)} A^{= (\\Phi_{(b,)} c_{1}) (\\Phi_{K} c_{8})}))";
        String metaTheo = sub1.replace(metaTheoT);

        try {
            Term newProof = new TypedApp(father.dsc("121"), CombUtilities.getTerm(metaTheo,user));
            newProof = new TypedApp(new TypedApp(father.dsc("11"), newProof),father.dsc("2"));
            return newProof;
        }
        catch (TypeVerificationException e) {
            Logger.getLogger(Solucion.class.getName()).log(Level.SEVERE, null, e);
            return father;
        }
    }
    
    private Term mergeSubProofs(Term subProof, List<Term> fathers, String user) {
        if (fathers.size()==1)
            return subProof;
        else {
            Term auxProof;
            int i;
            if (subProof==null || subProof.type()==null) {
                i=2;
                auxProof =(subProof==null ? fathers.get(1).dsc("2", true) : branchedOneLineSubProof(subProof,fathers.get(1), user));
            }
            else {
                i=1;
                auxProof = subProof;
            }
            while (i < fathers.size()) {
                GenericProofMethod ai = proofCrudOperations.returnProofMethodObject("AI");
                auxProof = ai.finishedMethodProof(fathers.get(i), auxProof, user, null, null);
                i++;
            }
            return auxProof;
        }
    }
    
    public boolean thereIsAxiom(String axiom) {
        List<String> l = new ArrayList<>();
        typedTerm.getAxioms(l);
        
        return false;
    }
    
    public void deleteFinishStack(Term methodTerm, Term statement) {
        Term[] T = proofCrudOperations.getCurrentMethodStack(typedTerm, methodTerm, statement);
        if ( T != null && T[0].type()!=null && T[0].type().equals(T[2]) ) {
           Term aux = T[1];
           GenericProofMethod objectMethod;
           while (aux instanceof App) {
              objectMethod = proofCrudOperations.returnProofMethodObject(((App)aux).p.toString());
              typedTerm = objectMethod.deleteFinishProof(typedTerm);
              aux = ((App)aux).q;
           }
           objectMethod = proofCrudOperations.returnProofMethodObject(aux.toString());
           typedTerm = objectMethod.deleteFinishProof(typedTerm);
        }
    }

    public int retrocederPaso(String user, Term methodTerm, GenericProofMethod objectMethod, 
                              SimboloManager s){
        String currentGroupMethod = objectMethod.getGroupMethod();
        List<Term> li = new ArrayList<>();
        li = proofCrudOperations.getFatherAndSubProof(typedTerm,methodTerm,li);
        Term auxTypedTerm = li.get(0);
        /*if( auxTypedTerm instanceof TypedM && ((TypedM)auxTypedTerm).getNumber() == 1)
                auxTypedTerm = ((TypedM)auxTypedTerm).getSubProof();
        if( objectMethod.getMethodStr().equals("SS") && auxTypedTerm instanceof TypedApp &&
            ((TypedApp)auxTypedTerm).inferType == 's' 
          )
            auxTypedTerm = ((App)auxTypedTerm).q;*/
        if (ProofBoolean.isOneLineProof(auxTypedTerm)){
            typedTerm= mergeSubProofs(null, li, user);
            demostracion =(typedTerm==null ? "" : typedTerm.toString());
            return 0;
        }
        if ( (
              !currentGroupMethod.equals("T") &&
              auxTypedTerm instanceof App && auxTypedTerm.dsc("1").containTypedA()
             ) 
             ||
              // next line checks if it is a no one step proof 
             (
                currentGroupMethod.equals("T")
                &&
                (
                    (auxTypedTerm instanceof TypedApp && ((TypedApp)auxTypedTerm).inferType=='t') ||
                    (auxTypedTerm instanceof TypedApp && ((TypedApp)auxTypedTerm).inferType=='m' &&
                        auxTypedTerm.dsc("1", true) instanceof TypedApp && 
                        ((TypedApp)auxTypedTerm.dsc("1", true)).inferType=='m' && 
                        auxTypedTerm.dsc("11", true) instanceof TypedApp &&
                        ((TypedApp)auxTypedTerm.dsc("11", true)).inferType=='i'
                    )
                )
             )
           )
        {
            if (currentGroupMethod.equals("T")&& 
                !(auxTypedTerm instanceof TypedApp && ((TypedApp)auxTypedTerm).inferType=='t')    
               )
            {
                typedTerm = mergeSubProofs(auxTypedTerm.dsc("12"), li, user);
                if (auxTypedTerm instanceof TypedApp && ((TypedApp)auxTypedTerm).inferType=='e' &&
                        auxTypedTerm.dsc("1", true) instanceof TypedApp && 
                        ((TypedApp)auxTypedTerm.dsc("1", true)).inferType=='s'     
                   )
                {
                    typedTerm = mergeSubProofs(auxTypedTerm.dsc("2", true), li, user);
                }
            }
            else {
                typedTerm = mergeSubProofs(auxTypedTerm.dsc("1"), li, user);
                if (auxTypedTerm instanceof TypedApp && ((TypedApp)auxTypedTerm).inferType=='e' &&
                        auxTypedTerm.dsc("1", true) instanceof TypedApp && 
                        ((TypedApp)auxTypedTerm.dsc("1", true)).inferType=='s'     
                   )
                {
                    typedTerm = mergeSubProofs(auxTypedTerm.dsc("2", true), li, user);
                }
            }
            demostracion = typedTerm.toString();
            return 2;
        }
        else {
            typedTerm = mergeSubProofs(auxTypedTerm.type().setToPrint(s).dsc("2"), li, user);
            demostracion = typedTerm.toString();
            return 1;
        }
    }
}
