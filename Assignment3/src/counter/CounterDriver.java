package counter;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.*;

public class CounterDriver extends Configured implements Tool {
	enum InfoCounter {
		MALCOLM, DUNCAN
	};

	public static class CounterMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String sp = "";
			String[] fields = value.toString().trim().split(" ");
			if (fields.length > 2) {
				sp = fields[0].trim();
				if (sp.equals("MALCOLM.")) {
					context.getCounter(InfoCounter.MALCOLM).increment(1);
				}
				if (sp.equals("DUNCAN.")) {
					context.getCounter(InfoCounter.DUNCAN).increment(1);
				}
			}
			
		}
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Configuration(), new CounterDriver(), args);
		System.exit(exitCode);
	}

	@Override
	public int run(String[] args) throws Exception {
		Job job = Job.getInstance(getConf());
		job.setJarByClass(CounterDriver.class);
		job.setJobName("Speech Counter");

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(CounterMapper.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setNumReduceTasks(0);

		boolean success = job.waitForCompletion(true);
		if (success) {
			long duncan = job.getCounters().findCounter(InfoCounter.DUNCAN).getValue();
			long malcolm = job.getCounters().findCounter(InfoCounter.MALCOLM).getValue();

			System.out.println("Duncan   = " + duncan);
			System.out.println("Malcolm   = " + malcolm);
			return 0;
		} else
			return 1;
	}
}
