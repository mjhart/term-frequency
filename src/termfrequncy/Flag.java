package termfrequncy;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Flag {
  private static final Pattern FLAG_MATCHER = Pattern.compile("--([a-z]+)=.+");
  final String name;
  final Set<String> values;
  
  private Flag(String name, Set<String> values) {
    this.name = name;
    this.values = values;
  }

  public static Flag parse(String arg) {
    Matcher matcher = FLAG_MATCHER.matcher(arg);
    matcher.find();
    String name = matcher.group(1);
    Set<String> values = new HashSet<>();
    for (String value : arg.substring(name.length() + 3).split(",")) {
      values.add(value);
    }
    return new Flag(name, values);
  }
}
