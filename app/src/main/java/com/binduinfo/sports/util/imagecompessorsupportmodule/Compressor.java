package com.binduinfo.sports.util.imagecompessorsupportmodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;

public class Compressor {
    public static final int PROFILE_PIC = 0x0010;
    public static final int TRAVELS_IMAGES = 0x0011;
    public static final File BASIC_PATH = new File(Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "data");
    public static String childPath = "";
    //max width and height values of the compressed image is taken as 612x816
    private int maxWidth = 612;
    private int maxHeight = 816;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 80;
    private String destinationDirectoryPath;

    public Compressor(Context context, int from) {
        switch (from) {
            case PROFILE_PIC:
                childPath = context.getPackageName() + File.separator + "cache/.sports/.compress";
            default:
                childPath = context.getPackageName() + File.separator + "cache/.sports/.compress";

                break;
        }

        File mediaStorageDir = new File(BASIC_PATH, childPath);
        if (!mediaStorageDir.exists()) {
            if (mediaStorageDir.mkdirs()) {
                destinationDirectoryPath = BASIC_PATH.toString() + File.separator + childPath;//context.getCacheDir().getPath() + File.separator + "images";
            }
        } else
            destinationDirectoryPath = BASIC_PATH.toString() + File.separator + childPath;
    }

    public static void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    public static boolean createCaptureFolder(Context context, int from) {

        switch (from) {
            case PROFILE_PIC://fromMedical record
                childPath = context.getPackageName() + File.separator + "cache/.sports/.capture";
                break;
        }
        File mediaStorageDir = new File(BASIC_PATH, childPath);
        if (!mediaStorageDir.exists()) {
            return mediaStorageDir.mkdirs();
        } else
            return true;

    }

    public Compressor setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public Compressor setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public Compressor setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public Compressor setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public Compressor setDestinationDirectoryPath(String destinationDirectoryPath) {
        this.destinationDirectoryPath = destinationDirectoryPath;
        return this;
    }

    public File compressToFile(File imageFile) throws IOException {
        return compressToFile(imageFile, imageFile.getName());
    }

    public File compressToFile(File imageFile, String compressedFileName) throws IOException {
        return ImageUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + compressedFileName);
    }

    public Bitmap compressToBitmap(File imageFile) throws IOException {
        return ImageUtil.decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight);
    }

    public Flowable<File> compressToFileAsFlowable(final File imageFile) {
        return compressToFileAsFlowable(imageFile, imageFile.getName());
    }

    public Flowable<File> compressToFileAsFlowable(final File imageFile, final String compressedFileName) {
        return Flowable.defer(new Callable<Flowable<File>>() {
            @Override
            public Flowable<File> call() {
                try {
                    return Flowable.just(compressToFile(imageFile, compressedFileName));
                } catch (IOException e) {
                    return Flowable.error(e);
                }
            }
        });
    }

    public Flowable<Bitmap> compressToBitmapAsFlowable(final File imageFile) {
        return Flowable.defer(new Callable<Flowable<Bitmap>>() {
            @Override
            public Flowable<Bitmap> call() {
                try {
                    return Flowable.just(compressToBitmap(imageFile));
                } catch (IOException e) {
                    return Flowable.error(e);
                }
            }
        });
    }
}
