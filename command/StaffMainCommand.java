package command;

public class StaffMainCommand implements ICommand {
  private final Runnable openImportCmd;

  public StaffMainCommand(Runnable openImportCmd) {
    this.openImportCmd = openImportCmd;
  }

  @Override
  public void execute() {
    if (openImportCmd != null) {
      openImportCmd.run();
    }
  }
}