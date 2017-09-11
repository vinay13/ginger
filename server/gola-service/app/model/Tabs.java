package model;

import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by senthil
 */
public class Tabs {

    @Id
    private String id;

    private Idiom idiom;

    private List<Tab> tabs = new LinkedList<Tab>();

    public Idiom getIdiom() {
        return idiom;
    }

    public void setIdiom(Idiom idiom) {
        this.idiom = idiom;
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void removeTabForTabId(String tabId)
    {
        tabs = tabs.stream().filter(tab ->  !tabId.equalsIgnoreCase(tab.getId())).collect(Collectors.toList());
    }

    public boolean hasTabForTabId(String tabId)
    {
        return tabs.stream().filter(tab ->  tabId.equals(tab.getId())).count()>0;
    }
}
