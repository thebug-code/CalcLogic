package com.calclogic.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 * @author alejandro
 */
@Entity
public class UserLevelProgress implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userLevelProgress_id_seq")
  @SequenceGenerator(
      name = "userLevelProgress_id_seq",
      sequenceName = "userLevelProgress_id_seq",
      allocationSize = 50)
  private int id;

  private int totalPoints;

  @ManyToOne
  @JoinColumn(name = "user_fk")
  private Usuario user;

  @ManyToOne
  @JoinColumn(name = "level_fk")
  private LevelInfo levelInfo;

  @OneToMany(mappedBy = "progress", cascade = CascadeType.ALL)
  private Set<Resuelve> resuelves;

  public UserLevelProgress() {}

  public UserLevelProgress(
      int totalPoints, Usuario user, LevelInfo levelInfo, Set<Resuelve> resuelves) {
    this.totalPoints = totalPoints;
    this.user = user;
    this.levelInfo = levelInfo;
    this.resuelves = resuelves;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTotalPoints() {
    return totalPoints;
  }

  public void setTotalPoints(int totalPoints) {
    this.totalPoints = totalPoints;
  }

  public Usuario getUser() {
    return user;
  }

  public void setUser(Usuario user) {
    this.user = user;
  }

  public LevelInfo getLevelInfo() {
    return levelInfo;
  }

  public void setLevelInfo(LevelInfo levelInfo) {
    this.levelInfo = levelInfo;
  }

  public Set<Resuelve> getResuelves() {
    return resuelves;
  }

  public void setResuelves(Set<Resuelve> resuelves) {
    this.resuelves = resuelves;
  }
}
