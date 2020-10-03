package com.yeweihui.common.utils;


import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 * https://mp.baomidou.com/guide/generator.html#%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B
 */
public class CodeGenerator {

//    private static String projectPath = "/Users/wangjianfeng/Documents/workspace-sts/bond-investment-api";
//    private static String dbUrl = "jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf8";
//    private static String projectPath = "E:\\ideaWorkSpace\\bond-investment-api";
    private static String projectPath = "F:\\ideaworkspace\\ywhserver\\yeweihui-service";
    private static String commonModelPath = "D:\\workspace\\ywhserver\\yeweihui-common";
    private static String dbUrl = "jdbc:mysql://129.204.83.206:3306/yeweihui-dev?useUnicode=true&characterEncoding=utf8&useSSL=true&verifyServerCertificate=false";
    //private static String dbUrl = "jdbc:mysql://211.157.186.157:29782/fcdb?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true";

    /**
     * 读取控制台内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setBaseColumnList(true);
        gc.setOpen(false);
        gc.setMapperName("%sDao");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dbUrl);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("jinMu@123!456*?");
        mpg.setDataSource(dsc);
        // debt_query_temp

        PackageConfig pc = new PackageConfig();
        pc.setParent("com.yeweihui.modules.accounts");
        pc.setEntity("entity");
        pc.setMapper("dao");

        //pc.setXml("xml");
        mpg.setPackageInfo(pc);

//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl(dbUrl);
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
//        dsc.setUsername("dzh_sync");
//        dsc.setPassword("Pm3Y_#s7BfT7*bQw");
//        mpg.setDataSource(dsc);
//
//        PackageConfig pc = new PackageConfig();
//        pc.setParent("com.ccxi.api");
//        pc.setEntity("entity.fcdb");
//        pc.setMapper("mapper.fcdb");//todo 使用fcdb还要先修改mapper.java.vm  @DS("fcdb")
//        pc.setXml("xml");
//        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置, 自定义配置会被优先输出
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/accounts/" + tableInfo.getEntityName() + "Dao.xml";
//                return projectPath + "/src/main/resources/mapper/fcdb/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);


        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity("/templates/entity.java.vm");
        templateConfig.setMapper("/templates/mapper.java.vm");
        templateConfig.setXml("/templates/mapper.xml.vm");
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setController(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
       // strategy.setSuperEntityClass("com.yeweihui.modules.bfly.entity.BaseEntity");
        strategy.setEntityLombokModel(true);
        // 写于父类中的公共字段
       // strategy.setSuperEntityColumns("created_at", "updated_at");
//        String s = "tq_fin_proincstatementnew";
        String input = scanner("表名，多个英文逗号分割，生成所有表输入all");
        if (input.equals("all")) {
            strategy.setExclude();
        } else {
            String[] tables = input.split(",");
            List<String> tableList = new ArrayList<>();
            for (String table : tables) {
                tableList.add(table.trim());
            }
            strategy.setInclude(tableList.toArray(new String[0]));
        }
        mpg.setStrategy(strategy);
        mpg.execute();
    }

}
