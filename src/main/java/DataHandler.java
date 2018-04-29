import com.google.gson.Gson;
import javafx.application.Platform;
import org.hildan.fxgson.FxGson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;


public class DataHandler {

    private static Gson gson = FxGson.coreBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private static String appDataDirectory;
    private static HashMap<String, Object> appPreferences ;
    static {

        // set nazel AppData directory path based on the user OS
        if(System.getProperty("os.name").toLowerCase().contains("win"))
            appDataDirectory = System.getenv("AppData") + System.getProperty("file.separator") + "nazel";
        else if(System.getProperty("os.name").toLowerCase().contains("mac"))
            appDataDirectory = System.getProperty("user.home") + System.getProperty("file.separator") + "Library" +
                    System.getProperty("file.separator") + "Preferences" + System.getProperty("file.separator") + "nazel";
        else
            appDataDirectory = System.getProperty("user.home") + System.getProperty("file.separator") + ".nazel";

        // Fill the appPreferences with default values
        appPreferences = new HashMap<>();
        appPreferences.put("Main.width", 800d);             //Double
        appPreferences.put("Main.height", 500d);            //Double
        appPreferences.put("Home.hideLog", Boolean.FALSE);     //Boolean
        appPreferences.put("Home.dividerPosition", 0.8d);   //Double
        appPreferences.put("Data.nextID", 0L);              //Long
        appPreferences.put("AES.date", new Date().toString());  //String
        appPreferences.put("AES.nanoTime", System.nanoTime());   //Long

    }
    private static final String DATA_DIRECTORY = appDataDirectory + System.getProperty("file.separator") + "data";
    private static final String CONFIG_FILE = appDataDirectory + System.getProperty("file.separator") + "config.dat";


    public static String getAppDataDirectory() {
        return appDataDirectory;
    }

    public static HashMap<String, Object> getAppPreferences() {
        return appPreferences;
    }

    public static void readAppPreferences() {

        File configFile = new File(CONFIG_FILE);

        if(configFile.exists()) {

            try {
                FileInputStream fileInputStream = new FileInputStream(configFile);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

                if(objectInputStream.readUTF().equals("_NVD")) {

                    appPreferences.replace("Main.width", objectInputStream.readDouble());
                    appPreferences.replace("Main.height", objectInputStream.readDouble());
                    appPreferences.replace("Home.hideLog", objectInputStream.readBoolean());
                    appPreferences.replace("Home.dividerPosition", objectInputStream.readDouble());
                    appPreferences.replace("Data.nextID", objectInputStream.readLong());
                    appPreferences.replace("AES.date", objectInputStream.readUTF());
                    appPreferences.replace("AES.nanoTime", objectInputStream.readLong());

                } else {
                    throw new Exception("configuration file is corrupted");
                }

                objectInputStream.close();
                bufferedInputStream.close();
                fileInputStream.close();

            } catch (Exception e) {
                Platform.runLater(() -> new MessageDialog("Error loading configuration file\n" +
                        "Try again later or report this issue", MessageDialog.Type.ERROR,
                        MessageDialog.Buttons.CLOSE).createErrorDialog(e.getStackTrace()).showAndWait());

            }

        }

    }

    public  static void writeAppPreferences() {

        File configFile = new File(CONFIG_FILE);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(configFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

            objectOutputStream.writeUTF("_NVD");

            objectOutputStream.writeDouble( (Double) appPreferences.get("Main.width"));
            objectOutputStream.writeDouble( (Double) appPreferences.get("Main.height"));
            objectOutputStream.writeBoolean( (Boolean) appPreferences.get("Home.hideLog"));
            objectOutputStream.writeDouble( (Double) appPreferences.get("Home.dividerPosition"));
            objectOutputStream.writeLong( (Long) appPreferences.get("Data.nextID"));
            objectOutputStream.writeUTF( (String) appPreferences.get("AES.date"));
            objectOutputStream.writeLong( (Long) appPreferences.get("AES.nanoTime"));

            objectOutputStream.close();
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            fileOutputStream.close();

        } catch (Exception e) {
            Platform.runLater(() -> new MessageDialog("Error writing the configuration file\n" +
                    "Try again later or report this issue", MessageDialog.Type.ERROR,
                    MessageDialog.Buttons.CLOSE).createErrorDialog(e.getStackTrace()).showAndWait());
        }

    }

    public static void loadSavedItems() {

        try {

            if(Files.notExists(Paths.get(DATA_DIRECTORY)) || (! Files.isDirectory(Paths.get(DATA_DIRECTORY))))
                Files.createDirectory(Paths.get(DATA_DIRECTORY));
            File[] dataFiles = new File(DATA_DIRECTORY).listFiles();

            if(dataFiles != null) {

                for(File file : dataFiles) {
                    if(file.getName().endsWith(".json")) {

                        Item item = read(file);

                        if(item != null) {

                            if (item.getDone() == 100.0 && item.getStatus().equals("Finished"))
                                item.setStatus("Finished");
                            else
                                item.setStatus("Stopped");

                            if (item.getIsAddedToQueue())
                                HomeController.getQueueItemList().add(item);
                            else
                                HomeController.getItemList().add(item);

                        }

                    }
                }
            }

        } catch (Exception e) {
            new MessageDialog("Error loading download items! \n" +
                    "Try again later or report this issue", MessageDialog.Type.ERROR,
                    MessageDialog.Buttons.CLOSE).createErrorDialog(e.getStackTrace()).showAndWait();
        }

    }

    public static Item read(File jsonFile) throws Exception {

        FileReader fileReader = new FileReader(jsonFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return gson.fromJson(bufferedReader, Item.class);

    }

    public static void save(Item item) {

        try {

            if(Files.notExists(Paths.get(DATA_DIRECTORY)) || (! Files.isDirectory(Paths.get(DATA_DIRECTORY))))
                Files.createDirectory(Paths.get(DATA_DIRECTORY));

            Path path = Paths.get(DATA_DIRECTORY + System.getProperty("file.separator") + item.getId() + ".json");
            Files.write(path, gson.toJson(item).getBytes());

        } catch (IOException e) {
            e.printStackTrace();
            new MessageDialog("Error saving item data! \n" +
                    "Try again later or report this issue", MessageDialog.Type.ERROR,
                    MessageDialog.Buttons.CLOSE).createErrorDialog(e.getMessage()).showAndWait();
        }

    }

    public static void delete(Item item) {

        try {

            String path = DATA_DIRECTORY + System.getProperty("file.separator") + item.getId() + ".json";
            File file = new File(path);
            if(! file.delete())
                throw new Exception("file" + item.getId() + ".json could not be deleted");

        } catch (Exception e) {
            new MessageDialog("Error deleting item from database! \n" +
                    "Try again later or report this issue", MessageDialog.Type.ERROR,
                    MessageDialog.Buttons.CLOSE).createErrorDialog(e.getMessage()).showAndWait();
        }

    }

    public static long getNextId() {

        Long nextID = (Long) getAppPreferences().get("Data.nextID");
        getAppPreferences().replace("Data.nextID", nextID + 1);
        writeAppPreferences();
        return nextID;

    }

}
