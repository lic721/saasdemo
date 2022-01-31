package com.saas.demo.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.saas.demo.mybatis.component.InjectionConfigImpl;
import com.saas.demo.mybatis.component.XmlFileOutConfigImpl;
import org.junit.Test;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenMain {

    @Test
    public void testGenEntity() throws IOException, XMLParserException {
        List<String> warnings = new ArrayList<>();
        File configFile = new File(GenMain.class.getResource("/generatorConfig.xml").getFile());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        warnings.stream().forEach(System.out::println);
        Context context = config.getContexts().get(0);

        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());

        GlobalConfig globalConfig = new GlobalConfig();
        String projectRoot = new File(context.getProperty("projectRoot")).getAbsolutePath();
        globalConfig.setOpen(false);
        globalConfig.setOutputDir(projectRoot + "/src/main/java");
        globalConfig.setAuthor("auto created by generator");
        globalConfig.setFileOverride(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setSwagger2(true);
       // globalConfig.setControllerName("%sController");
        autoGenerator.setGlobalConfig(globalConfig);

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        JDBCConnectionConfiguration jdbcConnectionConfiguration = context.getJdbcConnectionConfiguration();
        dataSourceConfig.setDriverName(jdbcConnectionConfiguration.getDriverClass());
        dataSourceConfig.setUrl(jdbcConnectionConfiguration.getConnectionURL());
        dataSourceConfig.setUsername(jdbcConnectionConfiguration.getUserId());
        dataSourceConfig.setPassword(jdbcConnectionConfiguration.getPassword());
        autoGenerator.setDataSource(dataSourceConfig);

        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setTablePrefix(new String[] { "" });
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
        List<String> tableNames = tableConfigurations.parallelStream().map(item -> item.getTableName()).distinct()
                .collect(Collectors.toList());
        strategyConfig.setInclude(tableNames.toArray(new String[tableNames.size()]));
        strategyConfig.setEntityColumnConstant(true);
        strategyConfig.setEntityBuilderModel(true);
        strategyConfig.setEntityLombokModel(true);

        strategyConfig.setControllerMappingHyphenStyle(false);
        autoGenerator.setStrategy(strategyConfig);

        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setController(null);
        templateConfig.setEntity("template/entity.java");

        autoGenerator.setTemplate(templateConfig);

        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.saas.demo.common");
        autoGenerator.setPackageInfo(packageConfig);
        InjectionConfig injectionConfig = new InjectionConfigImpl();
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();
        FileOutConfig fileOutConfig = new XmlFileOutConfigImpl(globalConfig);
        fileOutConfigList.add(fileOutConfig);
        injectionConfig.setFileOutConfigList(fileOutConfigList);
        autoGenerator.setCfg(injectionConfig);

        autoGenerator.execute();
    }

}
