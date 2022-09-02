package com.calclogic.controller;

import com.calclogic.entity.Categoria;
import com.calclogic.entity.Dispone;
import com.calclogic.entity.PredicadoId;
import com.calclogic.entity.MostrarCategoria;
import com.calclogic.entity.Predicado;
import com.calclogic.entity.Resuelve;
import com.calclogic.entity.Simbolo;
import com.calclogic.entity.Solucion;
import com.calclogic.entity.Teorema;
import com.calclogic.entity.Usuario;
import com.calclogic.forms.AutoSustResponse;
import com.calclogic.forms.InferResponse;
import com.calclogic.forms.InfersForm;
import com.calclogic.forms.InstResponse;
import com.calclogic.forms.StringResponse;
import com.calclogic.forms.SubstResponse;
import com.calclogic.lambdacalculo.App;
import com.calclogic.lambdacalculo.Bracket;
import com.calclogic.lambdacalculo.Const;
import com.calclogic.lambdacalculo.Equation;
import com.calclogic.lambdacalculo.Sust;
import com.calclogic.lambdacalculo.Var;
import com.calclogic.lambdacalculo.Term;
import com.calclogic.lambdacalculo.TypeVerificationException;
import com.calclogic.lambdacalculo.TypedA;
import com.calclogic.lambdacalculo.TypedApp;
import com.calclogic.lambdacalculo.TypedI;
import com.calclogic.lambdacalculo.TypedS;
import com.calclogic.parse.ProofMethodUtilities;
import com.calclogic.parse.TermUtilities;
import com.calclogic.service.ResuelveManager;
import com.calclogic.service.SolucionManager;
import com.calclogic.service.UsuarioManager;
import com.calclogic.service.CategoriaManager;
import com.calclogic.service.DisponeManager;
import com.calclogic.service.MetateoremaManager;
import com.calclogic.service.PredicadoManager;
import com.calclogic.service.MostrarCategoriaManager;
import com.calclogic.service.SimboloManager;
import com.calclogic.proof.CrudOperations;
import com.calclogic.proof.GenericProofMethod;
import com.calclogic.proof.ProofBoolean;
import com.calclogic.proof.InferenceIndex;
import com.calclogic.proof.MetaTheorem;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.HashSet;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author shamuel
 */
@Controller
@RequestMapping(value="/infer")
public class InferController {
    @Autowired
    private UsuarioManager usuarioManager;
    @Autowired
    private PredicadoManager predicadoManager;
    @Autowired
    private SimboloManager simboloManager;
    @Autowired
    private SolucionManager solucionManager;
    @Autowired
    private MetateoremaManager metateoremaManager;
    @Autowired
    private ResuelveManager resuelveManager;
    @Autowired
    private HttpSession session;
    @Autowired
    private CategoriaManager categoriaManager;
    @Autowired
    private DisponeManager disponeManager;
    @Autowired
    private MostrarCategoriaManager mostrarCategoriaManager;
    @Autowired
    private CrudOperations crudOp;
    
    /**
     * Controller that responds to HTTP GET request and returns the selection statement
     * page if there is a user session active. 
     * 
     * @param username: login of the user that made the request. It's is in the URL also
     * @param map: Mapping with values of each variables that will send to infer.jsp 
     * @return the String "infer" that refer to infer.jsp template if the user have an active session.
     *         With the right values in map, the infer.jsp template is filled to obtain the 
     *         selection statement view
     */
    @RequestMapping(value="/{username}", method=RequestMethod.GET)
    public String selectTeoView(@PathVariable String username, ModelMap map) {
        if ( (Usuario)session.getAttribute("user") == null || !((Usuario)session.getAttribute("user")).getLogin().equals(username))
        {
            return "redirect:/index";
        }
        
        List<Resuelve> resuelves = resuelveManager.getAllResuelveByUserOrAdminWithSol(username);
        
        for (Resuelve r: resuelves) // Este for debes mandarlo para el manager y quitar
        {                           // la construccion del metateorema del true
            Teorema t = r.getTeorema();
            t.setTeoTerm(t.getTeoTerm());
            t.setMetateoTerm(new App(new App(new Const(1,"\\equiv ",false,1,1),new Const("true")),t.getTeoTerm()));
        }
        Usuario usr = usuarioManager.getUsuario(username);
        InfersForm infersForm = new InfersForm();
        List <Categoria> showCategorias = new LinkedList<>();
        List<MostrarCategoria> mostrarCategoria = mostrarCategoriaManager.getAllMostrarCategoriasByUsuario(usr);
        for (int i = 0; i < mostrarCategoria.size(); i++ ){// se puede evitar este for si se mandan
                                                           // los MostrarCategorias al jsp en lugar 
                                                           // de las categorias
            showCategorias.add(mostrarCategoria.get(i).getCategoria());
        }
        
        List<Simbolo> simboloList = simboloManager.getAllSimbolo();
        List<Predicado> predicadoList = predicadoManager.getAllPredicadosByUser(username);
        predicadoList.addAll(predicadoManager.getAllPredicadosByUser("AdminTeoremas"));
        String simboloDictionaryCode = PerfilController.simboloDictionaryCode(simboloList, predicadoList);
        
        map.addAttribute("usuario",usr);
        map.addAttribute("infer",infersForm);
        map.addAttribute("mensaje","");
        map.addAttribute("nStatement","");
        map.addAttribute("instanciacion","");
        map.addAttribute("leibniz","");
        map.addAttribute("formula","");
        map.addAttribute("guardarMenu","");
        map.addAttribute("selecTeo",true);
        map.addAttribute("nTeo","");
        map.addAttribute("admin","admin");
        map.addAttribute("listarTerminosMenu","");
        map.addAttribute("verTerminosPublicosMenu","");
        map.addAttribute("misPublicacionesMenu","");
        map.addAttribute("proveMenu","active");
        map.addAttribute("perfilMenu","");
        map.addAttribute("overflow","hidden");
        map.addAttribute("anchuraDiv","100%");
        map.addAttribute("categorias",categoriaManager.getAllCategorias());
        map.addAttribute("resuelves", resuelves);
        map.addAttribute("metateoremas",metateoremaManager);
        map.addAttribute("resuelveManager",resuelveManager);
        map.addAttribute("simboloManager",simboloManager);
        map.addAttribute("showCategorias",showCategorias);
        map.addAttribute("simboloList", simboloList);
        map.addAttribute("predicadoList", predicadoList);
        map.addAttribute("simboloDictionaryCode", simboloDictionaryCode);
        map.addAttribute("isAdmin",usr.isAdmin()?new Integer(1):new Integer(0));

        return "infer";
    }

