package com.renta.renta_driver.model.autocomplete;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuggestionList implements Serializable {

    @SerializedName("suggestions")
    private List<SuggestionsItem> suggestionsItemList;

    public List<SuggestionsItem> getSuggestionsItemList() {
        return suggestionsItemList;
    }

    public void setSuggestionsItemList(List<SuggestionsItem> suggestionsItemList) {
        this.suggestionsItemList = suggestionsItemList;
    }
}
