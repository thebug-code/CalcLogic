package com.calclogic.entity;

// Generated Mar 20, 2017 12:50:11 PM by Hibernate Tools 3.2.1.GA

import javax.persistence.*;

/** Employee generated by hbm2java */
@Entity
public class Employee implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_id_seq")
  @SequenceGenerator(
      name = "employee_id_seq",
      sequenceName = "employee_id_seq",
      allocationSize = 1)
  private int id;

  private String email;
  private String firstname;
  private String lastname;
  private String telephone;

  public Employee() {}

  public Employee(int id) {
    this.id = id;
  }

  public Employee(int id, String email, String firstname, String lastname, String telephone) {
    this.id = id;
    this.email = email;
    this.firstname = firstname;
    this.lastname = lastname;
    this.telephone = telephone;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstname() {
    return this.firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return this.lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getTelephone() {
    return this.telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }
}
