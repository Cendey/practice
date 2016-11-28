package edu.lab.mit.norm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Project: KEWILL FORWARD ENTERPRISE</p>
 * <p>File: edu.lab.mit.norm.FileIterator</p>
 * <p>Copyright: Copyright � 2015 Kewill Co., Ltd. All Rights Reserved.</p>
 * <p>Company: Kewill Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 7/29/2015
 */
public class FileIterator implements Iterator<String> {

    private String currentLine;
    private BufferedReader reader;
    private BufferedWriter writer;

    public FileIterator(String toReadFilePath, String toWriteFilePath)
        throws FileNotFoundException, UnsupportedEncodingException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(toReadFilePath), "UTF-8"));
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toWriteFilePath, true), "UTF-8"));
    }

    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void appendContentToFile(String content) {
        try {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean hasNext() {
        try {
            currentLine = reader.readLine();
        } catch (Exception ex) {
            currentLine = null;
            System.err.println(ex.getMessage());
        }

        return currentLine != null;
    }

    @Override
    public String next() {
        if (currentLine == null) {
            throw new NoSuchElementException("No content will be read");
        }
        return currentLine;
    }

    @Override
    public void remove() {
    }
}
