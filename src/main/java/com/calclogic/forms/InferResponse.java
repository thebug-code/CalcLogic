/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.calclogic.forms;

import com.calclogic.proof.ProofBoolean;
import com.calclogic.lambdacalculo.App;
import com.calclogic.lambdacalculo.Const;
import com.calclogic.lambdacalculo.Term;
import com.calclogic.service.DisponeManager;
import com.calclogic.service.ResuelveManager;
import com.calclogic.service.SimboloManager;
import com.calclogic.externalservices.MicroServices;
import com.calclogic.proof.CrudOperations;
import com.calclogic.proof.GenericProofMethod;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francisco
 */
public class InferResponse {
    
    // Has all the proof made at this point, written in LaTeX.
    private String historial; 

    // Indicates if the proof there was an error in the "generarHistorial" function,
    private Boolean errorParser1 = false;

    private String errorParser2;
    private String errorParser3;

    // Indicates if the users are at a step in which they have to select the proof method.
    // When it is "0", the option to select method does not appear.
    // When it is "1", the option to select method appears, but not the button "Go back".
    // When it is "2", the option to select method appears and also the "Go back" button.
    private String cambiarMetodo; 

    // Each demonstration that is attempted will have a different value in the database,
    // despite that they are made by different users. "nSol" is that unique value.
    private String nSol; 

    // When the proof method is starting from one side, this indicates the side: 
    // 'd' for the right one (derecho) and 'i' for the left one (izquierdo).
    private String lado;

    // Determines if the proof was solved so a congratulatory message should be displayed
    private String resuelto;

    // This is not included in the JSON response. Its purpose is local for the "generarHistorial" method.
    private boolean valid;

    // Represents if the solution is ending a case.
    private boolean endCase = false;

    private CrudOperations proofCrudOperations;

    public InferResponse(CrudOperations proofCrudOperations) {
        this.proofCrudOperations = proofCrudOperations;
    }

    public void setEndCase(boolean endCase) {
        this.endCase = endCase;
    };

    public boolean getEndCase() {
        return endCase;
    }

    public String getResuelto() {
        return resuelto;
    }

    public void setResuelto(String resuelto) {
        this.resuelto = resuelto;
    }

    public String getLado() {
        return lado;
    }

    public void setLado(String lado) {
        this.lado = lado;
    }

    public String getnSol() {
        return nSol;
    }

    public void setnSol(String nSol) {
        this.nSol = nSol;
    }

    public String getCambiarMetodo() {
        return cambiarMetodo;
    }

    public void setCambiarMetodo(String cambiarMetodo) {
        this.cambiarMetodo = cambiarMetodo;
    }

    public String getHistorial() {
        return historial;
    }
    
    public boolean getValid() {
        return valid;
    }

    public void setHistorial(String formula) {
        this.historial = formula;
    }

    public Boolean getErrorParser1() {
        return errorParser1;
    }

    public void setErrorParser1(Boolean errorParser1) {
        this.errorParser1 = errorParser1;
    }

    public String getErrorParser2() {
        return errorParser2;
    }

    public void setErrorParser2(String errorParser2) {
        this.errorParser2 = errorParser2;
    }

    public String getErrorParser3() {
        return errorParser3;
    }

    public void setErrorParser3(String errorParser3) {
        this.errorParser3 = errorParser3;
    }

