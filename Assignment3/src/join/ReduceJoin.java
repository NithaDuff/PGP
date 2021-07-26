package join;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ReduceJoin {
	public static class EmpMapper extends Mapper<Object, Text, Text, Text> {
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			if (value.toString().contains("EMPNO")) {
				return;
			} else {
				String record = value.toString();
				String[] parts = record.split(",");
				context.write(new Text(parts[7]), new Text("emp:" + parts[0] + ":" + parts[5]));
			}
		}
	}

	public static class ReduceJoinReducer extends Reducer<Text, Text, Text, Text> {
		HashMap<Integer, String> department = null;

		public void setup(Context context) throws IOException, InterruptedException {
			department = new HashMap<Integer, String>();
			Path[] files = DistributedCache.getLocalCacheFiles(context.getConfiguration());
			if (files != null && files.length > 0) {
				try {
					String line = "";
					for (Path p : files) {
						BufferedReader reader = new BufferedReader(new FileReader(p.toString()));
						line = reader.readLine();
						while ((line = reader.readLine()) != null) {
							String[] parts = line.split(",");
							department.put(Integer.parseInt(parts[0]), parts[2]);
						}
					}
				} catch (Exception e) {
					System.out.println("Unable to read the File");
					System.exit(1);
				}
			} else {
				throw new InterruptedException("No files in distributed cache");
			}
		}

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			String location = "";
			String output = "";
			String emps = "";
			Float totSal = 0.0f;
			int count = 0;
			for (Text value : values) {
				String parts[] = value.toString().split(":");
				count++;
				emps += emps.length() > 0 ? "," + parts[1] : parts[1];
				totSal += Float.parseFloat(parts[2]);
				output = count + "[Employees:" + emps + "\tTotal Salary:" + totSal + "]";
			}
			location = department.get(Integer.parseInt(key.toString()));

			context.write(new Text(location), new Text(output));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "Distributed Cache");
		job.setJarByClass(ReduceJoin.class);
		job.setNumReduceTasks(1);
		job.setReducerClass(ReduceJoinReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		try {
			DistributedCache.addCacheFile(new Path("hdfs:///user/edureka_1095243/inputs/dept.csv").toUri(),
					job.getConfiguration());
		} catch (Exception e) {
			System.exit(1);
		}
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, EmpMapper.class);
		Path outputPath = new Path(args[1]);

		FileOutputFormat.setOutputPath(job, outputPath);
		outputPath.getFileSystem(conf).delete(outputPath);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}