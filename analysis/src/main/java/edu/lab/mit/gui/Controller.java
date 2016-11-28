package edu.lab.mit.gui;

import edu.lab.mit.cell.Handler;
import edu.lab.mit.norm.Criterion;
import edu.lab.mit.norm.ErrorMeta;
import edu.lab.mit.norm.FileIterator;
import edu.lab.mit.norm.Loader;
import edu.lab.mit.norm.LogMeta;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;

/**
 * <p>Project: KEWILL FORWARD ENTERPRISE</p>
 * <p>File: edu.lab.mit.gui.Controller</p>
 * <p>Copyright: Copyright � 2015 Kewill Co., Ltd. All Rights Reserved.</p>
 * <p>Company: Kewill Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 7/29/2015
 */
public class Controller implements Initializable {

    public TextField operatorID;
    public TextField errorStartID;
    public TextField errorEndID;
    public TextField sourceErrorLog;
    public Button chooseSourceLogFile;
    public TableView<ErrorMeta> uniqueErrorLogInfo;
    public TextField targetErrorLog;
    public Button chooseTargetLogFile;
    public Button analyzeError;
    public TextField errorCounter;
    public Button previousItem;
    public TextField currentItemIndex;
    public Button nextItem;

    private Criterion criterion;
    private Map<String, Object> identifier = new HashMap<>();
    private Map<String, Object> filter = new HashMap<>();
    private final static LogMeta meta = LogMeta.getInstance();
    private final ObservableList<ErrorMeta> data = FXCollections.observableArrayList();

    private Handler handler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        criterion = new Criterion();
        Bindings.bindBidirectional(operatorID.textProperty(), criterion.userIDProperty());
        Bindings.bindBidirectional(errorStartID.textProperty(), criterion.errorStartIDProperty());
        errorStartID.textProperty().addListener((observable, oldItem, newItem) -> {
            if (newItem != null && !newItem.equals(oldItem)) {
                criterion.setErrorStartID(newItem);
            }
        });
        Bindings.bindBidirectional(errorEndID.textProperty(), criterion.errorEndIDProperty());
        errorEndID.textProperty().addListener((
            (observable, oldValue, newValue) -> {
                if (newValue != null && !newValue.equals(oldValue)) {
                    criterion.setErrorEndID(newValue);
                }
            }));

        initTableView();

        initNavigationBar();

        Bindings.bindBidirectional(sourceErrorLog.textProperty(), criterion.sourceFilePathProperty());
        addFileChooserListener(sourceErrorLog);