    /**
     * Controller that responds to HTTP GET request. Returns the proof environment 
     * page of the statement (if there is a user session active). 
     * 
     * @param username: login of the user that made the request. It is in the URL also
     * @param nTeo: code of statement that the user request to prove. It is in the URL also
     * @param map: Mapping with values of each variables that will send to infer.jsp 
     * @return the String "infer" that refer to infer.jsp template if the user have an active session
     *         With the right values in map, the infer.jsp template is filled to obtain the 
     *         proof environment page
     */
    @RequestMapping(value="/{username}/{nTeo:.+}", method=RequestMethod.GET)
    public String inferView(@PathVariable String username, @PathVariable String nTeo, ModelMap map) {
        if ( (Usuario)session.getAttribute("user") == null || !((Usuario)session.getAttribute("user")).getLogin().equals(username))
        {
            return "redirect:/index";
        }
        Resuelve resuel = resuelveManager.getResuelveByUserAndTeoNum(username,nTeo);

        // Case when the user could only see the theorem but had not a Resuelve object associated to it
        if (resuel == null) {
            resuel = resuelveManager.getResuelveByUserAndTeoNum("AdminTeoremas",nTeo);
            Usuario user = usuarioManager.getUsuario(username);
            resuel.setUsuario(user);
            resuel.setDemopendiente(-1);
            resuel.setSolucions(new HashSet());
            resuel.setResuelto(false);
            resuel = resuelveManager.addResuelve(resuel);
        }
        TypedA A = new TypedA(resuel.getTeorema().getTeoTerm(),username);
        Term formula = A.type();
        String solId = "new";
        if (resuel.getDemopendiente() != -1)
            solId ="" + resuel.getDemopendiente();
        List<Resuelve> resuelves = resuelveManager.getAllResuelveByUserOrAdminWithSolWithoutAxiom(username,nTeo); // Maybe: getAllResuelveByUserOrAdminResuelto

        // Usando algoritmo de punto fijo
        List<Resuelve> unResuelve = new ArrayList<Resuelve>();
        unResuelve.add(resuel);
        //System.out.println(unResuelve.get(0).getNumeroteorema());
        List<Resuelve> depend = resuelveManager.getResuelveDependent(username, unResuelve);
        HashSet<Integer> dependIds = new HashSet<Integer>();
        List<String> dependNum = new ArrayList<String>();
        for (Resuelve r: depend) {
            dependIds.add(r.getId());
            dependNum.add(r.getNumeroteorema());
        }
        //System.out.println(dependNum.toString());
        resuelves.removeIf(r -> dependIds.contains(r.getId()));
        //resuelves.removeAll(depend);
        //resuelves = new ArrayList<Resuelve>();

        for (Resuelve r: resuelves){
            Teorema t = r.getTeorema();
            Term term = t.getTeoTerm();

            if(r.isResuelto()==true){ // || r.getNumeroteorema().equals(nTeo)){
                t.setTeoTerm(term);
                t.setMetateoTerm(new App(new App(new Const(1,"\\cssId{clickmeta@"+r.getNumeroteorema()+"}{\\class{operator}{\\style{cursor:pointer; color:#08c;}{\\equiv}}}",false,1,1),new Const("true ")), term));
            }
        }

        Usuario usr = usuarioManager.getUsuario(username);
        map.addAttribute("usuario", usr);
        InfersForm infersForm = new InfersForm();
        infersForm.setSolucionId(0);
        
        map.addAttribute("infer",infersForm);
        map.addAttribute("mensaje","");
        map.addAttribute("nStatement","");
        map.addAttribute("instanciacion","");
        map.addAttribute("leibniz","");
        
        if (solId.equals("new")){
            map.addAttribute("formula","Theorem "+nTeo+":<br> <center>$"+formula.toStringInf(simboloManager,"")+"$</center> Proof:");
            map.addAttribute("elegirMetodo","1");
            map.addAttribute("teoInicial", "");
        }
        else
        {
            Solucion solucion = solucionManager.getSolucion(resuel.getDemopendiente(),username);
            infersForm.setHistorial("Theorem "+nTeo+":<br> <center>$"+formula.toStringInf(simboloManager,"")+"$</center> Proof:");  
            InferResponse response = new InferResponse(crudOp);
            Term typedTerm = solucion.getTypedTerm();

            response.generarHistorial(
                username,
                formula, 
                nTeo, 
                typedTerm, 
                true,
                (solucion.getMetodo().equals("") || 
                        ProofBoolean.isWaitingMethod(ProofMethodUtilities.getTerm(solucion.getMetodo()))?
                                false:true),
                (solucion.getMetodo().equals("")?null:ProofMethodUtilities.getTerm(solucion.getMetodo())), 
                resuelveManager, 
                disponeManager,
                simboloManager
            );
            map.addAttribute("elegirMetodo",response.getCambiarMetodo());
            map.addAttribute("formula",response.getHistorial());
        }

        List <Categoria> showCategorias = new LinkedList<>();
        List<MostrarCategoria> mostrarCategoria = mostrarCategoriaManager.getAllMostrarCategoriasByUsuario(usr);

        for (int i = 0; i < mostrarCategoria.size(); i++ ){
            showCategorias.add(mostrarCategoria.get(i).getCategoria());
        }
        
        List<Simbolo> simboloList = simboloManager.getAllSimbolo();
        List<Predicado> predicadoList = predicadoManager.getAllPredicadosByUser(username);
        predicadoList.addAll(predicadoManager.getAllPredicadosByUser("AdminTeoremas"));
        String simboloDictionaryCode = PerfilController.simboloDictionaryCode(simboloList, predicadoList);
        
        if (usr.isAutosust())
            map.addAttribute("autoSust","true");
        else
            map.addAttribute("autoSust","false");
        
        map.addAttribute("usuario",usr);
        map.addAttribute("guardarMenu","");
        map.addAttribute("selecTeo",false);
        map.addAttribute("nSol",solId);
        map.addAttribute("nTeo",nTeo);
        map.addAttribute("admin","admin");
        map.addAttribute("listarTerminosMenu","");
        map.addAttribute("verTerminosPublicosMenu","");
        map.addAttribute("misPublicacionesMenu","");
        map.addAttribute("proveMenu","active");
        map.addAttribute("perfilMenu","");
        map.addAttribute("overflow","hidden");
        map.addAttribute("anchuraDiv","1200px");
        map.addAttribute("categorias",categoriaManager.getAllCategorias());
        map.addAttribute("resuelves", resuelves);
        map.addAttribute("metateoremas",metateoremaManager);
        map.addAttribute("resuelveManager",resuelveManager);
        map.addAttribute("simboloManager",simboloManager);
        map.addAttribute("showCategorias",showCategorias);
        map.addAttribute("simboloList", simboloList);
        map.addAttribute("predicadoList", predicadoList);
        map.addAttribute("simboloDictionaryCode", simboloDictionaryCode);
        map.addAttribute("isAdmin",usr.isAdmin()?new Integer(1):new Integer(0));

        return "infer";
    }
    
