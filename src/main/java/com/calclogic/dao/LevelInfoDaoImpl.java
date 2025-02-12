package com.calclogic.dao;

import com.calclogic.entity.LevelInfo;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class has the implementation of the database queries that have to do with the table
 * "LevelInfo".
 *
 * @author alejandro
 */
public class LevelInfoDaoImpl implements LevelInfoDAO {

  @Autowired private SessionFactory sessionFactory;

  /**
   * Adds a new subject to the table. This query is made using standard Hibernate library functions.
   *
   * @param level The new level object to be added.
   */
  @Override
  public void addLevel(LevelInfo level) {
    this.sessionFactory.getCurrentSession().save(level);
  }

  /**
   * Deletes one of the subjects of the table. This query is made using standard Hibernate library
   * functions.
   *
   * @param id Is the principal key of the subject to delete.
   */
  @Override
  public void deleteLevel(int id) {
    LevelInfo level = (LevelInfo) sessionFactory.getCurrentSession().load(LevelInfo.class, id);
    if (level != null) {
      this.sessionFactory.getCurrentSession().delete(level);
    }
  }

  /**
   * Method to get a subject by its principal key. This query is made using standard Hibernate
   * library functions.
   *
   * @param id Is the principal key of the subject.
   */
  @Override
  public LevelInfo getLevel(int id) {
    return (LevelInfo) this.sessionFactory.getCurrentSession().get(LevelInfo.class, id);
  }

  /**
   * Method to get a list of all the entries of the table (all the subjects). This query is made
   * using HQL (Hibernate Query Language).
   */
  @Override
  public List<LevelInfo> getAllLevels() {
    return this.sessionFactory
        .getCurrentSession()
        .createQuery("FROM LevelInfo l", LevelInfo.class)
        .list();
  }
}
