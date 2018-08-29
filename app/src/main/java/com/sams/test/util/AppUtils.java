package com.sams.test.util;

import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Util methods used by the app
 */

public class AppUtils {
    private static final String LOG_TAG = "AppUtils";

    public static boolean getJsonResponse(String urlString, FileOutputStream outputStream) {
        Log.d(LOG_TAG, "getJsonResponse urlString: " + urlString);
        URL url;
        HttpURLConnection urlConnection = null;
        boolean success = false;
        try {
            url = new URL(urlString);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestProperty("User-Agent", "DeepuThomasTestApp");
            //urlConnection.setRequestProperty("Accept","application/vnd.github.v3+json");
            if (urlConnection.getResponseCode() == 200) {
                InputStream stream = urlConnection.getInputStream();
                // success
                //print(stream);
                success = AppUtils.writeInputStreamToFile(stream,
                        outputStream);
                //copy(stream, outputStream);
            } else {
                // Error
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        Log.d(LOG_TAG, "getJsonResponse result: " + success);
        return success;
    }

    /**
     * Can be a util method
     * @param inputStream
     * @param outputStream
     * @return success if inputStream is successfully written to outputStream.
     */
    public static boolean writeInputStreamToFile(InputStream inputStream, OutputStream outputStream) {
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        boolean success = false;
        try {
            bufferedOutputStream =
                    new BufferedOutputStream(outputStream);
            bufferedInputStream =
                    new BufferedInputStream(inputStream);
            int count = IOUtils.copy(bufferedInputStream, outputStream);
            Log.d(LOG_TAG, "writeInputStreamToFile count: " + count);
            success = true;
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG, "writeInputStreamToFile FileNotFoundException: " , e);
        } catch (IOException e) {
            Log.d(LOG_TAG, "writeInputStreamToFile IOException: " , e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(bufferedOutputStream);
            IOUtils.closeQuietly(bufferedInputStream);
        }
        Log.d(LOG_TAG, "writeInputStreamToFile result: " + success);
        return success;
    }

    public static int copy(InputStream input, OutputStream output) throws Exception, IOException {
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            Log.d(LOG_TAG, "copy count: " + count);
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
        return count;
    }

    public static String print(InputStream in){
        StringBuilder result;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            in.close();
            Log.d(LOG_TAG, "parsing: " + result.toString());
            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // close, used for only testing
        }
    }
}
