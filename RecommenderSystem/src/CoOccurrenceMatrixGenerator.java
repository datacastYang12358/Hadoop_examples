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

public class CoOccurrenceMatrixGenerator {
    public static class MatrixGeneratorMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    	 
        // map method
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //value = userid \t movie1: rating, movie2: rating...
            //key = movie1: movie2 value = 1
            //calculate each user rating list: <movieA, movieB>
            String line = value.toString().trim();
            String[] user_movie_rate = line.split("\t");
            String[] movie_rate = user_movie_rate[1].split(",");
            for (int i = 0; i < movie_rate.length; i++) {
                String movieA = movie_rate[i].split(":")[0];
                for (int j = 0; j < movie_rate.length; j++) {
                    String movieB = movie_rate[j].split(":")[0];
                    context.write(new Text(movieA + ":" + movieB), new IntWritable(1));
                }
            }

        }
    }

    public static class MatrixGeneratorReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        // reduce method
        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            //key movie1:movie2 value = iterable<1, 1, 1>
            //calculate each two movies have been watched by how many people
            int sum = 0;
            Iterator<IntWritable> iterator = values.iterator();
            while (iterator.hasNext()) {
                sum += iterator.next().get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception{

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setMapperClass(MatrixGeneratorMapper.class);
        job.setReducerClass(MatrixGeneratorReducer.class);

        job.setJarByClass(CoOccurrenceMatrixGenerator.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        TextInputFormat.setInputPaths(job, new Path(args[0]));
        TextOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);

    }
}
