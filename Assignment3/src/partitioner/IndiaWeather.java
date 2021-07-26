package partitioner;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import java.util.StringTokenizer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Partitioner;

public class IndiaWeather {

	public static class WeatherMapper extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] line = value.toString().toLowerCase().split(",");
			String year = "";
			if (line.length > 1 && line[4].equals("india")) {
				year = line[0].split("-")[0];
				context.write(new Text(year), value);
			}
		}
	}

	public static class MyPartitioner extends Partitioner<Text, Text> {

		public int getPartition(Text key, Text value, int numPartitions) {
			Integer myKey = Integer.parseInt(key.toString());
			if (myKey >= 1700 && myKey < 1800) {
				return 0;
			}
			if (myKey >= 1800 && myKey < 1900) {
				return 1;
			}
			if (myKey >= 1900 && myKey < 2000) {
				return 2;
			}
			if (myKey >= 2000 && myKey < 2100) {
				return 3;
			} else {
				return 4;
			}
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "partitioner");

		job.setJarByClass(IndiaWeather.class);
		job.setNumReduceTasks(4);
		job.setMapperClass(WeatherMapper.class);
		job.setPartitionerClass(MyPartitioner.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
