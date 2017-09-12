package dto;

import model.Tab;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class TabTopItemRequest {


    private String idiom;
    private Tab tab;

    public String getIdiom() {
        return idiom;
    }

    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }
}
