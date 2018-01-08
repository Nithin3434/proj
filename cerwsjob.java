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



public class cerwsjob {
	
	public static class MapClass extends Mapper<LongWritable,Text,Text,Text>
	   {
	      public void map(LongWritable key, Text value, Context context)
	      {	    	  
	         try{
	        	 
	            String[] str = value.toString().split("\t");	
	            
	          
	            
	            if(str[1].equals("CERTIFIED"))
	            {
	      
	            	String a = str[1]+"\t"+str[7];
	            	context.write(new Text(str[8]),new Text(a));
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
			long c=0;
			   			          	
	        	String[] b=values.toString().split("\t");
	        	 c = Long.parseLong(b[1]);
	        	if(c==2011)
	        	{
	        		return 0;
	        	}
	        	else if(c==2012)
	        	{
	        		return 1;
	        	}
	        	else if(c==2013)
	        	{
	        		return 2;
	        	} 
	        	else if(c==2014)
	        	{
	        		return 3;
	        	}
	        	else if(c==2015)
	        	{
	        		return 4;
	        	}
	   
	        	else
	        	{
	        		return 5;	
	        	}
	        		
	         }
	   }
	
	public static class ReduceClass extends Reducer<Text,Text,NullWritable,Text>
	   {
		   // private LongWritable result = new LongWritable();
		    
		 public TreeMap<Long, Text> tm = new TreeMap<Long, Text>();
	  		public void reduce (Text key, Iterable<Text> values, Context con) throws IOException, InterruptedException
	  		{
	  			long count=0;
	  			//String year="";
	  			//String job="";
	  			String myVal="";
	  			for(Text val:values)
	  			{
	  				String[] str = val.toString().split("\t");
	  				
	  					count++;
	  					myVal = str[1]+"\t"+key;
	  				
	  				
	  			}
	  			String myValue1 = myVal+"\t"+count;
	  			tm.put(new Long(count), new Text(myValue1));
	  			if(tm.size()>5)
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
 //conf.set("name", "value");
 //conf.set("mapreduce.input.fileinputformat.split.minsize", "134217728");
 Job job = Job.getInstance(conf, "job Count");
 job.setJarByClass(cerwsjob.class);
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
