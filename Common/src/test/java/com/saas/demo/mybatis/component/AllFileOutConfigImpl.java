package com.saas.demo.mybatis.component;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AllFileOutConfigImpl extends FileOutConfig {
    private String[] keywords = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
            "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
            "package", "private", "protected", "public", "return", "strictfp", "short", "static", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while" };

    private final GlobalConfig globalConfig;
    private final Map<String, String> pathInfo;
    private final TemplateConfig templateConfig;
    private final StrategyConfig strategyConfig;
    private Map<String, Integer> countMap = new HashMap<>();

    public AllFileOutConfigImpl(GlobalConfig globalConfig, PackageConfig packageConfig, TemplateConfig templateConfig,
            StrategyConfig strategyConfig) {
        this.globalConfig = globalConfig;
        this.templateConfig = templateConfig;
        this.strategyConfig = strategyConfig;
        pathInfo = handlerPackage(globalConfig, templateConfig, packageConfig);
        packageConfig.setPathInfo(pathInfo);
    }

    private Map<String, String> handlerPackage(GlobalConfig globalConfig, TemplateConfig templateConfig,
            PackageConfig packageConfig) {
        Map<String, String> pathInfo = new HashMap<>(6);
        setPathInfo(packageConfig, pathInfo, templateConfig.getEntity(globalConfig.isKotlin()),
                globalConfig.getOutputDir(), ConstVal.ENTITY_PATH, ConstVal.ENTITY);
        setPathInfo(packageConfig, pathInfo, templateConfig.getMapper(), globalConfig.getOutputDir(),
                ConstVal.MAPPER_PATH, ConstVal.MAPPER);
        setPathInfo(packageConfig, pathInfo, templateConfig.getXml(), globalConfig.getOutputDir(), ConstVal.XML_PATH,
                ConstVal.XML);
        setPathInfo(packageConfig, pathInfo, templateConfig.getService(), globalConfig.getOutputDir(),
                ConstVal.SERVICE_PATH, ConstVal.SERVICE);
        setPathInfo(packageConfig, pathInfo, templateConfig.getServiceImpl(), globalConfig.getOutputDir(),
                ConstVal.SERVICE_IMPL_PATH, ConstVal.SERVICE_IMPL);
        setPathInfo(packageConfig, pathInfo, templateConfig.getController(), globalConfig.getOutputDir(),
                ConstVal.CONTROLLER_PATH, ConstVal.CONTROLLER);
        return pathInfo;
    }

    private void setPathInfo(PackageConfig packageConfig, Map<String, String> pathInfo, String template,
            String outputDir, String path, String module) {
        Map<String, String> packageInfo = new HashMap<>(8);
        packageInfo.put(ConstVal.MODULE_NAME, packageConfig.getModuleName());
        packageInfo.put(ConstVal.ENTITY, joinPackage(packageConfig.getParent(), packageConfig.getEntity()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(packageConfig.getParent(), packageConfig.getMapper()));
        packageInfo.put(ConstVal.XML, joinPackage(packageConfig.getParent(), packageConfig.getXml()));
        packageInfo.put(ConstVal.SERVICE, joinPackage(packageConfig.getParent(), packageConfig.getService()));
        packageInfo.put(ConstVal.SERVICE_IMPL, joinPackage(packageConfig.getParent(), packageConfig.getServiceImpl()));
        packageInfo.put(ConstVal.CONTROLLER, joinPackage(packageConfig.getParent(), packageConfig.getController()));
        if (StringUtils.isNotBlank(template)) {
            pathInfo.put(path, joinPath(outputDir, packageInfo.get(module)));
        }
    }

    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isBlank(parent)) {
            return subPackage;
        }
        return parent + StringPool.DOT + subPackage;
    }

    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }
    @Override
    public String getTemplatePath() {
        return templateConfig.getEntity(globalConfig.isKotlin()) + ".ftl";
    }

    @Override
    public String outputFile(TableInfo tableInfo) {
        String tableName = tableInfo.getName();
        for (String prefix : strategyConfig.getTablePrefix()) {
            tableName = tableName.replace(prefix, "");
        }
        final String[] tableNames = tableName.split("_");
        String tableNamePrefix = tableNames[0];
        if (Arrays.stream(keywords).anyMatch(keyword -> tableNames[0].equalsIgnoreCase(keyword))) {
            if (tableNames.length > 1) {
                tableNamePrefix += (tableNames[1]);
            } else {
                throw new RuntimeException("error gen for keyword in java");
            }
        }
        String entityName = tableInfo.getEntityName();
        Integer accessCount = countMap.getOrDefault(tableName + "-entity", 0);
        if (accessCount < 2 && null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
            countMap.put(tableName + "-entity", accessCount + 1);
            return String.format((pathInfo.get(ConstVal.ENTITY_PATH) + "/%s%s" + ConstVal.JAVA_SUFFIX),
                    tableNamePrefix + "/", entityName);
        }
        accessCount = countMap.getOrDefault(tableName + "-mapper", 0);
        if (accessCount < 2 && null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
            countMap.put(tableName + "-mapper", accessCount + 1);
            return String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName()
                    + ConstVal.JAVA_SUFFIX), tableNamePrefix + "/", entityName);
        }
        accessCount = countMap.getOrDefault(tableName + "-xml", 0);
        if (accessCount < 2 && null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
            countMap.put(tableName + "-xml", accessCount + 1);
            return String.format((globalConfig.getOutputDir().replace("/src/main/java", "/src/main/resources")
                    + "/myBatis/%s/%s" + ConstVal.XML_SUFFIX), tableNamePrefix + "/", entityName);
        }
        accessCount = countMap.getOrDefault(tableName + "-service", 0);
        if (accessCount < 2 && null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
            countMap.put(tableName + "-service", accessCount + 1);
            return String.format(
                    (pathInfo.get(ConstVal.SERVICE_PATH) + "/%s" + tableInfo.getServiceName() + ConstVal.JAVA_SUFFIX),
                    tableNamePrefix + "/", entityName);
        }
        accessCount = countMap.getOrDefault(tableName + "-serviceImpl", 0);
        if (accessCount < 2 && null != tableInfo.getServiceImplName()
                && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
            countMap.put(tableName + "-serviceImpl", accessCount + 1);
            return String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + "/%s" + tableInfo.getServiceImplName()
                    + ConstVal.JAVA_SUFFIX), tableNamePrefix + "/", entityName);
        }
        accessCount = countMap.getOrDefault(tableName + "-controller", 0);
        if (accessCount < 2 && null != tableInfo.getControllerName()
                && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
            countMap.put(tableName + "-controller", accessCount + 1);
            return String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator
                    + tableInfo.getControllerName() + ConstVal.JAVA_SUFFIX), tableNamePrefix + "/", entityName);
        }
        return null;
    }
}
