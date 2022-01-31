package com.saas.demo.mybatis.component;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

public class XmlFileOutConfigImpl extends FileOutConfig {

    private final GlobalConfig globalConfig;

    public XmlFileOutConfigImpl(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    @Override
    public String getTemplatePath() {
        return "/templates/mapper.xml.ftl";
    }

    @Override
    public String outputFile(TableInfo tableInfo) {
        return String.format((globalConfig.getOutputDir().replace("/src/main/java", "/src/main/resources")
                + "/myBatis/%s" + ConstVal.XML_SUFFIX), tableInfo.getEntityName());
    }
}
