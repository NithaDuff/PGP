package com.main;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class ApplicationConnectionLoad {
	
	public static void main(String[] args) throws MasterNotRunningException,IOException {
		Configuration conf = HBaseConfiguration.create();
		conf.clear();
		
		conf.set("hbase.zookeeper.quorum", "ip-20-0-31-210.ec2.internal");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		
		HBaseAdmin admin = new HBaseAdmin(conf);
		int operation = Integer.parseInt(args[0]);
		if(!admin.tableExists(args[1]) && operation != 1) {
			System.out.println("Table does not exist");
			admin.close();
			return;
		}
		HBaseTable hb = new HBaseTable(admin, conf, args[1]);
		
		switch(operation) {
		case 1:hb.createTable(args);
			break;
		case 2: hb.putTable(args);
			break;
		case 3: hb.deleteTable();
			break;
		case 4: hb.deleteValue(args);
			break;
		case 5: hb.deleteCF(args);
			break;
		case 6: hb.printData();
		}
	}
}

class HBaseTable {
	String tableName = "";
	HBaseAdmin admin = null;
	Configuration conf = null;
	public HBaseTable(HBaseAdmin admin,Configuration conf, String tableName) {
		this.admin = admin;
		this.tableName = tableName;
		this.conf = conf;
	}
	
	public  void deleteCF(String[] args) throws IOException {
		//5 <table_name> <cf>
			admin.deleteColumn(tableName, args[2]);
			System.out.println("Column "+args[2]+" deleted sucessfully from table "+tableName+"!");
	}

	public  void printData() throws IOException {
		//6 <table_name>
		HTable table = new HTable(conf, tableName);
		Scan s = new Scan();
		ResultScanner sc = table.getScanner(s);
		for(Result r : sc) {
			for(KeyValue kv : r.raw()) {
				System.out.println(new String(kv.getRow())+" ");
				System.out.println(new String(kv.getFamily())+" ");
				System.out.println(new String(kv.getQualifier())+" ");
				System.out.println(new String(kv.getTimestamp()+" "));
				System.out.println(new String(kv.getValue()));
				System.out.println("\n---------------------------------------------------------\n");
			}
		}
		
	}

	public  void deleteValue(String[] args) throws IOException {
		// 4 <table_name> <rowkey>
		String rowKey = args[2];
		HTable table = new HTable(conf, tableName);
		ArrayList<Delete> list = new ArrayList<Delete>();
		Delete del = new Delete(rowKey.getBytes());
		list.add(del);
		table.delete(list);
		table.close();
		System.out.println("delete record " + rowKey + " ok.");
	}

	public  void deleteTable() throws IOException {
		//3 <table_name>
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		System.out.println("Table "+tableName+" deleted!");
	}

	public  void putTable(String[] args) throws IOException {
		//2 <table_name> <row_key> <cf> <qualifier> <value>
		String rowKey = args[2];
		String cf = args[3];
		String qualifier = args[4];
		String value = args[5];
		HTable table = new HTable(conf, tableName);
		Put put = new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(cf),Bytes.toBytes(qualifier),Bytes.toBytes(value));
		table.put(put);
		table.close();
		System.out.println("Sucessfully inserted value -> "+cf+":"+qualifier+":"+value);
	}

	public void createTable(String[] args) throws IOException {
		//1 <table_name> <n> <cf1> <cf2>, ... ,<cfn>
		//tableName = "table_name";
		if(admin.tableExists(tableName)) {
			System.out.println("Table ["+tableName+"] already exists");
		} else {
			HTableDescriptor table = new HTableDescriptor(tableName);
			for(int i = 0; i < Integer.parseInt(args[2]); i++) {
				table.addFamily(new HColumnDescriptor(args[3+i]));
			}
			admin.createTable(table);
			System.out.println("Table "+tableName+" created sucessfully!");
		}
	}
}
