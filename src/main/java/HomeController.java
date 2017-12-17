import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class HomeController implements Initializable {

    private static ObservableList<Item> itemList = FXCollections.observableArrayList();
    private static ObservableList<Item> queueItemList = FXCollections.observableArrayList();

    @FXML
    private BorderPane homeWindowPane;
    @FXML
    private SplitPane homeSplitPane;
    @FXML
    private ToggleButton queueBtn;
    @FXML
    private ListView<String> consoleListView;
    @FXML
    private TableView<Item> itemsTableView;
    @FXML
    private TableColumn<Item, Double> itemsProgressColumn;
    @FXML
    private TableColumn<Item, Boolean> itemsTypeColumn;
    @FXML
    private TableColumn<Item, String> itemsStatusColumn;
    // Table Progress bar cell class
    private class ProgressBarCell extends ProgressBarTableCell<Item> {

        StackPane stackPane = new StackPane();

        ProgressBar progressBar = new ProgressBar();
        Label label = new Label();
        ProgressBarCell() {
            progressBar.setMaxWidth(Double.MAX_VALUE);
            label.getStyleClass().add("progress-label");
            stackPane.getChildren().addAll(progressBar, label);
        }

        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty) {
                progressBar.setProgress(item);
                label.setText(BigDecimal.valueOf(item * 100d).setScale(1,BigDecimal.ROUND_HALF_UP).toString() + " %");
                setGraphic(stackPane);
            } else
                setGraphic(null);
        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // draw ImageView in the type column
        itemsTypeColumn.setCellFactory(param -> {

            final Image fileImage = new Image(getClass().getResource("theme/imgs/file.png").toString(), 16, 16, true, true);
            final Image playlistImage = new Image(getClass().getResource("theme/imgs/playlist.png").toString(), 16, 16, true, true);

            TableCell<Item, Boolean> cell = new TableCell<Item, Boolean>() {
                ImageView imageView = new ImageView();
                @Override
                protected void updateItem(Boolean isPlaylist, boolean empty) {
                    super.updateItem(isPlaylist, empty);
                    if(!empty) {
                        if(isPlaylist)
                            imageView.setImage(playlistImage);
                        else
                            imageView.setImage(fileImage);
                        setGraphic(imageView);
                    } else
                        setGraphic(null);
                }
            };

            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        // color status based on its value
        itemsStatusColumn.setCellFactory(param -> {

            return new TableCell<Item, String>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (!empty) {

                        setText(item);
                    } else {
                        setText(null);
                    }

                }

            };

        });

        // draw progress bar in the progress columns
        itemsProgressColumn.setCellFactory(param -> new ProgressBarCell());

        // context menu construction
        ContextMenu rowContextMenu = getRowContextMenu();

        // filling the table with download items
        itemsTableView.setItems(itemList);

        // Table row factory with context menu
        itemsTableView.setRowFactory(param -> {

            TableRow<Item> row = new TableRow<>();

            // change context menu queue option with selecting queueBtn
            queueBtn.selectedProperty().addListener((observable, mainMode, queueMode) -> {
                if (queueMode) {
                    rowContextMenu.getItems().get(3).setText("Remove from Queue");
                    rowContextMenu.getItems().get(3).setOnAction(event -> removeFromQueueMenuAction());
                } else {
                    rowContextMenu.getItems().get(3).setText("Add to Queue");
                    rowContextMenu.getItems().get(3).setOnAction(event -> addToQueueMenuAction());                }
            });

            // show context menu for not null rows only
            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty()))
                    .then(rowContextMenu)
                    .otherwise((ContextMenu) null));
            return row;
        });

        // listening for table row selection, to show the log and change status color of the selected item
        itemsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null)
                consoleListView.setItems(null);
            else
                consoleListView.setItems(newValue.getLogList());
        });

        // listening for queue ToggleButton state, to show Queue list or Main list
        queueBtn.selectedProperty().addListener((observableValue, wasSelected, nowSelected) -> {
            if(nowSelected) {
                itemsTableView.setItems(queueItemList);
            } else {
                itemsTableView.setItems(itemList);
            }
        });

    }

    private ContextMenu getRowContextMenu() {

        ContextMenu rowContextMenu = new ContextMenu();

        MenuItem startMenuItem = new MenuItem("Start");
        startMenuItem.setOnAction(event -> startBtnAction());
        startMenuItem.setGraphic(new ImageView(new Image(getClass().getResource("menu/start.png").toString())));

        MenuItem pauseMenuItem = new MenuItem("Pause");
        pauseMenuItem.setOnAction(event -> stopBtnAction());
        pauseMenuItem.setGraphic(new ImageView(new Image(getClass().getResource("menu/pause.png").toString())));

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> removeBtnAction());
        deleteMenuItem.setGraphic(new ImageView(new Image(getClass().getResource("menu/delete.png").toString())));

        MenuItem queueMenuItem = new MenuItem("Add to Queue");
        queueMenuItem.setOnAction(event -> addToQueueMenuAction());
        queueMenuItem.setGraphic(new ImageView(new Image(getClass().getResource("menu/queue.png").toString())));

        MenuItem clearMenuItem = new MenuItem("Clear Logs");
        clearMenuItem.setOnAction(event -> clearLogsMenuAction());
        clearMenuItem.setGraphic(new ImageView(new Image(getClass().getResource("menu/clear.png").toString())));

        MenuItem openFolderMenuItem = new MenuItem("Open Location");
        openFolderMenuItem.setOnAction(event -> openFolderMenuAction());
        openFolderMenuItem.setGraphic(new ImageView(new Image(getClass().getResource("menu/folder.png").toString())));

        MenuItem infoMenuItem = new MenuItem("Properties");
        infoMenuItem.setOnAction(event -> infoBtnAction());
        infoMenuItem.setGraphic(new ImageView(new Image(getClass().getResource("menu/details.png").toString())));

        rowContextMenu.getItems().addAll(startMenuItem, pauseMenuItem, deleteMenuItem,
                queueMenuItem, clearMenuItem, openFolderMenuItem, infoMenuItem);

        return rowContextMenu;
    }

    static List<Item> getItemList() {
        return itemList;
    }

    static List<Item> getQueueItemList() {
        return queueItemList;
    }

    public ToggleButton getQueueBtn() {
        return queueBtn;
    }

    private void addToQueueMenuAction() {

        Item selectedItem = itemsTableView.getSelectionModel().getSelectedItem();
        selectedItem.setAddToQueue(true);
        selectedItem.stopDownload();
        queueItemList.add(selectedItem);
        itemList.remove(selectedItem);

    }

    private void removeFromQueueMenuAction() {

        Item selectedItem = itemsTableView.getSelectionModel().getSelectedItem();
        selectedItem.setAddToQueue(false);
        itemList.add(selectedItem);
        queueItemList.remove(selectedItem);

    }

    private void clearLogsMenuAction() {

        itemsTableView.getSelectionModel().getSelectedItem().getLogList().clear();

    }

    private void openFolderMenuAction() {

        new Runnable() {

            @Override
            public void run() {

                try {

                    String location = itemsTableView.getSelectionModel().getSelectedItem().getLocation();
                    Desktop desktop = null;
                    File file = new File(location);
                    if (Desktop.isDesktopSupported())
                        desktop = Desktop.getDesktop();
                    if (desktop != null)
                        desktop.open(file);

                } catch (IOException e) {
                    System.err.println("Error Opening Location Folder");
                }

            }

        };

    }


    @FXML
    void addBtnAction() {

        try {

            FXMLLoader newDownloadWindowLoader = new FXMLLoader(getClass().getResource("windows/NewDownloadWindow.fxml"));
            newDownloadWindowLoader.load();
            ((NewDownloadController) newDownloadWindowLoader.getController()).setQueueBtnSelected(queueBtn.isSelected());
            Parent root = newDownloadWindowLoader.getRoot();
            homeWindowPane.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
            homeWindowPane.setOpacity(0.30);
            new MessageDialog("Error Loading NewDownload Window! \n" +
                    "Restart program and try again.", MessageDialog.Type.ERROR,
                    MessageDialog.Buttons.CLOSE).createErrorDialog(e.getStackTrace()).showAndWait();
            homeWindowPane.setOpacity(1);
        }

    }

    @FXML
    void startBtnAction() {

        Item selectedItem = itemsTableView.getSelectionModel().getSelectedItem();
        if( selectedItem != null && selectedItem.getStatus().equals("Stopped"))
            selectedItem.startDownload();

    }

    @FXML
    void stopBtnAction() {

        Item selectedItem = itemsTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            selectedItem.stopDownload();

    }

    @FXML
    void removeBtnAction() {

        Item selectedItem = itemsTableView.getSelectionModel().getSelectedItem();
        if( selectedItem != null) {

            MessageDialog deleteDialog = new MessageDialog("Are you sure you want to delete this item?\n" +
                    "You cannot undo this step", MessageDialog.Type.OPTION, MessageDialog.Buttons.OK_AND_CANCEL);
            CheckBox checkBox = new CheckBox("Delete files from the disk");
            checkBox.setTextFill(Color.CRIMSON);
            deleteDialog.addCheckBox(checkBox);
            deleteDialog.getNoButton().setOnAction(event -> deleteDialog.close());
            deleteDialog.getYesButton().setOnAction(event -> {
                if(checkBox.isSelected()) {

                    selectedItem.stopDownload();
                    boolean result;

                    if(selectedItem.getIsPlaylist()) {

                        if(System.getProperty("os.name").toLowerCase().contains("win"))
                            result = deleteItemFiles(selectedItem.getLocation() + "\\" +
                                    (selectedItem.getCustomName().equals("")? selectedItem.getTitle() : selectedItem.getCustomName()));
                        else
                            result = deleteItemFiles(selectedItem.getLocation() + "/" +
                                    (selectedItem.getCustomName().equals("")? selectedItem.getTitle() : selectedItem.getCustomName()));

                    } else {

                        if(System.getProperty("os.name").toLowerCase().contains("win"))
                            result = deleteItemFiles(selectedItem.getLocation() + "\\" +
                                    selectedItem.getTitle());
                        else
                            result = deleteItemFiles(selectedItem.getLocation() + "/" +
                                    selectedItem.getTitle());

                    }

                    if (result) {
                        itemList.remove(selectedItem);
                        queueItemList.remove(selectedItem);
                        DatabaseManager.delete(selectedItem);
                        deleteDialog.close();
                    }

                } else {

                    selectedItem.stopDownload();
                    itemList.remove(selectedItem);
                    queueItemList.remove(selectedItem);
                    DatabaseManager.delete(selectedItem);
                    deleteDialog.close();

                }
            });
            deleteDialog.showAndWait();

        }

    }

    @FXML
    void infoBtnAction() {

        Item selectedItem = itemsTableView.getSelectionModel().getSelectedItem();
        if( selectedItem != null) {

            ObservableList<String> infoList = FXCollections.observableArrayList();

            infoList.add("title : \t" + selectedItem.getTitle());
            infoList.add("url : \t" + selectedItem.getUrl());
            infoList.add("location : \t" + selectedItem.getLocation());
            infoList.add("format : \t" + selectedItem.getFormat());
            infoList.add("quality : \t" + selectedItem.getVideoQuality());
            infoList.add("limit : \t" + selectedItem.getSpeedLimit());

            ListView<String> infoListView = new ListView<>(infoList);
            Stage stage = new Stage();
            stage.setScene(new Scene(infoListView));
            stage.showAndWait();
        }

    }

    @FXML
    void settingBtnAction() {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("windows/SettingWindow.fxml"));
            Stage settingStage = new Stage();
            settingStage.setScene(new Scene(root));
            settingStage.setTitle("Setting");
            settingStage.show();

        } catch (Exception e) {
            homeWindowPane.setOpacity(0.30);
            new MessageDialog("Error Loading Settings Window! \n" +
                    "Restart program and try again.", MessageDialog.Type.ERROR,
                    MessageDialog.Buttons.CLOSE).createErrorDialog(e.getStackTrace()).showAndWait();
            homeWindowPane.setOpacity(1);
        }

    }

    @FXML
    void helpBtnAction() {

        MessageDialog messageDialog = new MessageDialog("Ooh, you need help in this simple program?\n" +
                "Sorry, no help in this version. help yourself", MessageDialog.Type.INFO, MessageDialog.Buttons.OK);
        messageDialog.getOkButton().setOnAction(event -> messageDialog.close());
        messageDialog.showAndWait();

    }

    @FXML
    void aboutBtnAction() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Nazel Video Downloader Team");
        alert.setContentText("Mohamed Bazazo \nIsmail Elmogy \nAhmed Elzoughby");
        alert.showAndWait();

    }

    @FXML
    void logBtnAction() {

        if(homeSplitPane.getItems().size() == 2)
            homeSplitPane.getItems().remove(consoleListView);
        else {
            homeSplitPane.getItems().add(1, consoleListView);
            homeSplitPane.setDividerPosition(0, 0.7);
        }

    }

    private boolean deleteItemFiles(String path) {

        File file = new File(path);

        if (file.exists()) {

            if (file.isDirectory()) {
                if ((file.list()).length > 0) {
                    for(String s : file.list())
                        deleteItemFiles(new File(path, s).getPath());
                }
            }

            boolean result = file.delete();

            // test if delete of file is success or not
            if (! result) {
                new MessageDialog("File cannot be deleted! \n" +
                        "Restart program and try again.", MessageDialog.Type.ERROR, MessageDialog.Buttons.CLOSE)
                        .createErrorDialog("The file may be in use by another program").showAndWait();
            }

            return result;

        } else {

            new MessageDialog("File delete failed, file does not exist! \n" +
                    "Restart program and try again.", MessageDialog.Type.ERROR, MessageDialog.Buttons.CLOSE)
                    .createErrorDialog("file is not exist").showAndWait();
            return false;

        }

    }

}
