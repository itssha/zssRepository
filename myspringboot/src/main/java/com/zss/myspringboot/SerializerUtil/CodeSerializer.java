package com.zss.myspringboot.SerializerUtil;



import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.zss.myspringboot.entity.Code;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeSerializer  extends JsonSerializer<List<Code>>{


    @Override
    public void serialize(List<Code> codes, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 构建简版code的集合，用于接收处于后的数据
        List simplecodes =new ArrayList();




        codes.forEach(code ->
                simplecodes.add(new SimpleCode(code.getId(),code.getName(),
                        code.getNameCapital(),code.getLabel(),code.getLevel(),
                        code.getUrl(),code.getRemarks())));

        // 将过滤后的数据进行回传
        gen.writeObject(simplecodes);
    }

    static class SimpleCode {
        private Integer id;
        private String name;
        private String nameCapital;
        private String url;
        private String label;
        private Integer level;
        private String remarks;
        private boolean selected = false;
        private List<Integer> groupIds;
        public SimpleCode(Integer id, String name, String nameCapital, String label,
                          Integer level, String url, String remarks){
            this.id=id;
            this.name=name;

            this.nameCapital=nameCapital;
            this.label=label;
            this.level=level;
            this.url=url;
            this.remarks=remarks;


        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNameCapital() {
            return nameCapital;
        }

        public void setNameCapital(String nameCapital) {
            this.nameCapital = nameCapital;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String getRemarks() {
            return remarks;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }



        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
        // constructor / getter / setter ...
    }
}
