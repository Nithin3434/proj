

h1b_final= load '/home/hduser/Project/h1b_final/*' using PigStorage('\t') AS   (s_no:int,case_status:chararray,employer_name:chararray,soc_name:chararray,job_title:chararray,full_time_position:chararray,prevailing_wage:double,year:chararray,worksite:chararray,longitute:double,latitute:double);


filjob = filter h1b_final by year=='2011';
--dump filjob;

grpjt = group filjob by $4;
  
grpcs1 = foreach grpjt generate group, COUNT(filjob.$1);
--dump grpcs;
 
filjob = filter h1b_final by year=='2012';
--dump filjob;

grpjt = group filjob by $4;
  
grpcs2 = foreach grpjt generate group, COUNT(filjob.$1);

filjob = filter h1b_final by year=='2013';
--dump filjob;

grpjt = group filjob by $4;
  
grpcs3 = foreach grpjt generate group, COUNT(filjob.$1);

filjob = filter h1b_final by year=='2014';
--dump filjob;

grpjt = group filjob by $4;
  
grpcs4 = foreach grpjt generate group, COUNT(filjob.$1);

filjob = filter h1b_final by year=='2015';
--dump filjob;

grpjt = group filjob by $4;
  
grpcs5 = foreach grpjt generate group, COUNT(filjob.$1);

filjob = filter h1b_final by year=='2016';
--dump filjob;

grpjt = group filjob by $4;
  
grpcs6 = foreach grpjt generate group, COUNT(filjob.$1);


jngr = join grpcs1 by $0 ,grpcs2 by $0 ,grpcs3 by $0 ,grpcs4 by $0 ,grpcs5 by $0 ,grpcs6 by $0;
--dump jngr;

jngr = foreach jngr generate $0 , $1 , $3 ,$5 ,$7 ,$9 ,$11;

growth = foreach jngr generate $0 , (float)(($2-$1)/$1)*100, (float)(($3-$2)/$2)*100,(float)(($4-$3)/$3)*100, (float)(($5-$4)/$4)*100, (float)(($6-$5)/$5)*100;

avggw = foreach growth generate $0 , ($1+$2+$3+$4+$5)/5;

lm = limit (order avggw by $1 desc) 5;
dump lm;








