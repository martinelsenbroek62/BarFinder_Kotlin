
package com.lemust.repository.models.rest.report_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceFilterField {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("field_type")
    @Expose
    private Integer fieldType;
    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;
    @SerializedName("filter_field_type")
    @Expose
    private String filterFieldType;

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

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getFilterFieldType() {
        return filterFieldType;
    }

    public void setFilterFieldType(String filterFieldType) {
        this.filterFieldType = filterFieldType;
    }

}