    /**
     * This function adds a proof of just one step into a bigger proof, 
     * when the demonstration method is counter reciprocal
     * @param user 
     * @param typedTerm
     * @param resuelveManager
     * @param disponeManager
     * @param s
     * @param header
     * @param clickeable
     * @param methodTerm
     * @param valida
     * @param labeled
     * @param formula
     * @param nTeo
     * @return Nothing.
     */
    private void setCounterRecipProof(String user, Term typedTerm, ResuelveManager resuelveManager, DisponeManager disponeManager, 
        SimboloManager s, String header, String clickeable, Term methodTerm, Boolean valida, Boolean labeled, Term formula, String nTeo, 
        GenericProofMethod objectMethod) {
        
        Term newFormula = objectMethod.initFormula(formula);
        String statement;
        try {
           statement = "<center>$" + clickeableST(newFormula, clickeable, methodTerm, false, s) 
                              + "$</center>";
        }
        catch (Exception e) {
            this.setErrorParser1(true);
            return;
        }
        header+="By counter-reciprocal method, the following must be proved:<br>"+statement+"Sub Proof:<br>";
        if (methodTerm instanceof App) {
            if ( typedTerm!=null && typedTerm.type()!=null && typedTerm.type().equals(formula) //&& 
                 // InferController.isBaseMethod(((App)methodTerm).q)
               )
                typedTerm = ((App)typedTerm).q;
            privateGenerarHistorial(user, newFormula, header, nTeo, typedTerm, valida, labeled, ((App)methodTerm).q, 
                             resuelveManager, disponeManager, s, clickeable, false);
        }
        else
            this.setHistorial(header);
    }

    /**
     * This function adds a proof of just one step into a bigger proof, 
     * when the demonstration method is contradiction
     * @param user 
     * @param typedTerm
     * @param resuelveManager
     * @param disponeManager
     * @param s
     * @param header
     * @param clickeable
     * @param methodTerm
     * @param valida
     * @param labeled
     * @param formula
     * @param nTeo
     * @return Nothing.
     */
    private void setContradictionProof(String user, Term typedTerm, ResuelveManager resuelveManager, DisponeManager disponeManager, 
        SimboloManager s, String header, String clickeable, Term methodTerm, Boolean valida, Boolean labeled, Term formula, String nTeo,
        GenericProofMethod objectMethod) {

        Term newFormula = objectMethod.initFormula(formula);
        String statement;
        try {
            statement = "<center>$" + clickeableST(newFormula, clickeable, methodTerm, false, s) 
                              + "$</center>";
        }
        catch (Exception e) {
            Logger.getLogger(InferResponse.class.getName()).log(Level.SEVERE, null, e);
            this.setErrorParser1(true);
            return;
        }
        
        header+="By contradiction method, the following must be proved:<br>"+statement+"Sub Proof:<br>";
        if (methodTerm instanceof App) {
            if ( typedTerm!=null && typedTerm.type()!=null && typedTerm.type().equals(formula) //&& 
                 // InferController.isBaseMethod(((App)methodTerm).q)
               ){
                typedTerm = ((App)typedTerm).q;
            }
            privateGenerarHistorial(user, newFormula, header, nTeo, typedTerm, valida, labeled, ((App)methodTerm).q, 
                             resuelveManager, disponeManager, s, clickeable, false);
        }
        else{
            this.setHistorial(header);
        }
    }

