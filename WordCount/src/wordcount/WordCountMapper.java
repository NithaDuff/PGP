//MAPPER

package wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		String inputLine = value.toString().toLowerCase();
		String[] strArr = inputLine.split(" ");
		for (String w : strArr) {
//remove all non alphabets from string.
			context.write(new Text(w.replaceAll("[^a-zA-Z]", "")), new IntWritable(1));
		}
	}

}         