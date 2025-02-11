package com.calclogic.dao;

import com.calclogic.entity.LevelInfo;
import java.util.List;

/**
 * @author alejandro
 */
public interface LevelInfoDAO {
      /** 
     * Adds a new subject to the table.
     * @param level The new level object to be added.
     */
    public void addLevel(LevelInfo level);

    /**
     * Deletes one of the subjects of the table.
     * @param id Is the principal key of the subject to delete.
     */ 
    public void deleteLevel(int id);
    
    /**
     * Method to get a subject by its principal key.
     * @param id Is the principal key of the subject.
     * @return level 
     */
    public LevelInfo getLevel(int id);
    
    /**
     * Method to get a list of all the entries of the table (all the subjects).
     * @return levels list 
     */
    public List<LevelInfo> getAllLevels();
}