    /**
     * This function adds a proof of just one step into a bigger proof, 
     * when the demonstration method is "And Introduction" (Conjunction by parts) or "Case Analysis"
     * @param user 
     * @param typedTerm
     * @param resuelveManager
     * @param disponeManager
     * @param s
     * @param header
     * @param clickeable
     * @param methodTerm
     * @param valida
     * @param labeled
     * @param formula
     * @param nTeo
     * @return Nothing.
     */
    private void set_AIorCA_Proof(String user, Term typedTerm, ResuelveManager resuelveManager, DisponeManager disponeManager, 
        SimboloManager s, String header, String clickeable, Term methodTerm, Boolean valida, Boolean labeled, Term formula, String nTeo) {

        String statement;
        boolean isAI = methodTerm.toStringFinal().substring(0, 2).equals("AI");
        String stOrCase = (isAI?"Statement":"Case");
        String methodName = (isAI?"Conjunction by parts":"Case Analysis");
        Term newFormula = ((App)formula).q;
        try {
           statement = "<center>$" + clickeableST(newFormula, clickeable, methodTerm, false, s) 
                                   + "$</center>";
        }
        catch (Exception e) {
            this.setErrorParser1(true);
            return;
        }
        header+="By "+methodName+" method:<br>"+stOrCase+" 1:<br>"+statement+"Sub Proof:<br>";
        if (methodTerm instanceof Const){
           historial = header;
        }
        else if ( ((App)methodTerm).p instanceof Const  ) {
            privateGenerarHistorial(user, newFormula, header, nTeo, typedTerm, valida, labeled, ((App)methodTerm).q, 
                                    resuelveManager, disponeManager, s, clickeable, false);
            if (!(typedTerm!=null && typedTerm.type()!=null && typedTerm.type().equals(newFormula))){
                cambiarMetodo = "0";
            }
            else {
                if (!clickeable.equals("n"))
                    cambiarMetodo = "0"; 
                newFormula = ((App)((App)formula).p).q;
                try {
                    statement = "<center>$" + clickeableST(newFormula, clickeable, new Const("AI"), false, s) 
                                   + "$</center>";
                }catch (Exception e) {
                    this.setErrorParser1(true);
                    return;
                }
                historial += stOrCase+" 2:<br>"+statement+"Sub Proof:<br>";
            }
        }
        else{
            privateGenerarHistorial(user, newFormula, header, nTeo,
                            (ProofBoolean.isAIProof2Started(methodTerm)?((App)typedTerm).q:typedTerm), 
                             valida, labeled, ((App)((App)methodTerm).p).q, resuelveManager, disponeManager, 
                             s, clickeable, false);
            newFormula = ((App)((App)formula).p).q;
            try {
                statement = "<center>$" + clickeableST(newFormula, clickeable, methodTerm, false, s) 
                                   + "$</center>";
            }catch (Exception e) {
                this.setErrorParser1(true);
                return;
            }
            header = historial + stOrCase+" 2:<br>"+statement+"Sub Proof:<br>";
            historial = "";
            Term newTypedTerm;
            newTypedTerm = proofCrudOperations.getSubProof(typedTerm, methodTerm);
            privateGenerarHistorial(user, newFormula, header, nTeo, newTypedTerm, valida, labeled, ((App)methodTerm).q, 
                             resuelveManager, disponeManager, s, clickeable, false);
        }
    }

    /**
     * Creates a LaTeX string that can be clicked by the user.
     * @param newTerm
     * @param clickeable
     * @param method
     * @param isRootTeorem
     * @return The string that the user can click.
     */
    private String clickeableST(Term newTerm, String clickeable, Term method, boolean isRootTeorem, 
                                SimboloManager s) throws Exception {

        if ( (method != null && !(method instanceof Const))||(isRootTeorem && method instanceof Const) ) // en plena recursion
            return newTerm.toStringInf(s,"");
        else if (clickeable.equals("DM"))  // final de la impresion
            return "\\cssId{teoremaMD}{\\style{cursor:pointer; color:#08c;}{"+ newTerm.toStringInf(s,"") + "}}";
        else if (clickeable.equals("SS")) { // final de la impresion
            String formulaDer = ((App)((App)newTerm).p).q.toStringInf(s,"");
            String formulaIzq = ((App)newTerm).q.toStringInf(s,"");
            Term operatorTerm = ((App)((App)newTerm).p).p;//resuelve.getTeorema().getOperador();
            String operator = operatorTerm.toStringInf(s,"");
            if(!operatorTerm.toString().startsWith("c_{1}") && !operatorTerm.toString().startsWith("c_{20}"))
               throw new Exception();
            
            formulaDer = "\\cssId{d}{\\class{teoremaClick}{\\style{cursor:pointer; color:#08c;}{"+ formulaDer + "}}}";
            formulaIzq = "\\cssId{i}{\\class{teoremaClick}{\\style{cursor:pointer; color:#08c;}{"+ formulaIzq + "}}}";
            return formulaIzq+"$ $"+ operator +"$ $" + formulaDer;
        }
        else // clickeable.equals("n")
            return newTerm.toStringInf(s,"");
    }
    
