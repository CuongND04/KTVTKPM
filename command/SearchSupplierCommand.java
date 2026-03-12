package command;

import java.util.ArrayList;

import db.SupplierDB;
import model.Supplier;

public class SearchSupplierCommand implements ICommand {
  private final String keyWord;
  private ArrayList<Supplier> resultList = new ArrayList<>();
  private final SupplierDB receiver = new SupplierDB();

  public SearchSupplierCommand(String keyWord) {
    this.keyWord = keyWord;
  }

  @Override
  public void execute() {
    resultList = receiver.searchSupplier(keyWord);
  }

  public ArrayList<Supplier> getResultList() {
    return resultList;
  }
}