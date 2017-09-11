package model;

import dto.TopItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by senthil
 */
public class CuratedTab {

    private static Logger logger = LoggerFactory.getLogger(CuratedTab.class);

    @Id
    private String id;

    private List<TopItem> topItems = new LinkedList<TopItem>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TopItem> getTopItems() {
        return topItems;
    }

    public void setTopItems(List<TopItem> topItems) {
        this.topItems = topItems;
    }

    public boolean containsTopItemForText(String text){
        return topItems.stream().filter(item -> text.equalsIgnoreCase(item.getText())).count() > 0;
    }

    public boolean containsTopItemForGifId(String gifId){
        return topItems.stream().filter(item -> gifId.equals(item.getGifId())).count() > 0;
    }

    public boolean addTopItemForText(String text){
        return topItems.add(new TopItem(text));
    }


    public void removeTopitemForText(String text){
        logger.info("text is " + text);

        topItems = topItems.stream().filter(item ->  !text.equalsIgnoreCase(item.getText())).collect(Collectors.toList());
    }



    public void removeTopitemForGifId(String gifId){
        topItems = topItems.stream().filter(item ->  !gifId.equals(item.getGifId())).collect(Collectors.toList());
    }

}
