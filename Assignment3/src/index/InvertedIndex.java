package index;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndex {
	public static class InvertedIndexMapper extends Mapper<Object, Text, Text, Text> {
		private Text nameKey = new Text();
		private Text fileNameValue = new Text();

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String data = value.toString();
			String[] field = data.split(",", -1);
			String firstName = null;
			if (null != field && field.length == 9 && field[0].length() > 0) {
				firstName = field[0];
				String fileName =  ((FileSplit) context.getInputSplit()).getPath().getName();
				nameKey.set(firstName);
				fileNameValue.set(fileName);
				context.write(nameKey, fileNameValue);
			}
		}
	}

	public static class InvertedIndexReducer extends Reducer<Text, Text, Text, Text> {
		private Text result = new Text();

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (Text value : values) {
				if (first) {
					first = false;
				} else {
					sb.append(" ");
				}
				if (sb.lastIndexOf(value.toString()) < 0) {
					sb.append(value.toString());
				}
			}
			result.set(sb.toString());
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws Exception {
		FileUtils.deleteDirectory(new File(args[1]));
		Configuration conf = new Configuration();
		Job job = new Job(conf);
		job.setJarByClass(InvertedIndex.class);
		job.setJobName("Inverted Index");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(InvertedIndexMapper.class);
		job.setReducerClass(InvertedIndexReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}