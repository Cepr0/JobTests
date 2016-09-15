# Capitalize all words in a given string
CREATE FUNCTION initcap(inputString VARCHAR(255))
  RETURNS VARCHAR(255) DETERMINISTIC
  BEGIN
    DECLARE len INT;
    DECLARE i INT;

    SET len = CHAR_LENGTH(inputString);
    SET inputString = LOWER(inputString);
    SET i = 0;

    WHILE (i < len) DO
      IF (MID(inputString, i, 1) = ' ' OR i = 0)
      THEN
        IF (i < len)
        THEN
          SET inputString = CONCAT(LEFT(inputString, i), UPPER(MID(inputString, i + 1, 1)), RIGHT(inputString, len - i - 1));
        END IF;
      END IF;
      SET i = i + 1;
    END WHILE;

    RETURN inputString;
  END;

# Usage example
select initcap('UNITED states Of AmERIca');
# Outout: United States Of America