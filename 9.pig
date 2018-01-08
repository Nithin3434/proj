--9
h1b_final= load '/home/hduser/Project/h1b_final/*' using PigStorage('\t') AS   (s_no:int,case_status:chararray,employer_name:chararray,soc_name:chararray,job_title:chararray,full_time_position:chararray,prevailing_wage:double,year:double,worksite:chararray,longitute:double,latitute:double);

empgrp = GROUP h1b_final BY $2;
--dump empgrp;

wordcount = FOREACH empgrp GENERATE group, COUNT(h1b_final.$1);
--dump wordcount;


casefil = filter h1b_final by case_status=='CERTIFIED' or case_status=='CERTIFIED-WITHDRAWN';
case_status1 = GROUP casefil by $2; 


wordcount1 = FOREACH case_status1 GENERATE group, COUNT(casefil.$1);
--dump wordcount1;


joinb = join wordcount by $0,wordcount1 by $0;
--dump joinbag;

out = FOREACH joinb GENERATE $0,(float)$3/$1*100, $1;
--dump out;

out1 = filter out by $1>70 and $2>=1000;
dump out1; 
