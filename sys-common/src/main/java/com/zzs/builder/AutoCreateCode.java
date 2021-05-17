package com.zzs.builder;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 代码生成器
 *
 * @author mm013
 * @Date 2021/5/10 15:22
 */
public class AutoCreateCode {
    @Value("${spring.datasource.url}")
    public static String url;

    @Value("${spring.datasource.driver-class-name}")
    public static String driverName;

    @Value("${spring.datasource.username}")
    public static String userName;

    @Value("${spring.datasource.password}")
    public static String password;

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + "/" + scanner("请输入模块名");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("mountain");
        gc.setOpen(false);
        gc.setMapperName("%sDao");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        // XML 映射文件中是否生成基本的 resultMap
        gc.setBaseResultMap(true);
        // XML 映射文件中是否生成基本的 columList
        gc.setBaseColumnList(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/background_system?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        dsc.setDriverName("org.gjt.mm.mysql.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("");
        pc.setParent("com.zzs");
        pc.setMapper("dao");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        // 模板引擎
        String templatePath = "/templates/mapper.xml.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();

        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity(null);
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 是否使用 lombok 注解
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 需要生成的表, 支持多表一起生成，以数组形式填写

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入表名:");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                if (ipt.indexOf("_") == -1) {
                    throw new MybatisPlusException("请输入正确的表前缀" + ipt + "！");
                }
                String s = ipt.substring(0, ipt.indexOf("_") + 1);
                strategy.setTablePrefix(s);
                strategy.setInclude(ipt);
            }
        }
        strategy.setSuperControllerClass("com.zzs.base.BaseController");
        strategy.setSuperServiceClass("com.zzs.base.BaseService");
        strategy.setSuperMapperClass("com.zzs.base.BaseDao");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // 执行生成
        mpg.execute();
    }
}