    /**
     * Controller that responds for HTTP POST request with JSON encoded parameters. The request is
     * response by showing a instantiation of a statement of the user in the application. The 
     * response is encode with JSON.
     * 
     * @param nStatement: code that identifies, with username, the statement. It is a 
     *                    HTTP POST parameter
     * @param instanciacion: String that encode the substitution operator. It is a 
     *                       HTTP POST parameter
     * @param username: login of the user. It is in the url also
     * @return JSON encoded InstResponse object with the instantiation info 
     */
    @RequestMapping(value="/{username}/inst", method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)    
    public @ResponseBody InstResponse instantiate(@RequestParam(value="nStatement") String nStatement, 
                           @RequestParam(value="instanciacion") String instanciacion, @PathVariable String username) 
    {
        InstResponse response = new InstResponse();
        PredicadoId predicadoid = new PredicadoId();
        predicadoid.setLogin(username);
        
        Term statementTerm = crudOp.findStatement(response, nStatement, username, resuelveManager, disponeManager);
        if (response.getError() != null){
            return response;
        }
       
        // CREATE THE INSTANTIATION
        ArrayList<Object> arr = null;
        if (!instanciacion.equals("")){
            arr=TermUtilities.instanciate(instanciacion, predicadoid, predicadoManager, simboloManager);
        }
        
        if (arr == null)
            response.setInstantiation(statementTerm.toStringInf(simboloManager, ""));
        else {// (ArrayList<Var>)arr.get(0), (ArrayList<Term>)arr.get(1)
            try {
            TypedI I = new TypedI(new Sust((ArrayList<Var>)arr.get(0), (ArrayList<Term>)arr.get(1)));
            response.setInstantiation(new TypedApp(I,statementTerm).type().toStringInf(simboloManager, ""));
            }
            catch (TypeVerificationException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * Controller that responds for HTTP POST request to set on/off the auto substitution 
     * functionality. The response is encode with JSON.
     * 
     * @param username: login of the user. It is in the URL also
     * @return JSON encoded AutoSustResponse object with the on/off info 
     */
    @RequestMapping(value="/{username}/auto", method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)    
    public @ResponseBody AutoSustResponse setAutoSust(@PathVariable String username) 
    {
        Usuario usuario = usuarioManager.getUsuario(username);
        AutoSustResponse response = new AutoSustResponse();
        if (usuario != null) {
            if (usuario.isAutosust()) {
                usuario.setAutosust(false);
                usuarioManager.updateUsuario(usuario);
                response.setAuto(false);
            }
            else {
                usuario.setAutosust(true);
                usuarioManager.updateUsuario(usuario);
                response.setAuto(true);
            }

        }else {
            response.setError("user doesn't exist");
        }
        
        return response;
    }
    
    /**
     * Controller for automatic filling of the substitution form in the user session. 
     * 
     * @param nStatement: code that identifies the statement. It is a JSON encode HTTP POST parameter
     * @param leibniz: String with the E of Leibniz rule in format C. It is JSON encode 
     *                 HTTP POST parameter
     * @param freeV: String with the list of free variables in format C. It is JSON encode
     *               HTTP POST parameter
     * @param username: name of the user doing the prove. It is in the URL also
     * @param nSol: Id of user solution in which the autosust match the last line.  It is in 
     *              the URL also
     * @return JSON encode SubstResponse Object with the result substitution info
     */
    @RequestMapping(value="/{username}/{nTeo:.+}/{nSol}/auto", method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)    
    public @ResponseBody SubstResponse autoSubst(@RequestParam(value="nStatement") String nStatement, 
                           @RequestParam(value="leibniz") String leibniz, @RequestParam(value="freeV") String freeV, 
                           @PathVariable String username, @PathVariable String nSol) 
    {
        SubstResponse response = new SubstResponse();
        PredicadoId predicadoid=new PredicadoId();
        predicadoid.setLogin(username);
        
        Term statementTerm = crudOp.findStatement(response, nStatement, username, resuelveManager, disponeManager);
        if (response.getError() != null){
            return response;
        }

        // String freeV = statementTerm.freeVars();
        if (!freeV.equals("")) {
            String[] freeVars = freeV.split(",");
            Solucion solucion = solucionManager.getSolucion(Integer.parseInt(nSol),username);

            String method = solucion.getMetodo();
            Term methodTerm = ProofMethodUtilities.getTerm(method);
            Term typedTerm = crudOp.getSubProof(solucion.getTypedTerm(),methodTerm,true);

            Term lastLine = typedTerm.type();
            
            if (lastLine == null)
                lastLine = typedTerm;
            else {
                method = crudOp.currentMethod(methodTerm).toStringFinal();
                GenericProofMethod objectMethod = crudOp.createProofMethodObject(method);

                if (objectMethod.getGroupMethod().equals("T")){
                    int index = objectMethod.transFirstOpInferIndex(typedTerm,false);
                    if (index == 0 || index == 1)
                        lastLine = ((App)((App)typedTerm.type()).p).q;
                    else
                        lastLine = ((App)((App)((App)((App)typedTerm.type()).p).q).p).q;
                }
                else
                    lastLine = ((App)((App)lastLine).p).q;
            }

            lastLine = lastLine.body();
            Term leibnizTerm = null;
            // CREATE THE INSTANTIATION
            Sust sust = null;
            Equation eq;
            boolean zUnifiable = true;
            if (!leibniz.equals("")){
                leibnizTerm =TermUtilities.getTerm(leibniz, predicadoid, predicadoManager, simboloManager);
                eq = new Equation(leibnizTerm,lastLine);
                sust = eq.mgu(simboloManager);
                if (sust != null) // si z no es de tipo atomico, no se puede unificar en primer orden
                    leibnizTerm = eq.mgu(simboloManager).getTerms().get(0);
                else
                    zUnifiable = false;
            }
            else 
                leibnizTerm = lastLine;

            if (zUnifiable)  {
                eq = new Equation(((App)statementTerm).q,leibnizTerm);
                sust = eq.mgu(simboloManager);
                if (sust == null) {
                    eq = new Equation(((App)((App)statementTerm).p).q,leibnizTerm);
                    sust = eq.mgu(simboloManager);
                }
            }

            if (sust != null) {
                //String[] sustVars = sust.getVars().toString().replaceAll("[\\s\\[\\]]", "").split(",");
                String[] sustFormatC = new String[freeVars.length];
                String[] sustLatex = new String[freeVars.length];
                for (int i=0; i<freeVars.length; i++) {
                    int j = sust.getVars().indexOf(new Var(freeVars[i].toCharArray()[0]));
                    if (j != -1) {
                        sustFormatC[i] = sust.getTerms().get(j).toStringFormatC(simboloManager,"",0,"substitutionButtonsId."+freeVars[i]);
                        sustLatex[i] = sust.getTerms().get(j).toStringWithInputs(simboloManager,"","substitutionButtonsId."+freeVars[i]);
                    }
                    else {
                        sustFormatC[i] = "";
                        sustLatex[i] = "";
                    }
                }
                response.setSustFormatC(sustFormatC);
                response.setSustLatex(sustLatex);
                return response;
            }
        }
        response.setSustFormatC(null);
        response.setSustLatex(null);
        return response;
    }
    
        /**
     * Controller that responds to HTTP POST request encoded with JSON.Returns an InferResponse
     * object with the selected metatheorem applied to the selected theorem, in latex format.
     *
     * @param username: login of the user that made the request. It is in the URL also.
     * @param nTheo: code of statement of the theorem.
     * @return InferResponse Object with the statement of the metatheorem, in latex format.
     */
    @RequestMapping(value="/{username}/metatheorem", method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody StringResponse metatheoremController(@PathVariable String username,
                                                      @RequestParam(value="nTheo") String nTheo
                                                    )
    {
        Term statement = resuelveManager.getResuelveByUserAndTeoNum(username,nTheo).getTeorema().getTeoTerm();

        // Specific case, we use the 3.7 one. The others should be obtained from a template in the database
        Term metaTheo = MetaTheorem.metaTheorem(statement).type();

        return new StringResponse(nTheo + " with MetaTheorem (" + "3.7" +") $" + metaTheo.toStringInf(simboloManager,"") + "$");

    }
    
    /**
     * Controller that responds to HTTP POST request encoded with JSON. Returns an InferResponse
     * Object with the the proof, in latex format, after an one step inference. 
     *
     * @param nStatement: id used by user to identify the statement of the inference. It is an HTTP POST 
     *                    parameter encoded with JSON
     * @param leibniz: String with the description of Leibniz of the inference in format C. It is an 
     *                 HTTP POST parameter encoded with JSON
     * @param instanciacion: String with the description of the substitution of the inference in format C. 
     *                       It is an HTTP POST parameter encoded with JSON
     * @param username: login of the user that made the request. It is in the URL also
     * @param nTeo: code of statement that the user is proving. It is in the URL also
     * @param nSol: id of the solution of nTeo that the user is editing. It is in the URL also 
     * @return InferResponse Object with the the proof, in latex format, after an one step inference. 
     */
    @RequestMapping(value="/{username}/{nTeo:.+}/{nSol}", method=RequestMethod.POST, params="submitBtn=Inferir",headers="Accept=application/json", produces= MediaType.APPLICATION_JSON_VALUE)    
    public @ResponseBody InferResponse infer(@RequestParam(value="nStatement") String nStatement, 
                                             @RequestParam(value="leibniz") String leibniz , 
                                             @RequestParam(value="instanciacion") String instanciacion, 
                                             @PathVariable String username, @PathVariable String nTeo, 
                                             @PathVariable String nSol 
                                             /*, @RequestParam(value="teoremaInicial") String teoremaInicial, @RequestParam(value="nuevoMetodo") String nuevoMetodo */) 
    {
        InferResponse response = new InferResponse(crudOp);
        PredicadoId predicadoid = new PredicadoId();
        predicadoid.setLogin(username);

        Term statementTerm = crudOp.findStatement(response, nStatement, username, resuelveManager, disponeManager);
        if (response.getError() != null){
            return response;
        }
        
        // CREATE THE INSTANTIATION
        ArrayList<Object> arr = null;
        if (!instanciacion.equals("")){
            arr=TermUtilities.instanciate(instanciacion, predicadoid, predicadoManager, simboloManager);
        }
        
        // CREATE LEIBNIZ
        Term leibnizTerm = null;
        if (!leibniz.equals("")){
            leibnizTerm =TermUtilities.lambda(leibniz, predicadoid, predicadoManager, simboloManager);
            if ( ((Bracket)leibnizTerm).isIdFunction()) {
                leibnizTerm = null;
                leibniz = "";
            }
        }   

        Resuelve resuel = resuelveManager.getResuelveByUserAndTeoNum(username,nTeo);
        Solucion solucion = solucionManager.getSolucion(Integer.parseInt(nSol),username);
        Term typedTerm = solucion.getTypedTerm();
        Term formula = resuel.getTeorema().getTeoTerm().setToPrinting(resuel.getVariables());
        // CHECK formula pudiera ser una igualdad y pasar por equival trans usando arboles de derivacion
        String metodo = solucion.getMetodo();
        Term methodTerm = ProofMethodUtilities.getTerm(metodo);
        Term methodTermIter = methodTerm;
        
        Stack<Term> methodStk       = new Stack<>();
        Stack<Term> fatherProofs    = new Stack<>();
        Stack<Term> formulasToProof = new Stack<>();
        formulasToProof.push(formula);
        Term initSt = formula;

        while (!(methodTermIter instanceof Const)) {
            methodStk.push(((App)methodTermIter).p);
            initSt = crudOp.initStatement(initSt,new App(((App)methodTermIter).p,new Const("DM")));
            formulasToProof.push(initSt);

            if (
                  ((App)methodTermIter).p instanceof App &&
                  ("B".equals(crudOp.createProofMethodObject( ((App)((App)methodTermIter).p).p.toStringFinal() ).getGroupMethod())) &&
                  (ProofBoolean.isBranchedProof2Started(methodTermIter))         
               )
            {
                fatherProofs.push(typedTerm);
                typedTerm = crudOp.getSubProof(typedTerm, methodTermIter);
            }
            methodTermIter = ((App)methodTermIter).q;
        }
        Term formulaBeingProved = formulasToProof.pop();
    
        // CREATE THE NEW INFERENCE DEPENDING ON THE PROOF TYPE
        Term infer;
        String strMethodTermIter = methodTermIter.toStringFinal();
        GenericProofMethod objectMethod = crudOp.createProofMethodObject(strMethodTermIter);
        
        try {
            infer = objectMethod.createBaseMethodInfer(username,statementTerm, arr, instanciacion, (Bracket)leibnizTerm, leibniz, resuel.getTeorema().getTeoTerm());
        } 
        catch(TypeVerificationException e) { // If something went wrong building the new step of the proof
            response.generarHistorial(username,formula, nTeo,typedTerm,false,true, methodTerm,resuelveManager,disponeManager,simboloManager);
            return response;
        }

        // CREATE THE NEW PROOF TREE BY ADDING THE NEW INFER
        Term newProof =null;
        
        boolean isOnlyOneLine = ProofBoolean.isOneLineProof(typedTerm);
        Term currentProof;
        if (isOnlyOneLine){
            // If the proof only has one line so far, it may not be a boolean expression yet, because it could only 
            // be arithmetic, like 3 + 4. But since we always need it to be boolean, if the only line was P, we make 
            // the proof to be provisionally: P == P. Soon we will discard again the first P-
            currentProof = new TypedA(new App(new App(new Const(0,"="),typedTerm),typedTerm));
        }
        else{
            currentProof = typedTerm;
        }

        int i;//, j;
        i = 0;
        //j = 0;
        while (newProof == null && i < 2) {

            try {
                if (i == 1 /*&& j == 0*/){
                    infer = new TypedApp(new TypedS(), infer);
                }
                // If proofCrudOperations.addInferToProof does not throw exception when typedTerm.type()==null, then the inference is valid respect of the first expression.
                // NOTE: The parameter "currentProof" changes with the application of this method, so this method cannot be omitted when "onlyOneLine" is true
                newProof = crudOp.addInferToProof(username,currentProof, infer, objectMethod);
                if (isOnlyOneLine){
                    // If the proof only has one line with expression P for the user, then at this point it is interally P == P
                    // as explained previously. But since we don't need the first P and the new inference "infer" consists of 
                    // the three parts that will be shown to the user: 1) the previous expression P, 2) the hint and 3) the new expresssion,
                    // then we make the proof to be equal to that inference.
                    if (newProof instanceof TypedApp && ((TypedApp)newProof).inferType == 'e'){
                        newProof = null;
                        throw new TypeVerificationException();
                    }
                    newProof = infer;
                }
            }
            catch (TypeVerificationException e) {
                if (i==1/*(i == 1 && !onlyOneLine) || (i == 1 && j == 1)*/){
                    response.generarHistorial(username,formula, nTeo,typedTerm,false,true, methodTerm,resuelveManager,disponeManager,simboloManager);
                    return response;
                }
                /*if (onlyOneLine && j == 0) {
                    currentProof = new TypedA(new App(new App(new Const(1,"c_{20}",false,1,1),
                            typedTerm),typedTerm));
                    j=1;
                }
                else if (onlyOneLine && j == 1) {
                    currentProof = new TypedA(new App(new App(new Const(1,"c_{1}",false,1,1),
                            typedTerm),typedTerm));
                    j=0;
                }*/
            }
            //i = (j == 1?i:i+1);
            i++;
        }       
        response.setResuelto("0");
        // CHECK IF THE PROOF FINISHED

        // *************** Note: Try to include both cases (recursive and not recursive) in a single cycle.
        // For example, like pushing the non-recursive method to the stack **************************
        Term finalProof = newProof;

        if (!objectMethod.getIsRecursiveMethod()){
            finalProof = objectMethod.finishedBaseMethodProof(formulaBeingProved, newProof, username, resuelveManager, simboloManager);
        }
        
        // Get the complete method in case it was not atomic
        Boolean isFinalSolution = formulaBeingProved.equals(finalProof.type().setToPrinting(resuel.getVariables()));
        // CHECK puede ser una igualdad y pasar por equivalencia, esto hay que transformarlo usando arboles de deriv

        // We need this because in branched recursive methods we use And Introduction structure anyway
        GenericProofMethod aiObject = crudOp.createProofMethodObject("AI");

        while (!methodStk.isEmpty()){
            Term methodTermAux = methodStk.pop();
            String strMethodTermAux = methodTermAux.toStringFinal();
            objectMethod = crudOp.createProofMethodObject(strMethodTermAux);

            if (isFinalSolution && methodTermAux instanceof Const) {
                // Recursive branched method
                if ("B".equals(objectMethod.getGroupMethod())) {
                    isFinalSolution = false;
                    response.setEndCase(true);   
                }
                // Recursive linear method
                else if (objectMethod.getIsRecursiveMethod()) {
                    finalProof = objectMethod.finishedLinearRecursiveMethodProof(username,formulasToProof.pop(), finalProof);
                }
                else{
                    break;
                }
            }

            // This part ensures that after each one-step inference the tree for the second proof is updated
            if (methodTermAux instanceof App){
                Term m = ((App)methodTermAux).p;
                String strM = m.toStringFinal();
                if (m != null){
                    objectMethod = crudOp.createProofMethodObject(strM);
                    if ("B".equals(objectMethod.getGroupMethod())){
                        finalProof = aiObject.finishedBranchedRecursiveMethodProof(username,fatherProofs.pop(), finalProof);
                        if (isFinalSolution && !("AI".equals(strM))){
                            finalProof = objectMethod.finishedBranchedRecursiveMethodProof(username,null, finalProof);
                        }
                    }
                }
            }
        }
        
        // UPDATE SOLUCION 
        solucion.setTypedTerm(finalProof);
        
        // If finished mark solucion as solved
        if (isFinalSolution) {
            response.setResuelto("1");
            solucion.setResuelto(true);
            resuel.setResuelto(true);
            resuelveManager.updateResuelve(resuel);
        }

        response.generarHistorial(
            username,
            formula, 
            nTeo,
            finalProof,
            true,
            true,
            methodTerm,
            resuelveManager,
            disponeManager,
            simboloManager
        );

        solucionManager.updateSolucion(solucion);
        
        return response;
    }

    /**
     * Controller that responds to HTTP POST request encoded with JSON. Returns an InferResponse
     * Object with the the proof, in latex format, without the last line. If delete the last 
     * line implies that you must select a new proof method, the InferResponse has the select 
     * method attribute on.
     *
     * @param username: login of the user that made the request. It is in the URL also
     * @param nTeo: code of statement that the user is proving. It is in the URL also
     * @param nSol: id of the solution of nTeo that the user is editing. It is in the url also 
     * @return InferResponse Object with the the proof, in latex format, without the last line. If delete the last 
     * line implies that you must select a new proof method, the InferResponse has the select 
     * method attribute on. 
     */
    @RequestMapping(value="/{username}/{nTeo:.+}/{nSol}", method=RequestMethod.POST, params="submitBtn=Retroceder",produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody InferResponse retroceder(@PathVariable String username, 
                                                  @PathVariable String nTeo, 
                                                  @PathVariable String nSol)
    {   
        InferResponse response = new InferResponse(crudOp);

        Resuelve resuelve = resuelveManager.getResuelveByUserAndTeoNum(username,nTeo);
        Solucion solucion = solucionManager.getSolucion(resuelve.getDemopendiente(),username);
        int respRetroceder;
        Term method = null;
        
        if(nSol.equals("new")){
            respRetroceder = 0;
        }
        else{
            method = (solucion.getMetodo().equals("")?null:ProofMethodUtilities.getTerm(solucion.getMetodo()));
            Term currentMethod = crudOp.currentMethod(method);
            boolean isWaitingMethod = !ProofBoolean.isBaseMethod(currentMethod);
            if (!solucion.getMetodo().equals("") && isWaitingMethod){
                respRetroceder = 0;
            }
            else {
                GenericProofMethod objectMethod = crudOp.createProofMethodObject(currentMethod.toStringFinal());
                respRetroceder = solucion.retrocederPaso(username,method,objectMethod);
            }
            if (respRetroceder == 0) {
               method = crudOp.eraseMethod(solucion.getMetodo());
               solucion.setMetodo((method == null?"":method.toStringFinal()));
            }
            
            solucionManager.updateSolucion(solucion);
        }
        
        //List<PasoInferencia> inferencias = solucion.getArregloInferencias();
        Term formula = resuelve.getTeorema().getTeoTerm().setToPrinting(resuelve.getVariables());

        response.generarHistorial(
               username,
               formula, 
               nTeo, 
               nSol.equals("new")?null:solucion.getTypedTerm(), 
               true, 
               true, 
               method,
               resuelveManager, 
               disponeManager, 
               simboloManager);
        
        // estos set se podrían calcular dentro de generar historial
        if(respRetroceder==0 && method != null){
            response.setCambiarMetodo("2");
        }
        else if(respRetroceder==0 && method == null){
            response.setCambiarMetodo("1");
        }
        else{
            response.setCambiarMetodo("0");
        }
        
        return response;
    }
        
    /**
     * Controller that responds to HTTP POST request encoded with JSON. Returns an InferResponse
     * Object with the proof, in latex format, in which the statement of the last sub proof is 
     * clickable
     *
     * @param newMethod: Proof method that will be introduced in the proof.
     * @param username: Login of the user that made the request. It is in the url also.
     * @param nSol: Identifier of the solution of nTeo that the user is editing. It is in the url also.
     * @param nTeo: Code of statement that the user is proving. It is in the url also.
     * @return Returns an InferResponse object with the proof, in latex format, in which the 
     *         statement of the last sub proof is clickable.
     */
    @RequestMapping(value="/{username}/{nTeo:.+}/{nSol}/clickableST", method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody InferResponse clickableSTController(
                                            @RequestParam(value="method") String newMethod,
                                            @PathVariable String username, 
                                            @PathVariable String nSol, 
                                            @PathVariable String nTeo)
    {   
        InferResponse response = new InferResponse(crudOp);
      
        Resuelve resuelve = resuelveManager.getResuelveByUserAndTeoNum(username,nTeo);
        Teorema t = resuelve.getTeorema();
        Term term = t.getTeoTerm().setToPrinting(resuelve.getVariables());
        
        Term methodTerm, typedTerm;
        methodTerm = typedTerm = null;
        
        if (((App)((App)term).p).q.containT()){
            response.setErrorParser1(true);
            return response;
        }
        else if ("SS".equals(newMethod)){
            response.setLado("1");
        }
        
        // When the proof already exists in the DB, we obtain the solution from it.
        if (!nSol.equals("new")){       
            Solucion solucion = solucionManager.getSolucion(Integer.parseInt(nSol),username);
            
            String previousMethod = solucion.getMetodo();
            if (!previousMethod.equals(""))
                methodTerm = ProofMethodUtilities.getTerm(previousMethod);

            typedTerm = solucion.getTypedTerm();
        }

        response.generarHistorial(
            username, 
            term,
            "",
            nTeo, 
            typedTerm, 
            true, 
            false, 
            methodTerm, 
            resuelveManager, 
            disponeManager, 
            simboloManager, 
            newMethod,
            true
        );
        return response;
    }

    /**
     * Controller that responds to HTTP POST request encoded with JSON. Returns an InferResponse
     * Object with the proof, in latex format, with only the expression selected for the 
     * user (a complete statement or a side of the equation) in the first line of the current sub proof.
     *
     * @param teoid: Identifier of the theorem that the user selects to start the proof. It is a 
     *               HTTP POST parameter encode with JSON.
     * @param lado: String that encodes the side of the equation selected for the user. It is a 
     *              HTTP POST parameter encoded with JSON.
     * @param newMethod: Proof method that will be introduced in the proof.
     * @param nSol: id of the solution of nTeo that the user is editing. It is in the url also.
     * @param username: login of the user that made the request. It is in the URL also.
     * @param nTeo: code of statement that the user is proving. It is in the URL also .
     * @return InferResponse Object with the the proof, in latex format, with only the statement 
     *         selected for the user in the first line of the current sub proof.
     */
    /*@RequestMapping(value="/{username}/{nTeo:.+}/{nSol}/teoremaInicialMD", method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody InferResponse teoremaInicialMD(@RequestParam(value="teoid") String teoid, 
                                                        @PathVariable String nSol, 
                                                        @PathVariable String username, 
                                                        @PathVariable String nTeo) //Jean throws TypeVerificationException
    {   
        String nuevoMetodo = "DM";
        InferResponse response = new InferResponse(crudOp);

        // Jean response.setLado("1");
        Resuelve resuelveAnterior = resuelveManager.getResuelveByUserAndTeoNum(username,nTeo);
        String variables = resuelveAnterior.getVariables();
        Term formulaAnterior = resuelveAnterior.getTeorema().getTeoTerm().setToPrinting(variables);
        
        //String formula = "";
        Term formulaTerm = null;
        if (teoid.substring(0, 3).equals("ST-")){
            Resuelve resuelve = resuelveManager.getResuelveByUserAndTeoNum(username,teoid.substring(3,teoid.length()));
            if (resuelve == null){
                resuelve = resuelveManager.getResuelveByUserAndTeoNum("AdminTeoremas",teoid.substring(3,teoid.length()));
            }
            //String formula = resuelve.getTeorema().getTeoTerm().toStringInfFinal();
            formulaTerm = resuelve.getTeorema().getTeoTerm().setToPrinting(resuelve.getVariables());
            //formula = formulaTerm.toStringInfLabeled();
        }
        else if (teoid.substring(0, 3).equals("MT-")){
            Dispone dispone = disponeManager.getDisponeByUserAndTeoNum(username, teoid.substring(3,teoid.length()));
            formulaTerm = dispone.getMetateorema().getTeoTerm();
            //formula = formulaTerm.toStringInfLabeled();
        }
        
        Term metodoTerm = null;
        Term typedTerm = null;
        if (nSol.equals("new")){
            typedTerm = formulaTerm;
            Solucion solucion = new Solucion(resuelveAnterior,false,formulaTerm, nuevoMetodo, 
                                             finiPMeth, crudOp);
            solucionManager.addSolucion(solucion);
            response.setnSol(solucion.getId()+"");
            metodoTerm = new Const(nuevoMetodo);
        }
        else{
            // Obtains the solution from DB.
            Solucion solucion = solucionManager.getSolucion(Integer.parseInt(nSol),username);     
            String method = solucion.getMetodo();
            
            metodoTerm = crudOp.updateMethod(method, nuevoMetodo);
            if (teoid.substring(3,teoid.length()).equals(nTeo)) {
               formulaTerm = crudOp.initStatement(formulaTerm, metodoTerm);
               typedTerm = crudOp.addFirstLineSubProof(username,formulaTerm, solucion.getTypedTerm(), metodoTerm);
               solucion.setTypedTerm(typedTerm);
            } else {
               typedTerm = crudOp.addFirstLineSubProof(username,formulaTerm, solucion.getTypedTerm(), metodoTerm);
               solucion.setTypedTerm(typedTerm);
            }
            nuevoMetodo = metodoTerm.toStringFinal();
            solucion.setMetodo(nuevoMetodo);
            solucionManager.updateSolucion(solucion);
        }
        
        response.generarHistorial(
            username,
            formulaAnterior, 
            nTeo,
            typedTerm,
            true,
            true,
            metodoTerm,
            resuelveManager,
            disponeManager,
            simboloManager 
        );
        //String historial = "Theorem "+nTeo+":<br> <center>$"+formulaAnterior+"$</center> Proof:<br><center>$"+formula+"</center>";
        //response.setHistorial(historial);
        response.setCambiarMetodo("0");

        return response;
    }*/

    /**
     * Controller that respond to HTTP POST request encoded with JSON. Return an InferResponse
     * Object with the proof, in latex format, with only the expression selected (right or left 
     * side of the equation) for the user in the first line of the current sub proof.
     *
     * @param nSol: id of the solution of nTeo that the user is editing. It is in the URL also
     * @param lado: String that encode the side of the equation selected for the user. It is a 
     *              HTTP POST parameter encode with JSON
     * @param username: login of the user that make the request. It is in the URL also
     * @param nTeo: code of statement that the user is proving. It is in the URL also 
     * @return InferResponse Object with the the proof, in latex format, with only the expression 
     *         selected (right or left side of the equation) for the user in the first line of the 
     *         current sub proof.
     */
    /*@RequestMapping(value="/{username}/{nTeo:.+}/{nSol}/teoremaInicialPL", method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody InferResponse teoremaInicialPL(@PathVariable String nSol, 
                                                        @RequestParam(value="lado") String lado, 
                                                        @PathVariable String username, 
                                                        @PathVariable String nTeo)
    {
        String nuevoMetodo = "SS";
        InferResponse response = new InferResponse(crudOp);
    */    
        /* Jean
        boolean naturalSide = nuevoMetodo.equals("Natural Deduction,one-sided");
        Term H = null;
        */
        
        /*Resuelve resuelve = resuelveManager.getResuelveByUserAndTeoNum(username,nTeo);
        Term formulaAnterior = resuelve.getTeorema().getTeoTerm().setToPrinting(resuelve.getVariables());
        Term formulaTerm = (Term)formulaAnterior.clone2();*/
        /* Jean
        if(naturalSide) {
+               H = ((App)formulaAnterior).q;
+               formulaAnterior = ((App)((App)formulaAnterior).p).q;
+        }
        */
        
        /* Jean
        // In natural dededuction case add H /\ to the start
+        if(naturalSide) {
+               formulaTerm = new App(new App(new Const("c_{5}"), formulaTerm), H);
        */
        /*Solucion solucion = null;
        Term metodoTerm = null;
        Term typedTerm = null;
        if (nSol.equals("new")){
            solucion = new Solucion(resuelve,false,null, nuevoMetodo,finiPMeth, crudOp);
            metodoTerm = new Const(nuevoMetodo);
        }
        else{
            solucion = solucionManager.getSolucion(Integer.parseInt(nSol),username);
            String method = solucion.getMetodo();
            metodoTerm = crudOp.updateMethod(method, nuevoMetodo);
            formulaTerm = crudOp.initStatement(formulaTerm,metodoTerm);
            nuevoMetodo = metodoTerm.toStringFinal();
            solucion.setMetodo(nuevoMetodo);
        }
        if(lado.equals("d")){
            //formula = ((App)((App)resuelve.getTeorema().getTeoTerm()).p).q.toStringInfFinal();
            formulaTerm = ((App)((App)formulaTerm).p).q;
            //Jean formulaTerm = ((App)((App)formulaAnterior).p).q;
        }
        else if(lado.equals("i")){
            //formula = ((App)resuelve.getTeorema().getTeoTerm()).q.toStringInfFinal();
            formulaTerm = ((App)formulaTerm).q;
            // Jean formulaTerm = ((App)formulaAnterior).q;
        }
        
        if (nSol.equals("new")) {
            typedTerm = formulaTerm;
            solucion.setTypedTerm(formulaTerm);
            solucionManager.addSolucion(solucion);
            response.setnSol(solucion.getId()+"");
        }
        else {
            typedTerm = crudOp.addFirstLineSubProof(username,formulaTerm, solucion.getTypedTerm(), metodoTerm);
            solucion.setTypedTerm(typedTerm);
            solucionManager.updateSolucion(solucion);
        }
        
        response.generarHistorial(
            username,
            formulaAnterior, 
            nTeo,
            typedTerm,
            true,
            true,
            metodoTerm,
            resuelveManager,
            disponeManager,
            simboloManager 
        );*/
        /*String historial = "Theorem "+nTeo+":<br> <center>$"+formulaAnterior+"$</center> Proof:<br><center>$"+formula+"</center>";
        response.setHistorial(historial); */
        /*response.setCambiarMetodo("0");

        return response;
    }*/
    
    /**
     * Controller that respond to HTTP POST request encoded with JSON. Return an InferResponse
     * Object with the proof, in latex format, with only the antecedent of the current statement 
     * in the first line of the current sub proof.
     *
     * @param nSol: id of the solution of nTeo that the user is editing. It is in the URL also
     * @param username: login of the user that make the request. It is in the URL also
     * @param nTeo: code of statement that the user is proving. It is in the URL also 
     * @return InferResponse Object with the the proof, in latex format, with only the antecedent 
     *         of the current statement in the first line of the current sub proof.
     */
    /*@RequestMapping(value="/{username}/{nTeo:.+}/{nSol}/teoremaInicialD", method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody InferResponse teoremaInicialD(@PathVariable String nSol, 
                                                       @PathVariable String username, 
                                                       @PathVariable String nTeo)
    {   
        String nuevoMetodo = "WE";
        InferResponse response = new InferResponse(crudOp);
        
        Resuelve resuelve = resuelveManager.getResuelveByUserAndTeoNum(username,nTeo);
        Term formulaAnterior = resuelve.getTeorema().getTeoTerm().setToPrinting(resuelve.getVariables());
        
        //Teorema t = resuelve.getTeorema();
        //Term term = t.getTeoTerm();
        
        //String formula = "";
        Term formulaTerm = formulaAnterior;
        
        Term metodoTerm = null;
        Solucion solucion = null;
        Term typedTerm = null;
        if (nSol.equals("new")){
            solucion = new Solucion(resuelve,false,null, nuevoMetodo,finiPMeth, crudOp);
            metodoTerm = new Const(nuevoMetodo);
        }
        else{
            solucion = solucionManager.getSolucion(Integer.parseInt(nSol),username);
            String method = solucion.getMetodo();
            metodoTerm = crudOp.updateMethod(method, nuevoMetodo);
            formulaTerm = crudOp.initStatement(formulaTerm, metodoTerm);
            nuevoMetodo = metodoTerm.toStringFinal();
            solucion.setMetodo(nuevoMetodo);
        }
        
        String operador = ((Const)((App)((App)formulaTerm).p).p).getCon();
        switch (operador) {
            case "c_{3}":
                response.setLado("d");
                formulaTerm = ((App)((App)formulaTerm).p).q;
                break;
            case "c_{2}":
                response.setLado("i");
                formulaTerm = ((App)formulaTerm).q;
                break;
            default:
                response.setErrorParser1(true);
                break;
        }
        
        if (nSol.equals("new")) {
            typedTerm = formulaTerm;
            solucion.setTypedTerm(formulaTerm);
            solucionManager.addSolucion(solucion);
            response.setnSol(solucion.getId()+"");
        } else {
            typedTerm = crudOp.addFirstLineSubProof(username,formulaTerm, solucion.getTypedTerm(), metodoTerm);
            solucion.setTypedTerm(typedTerm);
            solucionManager.updateSolucion(solucion);
        }
        
        solucionManager.addSolucion(solucion);
        response.generarHistorial(
            username,
            formulaAnterior, 
            nTeo,
            typedTerm,
            true,
            true,
            metodoTerm,
            resuelveManager,
            disponeManager,
            simboloManager 
        );*/
        /*String historial = "Theorem "+nTeo+":<br> <center>$"+formulaAnterior+"$</center> Proof:<br><center>$"+formula+"</center>";
        response.setHistorial(historial);  */
        //response.setCambiarMetodo("0");

    @RequestMapping(value="/{username}/{nTeo:.+}/{nSol}/iniStatement", method=RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody InferResponse iniStatementController(
                                            @RequestParam(value="teoid") String teoid,
                                            @RequestParam(value="lado") String lado, 
                                            @RequestParam(value="method") String newMethod,
                                            @PathVariable String nSol, 
                                            @PathVariable String username, 
                                            @PathVariable String nTeo)
    {
        GenericProofMethod objectMethod = crudOp.createProofMethodObject(newMethod);
        Boolean isRecursive = objectMethod.getIsRecursiveMethod();
        String groupMethod = objectMethod.getGroupMethod();
        boolean sideOrTransitive = ("SS".equals(newMethod) || "T".equals(groupMethod));

        InferResponse response = new InferResponse(crudOp);
        Resuelve resuelveAnterior = resuelveManager.getResuelveByUserAndTeoNum(username,nTeo);

        // This is the theorem statement but parsed as a binary tree. 
        // We call it as "previous" because it may change when the proof starts
        Term formulaAnterior = resuelveAnterior.getTeorema().getTeoTerm().setToPrinting(resuelveAnterior.getVariables());

        Integer opId; // Id of the symbol of the main operator of an expression
        Solucion solucion = null;
        Term methodTerm, typedTerm;
        methodTerm = typedTerm = null;

        try{
            // ---- In this section we determine the value of "formulaTerm" -----
            Term formulaTerm = "T".equals(groupMethod) ? formulaAnterior : null;

            if ("DM".equals(newMethod)){
                if (teoid.substring(0, 3).equals("ST-")){
                    Resuelve resuelve = resuelveManager.getResuelveByUserAndTeoNum(username,teoid.substring(3,teoid.length()));
                    if (resuelve == null){
                        resuelve = resuelveManager.getResuelveByUserAndTeoNum("AdminTeoremas",teoid.substring(3,teoid.length()));
                    }
                    formulaTerm = resuelve.getTeorema().getTeoTerm().setToPrinting(resuelve.getVariables());
                }
                else if (teoid.substring(0, 3).equals("MT-")){
                    Dispone dispone = disponeManager.getDisponeByUserAndTeoNum(username, teoid.substring(3,teoid.length()));
                    if (dispone == null){
                        dispone = disponeManager.getDisponeByUserAndTeoNum("AdminTeoremas", teoid.substring(3,teoid.length()));
                    }
                    formulaTerm = dispone.getMetateorema().getTeoTerm();
                }
            }
            else if ("SS".equals(newMethod)){
                formulaTerm = resuelveAnterior.getTeorema().getTeoTerm().setToPrinting(resuelveAnterior.getVariables());
            }
            // ---- End of assigning "formulaTerm" 

            if (nSol.equals("new")){
                if ( ("CR".equals(newMethod) && ((opId=crudOp.binaryOperatorId(formulaAnterior,null)) != 2) && (opId !=3) ) || // Right arrow ==> or left arrow <==
                     ("AI".equals(newMethod) && (crudOp.binaryOperatorId(formulaAnterior,null) != 5) ) || // Conjunction /\
                     ("MI".equals(newMethod) && (crudOp.binaryOperatorId(formulaAnterior,null) != 1) )    // Equivalence ==
                    )
                {
                    throw new ClassCastException("Error");
                }

                if (sideOrTransitive){
                    solucion = new Solucion(resuelveAnterior, false, null, newMethod, crudOp);
                }
                else{
                    // Arguments: 1) associated Resuelve object, 2) if it is solved, 3) binary tree of the proof, 4) demonstration method, 5) CrudOperations object
                    solucion = new Solucion(resuelveAnterior, false, formulaTerm, newMethod, crudOp);
                    typedTerm = formulaTerm;
                    solucionManager.addSolucion(solucion); // This adds a new row to the "solucion" table
                    response.setnSol(solucion.getId()+""); // The concatenation with "" converts the id to a string
                }

                methodTerm = new Const(newMethod);        
            }
            else{
                // When the proof already exists in the DB, we obtain the solution from it.
                solucion = solucionManager.getSolucion(Integer.parseInt(nSol),username);     
                methodTerm = crudOp.updateMethod(solucion.getMetodo(), newMethod);
                if ( ("CR".equals(newMethod) && ((opId=crudOp.binaryOperatorId(formulaAnterior,methodTerm)) != 2) && (opId !=3) ) || // Right arrow ==> or left arrow <==
                     ("AI".equals(newMethod) && (crudOp.binaryOperatorId(formulaAnterior,methodTerm) != 5) ) || // Conjunction /\
                     ("MI".equals(newMethod) && (crudOp.binaryOperatorId(formulaAnterior,methodTerm) != 1) )    // Equivalence ==
                    )
                {
                    throw new ClassCastException("Error");
                }

                // We save in the database the concatenation of the previous list of methods with the new one
                solucion.setMetodo(methodTerm.toStringFinal());

                if (sideOrTransitive){
                    formulaTerm = crudOp.initStatement(formulaTerm,methodTerm);
                }
                else{
                    if ("DM".equals(newMethod)){
                        if (teoid.substring(3,teoid.length()).equals(nTeo)) {
                           formulaTerm = crudOp.initStatement(formulaTerm, methodTerm);
                        }
                        typedTerm = crudOp.addFirstLineSubProof(username,formulaTerm, solucion.getTypedTerm(), methodTerm);
                        solucion.setTypedTerm(typedTerm);
                    }
                    solucionManager.updateSolucion(solucion);       
                }
                if (isRecursive){ // If we don't put this, we will get "typedTerm" as null in a branched sub-proof
                    typedTerm = solucion.getTypedTerm();
                }
            }

            if (sideOrTransitive){
                // CAUTION: The controller sets the String null parameters as ""
                if ("".equals(lado)){ // This does not occur in Starting from one side method, so we are in a transitive one
                    opId = crudOp.binaryOperatorId(formulaTerm,null);

                    switch (opId){
                        case 2: // Right arrow ==> 
                            lado = "TR".equals(newMethod) ? null : ("WE".equals(newMethod) ? "i" : "d");
                            break;
                        case 3: // Left arrow <==
                            lado = "TR".equals(newMethod) ? null : ("WE".equals(newMethod) ? "d" : "i");
                            break;
                        default:
                            // **** For the "TR" method we should check first if the operator is transitive
                            // **** Also, the user should be able to start from whichever side they want
                            lado = "TR".equals(newMethod) ? "i" : null;
                            break;
                    }
                }
                if (!"".equals(lado)){ // THIS IS NOT AN ELSE, BECAUSE "lado" MAY HAVE CHANGED IN THE PREVIOUS BLOCK
                    response.setLado(lado);
                    formulaTerm = lado.equals("i") ? ((App)formulaTerm).q : ((App)((App)formulaTerm).p).q;  
                } 
                else {
                    throw new ClassCastException("Error");
                }

                if (nSol.equals("new")) {
                    typedTerm = formulaTerm;
                    solucion.setTypedTerm(formulaTerm);
                    solucionManager.addSolucion(solucion);
                    response.setnSol(solucion.getId()+"");
                }
                else {
                    typedTerm = crudOp.addFirstLineSubProof(username,formulaTerm, solucion.getTypedTerm(), methodTerm);
                    solucion.setTypedTerm(typedTerm);
                    solucionManager.updateSolucion(solucion);
                }       
            }

            // This occurs because the first line of the proof is printed inmediately after selecting the following methods
            if ("WE".equals(newMethod) || "ST".equals(newMethod)){
                solucionManager.addSolucion(solucion);
            }

        } catch (ClassCastException e) {
            response.setErrorParser1(true);
            return response;
        }
        
        response.generarHistorial(
            username, // user
            formulaAnterior, // formula
            nTeo, 
            typedTerm,
            true, // valida
            !isRecursive, // labeled
            methodTerm,
            resuelveManager,
            disponeManager,
            simboloManager 
        );

        // In the recursive case, the user still needs to choose another proof method for the sub-proof
        response.setCambiarMetodo(isRecursive ? "2" : "0");

        return response;
    }
    
}