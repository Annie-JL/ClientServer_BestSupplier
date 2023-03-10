package controllers;

import java.io.IOException;

public interface IController {
    public void openWindow(String path, String title) throws IOException;
}
