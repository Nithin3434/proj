
import java.io.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;


public class dataengpetitions 
{
	public static class MapClass extends Mapper<LongWritable,Text,Text,Text>
	   {
	      public void map(LongWritable key, Text value, Context context)
	      {	    	  
	         try{
	        	 
	            String[] str = value.toString().split("\t");	
	            //String pet = str[4];
	            //String year =(str[7]);
	            
	            if(str[4].equals("DATA ENGINEER"))
	            {
	            	context.write(new Text(str[7]),new Text(str[4]));
	            }
	            
	            
	         }
	         catch(Exception e)
	         {
	            System.out.println(e.getMessage());
	         }
	      }	
	   }
	      
	      public static class ReduceClass extends Reducer<Text,Text,Text,LongWritable>
		   {
			   // private LongWritable result = new LongWritable();
	    	  
	  		
			    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException,ArrayIndexOutOfBoundsException 
			    {
			      long count=0;
			      //String job1 ="";
			      //String case_status="";
					
			         for (Text val : values)
			         {   
			        	 			        	 
			        	count++;
			        	 			      			        					        			        			        	
			         }
			         		      
			      context.write(key, new LongWritable (count));
			      
			    }
		   }
			    public static void main(String[] args) throws Exception {
				    Configuration conf = new Configuration();
				    //conf.set("name", "value")
				    //conf.set("mapreduce.input.fileinputformat.split.minsize", "134217728");
				    Job job = Job.getInstance(conf, "job Count");
				    job.setJarByClass(dataengpetitions.class);
				    job.setMapperClass(MapClass.class);
				    //job.setCombinerClass(ReduceClass.class);
				    job.setReducerClass(ReduceClass.class);
				    job.setNumReduceTasks(2);
				    job.setMapOutputKeyClass(Text.class);
				    job.setMapOutputValueClass(Text.class);
				    job.setOutputKeyClass(Text.class);
				    job.setOutputValueClass(LongWritable.class);
				    FileInputFormat.addInputPath(job, new Path(args[0]));
				    FileOutputFormat.setOutputPath(job, new Path(args[1]));
				    System.exit(job.waitForCompletion(true) ? 0 : 1);
				  }
		   }

