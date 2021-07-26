package wordcount;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class AmaSort
{
    static Configuration conf = null;
    private static boolean caseSensitive;
    private static Text word = new Text();

    public static class LineMapper extends Mapper<Object, Text, Text, NullWritable>{

        public void setup(Context context) throws IOException, InterruptedException
        {
            conf = context.getConfiguration();
            caseSensitive = conf.getBoolean("amasort.case.sensitive", true);

        }

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException
        {
            String line = (caseSensitive) ? value.toString() : value.toString().toLowerCase();
            word.set(line);
            context.write(word, NullWritable.get());

        }
    }

    public static class ForwardReducer extends Reducer<Text, NullWritable, Text, NullWritable>
    {
        private NullWritable result = NullWritable.get();

        public void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException
        {
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception
    {
    Configuration conf = new Configuration();
    //GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);
    //String[] remainingArgs = optionParser.getRemainingArgs();
//  Job job = Job.getInstance(conf, "word sort");
    Job job = new Job(conf, "word sort");
    job.setJarByClass(AmaSort.class);
    job.setMapperClass(LineMapper.class);
    // job.setCombinerClass(ForwardReducer.class);
    job.setReducerClass(ForwardReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(NullWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}