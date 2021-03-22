
package com.lemust.repository.models.rest.user.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserFavoriteDTO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("options")
    @Expose
    private List<UserOptionDTO> options = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<UserOptionDTO> options) {
        this.options = options;
    }

}
