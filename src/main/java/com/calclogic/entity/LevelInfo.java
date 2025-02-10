package com.calclogic.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import com.calclogic.types.Level;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

/**
 * @author alejandro
 */
@Entity
public class LevelInfo implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "level_id_seq")
  @SequenceGenerator(name = "level_id_seq", sequenceName = "level_id_seq", allocationSize = 50)
  private int id;

  @Enumerated(EnumType.STRING)
  private Level level;

  @OneToMany(mappedBy = "levenInfo", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Teorema> theorems;

  public LevelInfo() {}

  public LevelInfo(Level level, Set<Teorema> theorems) {
    this.level = level;
    this.theorems = theorems;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public Set<Teorema> getTheorems() {
    return theorems;
  }

  public void setTheorems(Set<Teorema> theorems) {
    this.theorems = theorems;
  }
}
