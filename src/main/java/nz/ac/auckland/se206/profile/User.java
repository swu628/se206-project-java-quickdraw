package nz.ac.auckland.se206.profile;

public class User {
  private String username;

  public User(String username) {
    this.username = username;
  }

  public String getName() {
    return username;
  }

  public void setName(String username) {
    this.username = username;
  }
}
