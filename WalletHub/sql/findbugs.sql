# Create table bugs
CREATE TABLE bugs (
  id INT,
  open_date DATE,
  close_date DATE,
  severity VARCHAR(16)
);

# Populate test data
insert into bugs values
  (1, '2016.01.01', '2016.01.20', 'error'),
  (2, '2016.01.02', '2016.01.22', 'improvement'),
  (3, '2016.01.03', '2016.01.10', 'improvement'),
  (4, '2016.01.04', '2016.01.05', 'error'),
  (5, '2016.01.05', '2016.01.10', 'error'),
  (6, '2016.02.01', NULL , 'feature request'),
  (7, '2016.02.03', '2016.02.20', 'error'),
  (8, '2016.02.05', '2016.02.12', 'error'),
  (9, '2016.02.15', NULL , 'feature request'),
  (10, '2016.02.22', '2016.02.29', 'error'),
  (11, '2016.03.01', '2016.03.20', 'error'),
  (12, '2016.03.04', '2016.04.24', 'improvement');

# Query to show the number of bugs open each day for a range of dates.
SET @start = date('2016.02.01') - INTERVAL 1 DAY; # start date of range
SET @end = date('2016.02.10');                    # end date of range
SELECT
  @start := (@start + INTERVAL 1 DAY) AS test_date,
  (SELECT count(*)
   FROM
     bugs b
   WHERE
     b.open_date <= test_date
     AND (b.close_date > test_date OR b.close_date IS NULL)) AS opened_bugs_count
FROM bugs a, bugs b, bugs c, bugs d
GROUP BY test_date
HAVING test_date <= @end;