package habse;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 这个类是Hbase的util 类
 * @author Chris
 */
public class HbaseUtil {
	
	/**
	 * 创建表的方法
	 * 
	 * @param tableName
	 *            创建ragion分区表
	 * @param family
	 *            需要创建的列族名称
	 * @throws Exception
	 *             创建时的异常
	 */
	public  static void createTbaleBySplit(String tableName, String[] family)  {
		Admin admin=null;
		Connection connection=null;
		Configuration configuration;
		try {
			configuration = HBaseConfiguration.create();// 设置配置文件
			connection = ConnectionFactory.createConnection(configuration);// 创建一个连接
			admin = connection.getAdmin();
			TableName tname = TableName.valueOf(tableName);// 转换
			// 如果表名存在就删除
			if (admin.tableExists(tname)) {
				admin.disableTable(tname);
				admin.deleteTable(tname);
			}
			// 开始创建表.这里的表名需要进行序列化
			 HTableDescriptor     hTableDescriptor=new HTableDescriptor(TableName.valueOf(tableName));
			 HColumnDescriptor  hColumnDescriptor=new  HColumnDescriptor(family[0]);
			 hTableDescriptor.addFamily(hColumnDescriptor);
			 admin.createTable(hTableDescriptor,getSplitKeysByRandom());// 创建表创建分区
			System.err.println("创建分区表成功");
		 } catch (Exception e) {
			 e.printStackTrace();
			 System.exit(1);
		}finally {
			// 关闭连接释放资源
			if(admin!=null){
				try {
					admin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(connection!=null){
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
    /**
     * 这里RowKey的命名规则是两位随机数加+UUID
     * @return
     */
    public static  byte[][] getSplitKeysByRandom() {  
        String[] keys = new String[] { "10|", "20|", "30|", "40|", "50|",  
                "60|", "70|", "80|", "90|" };  
        byte[][] splitKeys = new byte[keys.length][];  
        TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);//升序排序  
        for (int i = 0; i < keys.length; i++) {  
            rows.add(Bytes.toBytes(keys[i]));  
        }  
        Iterator<byte[]> rowKeyIter = rows.iterator();  
        int i=0;  
        while (rowKeyIter.hasNext()) {  
            byte[] tempRow = rowKeyIter.next();  
            rowKeyIter.remove();  
            splitKeys[i] = tempRow;  
            i++;  
        }  
        return splitKeys;  
    }
    /**
     * 获取两位随机数
     * @return
     */
    public static String getRandomNumber(){  
        String ranStr = Math.random()+"";  
        int pointIndex = ranStr.indexOf(".");  
        return ranStr.substring(pointIndex+1, pointIndex+3);  
    } 
    public static void main(String[] args) {
    	createTbaleBySplit("test",new String[]{"info"});
	}
}