    /**
     * Calls function generarHistorial assuming that it is always a root theorem
     * and thus is doesn't contain identation.
     * @param user
     * @param formula
     * @param nTeo
     * @param typedTerm
     * @param valida
     * @param labeled
     * @param methodTerm
     * @param resuelveManager
     * @param disponeManager
     * @param s
     */
    public void generarHistorial(
        String user, 
        Term formula, 
        String nTeo, 
        Term typedTerm,  
        Boolean valida, 
        Boolean labeled, 
        Term methodTerm, 
        ResuelveManager resuelveManager, 
        DisponeManager disponeManager, 
        SimboloManager s
    ) {
        privateGenerarHistorial(
            user, 
            formula, 
            "",
            nTeo, 
            typedTerm, 
            valida, 
            labeled, 
            methodTerm, 
            resuelveManager, 
            disponeManager, 
            s, 
            "n",
            true
        );
        historial = MicroServices.transformLaTexToHTML(historial);
    }

    /**
     * Prints all the demonstration made at the moment. Note that if a new step will be added,
     * all the proof from the beginning will be printed again.
     * @param user
     * @param formula
     * @param header
     * @param nTeo
     * @param typedTerm
     * @param valida
     * @param labeled
     * @param methodTerm
     * @param resuelveManager
     * @param disponeManager
     * @param s
     * @param clickeable
     * @param isRootTeorem
     */
    public void generarHistorial(
        String user, 
        Term formula,
        String header,
        String nTeo, 
        Term typedTerm,  
        Boolean valida, 
        Boolean labeled, 
        Term methodTerm, 
        ResuelveManager resuelveManager, 
        DisponeManager disponeManager, 
        SimboloManager s,
        String clickeable,
        Boolean isRootTeorem
    ) { 
        privateGenerarHistorial(
            user, 
            formula, 
            header,
            nTeo, 
            typedTerm, 
            valida, 
            labeled, 
            methodTerm, 
            resuelveManager, 
            disponeManager, 
            s, 
            clickeable,
            isRootTeorem
        );
        historial = MicroServices.transformLaTexToHTML(historial);
    }

