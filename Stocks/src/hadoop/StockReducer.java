package hadoop;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StockReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

	@Override
	public void reduce(Text key, Iterable<FloatWritable> values, Context context)
			throws IOException, InterruptedException {

		float maxHighPrice = Float.MIN_VALUE;

		// Iterate all high prices and calculate maximum
		for (FloatWritable value : values) {
			maxHighPrice = value.get()>maxHighPrice?value.get():maxHighPrice;
		}

		// Write output
		context.write(key, new FloatWritable(maxHighPrice));
	}
}