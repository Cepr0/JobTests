# Create table bugs
create table bugs (
  id int,
  open_date date,
  close_date date,
  severity varchar(16)
);

# Populate test data
insert into bugs values
  (1, '2016.01.01', '2016.01.20', 'error'),
  (2, '2016.01.02', '2016.01.22', 'improvement'),
  (3, '2016.01.03', '2016.01.10', 'improvement'),
  (4, '2016.01.04', '2016.01.05', 'error'),
  (5, '2016.01.05', '2016.01.10', 'error'),
  (6, '2016.02.01', null, 'feature request'),
  (7, '2016.02.03', '2016.02.20', 'error'),
  (8, '2016.02.05', '2016.02.12', 'error'),
  (9, '2016.02.15', null, 'feature request'),
  (10, '2016.02.22', '2016.02.29', 'error'),
  (11, '2016.03.01', '2016.03.20', 'error'),
  (12, '2016.03.04', '2016.04.24', 'improvement');

# Query to show the number of bugs open each day for a range of dates.
# Размер диапазона завсисит от кол-ва записей в таблице bugs
# чтобы сделать его достаточно большим подключаем эту таблицу несколько раз в болке from:
# bugs a, bugs b, bugs c и т.д...
# Чтобы сделать универсальное решение, необходимо для дат использовать отдельную талблицу с каленарем.
#
set @start = date('2016.02.01') - interval 1 day; # start date of range
set @end = date('2016.02.10');                    # end date of range
select
  @start := (@start + interval 1 day) as test_date, (
    select count(*) from bugs b
    where
      b.open_date <= test_date
      and (b.close_date > test_date or b.close_date is null)
  ) as opened_bugs_count

from bugs a, bugs b, bugs c, bugs d
group by test_date
having test_date <= @end;