    /**
     * Prints all the demonstration made at the moment. Note that if a new step will be added,
     * all the proof from the beginning will be printed again.
     * @param user
     * @param formula
     * @param header
     * @param nTeo
     * @param typedTerm
     * @param valida
     * @param labeled
     * @param methodTerm
     * @param resuelveManager
     * @param disponeManager
     * @param s
     * @param clickeable
     * @param isRootTeorem
     */
    private void privateGenerarHistorial(
        String user, 
        Term formula,
        String header,
        String nTeo, 
        Term typedTerm,  
        Boolean valida, 
        Boolean labeled, 
        Term methodTerm, 
        ResuelveManager resuelveManager, 
        DisponeManager disponeManager, 
        SimboloManager s,
        String clickeable,
        Boolean isRootTeorem
    ) {        
        // siempre que el metodo sea vacio o se este esperando un metodo, hay 
        // que pedirlo, salvo cuando no se haya terminado la primera prueba de
        // un metodo binario
        if (isRootTeorem && methodTerm == null)
            cambiarMetodo = "1";
        else if (isRootTeorem && ProofBoolean.isWaitingMethod(methodTerm)) 
            cambiarMetodo = "2";
        else if(isRootTeorem)
            cambiarMetodo ="0";
        
        // If we're printing a root teorem, print it as a theorem.
        if (isRootTeorem) {
            this.setHistorial("");
            try {
                header = "Theorem " + nTeo + ":<br> <center>$" + 
                         clickeableST(formula, clickeable, methodTerm, isRootTeorem, s) + "$</center>" +
                         "Proof:<br>";
            }
            catch (Exception e) {
                this.setErrorParser1(true);
                return ;
            }
        }

        String strMethod = "   "; // We need substring(0,2) can always be applied
        if(methodTerm != null) {
            valid = valida;
            strMethod = methodTerm.toStringFinal();
        }

        strMethod = strMethod.substring(0, 2);
        GenericProofMethod objectMethod = proofCrudOperations.createProofMethodObject(strMethod);

        boolean recursive = objectMethod.getIsRecursiveMethod();
        header += objectMethod.header(nTeo);

        // PROVISIONAL <--- ESTO DEBE SER BORRADO
        Boolean naturalSide, naturalDirect;
        naturalSide = naturalDirect = false;
        // ---- FIN DE LO QUE DEBE SER BORRADO
        
        Term type = typedTerm==null?null:typedTerm.type();

        boolean solved;
        if (typedTerm==null && methodTerm == null && !recursive){
            this.setHistorial(this.getHistorial()+header);
            return;
        }
        if (typedTerm!=null && type == null && !valida && !recursive){
            this.setHistorial(this.getHistorial()+header+"<center>$"+typedTerm.toStringInfLabeled(s)+MicroServices.transformLaTexToHTML("$\\text{No valid inference}$"));
            return;
        }
        if (typedTerm!=null && type == null && valida && !recursive) { // Case where what we want to print is the first line
            String firstLine;
            if(naturalSide){
                firstLine = ((App)((App)typedTerm).p).q.toStringInfLabeled(s);  
                this.setHistorial(this.getHistorial()+header+"<br>Assuming H1: $"+ ((App)typedTerm).q.toStringInf(s, "") +"$<center>$"+firstLine+"</center>");
            }else {
                firstLine = typedTerm.toStringInfLabeled(s);
                this.setHistorial(this.getHistorial()+header+"<center>$"+firstLine+"</center>");
            }
            return;
        }

        if (labeled && !recursive)
            solved = type.equals(formula);
        else
            solved = true; // importante: Se debe implementar setDirectProof y setWSProof sensible a
                           // si se pide labeled o no la ultima linea- Aqui se cablea con solved = true
                           // OJO: Recordar que ahora esos métodos se llaman "setProofMethod" y están
                           // en la respectiva clase del método 

        // -- Here is where we really generate the proof record accoding to the demonstration method ---

        // Case when we are using a base method from the start
        if (!recursive){
            this.setHistorial(objectMethod.setBaseMethodProof(
                this.getHistorial(), user, typedTerm, solved, resuelveManager, disponeManager, s
            ));
        }

        // Case when we are using a recursive method
        else{
            switch (strMethod){
                case "CR":
                    setCounterRecipProof(user, typedTerm, resuelveManager, disponeManager, s, header, 
                        clickeable, methodTerm, valida, labeled, formula, nTeo, objectMethod);
                    break;
                case "CO":
                    setContradictionProof(user, typedTerm, resuelveManager, disponeManager, s, header, 
                        clickeable, methodTerm, valida, labeled, formula, nTeo, objectMethod);
                    break;
                case "AI":
                    set_AIorCA_Proof(user, typedTerm, resuelveManager, disponeManager, s, header, 
                        clickeable, methodTerm, valida, labeled, formula, nTeo);
                case "CA":
                    // ESTE newFormula ES SÓLO PARA MIENTRAS SE HACEN LAS PRUEBAS CON EL MÉTODO CA
                    Term newFormula = objectMethod.initFormula(formula);
                    set_AIorCA_Proof(user, typedTerm, resuelveManager, disponeManager, s, header, 
                        clickeable, methodTerm, valida, labeled, newFormula, nTeo);
                    break;
                default:
                    break;                                                                    
            }
        }

        //else if (naturalDirect)
        //    ; //setDirectProof(user, translateToDirect(typedTerm), resuelveManager, disponeManager, s, false);
        //else if (naturalSide)
        //    ; //setDirectProof(user, translateToOneSide(typedTerm), resuelveManager, disponeManager, s, true);
        
        // Add the hypothesis if we are doing natural deduction
        // if(naturalDirect || naturalSide) { 
        //     header += "<br>Assuming H1: $" +((App)((App)((App)type).p).q).q.toStringInf(s,"") + "$<br><br>";
        // }

        if (!recursive) { // <--- It had commented (!andIntroduction) as a condition
            historial = header+"<center>$" +historial+"</center>";
            if (!valida){
                historial = historial + MicroServices.transformLaTexToHTML("$\\text{No valid inference}$");
            }
        }
        //else if (/*!andIntroduction &&*/ recursive)
        //    ;
        //else 
        //    this.setHistorial(header +this.getHistorial());
        
        //this.setHistorial(this.getHistorial()+ "$$" +pasoPost + "$$");        
    }
    
}