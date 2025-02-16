package com.calclogic.forms;

import com.calclogic.types.LevelType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author alejandro
 */
public class AddLevelForm {

  @Enumerated(EnumType.STRING)
  private LevelType levelType;

  // Constructor
  public AddLevelForm() {}

  public AddLevelForm(LevelType level) {
    this.levelType = level;
  }

  // Getters and setters

  public LevelType getLevelType() {
    return levelType;
  }

  public void setLevelType(LevelType levelType) {
    this.levelType = levelType;
  }
}
