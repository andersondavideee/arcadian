package org.steelthread.arcadian.domain.logical;

public enum RoleName {

  ADMIN("Admin"), STANDARD_ACCOUNT("Standard Account"), PREMIUM_ACCOUNT("Premium Account");

  private String description;

  private RoleName(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}