import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class BulkLoad {
	private static Configuration conf = null;
	static {
		conf = HBaseConfiguration.create();
		conf.clear();
		conf.set("hbase.zookeeper.quorum", "ip-20-0-31-210.ec2.internal");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
	}

	@SuppressWarnings("deprecation")
	public static void creatTable(String tableName, String[] familys) throws Exception {
		@SuppressWarnings("resource")

		HBaseAdmin admin = new HBaseAdmin(conf);

		if (admin.tableExists(tableName)) {
			System.out.println("table already exists!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			for (int i = 0; i < familys.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(familys[i]));
			}
			admin.createTable(tableDesc);
			System.out.println("create table " + tableName + " ok.");
		}
	}

	@SuppressWarnings("deprecation")
	public static void addRecord(String tableName, String rowKey, String family, String qualifier, String value)
			throws Exception {
		try {
			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
			table.put(put);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		String fileName = args[0];
		String line = null;
		String tableName = args[1];
		int lineno = 1;
		String[] familys = { "user", "src", "dst" };
		System.out.println("===========create table========");
		BulkLoad.creatTable(tableName, familys);
		System.out.println("=========insert records========");
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {

				if (lineno == 1) {
					lineno++;
				} else {

					String[] values = line.split(",");
					BulkLoad.addRecord(tableName, values[0], familys[0], "ID", values[1]);
					BulkLoad.addRecord(tableName, values[0], familys[0], "Duration", values[2]);
					BulkLoad.addRecord(tableName, values[0], familys[1], "StartDate", values[3]);
					BulkLoad.addRecord(tableName, values[0], familys[1], "StartStation", values[4]);
					BulkLoad.addRecord(tableName, values[0], familys[1], "StartStationCode", values[5]);
					BulkLoad.addRecord(tableName, values[0], familys[2], "EndDate", values[6]);
					BulkLoad.addRecord(tableName, values[0], familys[2], "EndStation", values[7]);
					BulkLoad.addRecord(tableName, values[0], familys[2], "EndStationCode", values[8]);
					BulkLoad.addRecord(tableName, values[0], familys[0], "BikeNumber", values[9]);
					BulkLoad.addRecord(tableName, values[0], familys[0], "Subscriber", values[10]);
					BulkLoad.addRecord(tableName, values[0], familys[0], "Zipcode", values[11]);
					System.out.println("Records are getting inserted. Please wait");
				}
			}
			System.out.println("Successfully inserted all records");
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}
}
