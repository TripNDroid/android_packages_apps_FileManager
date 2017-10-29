package com.tripndroid.filemanager.filesystem;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import com.tripndroid.filemanager.exceptions.RootNotPermittedException;
import com.tripndroid.filemanager.fragments.MainFragment;
import com.tripndroid.filemanager.ui.LayoutElement;
import com.tripndroid.filemanager.ui.icons.Icons;
import com.tripndroid.filemanager.utils.DataUtils;
import com.tripndroid.filemanager.utils.files.Futils;
import com.tripndroid.filemanager.utils.Logger;
import com.tripndroid.filemanager.utils.OTGUtil;
import com.tripndroid.filemanager.utils.OpenMode;
import com.tripndroid.filemanager.utils.RootUtils;
import com.tripndroid.filemanager.utils.provider.UtilitiesProviderInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by Arpit on 07-07-2015.
 */
//Hybrid file for handeling all types of files
public class HFile {

    String path;
    OpenMode mode = OpenMode.FILE;

    private DataUtils dataUtils = DataUtils.getInstance();

    public HFile(OpenMode mode, String path) {
        this.path = path;
        this.mode = mode;
    }

    public HFile(OpenMode mode, String path, String name, boolean isDirectory) {
        this.mode = mode;
        this.path = path + "/" + name;
    }

