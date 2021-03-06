package pinguo.rocket.mq.comm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * properties文件读取类
 */
public class PropertiesHelper {
    private ResourceBundle rb;

    /**
     * 初始化
     *
     * @param filePath
     */
    public PropertiesHelper(String filePath) {
        InputStream inStream;
        try {
            // 解决jar路径问题
            inStream = this.getClass().getResourceAsStream("/config/production/rmq.properties");
            this.rb = new PropertyResourceBundle(inStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回string
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return this.rb.getString(key);
    }
}
