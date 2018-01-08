import java.io.*;
import java.util.TreeMap;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

			
public class deJobs {
	public static class MapClass extends Mapper<LongWritable,Text,Text,Text> 
	   {
	      public void map(LongWritable key, Text value, Context context)
	      {	    	  
	         try{
	        	 
	            String[] str = value.toString().split("\t");	
	        
	            
	            if((str[4].equals("DATA ENGINEER"))&&(str[1].equals("CERTIFIED")))
	            {
	            
	            String c = str[4]+"\t"+str[7];
	            	context.write(new Text(str[8]),new Text(c));
	            }
	            
	            
	         }
	         catch(Exception e)
	         {
	            System.out.println(e.getMessage());
	         }
	      }	
	   }
	
	public static class yearPartitioner extends Partitioner<Text,Text>
	   {

		public int getPartition(Text key, Text values, int numReduceTasks) {
			
			//String b[]="";
			 
			   			          	
	        	String[] b=values.toString().split("\t");
	   
	        	if(b[1].equals("2011"))
	        	{
	        		return 0;
	        	}
	        	else if(b[1].equals("2012"))
	        	{
	        		return 1;
	        	}
	        	else if(b[1].equals("2013"))
	        	{
	        		return 2;
	        	} 
	        	else if(b[1].equals("2014"))
	        	{
	        		return 3;
	        	}
	        	else if(b[1].equals("2015"))
	        	{
	        		return 4;
	        	}
	        	else
	        	{
	        		return 5;
	        	}
	        		        		      		
	         }
			
			
			
		}    
		    
	   
	public static class ReduceClass extends Reducer<Text, Text, NullWritable, Text>
	{
	      public TreeMap<Long, Text> tm = new TreeMap<Long, Text>();
			public void reduce(Text key, Iterable<Text> values, Context con) throws IOException, InterruptedException
			{
				long count=0;
				//String year="";
				//String job="";
				String myVal="";
				for(Text val:values)
				{
					String[] str = val.toString().split("\t");
					
						count++;
						myVal = str[1]+"\t"+key+"\t"+str[0];
					
					
				}
				String myValue = myVal+"\t"+count;
				tm.put(new Long(count), new Text(myValue));
				if(tm.size()>1)
				{
					tm.remove(tm.firstKey());
				}
		   }
			public void cleanup(Context con) throws IOException, InterruptedException
			{
				for(Text t:tm.descendingMap().values())
				{
					con.write(NullWritable.get(), t);
				}
			}
	}
	
	
	public static void main(String[] args) throws Exception {
	    Configuration conf = new Configuration();
	    //conf.set("name", "value")
	    //conf.set("mapreduce.input.fileinputformat.split.minsize", "134217728");
	    Job job = Job.getInstance(conf, "job Count");
	    job.setJarByClass(deJobs.class);
	    job.setMapperClass(MapClass.class);
	    job.setPartitionerClass(yearPartitioner.class);
	    //job.setCombinerClass(ReduceClass.class);
	    job.setReducerClass(ReduceClass.class);
	    job.setNumReduceTasks(6);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(NullWritable.class);
	    job.setOutputValueClass(Text.class);
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	  }
}


