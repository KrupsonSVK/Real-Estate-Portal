SELECT  
min(price) AS min,
max(price) AS max,
avg(price) AS avg,
percentile_cont(.25) WITHIN GROUP (ORDER BY price ASC) dolny, 
percentile_cont(.50) WITHIN GROUP (ORDER BY price ASC) median,
percentile_cont(.75) WITHIN GROUP (ORDER BY price ASC) horny 
FROM ads;