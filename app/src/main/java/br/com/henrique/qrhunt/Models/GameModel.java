package br.com.henrique.qrhunt.Models;

/**
 * Created by Henrique on 27/05/2017.
 */

public class GameModel {

    private String name;
    private long quantity;
    private String found;
    private String code;

    public GameModel() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
