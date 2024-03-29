package RecommenderSystem;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.Iterator;

public class DataDividerByUser {
    public static class DataDividerMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
    	 
        // map method
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //input user,movie,rating
            String line = value.toString().trim();
            String[] user_movie_value = line.split(",");
            //divide data by user
            context.write(new IntWritable(Integer.valueOf(user_movie_value[0])), new Text(user_movie_value[1] + ":" + user_movie_value[2]));
        }
    }

    public static class DataDividerReducer extends Reducer<IntWritable, Text, IntWritable, Text> {
        // reduce method
        @Override
        public void reduce(IntWritable key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {

            //merge data for one user
            StringBuilder sb = new StringBuilder();
            Iterator<Text> iterator = values.iterator();
            while (iterator.hasNext()) {
                sb.append(",").append(iterator.next());
            }
            context.write(key, new Text(sb.toString().replaceFirst(",","")));
        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setMapperClass(DataDividerMapper.class);
        job.setReducerClass(DataDividerReducer.class);

        job.setJarByClass(DataDividerByUser.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        TextInputFormat.setInputPaths(job, new Path(args[0]));
        TextOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}
