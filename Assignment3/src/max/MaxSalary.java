package max;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxSalary {

	public static class MaxMapper extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			String[] fields = value.toString().split(",");
			if (fields[0].equals("EMPNO")) {
				return;
			}
			String result = fields[5] + ":" + fields[0];
			context.write(new Text(fields[7]), new Text(result));
		}
	}

	public static class MaxReducer extends Reducer<Text, Text, Text, Text> {

		@Override
		protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			String[] value;
			String empno = "";
			float maxSalary = Float.MIN_VALUE;
			float sal = 0.0f;
			for (Text i : values) {
				value = i.toString().split(":");
				sal = Float.parseFloat(value[0]);
				if (sal > maxSalary) {
					empno = value[1];
					maxSalary = sal;
				}
			}
			context.write(key, new Text(maxSalary + "\t[Employee:" + empno + "]"));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = new Job(conf);
		job.setJarByClass(MaxSalary.class);
		job.setJobName("Max Salary");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(MaxMapper.class);
		job.setReducerClass(MaxReducer.class);
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.waitForCompletion(true);

	}
}
