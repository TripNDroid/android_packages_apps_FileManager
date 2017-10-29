package com.tripndroid.filemanager.utils;

import com.tripndroid.filemanager.ui.drawer.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by arpitkh996 on 20-01-2016.
 *
 * Singleton class to handle data for various services
 */

//Central data being used across activity,fragments and classes
public class DataUtils {

    public static final int DELETE = 0, COPY = 1, MOVE = 2, NEW_FOLDER = 3,
            RENAME = 4, NEW_FILE = 5, EXTRACT = 6, COMPRESS = 7;

    private ArrayList<String> hiddenfiles = new ArrayList<>(), gridfiles = new ArrayList<>(),
            listfiles = new ArrayList<>(), history = new ArrayList<>(), storages = new ArrayList<>();

    private ArrayList<Item> list = new ArrayList<>();
    private ArrayList<String[]> servers = new ArrayList<>(), books = new ArrayList<>();

    private DataChangeListener dataChangeListener;

    private static DataUtils sDataUtils;

    public static DataUtils getInstance() {
        if (sDataUtils == null) {
            sDataUtils = new DataUtils();
        }
        return sDataUtils;
    }

    public int containsServer(String[] a) {
        return contains(a, servers);
    }

    public int containsServer(String path) {

        synchronized (servers) {

            if (servers == null) return -1;
            int i = 0;
            for (String[] x : servers) {
                if (x[1].equals(path)) return i;
                i++;

            }
        }
        return -1;
    }

    public int containsBooks(String[] a) {
        return contains(a, books);
    }

    /*public int containsAccounts(CloudEntry cloudEntry) {
        return contains(a, accounts);
    }*/

    public void clear() {
        hiddenfiles = new ArrayList<>();
        gridfiles = new ArrayList<>();
        listfiles = new ArrayList<>();
        history = new ArrayList<>();
        storages = new ArrayList<>();
        servers = new ArrayList<>();
        books = new ArrayList<>();
    }

    public void registerOnDataChangedListener(DataChangeListener l) {

        dataChangeListener = l;
        clear();
    }

    int contains(String a, ArrayList<String[]> b) {
        int i = 0;
        for (String[] x : b) {
            if (x[1].equals(a)) return i;
            i++;

        }
        return -1;
    }

    int contains(String[] a, ArrayList<String[]> b) {
        if (b == null) return -1;
        int i = 0;
        for (String[] x : b) {
            if (x[0].equals(a[0]) && x[1].equals(a[1])) return i;
            i++;

        }
        return -1;
    }

    public void removeBook(int i) {
        synchronized (books) {

            if (books.size() > i)
                books.remove(i);
        }
    }

    public void removeServer(int i) {
        synchronized (servers) {

            if (servers.size() > i)
                servers.remove(i);
        }
    }

    public void addBook(String[] i) {
        synchronized (books) {

            books.add(i);
        }
    }

    public void addBook(final String[] i, boolean refreshdrawer) {
        synchronized (books) {

            books.add(i);
        }
        if (refreshdrawer && dataChangeListener != null) {
            AppConfig.runInBackground(new Runnable() {
                @Override
                public void run() {

                    dataChangeListener.onBookAdded(i, true);
                }
            });
        }
    }

    public void addServer(String[] i) {
        servers.add(i);
    }

    public void addHiddenFile(final String i) {

        synchronized (hiddenfiles) {

            hiddenfiles.add(i);
        }
        if (dataChangeListener != null) {
            AppConfig.runInBackground(new Runnable() {
                @Override
                public void run() {

                    dataChangeListener.onHiddenFileAdded(i);
                }
            });
        }
    }

    public void removeHiddenFile(final String i) {

        synchronized (hiddenfiles) {

            hiddenfiles.remove(i);
        }
        if (dataChangeListener != null) {
            AppConfig.runInBackground(new Runnable() {
                @Override
                public void run() {

                    dataChangeListener.onHiddenFileRemoved(i);
                }
            });
        }
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void addHistoryFile(final String i) {

        synchronized (history) {

            history.add(i);
        }
        if (dataChangeListener != null) {
            AppConfig.runInBackground(new Runnable() {
                @Override
                public void run() {

                    dataChangeListener.onHistoryAdded(i);
                }
            });
        }
    }

    public void sortBook() {
        Collections.sort(books, new BookSorter());
    }

    public synchronized void setServers(ArrayList<String[]> servers) {
        if (servers != null)
            this.servers = servers;
    }

    public synchronized void setBooks(ArrayList<String[]> books) {
        if (books != null)
            this.books = books;
    }

    public synchronized ArrayList<String[]> getServers() {
        return servers;
    }

    public synchronized ArrayList<String[]> getBooks() {
        return books;
    }

    public ArrayList<String> getHiddenfiles() {
        return hiddenfiles;
    }

    public synchronized void setHiddenfiles(ArrayList<String> hiddenfiles) {
        if (hiddenfiles != null)
            this.hiddenfiles = hiddenfiles;
    }

    public ArrayList<String> getGridFiles() {
        return gridfiles;
    }

    public synchronized void setGridfiles(ArrayList<String> gridfiles) {
        if (gridfiles != null)
            this.gridfiles = gridfiles;
    }

    public ArrayList<String> getListfiles() {
        return listfiles;
    }

    public synchronized void setListfiles(ArrayList<String> listfiles) {
        if (listfiles != null)
            this.listfiles = listfiles;
    }

    public void clearHistory() {
        history = new ArrayList<>();
        if (dataChangeListener != null) {
            AppConfig.runInBackground(new Runnable() {
                @Override
                public void run() {
                    dataChangeListener.onHistoryCleared();
                }
            });
        }
    }

    public synchronized List<String> getStorages() {
        return storages;
    }

    public synchronized void setStorages(ArrayList<String> storages) {
        this.storages = storages;
    }

    public ArrayList<Item> getList() {
        return list;
    }

    public synchronized void setList(ArrayList<Item> list) {
        this.list = list;
    }

    /**
     * Callbacks to do original changes in database (and ui if required)
     * The callbacks are called in a background thread
     */
    public interface DataChangeListener {
        void onHiddenFileAdded(String path);

        void onHiddenFileRemoved(String path);

        void onHistoryAdded(String path);

        void onBookAdded(String path[], boolean refreshdrawer);

        void onHistoryCleared();
    }

}
