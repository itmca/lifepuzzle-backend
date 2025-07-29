package io.itmca.lifepuzzle.testsupport;

import com.github.database.rider.core.replacers.Replacer;
import org.dbunit.dataset.ReplacementDataSet;

public class BooleanToTinyintReplacer implements Replacer {

  @Override
  public void addReplacements(ReplacementDataSet dataSet) {
    dataSet.addReplacementObject(true, 1);
    dataSet.addReplacementObject(false, 0);
  }
}
