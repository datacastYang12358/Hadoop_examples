package RecommenderSystem;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Multiplication {
    public static class CooccurrenceMapper extends Mapper<LongWritable, Text, Text, Text> {
    	 
        // map method
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //input: movieB \t movieA=relation
            String line = value.toString().trim();
            String[] movie_movie_ralation = line.split("\t");
            //pass data to reducer
            context.write(new Text(movie_movie_ralation[0]), new Text(movie_movie_ralation[1]));
        }
    }

    public static class RatingMapper extends Mapper<LongWritable, Text, Text, Text> {

        // map method
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            //input: user,movie,rating
            String line = value.toString().trim();
            String[] user_movie_rate = line.split(",");
            //pass data to reducer
            context.write(new Text(user_movie_rate[1]), new Text(user_movie_rate[0] + ":" + user_movie_rate[2]));
        }
    }

    public static class MultiplicationReducer extends Reducer<Text, Text, Text, DoubleWritable> {
        // reduce method
        @Override
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {

            //key = movieB value = <movieA=relation, movieC=relation... userA:rating, userB:rating...>
            //collect the data for each movie, then do the multiplication
            HashMap<String, Double> movie_relation_map = new HashMap<String, Double>();
            HashMap<String, Double> movie_rate_map = new HashMap<String, Double>();
            Iterator<Text> iterator = values.iterator();
            while (iterator.hasNext()) {
                String value =iterator.next().toString().trim();
                if (value.contains("=")) {
                    movie_relation_map.put(value.split("=")[0], Double.parseDouble(value.split("=")[1]));
                } else {
                    movie_rate_map.put(value.split(":")[0], Double.parseDouble(value.split(":")[1]));
                }
            }
            for (Map.Entry<String,Double> entry1 : movie_relation_map.entrySet()) {
                String movie = entry1.getKey();
                double relation = entry1.getValue();
                for (Map.Entry<String, Double> entry2 : movie_rate_map.entrySet()) {
                    String user = entry2.getKey();
                    double rate = entry2.getValue();
                    context.write(new Text(user + ":" + movie), new DoubleWritable(relation * rate));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(Multiplication.class);

        ChainMapper.addMapper(job, CooccurrenceMapper.class, LongWritable.class, Text.class, Text.class, Text.class, conf);
        ChainMapper.addMapper(job, RatingMapper.class, Text.class, Text.class, Text.class, Text.class, conf);

        job.setMapperClass(CooccurrenceMapper.class);
        job.setMapperClass(RatingMapper.class);

        job.setReducerClass(MultiplicationReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, CooccurrenceMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, RatingMapper.class);

        TextOutputFormat.setOutputPath(job, new Path(args[2]));

        job.waitForCompletion(true);
    }
}
