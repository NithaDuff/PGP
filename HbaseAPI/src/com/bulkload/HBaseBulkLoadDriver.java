package com.bulkload;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HBaseBulkLoadDriver extends Configured implements Tool {	
    private static final String DATA_SEPERATOR = ",";	
    private static final String TABLE_NAME = "Airlines";	
    private static final String COLUMN_FAMILY_1="flightDetails";	
    /**
     * HBase bulk import example
     * Data preparation MapReduce job driver
     * 
     * args[0]: HDFS input path
     * args[1]: HDFS output path
     * 
     */
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {		
        try {
            int response = ToolRunner.run(HBaseConfiguration.create(), new HBaseBulkLoadDriver(), args);			
            if(response == 0) {				
                System.out.println("Job is successfully completed...");
            } else {
                System.out.println("Job failed...");
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        int result=0;
        String outputPath = args[1];		
        Configuration configuration = getConf();		
        configuration.set("data.seperator", DATA_SEPERATOR);		
        configuration.set("hbase.table.name",TABLE_NAME);		
        configuration.set("COLUMN_FAMILY_1",COLUMN_FAMILY_1);		
        Job job = new Job(configuration);		
        job.setJarByClass(HBaseBulkLoadDriver.class);		
        job.setJobName("Bulk Loading HBase Table::"+TABLE_NAME);		
        job.setInputFormatClass(TextInputFormat.class);		
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);		
        job.setMapperClass(HBaseBulkLoadMapper.class);		
        FileInputFormat.addInputPaths(job, args[0]);		
        FileSystem.getLocal(getConf()).delete(new Path(outputPath), true);		
        FileOutputFormat.setOutputPath(job, new Path(outputPath));		
        job.setMapOutputValueClass(Put.class);		
        HFileOutputFormat.configureIncrementalLoad(job, new HTable(configuration,TABLE_NAME));		
        job.waitForCompletion(true);		
        if (job.isSuccessful()) {
            HBaseBulkLoad.doBulkLoad(outputPath, TABLE_NAME);
        } else {
            result = -1;
        }
        return result;
    }

    class HBaseBulkLoadMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
        private String hbaseTable;	
        private String dataSeperator;
        private String columnFamily1;
        private ImmutableBytesWritable hbaseTableName;
        public void setup(Context context) {
            Configuration configuration = context.getConfiguration();		
            hbaseTable = configuration.get("hbase.table.name");		
            dataSeperator = configuration.get("data.seperator");		
            columnFamily1 = configuration.get("COLUMN_FAMILY_1");		
            hbaseTableName = new ImmutableBytesWritable(Bytes.toBytes(hbaseTable));		
        }
        
        

        public void map(LongWritable key, Text value, Context context) {
        	String [] columns = {"ID" ,"Month" ,"DayofMonth","DayOfWeek" ,
        			 "DepTime","CRSDepTime",
        			 "ArrTime",
        			 "CRSArrTime",
        			 "UniqueCarrier",
        			 "FlightNum",
        			 "TailNum",
        			 "ActualElapsedTime",
        			 "CRSElapsedTime",
        			 "AirTime",
        			 "ArrDelay",
        			 "DepDelay",
        			 "Origin",
        			 "Dest",
        			 "Distance",
        			 "TaxiIn",
        			 "TaxiOut",
        			 "Cancelled",
        			 "CancellationCode",
        			 "Diverted",
        			 "CarrierDelay",
        			 "WeatherDelay","NASDelay","SecurityDelay","LateAircraftDelay"};
            try {		
                String[] values = value.toString().split(dataSeperator);			
                String rowKey = values[0];
                int  i = 1;
                Put put = new Put(Bytes.toBytes(rowKey));	
                for(String col : columns) {
                	put.add(Bytes.toBytes(columnFamily1), Bytes.toBytes(col), Bytes.toBytes(values[i++]));
                }
                
                
                context.write(hbaseTableName, put);			
            } catch(Exception exception) {			
                exception.printStackTrace();			
            }
        }
    }

    static class HBaseBulkLoad {	
        /**
         * doBulkLoad.
         *
         * @param pathToHFile path to hfile
         * @param tableName 
         */
        public static void doBulkLoad(String pathToHFile, String tableName) {
            try {		
                Configuration configuration = new Configuration();			
                configuration.set("mapreduce.child.java.opts", "-Xmx1g");	
                HBaseConfiguration.addHbaseResources(configuration);	
                LoadIncrementalHFiles loadFfiles = new LoadIncrementalHFiles(configuration);	
                HTable hTable = new HTable(configuration, tableName);	
                loadFfiles.doBulkLoad(new Path(pathToHFile), hTable);	
                System.out.println("Bulk Load Completed..");		
            } catch(Exception exception) {			
                exception.printStackTrace();			
            }		
        }	
    }
}