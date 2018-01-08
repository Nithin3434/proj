h1b_final = load '/home/hduser/Project/h1b_final/*' using PigStorage('\t') as (sno:int, case_status:chararray, emp_name:chararray, soc_name:chararray, job_title:chararray, full_time_pos:chararray, wage:long, year:chararray, worksite:chararray, longitude:double, lattitude:double);
year_grp = group h1b_final by $7;
total = foreach year_grp generate group, (float)COUNT(h1b_final.$1) as tot;
--dump total;
grp = group h1b_final by ($7,$1);
case_total = foreach grp generate flatten(group), (float)COUNT(h1b_final.$1) as per;
--dump case_total;
join_grp = join total by $0,case_total by $0;
--dump join_grp;
final = foreach join_grp generate $0,$3,$4,ROUND_TO(($4/$1)*100,2);
--dump final;
store final into '/home/hduser/Documents/project pig querries/project6';

  










