package com.calclogic.service;

import com.calclogic.dao.LevelInfoDAO;
import com.calclogic.entity.LevelInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class has the implementation of "LevelInfoManager" queries.
 *
 * @author alejandro
 */
public class LevelInfoManagerImpl implements LevelInfoManager {

  @Autowired LevelInfoDAO levelInfoDAO;

  /**
   * Adds a new subject to the table.
   *
   * @param level The new level object to be added.
   */
  @Override
  @Transactional
  public void addLevel(LevelInfo level) {
    levelInfoDAO.addLevel(level);
  }

  /**
   * Deletes one of the subjects of the table.
   *
   * @param id Is the principal key of the subject to delete.
   */
  @Override
  @Transactional
  public void deleteLevel(int id) {
    levelInfoDAO.deleteLevel(id);
  }

  /**
   * Method to get a subject by its principal key.
   *
   * @param id Is the principal key of the subject.
   */
  @Override
  @Transactional
  public LevelInfo getLevel(int id) {
    return levelInfoDAO.getLevel(id);
  }

  /** Method to get a list of all the entries of the table (all the subjects). */
  @Override
  @Transactional
  public List<LevelInfo> getAllLevels() {
    return levelInfoDAO.getAllLevels();
  }
}
