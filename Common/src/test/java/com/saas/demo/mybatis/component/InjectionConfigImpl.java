package com.saas.demo.mybatis.component;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.IFileCreate;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.FileType;

import java.io.File;

public class InjectionConfigImpl extends InjectionConfig {

    @Override
    public void initMap() {
        this.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建

                checkDir(filePath);
                File file = new File(filePath);
                boolean exist = file.exists();
                if(exist==false)
                    return  true;
                if(fileType.equals(FileType.ENTITY))
                    return  true;
                return false;
            }
        });
    }

}
