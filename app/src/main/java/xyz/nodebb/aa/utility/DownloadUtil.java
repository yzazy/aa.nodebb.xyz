package xyz.nodebb.aa.utility;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import xyz.nodebb.aa.WebAppConfig;


public class DownloadUtil {
    public static void downloadFile(Context context, String url, String fileName) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            // Download Directory
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            // Add media files to media scanner
            request.allowScanningByMediaScanner();

            // Notification for complete download
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Start download
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        } else {
            try {
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                }
            } catch (android.content.ActivityNotFoundException e) {
            }
        }
    }

    // Checks for MIME Types
    public static boolean isDownloadableFile(String url) {
        int index = url.indexOf("?");
        if (index > -1) {
            url = url.substring(0, index);
        }
        url = url.toLowerCase();

        for (String type : WebAppConfig.DOWNLOAD_FILE_TYPES) {
            if (url.endsWith(type)) return true;
        }

        return false;
    }

    // Get file name
    public static String getFileName(String url) {
        int index = url.indexOf("?");
        if (index > -1) {
            url = url.substring(0, index);
        }
        url = url.toLowerCase();

        index = url.lastIndexOf("/");
        if (index > -1) {
            return url.substring(index + 1, url.length());
        } else {
            return Long.toString(System.currentTimeMillis());
        }
    }
}
