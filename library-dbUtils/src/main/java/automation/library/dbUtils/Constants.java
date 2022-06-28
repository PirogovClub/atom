package automation.library.dbUtils;

public class Constants  {
    public static final String BASEPATH = System.getProperty("user.dir") + "/src/test/resources/";
    public static final String ENVIRONMENTPATH = BASEPATH + "config/environments/";
    public static final String DB_UTILS_PROPERTIES = BASEPATH + "config/"+System.getProperty("atom.db.utils.property.file");

    public static final String NULL_TO_STRING_VALUE = "";
}
