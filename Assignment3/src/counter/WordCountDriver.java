package counter;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountDriver {

	public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		@Override
		public void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			String inputLine = value.toString().toLowerCase();
			String[] strArr = inputLine.split(" ");
			String word = "";
			for (String w : strArr) {
				word = w.replaceAll("[^a-zA-Z0-9]", "");
				if (word.length() <= 5) {
					continue;
				}
				context.write(new Text(word), new IntWritable(1));
			}
		}
	}

	public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
				Reducer<Text, IntWritable, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			Integer count = 0;
			for (IntWritable i : values) {
				count++;
			}
			context.write(key, new IntWritable(count));
		}
	}

	public static class WordFilterMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		@Override
		public void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			String[] strArr = value.toString().split("\t");
			int count = Integer.parseInt(strArr[1]);
			if(count>100) {
				context.write(new Text(strArr[0]), new IntWritable(count));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Path out = new Path(args[1]);
		
		Configuration conf1 = new Configuration();
		Job job1 = new Job(conf1);
		job1.setJarByClass(WordCountDriver.class);
		job1.setJobName("Word Counter");
		FileInputFormat.addInputPath(job1, new Path(args[0]));
		FileOutputFormat.setOutputPath(job1, out);
		job1.setMapperClass(WordCountMapper.class);
		job1.setReducerClass(WordCountReducer.class);
		job1.setNumReduceTasks(1);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(IntWritable.class);
		job1.waitForCompletion(true);

		Configuration conf2 = new Configuration();
		Job job2 = new Job(conf2);
		job2.setJarByClass(WordCountDriver.class);
		job2.setJobName("Word Filter");
		FileInputFormat.addInputPath(job2, new Path(out +"/part-r-00000"));
		FileOutputFormat.setOutputPath(job2, new Path(args[2]));
		job2.setMapperClass(WordFilterMapper.class);
		job2.setNumReduceTasks(1);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(IntWritable.class);
		job2.waitForCompletion(true);
		FileSystem fs = FileSystem.get(conf1);
		fs.delete(new Path(args[1]));

	}
}
