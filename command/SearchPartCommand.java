package command;

import java.util.ArrayList;

import db.PartDB;
import model.Part;

public class SearchPartCommand implements ICommand {
  private final String keyWord;
  private ArrayList<Part> resultList = new ArrayList<>();
  private final PartDB receiver = new PartDB();

  public SearchPartCommand(String keyWord) {
    this.keyWord = keyWord;
  }

  @Override
  public void execute() {
    resultList = receiver.searchPart(keyWord);
  }

  public ArrayList<Part> getResultList() {
    return resultList;
  }
}