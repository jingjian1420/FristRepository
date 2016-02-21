import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.config.Configuration;

import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;



public class MyBatisGeneratorTool {

	public static void main(String[] args) {  
        List<String> warnings = new ArrayList<String>();  
        boolean overwrite = true;  
        String genCfg = "/spring-mybatis-generator/generator.xml"; //src/*/resources的一级目录下
        File configFile = new File(MyBatisGeneratorTool.class.getResource(genCfg).getFile());  
        ConfigurationParser cp = new ConfigurationParser(warnings);  
        Configuration config = null;  
        try {  
            config = cp.parseConfiguration(configFile);
        } catch (IOException e) {
            e.printStackTrace();  
        } catch (XMLParserException e) {  
            e.printStackTrace();  
        }  
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);  
        MyBatisGeneratorProxy myBatisGenerator = null;  
        try {  
            myBatisGenerator = new MyBatisGeneratorProxy(config, callback, warnings);  
        } catch (InvalidConfigurationException e) {  
            e.printStackTrace();  
        }  
        try {
        	System.out.println("begin generate......");
            myBatisGenerator.generate(null);
            System.out.println("end generate......");
        } catch (SQLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
}
