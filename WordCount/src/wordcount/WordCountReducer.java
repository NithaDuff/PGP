//REDUCER

package wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	@Override
	protected void reduce(Text key, Iterable<IntWritable> value,
			Reducer<Text, IntWritable, Text, IntWritable>.Context context) 
					throws IOException, InterruptedException {
		Integer count = 0;
		for (IntWritable i : value) {
			count ++;
		}
		//mark -1 for words with occurrences >100
		context.write(key, count<100?new IntWritable(count):new IntWritable(-1));
	}

}
