# drop table if exists sometbl;
# CREATE TABLE sometbl (
#   ID   INT,
#   NAME VARCHAR(50)
# );
#
# insert into sometbl values
#   (1, 'Smith'),
#   (2, 'Julio|Jones|Falcons'),
#   (3, 'White|Snow'),
#   (4, 'Paint|It|Red'),
#   (5, 'Green|Lantern'),
#   (6, 'Brown|bag');
#
# select * from sometbl;

drop procedure if exists splitString;
create procedure splitString() begin
  declare splitter char;
  declare cur_id int;
  declare cur_name varchar(255);

  declare splitterPos int;
  declare prevPos int;
  declare curName varchar(255);
  declare hasSplitter boolean;

  declare empty boolean;
  declare cSometbl cursor for select t.id, t.name from sometbl t;
  declare continue handler for not found set empty = true;

  set splitter = '|';

  open cSometbl;
  set empty = false;

  while not empty do
    set hasSplitter = false;
    fetch cSometbl into cur_id, cur_name;

    set prevPos = 1;
    set splitterPos = locate(splitter, cur_name);
    while splitterPos != 0 do
      set curName = mid(cur_name, prevPos, splitterPos - prevPos);
      insert into sometbl values (cur_id, curName);
      set prevPos = splitterPos + 1;
      set splitterPos = locate(splitter, cur_name, prevPos);
      set hasSplitter = true;
    end while;

    # processing a phrase after last splitter, if exists
    if prevPos != 1 then
      set curName = mid(cur_name, prevPos);
      insert into sometbl values (cur_id, curName);
    end if;

    if hasSplitter = true then
      delete from sometbl where id = cur_id and name = cur_name;
    end if;
  end while;
  close cSometbl;
end;

# Usage example:
# call splitString();

# Output:
# ID NAME
# 1	Smith
# 2	Julio
# 2	Jones
# 2	Falcons
# 3	White
# 3	Snow
# 4	Paint
# 4	It
# 4	Red
# 5	Green
# 5	Lantern
# 6	Brown
# 6	bag
# 6	Brown
# 6	bag
