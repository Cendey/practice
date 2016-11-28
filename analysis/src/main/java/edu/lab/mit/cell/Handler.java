package edu.lab.mit.cell;

import edu.lab.mit.norm.Criterion;
import edu.lab.mit.norm.ErrorMeta;
import edu.lab.mit.norm.FileIterator;
import edu.lab.mit.norm.Loader;
import edu.lab.mit.utils.StringSimilarity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

/**
 * <p>Project: KEWILL FORWARD ENTERPRISE</p>
 * <p>File: edu.lab.mit.cell.Handler</p>
 * <p>Copyright: Copyright � 2015 Kewill Co., Ltd. All Rights Reserved.</p>
 * <p>Company: Kewill Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 7/29/2015
 */
public class Handler {

    private FileIterator iterator;
    private final static Pattern pattern = Pattern.compile(
        "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d",
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    @SuppressWarnings(value = {"UnusedDeclaration"})
    public Handler(String fromFilePath, String toFilePath) throws Exception {
        super();
        iterator = new FileIterator(fromFilePath, toFilePath);
    }

    public Iterator<String> getIterator() {
        return iterator;
    }

    public BlockingQueue<ErrorMeta> analyzeUniqueError(Criterion instance) {
        Boolean errorOccurred = false;
        Boolean successiveError = false;
        Boolean newErrorFollowed = false;
        StringBuilder error = new StringBuilder();
        StringBuilder tempError = new StringBuilder();
        List<String> lstUserID = operators(instance.getUserID());
        BlockingQueue<ErrorMeta> uniqueErrorLogQueue = new LinkedBlockingQueue<>();
        Properties ignoredIdentifies = pullCachedIdentifiedErrors();
        Map<String, Object> markedIgnoreInfo = new HashMap<>(ignoredIdentifies.size());
        ignoredIdentifies.entrySet().parallelStream()
            .forEach(item -> markedIgnoreInfo.put(String.valueOf(item.getKey()), item.getValue()));
        String currDate = null;
        int errorCounter = 0;
        iterator.appendContentToFile(
            "\r\n##############################" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(GregorianCalendar.getInstance().getTime())
                + "##############################\r\n");
        while (iterator.hasNext()) {
            String content = iterator.next();
            if (content != null && content.trim().length() != 0) {
                if (content.contains(instance.getErrorStartID()) && (
                    lstUserID == null || lstUserID.stream().anyMatch(content::contains))) {
                    errorOccurred = true;
                    if (!successiveError) {
                        successiveError = true;
                        newErrorFollowed = false;
                        error.append(content).append("\r\n");
                        tempError.append(content.substring(instance.getErrorEndID().length() + 1));
                        currDate = content.substring(0, content.indexOf(instance.getErrorStartID()));
                    } else {
                        successiveError = false;
                        newErrorFollowed = true;
                    }
                } else {
                    if (errorOccurred) {
                        if (successiveError = !pattern.matcher(content).find()) {
                            error.append(content).append("\r\n");
                            tempError.append(content);
                        }
                    }
                }
                if (errorOccurred && !successiveError) {
                    String currentErrorContent = refineErrorContents(tempError, lstUserID);
                    String errorMD5 = genContentMD5(currentErrorContent);
                    if (!markedIgnoreInfo.keySet().contains(errorMD5)) {
                        if (!markedIgnoreInfo.values().parallelStream().filter(
                            item -> String.valueOf(item).length() > currentErrorContent.length() * 0.9
                                && String.valueOf(item).length() < currentErrorContent.length() * 1.1).anyMatch(
                            exist -> StringSimilarity.similarity(String.valueOf(exist), currentErrorContent) > 0.9)) {
                            iterator
                                .appendContentToFile("[No." + errorCounter + "]" + error.toString() + "\r\n");
                            uniqueErrorLogQueue
                                .add(new ErrorMeta(errorCounter, currDate, errorMD5, error.toString()));
                            markedIgnoreInfo.put(errorMD5, currentErrorContent);
                            errorCounter++;
                        }
                    }

                    error.delete(0, error.length());
                    tempError.delete(0, tempError.length());
                    errorOccurred = newErrorFollowed;
                    if (newErrorFollowed) {
                        newErrorFollowed = false;
                        successiveError = true;
                        error.append(content).append("\r\n");
                        tempError.append(content.substring(instance.getErrorEndID().length() + 1));
                        currDate = content.substring(0, content.indexOf(instance.getErrorStartID()));
                    }
                }
            }
        }
        iterator.appendContentToFile("Found " + errorCounter + " errors!");
        iterator.close();
        return uniqueErrorLogQueue;
    }

    public String refineErrorContents(StringBuilder tempError, List<String> lstUserID) {
        final String[] temp = {tempError.toString()};
        if (lstUserID != null && lstUserID.size() > 0) {
            lstUserID.stream().filter(temp[0]::contains)
                .mapToInt(id -> temp[0].indexOf(id) + id.length() + 1).min()
                .ifPresent(pos -> temp[0] = temp[0].substring(pos));
        }
        return temp[0];
    }

    private Properties pullCachedIdentifiedErrors() {
        return Loader.getIgnores();
    }

    private String genContentMD5(String content) {
        if (content != null && content.length() > 0) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(content.getBytes(StandardCharsets.UTF_8));
                return new BigInteger(1, digest.digest()).toString();
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e.getMessage());
            }
        }
        return "";
    }

    public List<String> operators(String operators) {
        List<String> lstOperator = null;
        if (operators != null && operators.trim().length() > 0) {
            lstOperator = new ArrayList<>();
            final List<String> finalLstOperator = lstOperator;
            Arrays.stream(operators.split("\\|")).forEach(item -> finalLstOperator.add("[" + item + "]"));
        }
        return lstOperator;
    }
}
