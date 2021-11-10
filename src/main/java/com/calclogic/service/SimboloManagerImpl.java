package com.calclogic.service;

import com.calclogic.dao.SimboloDAO;
import com.calclogic.entity.Simbolo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class has the implementation of "SimboloManager" queries.
 *
 * @author jt
 */
@Service
public class SimboloManagerImpl implements SimboloManager {
       
    // @Autowired
    private SimboloDAO simboloDAO;
    private int propFunApp;
    private int termFunApp;
    Simbolo[] symbolsCache; 
    
    @Autowired
    public SimboloManagerImpl(SimboloDAO simboloDAO) {
        this.simboloDAO = simboloDAO;
        List<Simbolo> l = simboloDAO.getAllSimbolo();
        symbolsCache = new Simbolo[l.size()];
        for (int i=0; i < l.size(); i++)
            symbolsCache[i] = l.get(i);
    }
    
    public int getPropFunApp() {
        return propFunApp;
    }
    
    public int getTermFunApp() {
        return termFunApp;
    }
    
    public String propFunAppSym() {
        return "c_{"+ propFunApp +"}";
    }
    
    public String termFunAppSym() {
        return "c_{"+ termFunApp +"}";
    }
	
    public void setPropFunApp(int propFunApp) {
        this.propFunApp = propFunApp;
    }
    
    public void setTermFunApp(int termFunApp) {
        this.termFunApp = termFunApp;
    }
    
    /** 
     * Adds a new symbol (Simbolo object) to the table.
     * @param simbolo The new symbol to be added.
     * @return Nothing.
     */
    @Override
    @Transactional
    public Simbolo addSimbolo(Simbolo simbolo){
        simboloDAO.addSimbolo(simbolo);
        List<Simbolo> l = simboloDAO.getAllSimbolo();
        symbolsCache = new Simbolo[l.size()];
        for (int i=0; i < l.size(); i++)
            symbolsCache[i] = l.get(i);
        return simbolo;
    }
    
    /**
     * This method let us update the entry that corresponds to an already 
     * stored symbol. For example, to update the code that creates it.
     * @param simbolo Is the Simbolo object to be updated.
     * @return Nothing.
     */ 
    @Override   
    @Transactional
    public void updateSimbolo(Simbolo simbolo){
        int id = simbolo.getId()-1;
        if (0 < id && id <= symbolsCache.length) {
            symbolsCache[id] = simbolo;
        }
        simboloDAO.updateSimbolo(simbolo);
    }
    
    /**
     * Deletes one of the symbols of the table.
     * @param id Is the principal key of the symbol to delete.
     * @return Nothing.
     */ 
    @Override
    @Transactional
    public void deleteSimbolo(int id){
        simboloDAO.deleteSimbolo(id);
    }
    
    /**
     * Method to get a Simbolo object by its principal key.
     * @param id Is the principal key of the Simbolo object.
     */ 
    @Override
    @Transactional
    public Simbolo getSimbolo(int id){
        if (0 < id && id <= symbolsCache.length)
            return symbolsCache[id-1];
        else
            return null;
    }
    
    /**
     * Method to get a list of all the entries of the table.
     */
    @Override
    @Transactional
    public List<Simbolo> getAllSimbolo(){
        List<Simbolo> list = new ArrayList<Simbolo>();
        for (int i= 0; i < symbolsCache.length; i++)
//            if (i >= 9)
            list.add(symbolsCache[i]);
        return list;
    }
    
}
