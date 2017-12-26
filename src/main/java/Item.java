import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Item {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty url = new SimpleStringProperty();
    private StringProperty location = new SimpleStringProperty();
    private StringProperty title = new SimpleStringProperty("");
    private StringProperty customName = new SimpleStringProperty("");
    private IntegerProperty speedLimit = new SimpleIntegerProperty();
    private BooleanProperty shutdownAfterFinish = new SimpleBooleanProperty();
    private BooleanProperty isAddedToQueue = new SimpleBooleanProperty();
    private BooleanProperty isVideo = new SimpleBooleanProperty();
    private StringProperty format = new SimpleStringProperty();
    private IntegerProperty videoQuality = new SimpleIntegerProperty();
    private IntegerProperty audioQuality = new SimpleIntegerProperty();
    private StringProperty subtitleLanguage = new SimpleStringProperty();
    private BooleanProperty needEmbeddedSubtitle = new SimpleBooleanProperty();
    private BooleanProperty needAutoGeneratedSubtitle = new SimpleBooleanProperty();
    private BooleanProperty isPlaylist = new SimpleBooleanProperty();
    private IntegerProperty playlistStartIndex = new SimpleIntegerProperty();
    private IntegerProperty playlistEndIndex = new SimpleIntegerProperty();
    private StringProperty playlistItems = new SimpleStringProperty();
    private BooleanProperty needAllPlaylistItems = new SimpleBooleanProperty();
    private StringProperty status = new SimpleStringProperty();
    private DoubleProperty done = new SimpleDoubleProperty();
    private StringProperty size = new SimpleStringProperty();
    private StringProperty speed = new SimpleStringProperty();
    private StringProperty eta = new SimpleStringProperty();
    private ObservableList<String> logList = FXCollections.observableArrayList();
    private Process ytdlProcess = null;



    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getCustomName() {
        return customName.get();
    }

    public StringProperty customNameProperty() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName.set(customName);
    }

    public int getSpeedLimit() {
        return speedLimit.get();
    }

    public IntegerProperty speedLimitProperty() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit.set(speedLimit);
    }

    public boolean getShutdownAfterFinish() {
        return shutdownAfterFinish.get();
    }

    public BooleanProperty shutdownAfterFinishProperty() {
        return shutdownAfterFinish;
    }

    public void setShutdownAfterFinish(boolean shutdownAfterFinish) {
        this.shutdownAfterFinish.set(shutdownAfterFinish);
    }

    public boolean getIsAddedToQueue() {
        return isAddedToQueue.get();
    }

    public BooleanProperty isAddedToQueueProperty() {
        return isAddedToQueue;
    }

    public void setIsAddedToQueue(boolean isAddedToQueue) {
        this.isAddedToQueue.set(isAddedToQueue);
    }

    public boolean getIsVideo() {
        return isVideo.get();
    }

    public BooleanProperty isVideoProperty() {
        return isVideo;
    }

    public void setIsVideo(boolean isVideo) {
        this.isVideo.set(isVideo);
    }

    public String getFormat() {
        return format.get();
    }

    public StringProperty formatProperty() {
        return format;
    }

    public void setFormat(String format) {
        this.format.set(format);
    }

    public int getVideoQuality() {
        return videoQuality.get();
    }

    public IntegerProperty videoQualityProperty() {
        return videoQuality;
    }

    public void setVideoQuality(int videoQuality) {
        this.videoQuality.set(videoQuality);
    }

    public int getAudioQuality() {
        return audioQuality.get();
    }

    public IntegerProperty audioQualityProperty() {
        return audioQuality;
    }

    public void setAudioQuality(int audioQuality) {
        this.audioQuality.set(audioQuality);
    }

    public String getSubtitleLanguage() {
        return subtitleLanguage.get();
    }

    public StringProperty subtitleLanguageProperty() {
        return subtitleLanguage;
    }

    public void setSubtitleLanguage(String subtitleLanguage) {
        this.subtitleLanguage.set(subtitleLanguage);
    }

    public boolean getNeedEmbeddedSubtitle() {
        return needEmbeddedSubtitle.get();
    }

    public BooleanProperty needEmbeddedSubtitleProperty() {
        return needEmbeddedSubtitle;
    }

    public void setNeedEmbeddedSubtitle(boolean needEmbeddedSubtitle) {
        this.needEmbeddedSubtitle.set(needEmbeddedSubtitle);
    }

    public boolean getNeedAutoGeneratedSubtitle() {
        return needAutoGeneratedSubtitle.get();
    }

    public BooleanProperty needAutoGeneratedSubtitleProperty() {
        return needAutoGeneratedSubtitle;
    }

    public void setNeedAutoGeneratedSubtitle(boolean needAutoGeneratedSubtitle) {
        this.needAutoGeneratedSubtitle.set(needAutoGeneratedSubtitle);
    }

    public boolean getIsPlaylist() {
        return isPlaylist.get();
    }

    public BooleanProperty isPlaylistProperty() {
        return isPlaylist;
    }

    public void setIsPlaylist(boolean isPlaylist) {
        this.isPlaylist.set(isPlaylist);
    }

    public int getPlaylistStartIndex() {
        return playlistStartIndex.get();
    }

    public IntegerProperty playlistStartIndexProperty() {
        return playlistStartIndex;
    }

    public void setPlaylistStartIndex(int playlistStartIndex) {
        this.playlistStartIndex.set(playlistStartIndex);
    }

    public int getPlaylistEndIndex() {
        return playlistEndIndex.get();
    }

    public IntegerProperty playlistEndIndexProperty() {
        return playlistEndIndex;
    }

    public void setPlaylistEndIndex(int playlistEndIndex) {
        this.playlistEndIndex.set(playlistEndIndex);
    }

    public String getPlaylistItems() {
        return playlistItems.get();
    }

    public StringProperty playlistItemsProperty() {
        return playlistItems;
    }

    public void setPlaylistItems(String playlistItems) {
        this.playlistItems.set(playlistItems);
    }

    public boolean getNeedAllPlaylistItems() {
        return needAllPlaylistItems.get();
    }

    public BooleanProperty needAllPlaylistItemsProperty() {
        return needAllPlaylistItems;
    }

    public void setNeedAllPlaylistItems(boolean needAllPlaylistItems) {
        this.needAllPlaylistItems.set(needAllPlaylistItems);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public double getDone() {
        return done.get();
    }

    public DoubleProperty doneProperty() {
        return done;
    }

    public void setDone(double done) {
        this.done.set(done);
    }

    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public String getSpeed() {
        return speed.get();
    }

    public StringProperty speedProperty() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed.set(speed);
    }

    public String getEta() {
        return eta.get();
    }

    public StringProperty etaProperty() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta.set(eta);
    }

    public ObservableList<String> getLogList() {
        return logList;
    }

    public void setLogList(ObservableList<String> logList) {
        this.logList = logList;
    }

    public Process getYtdlProcess() {
        return ytdlProcess;
    }

    public void setYtdlProcess(Process ytdlProcess) {
        this.ytdlProcess = ytdlProcess;
    }



    public void startDownload() {

        Task<Void> downloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                List<String> cmd = commandBuilder();
                System.out.println(cmd.toString().replace(",", ""));
                ytdlProcess = new ProcessBuilder(cmd).start();
                setStatus("Starting");

                InputStream inputStream = ytdlProcess.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String downloadRegex = "\\[download\\]\\s*(\\d+\\.\\d+)%\\s*of\\s*(~?\\d+\\.\\d+[MKG]?i?B)\\s*at\\s*(\\d+\\.\\d+[MKG]?i?B/s)\\s*ETA\\s*(.*)";
                String fileFinishRegex = "\\[download\\]\\s*100%\\s*of\\s*(~?\\d+\\.\\d+[MKG]?i?B).*";

                Pattern downloadPattern = Pattern.compile(downloadRegex);
                Pattern fileFinishPattern = Pattern.compile(fileFinishRegex);
                String buff;


                while ((buff = bufferedReader.readLine()) != null &&
                        (getStatus().equals("Starting") || getStatus().equals("Running"))) {

                    final String line = buff;
                    final Matcher downloadMatcher = downloadPattern.matcher(line);
                    final Matcher fileFinishMatcher = fileFinishPattern.matcher(line);

                    Platform.runLater( () -> {

                        //parsing download status info
                        if (downloadMatcher.find()) {
                            setStatus("Running");

                            //combine the download messages in one line
                            if (logList.size() > 0 && logList.get(logList.size() - 1).matches(downloadRegex)) {
                                logList.remove(logList.size() - 1);
                                logList.add(line);
                            } else
                                logList.add(line);

                            String x = downloadMatcher.group(1);
                            setDone(Double.parseDouble(x));
                            DatabaseManager.updateDouble(getThisItem(), "done", getDone());  //update done percent in database
                            x = downloadMatcher.group(2);
                            setSize(x);
                            DatabaseManager.updateString(getThisItem(), "size", getSize());
                            x = downloadMatcher.group(3);
                            setSpeed(x);
                            x = downloadMatcher.group(4);
                            setEta(x);

                        } else if (!line.equals("")) {

                            logList.add(line);

                            if (getIsPlaylist()) {
                                //parse the title of download playlist item and add it to the database
                                if (line.matches("\\[download\\]\\s*Downloading playlist:\\s*.+")) {
                                    String title = line.split(":\\s+")[1];
                                    setTitle(title);
                                    DatabaseManager.updateString(getThisItem(), "title", title);
                                //Check If download is completed and set Finished status
                                } else if (line.matches("\\[download\\]\\s*Finished\\s*downloading\\s*playlist:.*")) {
                                    setDone(100);
                                    setSize("");
                                    finishDownload();
                                }
                            } else {
                                //parse the title of download item and add it to the database
                                if (line.matches("\\[download\\]\\s*(Destination:\\s*)?" + getLocation() + "[/\\\\]?.+")) {
                                    String title = line.split(getLocation() + "[/\\\\]?")[1].split("\\s*has already been downloaded")[0];
                                    setTitle(title);
                                    DatabaseManager.updateString(getThisItem(), "title", title);
                                //Check If download is completed and set Finished status
                                } else if(!getIsPlaylist() && fileFinishMatcher.find()) {
                                    setDone(100);
                                    String x = fileFinishMatcher.group(1);
                                    setSize(x);
                                    DatabaseManager.updateString(getThisItem(), "size", getSize());
                                    finishDownload();
                                }
                            }

                        } else {
                            // Do Nothing!
                        }

                    });
                }

                return null;
            }

        };

        Thread backgroundThread = new Thread(downloadTask);
        backgroundThread.start();

    }

    public void stopDownload() {
        setStatus("Stopped");
        if(ytdlProcess != null)
            ytdlProcess.destroy();
        setSpeed("");
        setEta("");
    }

    public void finishDownload() {

        setStatus("Finished");
        ytdlProcess.destroy();
        setSpeed("");
        setEta("");

        if(getShutdownAfterFinish()) {
            shutdownAfter(30);
        }

        if(getIsAddedToQueue()) {
            Item nextQueueItem = getNextQueueItemTo(this);
            while(nextQueueItem != null && (nextQueueItem.getStatus().equals("Stopped") || nextQueueItem.getStatus().equals("Finished")))
                nextQueueItem = getNextQueueItemTo(this);
            if(nextQueueItem != null && nextQueueItem.getStatus().equals("Waiting"))
                nextQueueItem.startDownload();
        }
    }


    private List<String> commandBuilder() {

        List<String> cmdList = new ArrayList<>(Arrays.asList("python", "youtube-dl", "-i", "-c", "--no-part"));

        if (getSpeedLimit() != 0) {
            cmdList.add("-r");
            cmdList.add(String.valueOf(getSpeedLimit() + "K"));
        }

        if (audioQuality.getValue() == 0 && videoQuality.getValue() != 0) {
            cmdList.add("-f");
            cmdList.add(videoQuality.getValue().toString());
        } else if (audioQuality.getValue() != 0 && videoQuality.getValue() != 0) {
            cmdList.add("-f");
            cmdList.add(videoQuality.getValue().toString() + "+" + audioQuality.getValue().toString());
        } else if (audioQuality.getValue() != 0 && videoQuality.getValue() == 0) {
            cmdList.add("-f");
            cmdList.add(audioQuality.getValue().toString());
        }

        if (getIsVideo()) {
            cmdList.add("--merge-output-format");
            cmdList.add("mp4");
        }

        if (needEmbeddedSubtitle.getValue()) {
            cmdList.add("--write-sub");
            cmdList.add("--sub-lang");
            cmdList.add(subtitleLanguage.getValue());
        }

        if (needAutoGeneratedSubtitle.getValue())
            cmdList.add("--write-auto-sub");

        if (isPlaylist.getValue()) {
            cmdList.add("--yes-playlist");
            if (customName.get().equals("")) {
                cmdList.add("-o");
                cmdList.add(location.getValue() + "/%(playlist_title)s/%(playlist_index)s - %(title)s.%(ext)s");
            } else {
                cmdList.add("-o");
                cmdList.add(location.getValue() + "/" + customName.getValue() + "/%(playlist_index)s - %(title)s.%(ext)s");
            }
        } else {
            cmdList.add("--no-playlist");
            if (customName.get().equals("")) {
                cmdList.add("-o");
                cmdList.add(location.getValue() + "/" + "%(title)s.%(ext)s");
            } else {
                cmdList.add("-o");
                cmdList.add(location.getValue() + "/" + customName.getValue() + ".%(ext)s");
            }
        }

        cmdList.add(url.getValue());

        return cmdList;
    }

    private Item getThisItem() {
        return this;
    }

    private void shutdownAfter(int seconds) {

        Timer timer = new Timer();
        Platform.runLater(() -> timer.schedule(new TimerTask() {

            @Override
            public void run() {

                Main.saveAndExit();
                try {

                    String os = System.getProperty("os.name").toLowerCase();

                    if (os.startsWith("win"))
                        Runtime.getRuntime().exec("shutdown.exe -s -t 0");
                    else
                        Runtime.getRuntime().exec("shutdown -h now");

                } catch(Exception e) {

                    new MessageDialog("Error executing shutdown command!\n" +
                            "Restart program and try again.", MessageDialog.Type.ERROR,
                            MessageDialog.Buttons.CLOSE).createErrorDialog(e.getStackTrace()).show();

                }
                System.exit(0);
            }

        }, seconds * 1000));

        MessageDialog messageDialog = new MessageDialog("Attention, Computer will shutdown after " +
                seconds + " seconds !\nSave your work, or click cancel to stop.", MessageDialog.Type.INFO, MessageDialog.Buttons.OK_AND_CANCEL);
        messageDialog.getOkButton().setOnAction(actionEvent -> messageDialog.close());
        messageDialog.getCancelButton().setOnAction(actionEvent -> {
            timer.cancel();
            messageDialog.close();
        });
        messageDialog.show();

    }

    private Item getNextQueueItemTo(Item currentItem) {

        int itemIndex = HomeController.getQueueItemList().indexOf(currentItem);
        int queueItemListSize = HomeController.getQueueItemList().size();
        boolean thereIsAnotherItem = itemIndex != queueItemListSize - 1;

        return thereIsAnotherItem? HomeController.getQueueItemList().get(itemIndex + 1) : null;
    }

}
