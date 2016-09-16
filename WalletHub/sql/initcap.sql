# Capitalize all words in a given string
create function initcap(str varchar(255))
  returns varchar(255) deterministic
  begin
    declare len int;
    declare i int;

    set len = char_length(str);
    set str = lower(str);
    set i = 0;

    while (i < len) do
      if (mid(str, i, 1) = ' ' or i = 0)
      then
        if (i < len)
        then
          set str = concat(left(str, i), upper(mid(str, i + 1, 1)), right(str, len - i - 1));
        end if;
      end if;
      set i = i + 1;
    end while;

    return str;
  end;

# Usage example
select initcap('UNITED states Of AmERIca');
# Outout: United States Of America