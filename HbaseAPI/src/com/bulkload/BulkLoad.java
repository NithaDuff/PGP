package com.bulkload;

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
/* Initialization */
static {
conf = HBaseConfiguration.create();
conf.clear();
conf.set("hbase.zookeeper.quorum", "ip-20-0-31-210.ec2.internal");
conf.set("hbase.zookeeper.property.clientPort", "2181");
}
/* Create a table */
@SuppressWarnings("deprecation")
public static void creatTable(String tableName, String[] familys) throws Exception {
@SuppressWarnings("resource")

HBaseAdmin admin = new HBaseAdmin(conf);

if (admin.tableExists(tableName)) {
System.out.println("table already exists!");
}
else {
HTableDescriptor tableDesc = new HTableDescriptor(tableName);
for (int i = 0; i < familys.length; i++) {
tableDesc.addFamily(new HColumnDescriptor(familys[i]));
}
admin.createTable(tableDesc);
System.out.println("create table " + tableName + " ok.");
}
}


@SuppressWarnings("deprecation")
public static void addRecord(String tableName, String rowKey, String family, String qualifier,
String value) throws Exception {
try {
HTable table = new HTable(conf, tableName);
Put put = new Put(Bytes.toBytes(rowKey));
put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
table.put(put);
//System.out.println("insert record " + rowKey + " to table " + tableName + " ok.");
}
catch (IOException e) {
e.printStackTrace();
}
}

public static void main(String[] args) throws Exception {
String fileName = args[0];
// This will reference one line at a time
String line = null;
String tableName = args[1];
int lineno=1;
String[] familys = { "time", "flight", "status"};
System.out.println("===========create table========");
BulkLoad.creatTable(tableName, familys);
System.out.println("=========insert records========");
try {
// FileReader reads text files in the default encoding.
FileReader fileReader = new FileReader(fileName);
// Always wrap FileReader in BufferedReader.
BufferedReader bufferedReader = new BufferedReader(fileReader);
while ((line = bufferedReader.readLine()) != null) {

if(lineno==1)
{ lineno++;}
else
{

String[] values = line.split(",");
BulkLoad.addRecord(tableName, values[0], familys[0], "year", values[1]);
BulkLoad.addRecord(tableName, values[0], familys[0], "month", values[2]);
BulkLoad.addRecord(tableName, values[0], familys[0], "day_of_month",
values[3]);
BulkLoad.addRecord(tableName, values[0], familys[0], "day_of_week",
values[4]);

BulkLoad.addRecord(tableName, values[0], familys[0], "deptime", values[5]);
BulkLoad.addRecord(tableName, values[0], familys[0], "schdeptime",
values[6]);
BulkLoad.addRecord(tableName, values[0], familys[0], "arrtime", values[7]);
BulkLoad.addRecord(tableName, values[0], familys[0], "scharrtime",
values[8]);
BulkLoad.addRecord(tableName, values[0], familys[0], "arrDelay", values[15]);
BulkLoad.addRecord(tableName, values[0], familys[0], "depDelay",
values[16]);
BulkLoad.addRecord(tableName, values[0], familys[1], "uniqCarrier",
values[9]);
BulkLoad.addRecord(tableName, values[0], familys[1], "flightNum",
values[10]);
BulkLoad.addRecord(tableName, values[0], familys[1], "tailNum", values[11]);
BulkLoad.addRecord(tableName, values[0], familys[1], "origin", values[17]);
BulkLoad.addRecord(tableName, values[0], familys[1], "dest", values[18]);
BulkLoad.addRecord(tableName, values[0], familys[1], "distance", values[19]);
BulkLoad.addRecord(tableName, values[0], familys[2], "cancelled",
values[22]);
BulkLoad.addRecord(tableName, values[0], familys[2], "canCode", values[23]);
BulkLoad.addRecord(tableName, values[0], familys[2], "diverted", values[24]);
BulkLoad.addRecord(tableName, values[0], familys[2], "carrDelay",
values[25]);
BulkLoad.addRecord(tableName, values[0], familys[2], "weatherDelay",
values[26]);
BulkLoad.addRecord(tableName, values[0], familys[2], "nasDelay",
values[27]);
BulkLoad.addRecord(tableName, values[0], familys[2], "securityDelay",
values[28]);
BulkLoad.addRecord(tableName, values[0], familys[2], "lateAircraftDelay",
values[22]);
System.out.println("Records are getting inserted. Please wait");
}
}

System.out.println("Successfully inserted all records");

bufferedReader.close();
} catch (FileNotFoundException ex) {
System.out.println("Unable to open file '" + fileName + "'");
} catch (IOException ex) {
System.out.println("Error reading file '" + fileName + "'");
// Or we could just do this:
// ex.printStackTrace();
}
}
}