        Bindings.bindBidirectional(targetErrorLog.textProperty(), criterion.targetFilePathProperty());
        addFileChooserListener(targetErrorLog);
        Loader.init(Loader.getFilters(), Loader.FILTER_CRITERION_CONFIGURE);
        Loader.pushFilter(criterion);
    }

    private void initNavigationBar() {
        previousItem.setOnAction((ActionEvent event) -> {
            int currentRowIndex = uniqueErrorLogInfo.getSelectionModel().getSelectedIndex();
            if (currentRowIndex != 0) {
                uniqueErrorLogInfo.getSelectionModel().clearSelection();
                uniqueErrorLogInfo.getSelectionModel().select(currentRowIndex - 1);
                uniqueErrorLogInfo.getSelectionModel().focus(currentRowIndex - 1);
                uniqueErrorLogInfo.scrollTo(currentRowIndex - 1);
                uniqueErrorLogInfo.refresh();
                currentItemIndex.textProperty().set("SNo.: " + (currentRowIndex - 1));
            }
        });

        nextItem.setOnAction((ActionEvent event) -> {
            int currentRowIndex = uniqueErrorLogInfo.getSelectionModel().getSelectedIndex();
            if (currentRowIndex < uniqueErrorLogInfo.getItems().size() - 1) {
                uniqueErrorLogInfo.getSelectionModel().clearSelection();
                uniqueErrorLogInfo.getSelectionModel().select(currentRowIndex + 1);
                uniqueErrorLogInfo.getSelectionModel().focus(currentRowIndex + 1);
                uniqueErrorLogInfo.scrollTo(currentRowIndex + 1);
                uniqueErrorLogInfo.refresh();
                currentItemIndex.textProperty().set("SNo.: " + (currentRowIndex + 1));
            }
        });
    }

    private void initTableView() {
        uniqueErrorLogInfo.setItems(data);
        TableColumn<ErrorMeta, Integer> sNoCol = new TableColumn<>("SNo.");
        sNoCol.setCellValueFactory(new PropertyValueFactory<>("sNo"));
        sNoCol.prefWidthProperty().bind(uniqueErrorLogInfo.widthProperty().multiply(0.03));
        TableColumn<ErrorMeta, Date> currDateCol = new TableColumn<>("Date");
        currDateCol.setCellValueFactory(new PropertyValueFactory<>("currDate"));
        currDateCol.prefWidthProperty().bind(uniqueErrorLogInfo.widthProperty().multiply(0.07));
        TableColumn<ErrorMeta, String> md5Col = new TableColumn<>("Identified ID");
        md5Col.setCellValueFactory(new PropertyValueFactory<>("md5"));
        md5Col.prefWidthProperty().bind(uniqueErrorLogInfo.widthProperty().multiply(0.20));
        TableColumn<ErrorMeta, String> detailCol = new TableColumn<>("Detail");
        detailCol.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailCol.prefWidthProperty().bind(uniqueErrorLogInfo.widthProperty().multiply(0.70));
        ObservableList<TableColumn<ErrorMeta, ?>> columns = uniqueErrorLogInfo.getColumns();
        columns.addAll(Arrays.asList(sNoCol, currDateCol, md5Col, detailCol));
        Bindings.bindBidirectional(uniqueErrorLogInfo.itemsProperty(), new SimpleObjectProperty<>(data));
        uniqueErrorLogInfo.getSelectionModel().getSelectedIndices().addListener(
            (ListChangeListener.Change<? extends Integer> change) -> {
                int currentRowIndex = uniqueErrorLogInfo.getSelectionModel().getSelectedIndex();
                currentItemIndex.textProperty().set("SNo.: " + currentRowIndex);
            }
        );
    }

    private Boolean isFileAvailable(TextField instance) {
        Boolean available = true;
        StringProperty property = instance.textProperty();
        if (property.getValue() == null || property.getValue().trim().length() == 0) {
            property.setValue("");
            createMessageDialog("The file is required!");
            instance.setStyle("-fx-background-color: RED");
            instance.requestFocus();
            available = false;
        } else {
            File sourceLogFile = new File(property.getValue());
            if (sourceLogFile.isFile() && sourceLogFile.exists()) {
                instance.setStyle("-fx-background-color: WHITE");
            } else {
                property.setValue("");
                createMessageDialog("The file specified is not found, please double check first!");
                instance.setStyle("-fx-background-color: RED");
                instance.requestFocus();
                available = false;
            }
        }
        return available;
    }

    Handler getHandler() {
        return handler;
    }

    private void addFileChooserListener(TextField instance) {
        instance.textProperty().addListener(
            (observable, oldItem, newItem) -> {
                instance.tooltipProperty().setValue(null);
                instance.setStyle("-fx-text-fill: BLACK");

                File file = new File(newItem.trim());
                if (!file.isFile() || !file.exists()) {
                    instance.setStyle("-fx-text-fill: RED");
                    instance.tooltipProperty()
                        .setValue(new Tooltip("The file is not existed, please double check!"));
                }

            });
    }

    private String loadFilePath(String initDirectory) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Log Files", "*.log"),
            new FileChooser.ExtensionFilter("Text Files", "*.txt"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));

        String userDirectoryString = initDirectory(initDirectory);
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File(System.getProperty("user.dir"));
        }
        fileChooser.setInitialDirectory(userDirectory);
        File chosenFile = fileChooser.showOpenDialog(null);
        String path = null;
        if (chosenFile != null) {
            path = chosenFile.getPath();
        }
        return path;
    }

    private String initDirectory(String fullFilePath) {
        if (fullFilePath == null || fullFilePath.trim().length() == 0) return System.getProperty("user.home");

        String destination;
        File targetFile = new File(fullFilePath);
        if (targetFile.isFile()) {
            if (targetFile.exists()) {
                destination = targetFile.getParent();
                return destination;
            } else {
                destination = fullFilePath.substring(0, fullFilePath.lastIndexOf("\\"));
                File directory = new File(destination);
                if (directory.isDirectory()) {
                    return destination;
                } else {
                    return initDirectory(destination);
                }
            }
        } else {
            destination = fullFilePath.substring(0, fullFilePath.lastIndexOf("\\"));
            File directory = new File(destination);
            if (directory.isDirectory()) {
                return destination;
            } else {
                return initDirectory(destination);
            }
        }
    }

    public void chooseSourceLogFile() {
        String filePath = loadFilePath(criterion.getSourceFilePath());
        if (filePath != null && filePath.trim().length() > 0) {
            criterion.setSourceFilePath(filePath);
            sourceErrorLog.setStyle("-fx-background-color: white");
        }
    }

    public void chooseTargetLogFile() {
        String filePath = loadFilePath(criterion.getTargetFilePath());
        if (filePath != null && filePath.trim().length() > 0) {
            criterion.setTargetFilePath(filePath);
            targetErrorLog.setStyle("-fx-background-color: white");
        }
    }

    public void analyzeErrorLog() throws Exception {
        if (!criterionAlready() || !isFileAvailable(sourceErrorLog) || !isFileAvailable(targetErrorLog)) return;
        new Thread(() -> {
            try {
                prepareInfo();
                handler = new Handler(criterion.getSourceFilePath(), criterion.getTargetFilePath());
                BlockingQueue<ErrorMeta> queue = handler.analyzeUniqueError(criterion);
                queue.forEach(data::add);
                errorCounter.textProperty().set("Total errors: " + queue.size());
                if (queue.size() > 0) {
                    uniqueErrorLogInfo.getSelectionModel().select(0);
                    uniqueErrorLogInfo.getSelectionModel().focus(0);
                    currentItemIndex.textProperty().set("SNo.: 0");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                analyzeError.setDisable(false);
            }
        }).start();
    }

    private void prepareInfo() {
        meta.clearLog();
        identifier.clear();
        errorCounter.textProperty().set("");
        analyzeError.setDisable(true);
        Loader.load(Loader.getIgnores(), Loader.IGNORE_ERROR_ID_CONFIGURE);
        Loader.getIgnores().entrySet().stream()
            .forEach(item -> identifier.put(String.valueOf(item.getKey()), item.getValue()));
    }

    void keepFilter() {
        filter.clear();
        filter.put("error.start.id", criterion.getErrorStartID());
        filter.put("error.end.id", criterion.getErrorEndID());
        filter.put("user.id", criterion.getUserID());
        filter.put("source.file.path", criterion.getSourceFilePath());
        filter.put("target.file.path", criterion.getTargetFilePath());
    }

    private boolean criterionAlready() {
        String message;
        if (criterion.getErrorStartID() == null || criterion.getErrorStartID().trim().length() == 0) {
            message = "Error Start Identity Can't Be Empty!";
            errorStartID.tooltipProperty().setValue(new Tooltip(message));
            createMessageDialog(message);
            errorStartID.requestFocus();
            errorStartID.setStyle("-fx-background-color: RED");
            return false;
        }

        if (criterion.getErrorEndID() == null || criterion.getErrorEndID().trim().length() == 0) {
            message = "Error End Identity Can't Be Empty!";
            errorEndID.tooltipProperty().setValue(new Tooltip(message));
            createMessageDialog(message);
            errorEndID.requestFocus();
            errorEndID.setStyle("-fx-background-color: RED");
            return false;
        }


        if (criterion.getSourceFilePath() == null || criterion.getSourceFilePath().trim().length() == 0) {
            message = "Source Error Log Is Required!";
            sourceErrorLog.tooltipProperty().setValue(new Tooltip(message));
            createMessageDialog(message);
            sourceErrorLog.requestFocus();
            sourceErrorLog.setStyle("-fx-background-color: RED");
            return false;
        }
        if (criterion.getTargetFilePath() == null || criterion.getTargetFilePath().trim().length() == 0) {
            message = "Target Error Log Is Required!";
            targetErrorLog.tooltipProperty().setValue(new Tooltip(message));
            createMessageDialog(message);
            targetErrorLog.requestFocus();
            targetErrorLog.setStyle("-fx-background-color: RED");
            return false;
        }
        return true;
    }

    Map<String, Object> getIdentifier() {
        return identifier;
    }

    Map<String, Object> getFilter() {
        return filter;
    }

    private void createMessageDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Look, a Warning Dialog");
        alert.setContentText(message + "\r\nBe Careful For The Next Step!");
        alert.showAndWait();
    }

    public void ignoreError() {
        TableView.TableViewSelectionModel<ErrorMeta> model = uniqueErrorLogInfo.getSelectionModel();
        ErrorMeta currentError = model.getSelectedItem();
        if (!identifier.keySet().parallelStream().anyMatch(md5 -> md5.equals(currentError.getMd5()))) {
            identifier.put(currentError.getMd5(), handler
                .refineErrorContents(
                    new StringBuilder(currentError.getDetail()),
                    handler.operators(criterion.getUserID())));
        }
        int index = currentError.getsNo();
        uniqueErrorLogInfo.getItems().parallelStream().filter(item -> item.getsNo() > index)
            .forEach(item -> item.setsNo(item.getsNo() - 1));
        uniqueErrorLogInfo.getItems().remove(model.getSelectedItem());
        uniqueErrorLogInfo.getSelectionModel().select(index);
        uniqueErrorLogInfo.scrollTo(index);
        uniqueErrorLogInfo.refresh();
        errorCounter.textProperty().set("Total errors: " + uniqueErrorLogInfo.getItems().size());
        currentItemIndex.textProperty().set("SNo.: " + index);
    }

    public void markIdentifiedErrorInfo() {
    }

    public void exit() {
        if (handler != null) {
            FileIterator.class.cast(handler.getIterator()).close();
        }
        keepFilter();
        Loader.writeInfo(identifier, Loader.IGNORE_ERROR_ID_CONFIGURE);
        Loader.writeInfo(filter, Loader.FILTER_CRITERION_CONFIGURE);
        System.exit(0);
    }
}
