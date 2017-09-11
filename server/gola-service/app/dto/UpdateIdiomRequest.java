package dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class UpdateIdiomRequest {

    private List<String> idioms = new LinkedList<String>();

    public List<String> getIdioms() {
        return idioms;
    }

    public void setIdioms(List<String> idioms) {
        this.idioms = idioms;
    }
}
