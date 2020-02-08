package com.example.grpcclient.model;

/**
 * @author z.Taghizadeh
 */
public class BasicInfo {

    private Long id;
    private Integer code;
    private String name;
    private String englishName;
    private Boolean isActive;

    public BasicInfo() {
    }

    public BasicInfo(Long id, Integer code, String name, String englishName, Boolean isActive) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.englishName = englishName;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
