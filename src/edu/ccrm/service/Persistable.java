package edu.ccrm.service;

import java.io.IOException;

public interface Persistable {
    void loadData() throws IOException;
    void saveData() throws IOException;
}