    public void generateMode(Context context) {
        if (path.startsWith(OTGUtil.PREFIX_OTG)) {
            mode = OpenMode.OTG;
        } else if (isCustomPath()) {
            mode = OpenMode.CUSTOM;
        } else {
            if (context == null) {
                mode = OpenMode.FILE;
                return;
            }
            boolean rootMode = true;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mode = OpenMode.FILE;
                if (rootMode) {
                    if (!getFile().canRead()) mode = OpenMode.ROOT;
                }
                return;
            }
            if (FileUtil.isOnExtSdCard(getFile(), context)) mode = OpenMode.FILE;
            else if (rootMode) {
                if (!getFile().canRead()) mode = OpenMode.ROOT;
            }
            if (mode == OpenMode.UNKNOWN) mode = OpenMode.FILE;
        }

    }

    public void setMode(OpenMode mode) {
        this.mode = mode;
    }

    public OpenMode getMode() {
        return mode;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLocal() {
        return mode == OpenMode.FILE;
    }

    public boolean isRoot() {
        return mode == OpenMode.ROOT;
    }

    public boolean isOtgFile() {
        return mode == OpenMode.OTG;
    }

    File getFile() {
        return new File(path);
    }

    BaseFile generateBaseFileFromParent() {
        ArrayList<BaseFile> arrayList = null;
        try {
            arrayList = RootHelper.getFilesList(getFile().getParent(), true, true, null);
        } catch (RootNotPermittedException e) {
            e.printStackTrace();
            return null;
        }
        for (BaseFile baseFile : arrayList) {
            if (baseFile.getPath().equals(path))
                return baseFile;
        }
        return null;
    }

    public long lastModified() throws MalformedURLException {
        switch (mode) {
            case FILE:
                new File(path).lastModified();
                break;
            case ROOT:
                BaseFile baseFile = generateBaseFileFromParent();
                if (baseFile != null)
                    return baseFile.getDate();
        }
        return new File("/").lastModified();
    }

    /**
     * @deprecated use {@link #length(Context)} to handle content resolvers
     * @return
     */
    public long length() {
        long s = 0L;
        switch (mode) {
            case FILE:
                s = new File(path).length();
                return s;
            case ROOT:
                BaseFile baseFile = generateBaseFileFromParent();
                if (baseFile != null) return baseFile.getSize();
                break;
        }
        return s;
    }

    /**
     * Helper method to find length
     * @param context
     * @return
     */
    public long length(Context context) {

        long s = 0l;
        switch (mode){
            case FILE:
                s = new File(path).length();
                return s;
            case ROOT:
                BaseFile baseFile=generateBaseFileFromParent();
                if(baseFile!=null) return baseFile.getSize();
                break;
            case OTG:
                s = OTGUtil.getDocumentFile(path, context, false).length();
                break;
            default:
                break;
        }
        return s;
    }

    public String getPath() {
        return path;
    }

    /**
     * @deprecated use {@link #getName(Context)}
     * @return
     */
    public String getName() {
        String name = null;
        switch (mode) {
            case FILE:
                return new File(path).getName();
            case ROOT:
                return new File(path).getName();
            default:
                StringBuilder builder = new StringBuilder(path);
                name = builder.substring(builder.lastIndexOf("/") + 1, builder.length());
        }
        return name;
    }

    public String getName(Context context) {
        String name = null;
        switch (mode){
            case FILE:
                return new File(path).getName();
            case ROOT:
                return new File(path).getName();
            case OTG:
                return OTGUtil.getDocumentFile(path, context, false).getName();
            default:
                StringBuilder builder = new StringBuilder(path);
                name = builder.substring(builder.lastIndexOf("/")+1, builder.length());
        }
        return name;
    }

    public boolean isCustomPath() {
        return path.equals("0") ||
                path.equals("1") ||
                path.equals("2") ||
                path.equals("3") ||
                path.equals("4") ||
                path.equals("5") ||
                path.equals("6");
    }

    /**
     * Returns a path to parent for various {@link #mode}
     * @deprecated use {@link #getParent(Context)} to handle content resolvers
     *
     * @return
     */
    public String getParent() {
        String parentPath = "";
        switch (mode) {
            case FILE:
            case ROOT:
                parentPath = new File(path).getParent();
                break;
            default:
                StringBuilder builder = new StringBuilder(path);
                return builder.substring(0, builder.length() - (getName().length() + 1));
        }
        return parentPath;
    }

    /**
     * Helper method to get parent path
     *
     * @param context
     * @return
     */
    public String getParent(Context context) {

        String parentPath = "";
        switch (mode) {
            case FILE:
            case ROOT:
                parentPath = new File(path).getParent();
                break;
            case OTG:
            default:
                StringBuilder builder = new StringBuilder(path);
                StringBuilder parentPathBuilder = new StringBuilder(builder.substring(0,
                        builder.length()-(getName(context).length()+1)));
                return parentPathBuilder.toString();
        }
        return parentPath;
    }

    public String getParentName() {
        StringBuilder builder = new StringBuilder(path);
        StringBuilder parentPath = new StringBuilder(builder.substring(0,
                builder.length() - (getName().length() + 1)));
        String parentName = parentPath.substring(parentPath.lastIndexOf("/") + 1,
                parentPath.length());
        return parentName;
    }

    /**
     * Whether this object refers to a directory or file, handles all types of files
     * @deprecated use {@link #isDirectory(Context)} to handle content resolvers
     *
     * @return
     */
    public boolean isDirectory() {
        boolean isDirectory;
        switch (mode) {
            case FILE:
                isDirectory = new File(path).isDirectory();
                break;
            case ROOT:
                try {
                    isDirectory = RootHelper.isDirectory(path, true, 5);
                } catch (RootNotPermittedException e) {
                    e.printStackTrace();
                    isDirectory = false;
                }
                break;
            case OTG:
                // TODO: support for this method in OTG on-the-fly
                // you need to manually call {@link RootHelper#getDocumentFile() method
                isDirectory = false;
                break;
            default:
                isDirectory = new File(path).isDirectory();
                break;

        }
        return isDirectory;
    }

    public boolean isDirectory(Context context) {

        boolean isDirectory;
        switch (mode) {
            case FILE:
                isDirectory = new File(path).isDirectory();
                break;
            case ROOT:
                try {
                    isDirectory = RootHelper.isDirectory(path,true,5);
                } catch (RootNotPermittedException e) {
                    e.printStackTrace();
                    isDirectory = false;
                }
                break;
            case OTG:
                isDirectory = OTGUtil.getDocumentFile(path, context, false).isDirectory();
                break;
            default:
                isDirectory = new File(path).isDirectory();
                break;

        }
        return isDirectory;
    }

    /**
     * @deprecated use {@link #folderSize(Context)}
     * @return
     */
    public long folderSize() {
        long size = 0L;

        switch (mode) {
            case FILE:
                size = Futils.folderSize(new File(path), null);
                break;
            case ROOT:
                BaseFile baseFile = generateBaseFileFromParent();
                if (baseFile != null) size = baseFile.getSize();
                break;
            default:
                return 0L;
        }
        return size;
    }

    /**
     * Helper method to get length of folder in an otg
     *
     * @param context
     * @return
     */
    public long folderSize(Context context) {

        long size = 0l;

        switch (mode){
            case FILE:
                size = Futils.folderSize(new File(path), null);
                break;
            case ROOT:
                BaseFile baseFile=generateBaseFileFromParent();
                if(baseFile!=null) size = baseFile.getSize();
                break;
            case OTG:
                size = Futils.folderSize(path, context);
                break;
            default:
                return 0l;
        }
        return size;
    }


    /**
     * Gets usable i.e. free space of a device
     * @return
     */
    public long getUsableSpace() {
        long size = 0L;
        switch (mode) {
            case FILE:
            case ROOT:
                size = new File(path).getUsableSpace();
                break;
            case OTG:
                // TODO: Get free space from OTG when {@link DocumentFile} API adds support
                break;

        }
        return size;
    }

    /**
     * Gets total size of the disk
     * @param context
     * @return
     */
    public long getTotal(Context context) {
        long size = 0l;
        switch (mode) {
            case FILE:
            case ROOT:
                size = new File(path).getTotalSpace();
                break;
            case OTG:
                // TODO: Find total storage space of OTG when {@link DocumentFile} API adds support
                DocumentFile documentFile = OTGUtil.getDocumentFile(path, context, false);
                documentFile.length();
                break;
        }
        return size;
    }

    /**
     * @deprecated use {@link #listFiles(Context, boolean)}
     * @param rootMode
     * @return
     */
    public ArrayList<BaseFile> listFiles(boolean rootMode) {
        ArrayList<BaseFile> arrayList = new ArrayList<>();
        if (isOtgFile()) {

        } else {
            try {
                arrayList = RootHelper.getFilesList(path, rootMode, true, null);
            } catch (RootNotPermittedException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    /**
     * Helper method to list children of this file
     *
     * @param context
     * @return
     */
    public ArrayList<BaseFile> listFiles(Context context, boolean isRoot) {
        ArrayList<BaseFile> arrayList = new ArrayList<>();
        switch (mode) {
            case OTG:
                arrayList = OTGUtil.getDocumentFilesList(path, context);
                break;
            default:
                try {
                    arrayList = RootHelper.getFilesList(path, isRoot, true, null);
                } catch (RootNotPermittedException e) {
                    e.printStackTrace();
                }
        }

        return arrayList;
    }

    public String getReadablePath(String path) {
        return path;
    }

    /**
     * Handles getting input stream for various {@link OpenMode}
     * @deprecated use {@link #getInputStream(Context)} which allows handling content resolver
     * @return
     */
    public InputStream getInputStream() {
        InputStream inputStream;
            try {
                inputStream = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                inputStream = null;
                e.printStackTrace();
            }
        return inputStream;
    }

    public InputStream getInputStream(Context context) {
        InputStream inputStream;

        switch (mode) {
            case OTG:
                ContentResolver contentResolver = context.getContentResolver();
                DocumentFile documentSourceFile = OTGUtil.getDocumentFile(path,
                        context, false);
                try {
                    inputStream = contentResolver.openInputStream(documentSourceFile.getUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    inputStream = null;
                }
                break;
            default:
                try {
                    inputStream = new FileInputStream(path);
                } catch (FileNotFoundException e) {
                    inputStream = null;
                    e.printStackTrace();
                }
                break;
        }
        return inputStream;
    }

    public OutputStream getOutputStream(Context context) {
        OutputStream outputStream;
        switch (mode) {
            case OTG:
                ContentResolver contentResolver = context.getContentResolver();
                DocumentFile documentSourceFile = OTGUtil.getDocumentFile(path,
                        context, true);
                try {
                    outputStream = contentResolver.openOutputStream(documentSourceFile.getUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    outputStream = null;
                }
                break;
            default:
                try {
                    outputStream = FileUtil.getOutputStream(new File(path), context, length());
                } catch (Exception e) {
                    outputStream=null;
                    e.printStackTrace();
                }

        }
        return outputStream;
    }

    public boolean exists() {
        boolean exists = false;
        if (isLocal()) {
            exists = new File(path).exists();
        } else if (isRoot()) {
            try {
                return RootHelper.fileExists(path);
            } catch (RootNotPermittedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return exists;
    }

    /**
     * Helper method to check file existence in otg
     *
     * @param context
     * @return
     */
    public boolean exists(Context context) {
        if (isOtgFile()) {
            DocumentFile fileToCheck = OTGUtil.getDocumentFile(path, context, false);
            return fileToCheck != null;
        } else return (exists());
    }

    /**
     * Whether file is a simple file (i.e. not a directory/otg/other)
     *
     * @return true if file; other wise false
     */
    public boolean isSimpleFile() {
        return !isOtgFile() && !isCustomPath()
                && !android.util.Patterns.EMAIL_ADDRESS.matcher(path).matches() &&
                !new File(path).isDirectory();
    }

    public boolean setLastModified(long date) {
        File f = new File(path);
        return f.setLastModified(date);
    }

    public void mkdir(Context context) {
        if (isOtgFile()) {
            if (!exists(context)) {
                DocumentFile parentDirectory = OTGUtil.getDocumentFile(getParent(context), context, false);
                if (parentDirectory.isDirectory()) {
                    parentDirectory.createDirectory(getName(context));
                }
            }
        } else
            FileUtil.mkdir(new File(path), context);
    }

    public boolean delete(Context context, boolean rootmode) throws RootNotPermittedException {
        if (isRoot() && rootmode) {
            setMode(OpenMode.ROOT);
            RootUtils.delete(getPath());
        } else {
            FileUtil.deleteFile(new File(path), context);
        }
        return !exists();
    }

    /**
     * Returns the name of file excluding it's extension
     * If no extension is found then whole file name is returned
     * @param context
     * @return
     */
    public String getNameString(Context context) {
        String fileName = getName(context);

        int extensionStartIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, extensionStartIndex == -1 ? fileName.length() : extensionStartIndex);
    }

    /**
     * Generates a {@link LayoutElement} adapted compatible element.
     * Currently supports only local filesystem
     * @param mainFragment
     * @param utilitiesProvider
     * @return
     */
    public LayoutElement generateLayoutElement(MainFragment mainFragment, UtilitiesProviderInterface utilitiesProvider) {
        switch (mode) {
            case FILE:
            case ROOT:
                File file = new File(path);
                LayoutElement layoutElement;
                if (isDirectory()) {

                    layoutElement = utilitiesProvider.getFutils()
                            .newElement(mainFragment.folder,
                                    path, RootHelper.parseFilePermission(file),
                                    "", folderSize() + "", 0, true, false,
                                    file.lastModified() + "");
                } else {
                    layoutElement = utilitiesProvider.getFutils().newElement(Icons.loadMimeIcon(
                            file.getPath(), !mainFragment.IS_LIST, mainFragment.getResources()),
                            file.getPath(), RootHelper.parseFilePermission(file),
                            file.getPath(), file.length() + "", file.length(), false, false, file.lastModified() + "");
                }
                layoutElement.setMode(mode);
                return layoutElement;
            default:
                return null;
        }
    }
}
